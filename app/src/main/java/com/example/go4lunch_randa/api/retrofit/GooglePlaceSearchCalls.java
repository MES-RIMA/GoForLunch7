package com.example.go4lunch_randa.api.retrofit;

import androidx.annotation.Nullable;

import com.example.go4lunch_randa.BuildConfig;
import com.example.go4lunch_randa.models.googleplaces_gson.ResultSearch;
import com.example.go4lunch_randa.models.googleplaces_gson.SearchPlace;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlaceSearchCalls {
    // 2 - Public method to start fetching nearby restaurants
    public static void fetchNearbyRestaurants(Callbacks callbacks, String location) {

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GooglePlaceSearchService googlePlaceSearchService = GooglePlaceSearchService.retrofit.create(GooglePlaceSearchService.class);

        // 2.3 - Create the call on googlePlaceSearchService
        Call<SearchPlace> call = googlePlaceSearchService.getNearbyRestaurants(location, "distance", "restaurant", BuildConfig.api_key);

        // 2.4 - Start the call
        call.enqueue(new Callback<SearchPlace>() {

            @Override
            public void onResponse(@NotNull Call<SearchPlace> call, @NotNull Response<SearchPlace> response) {
                // 2.5 - Call the proper callback used in controller
                if (callbacksWeakReference.get() != null) {
                    SearchPlace searchPlace = response.body();
                    assert searchPlace != null;
                    List<ResultSearch> resultSearchList = searchPlace.getResultSearches();
                    callbacksWeakReference.get().onResponse(resultSearchList);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SearchPlace> call, @NotNull Throwable t) {
                System.out.println(t.toString());
                // 2.5 - Call the proper callback used in controller
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    // 1 - Creating a callback
    public interface Callbacks {

        void onResponse(@Nullable List<ResultSearch> resultSearchList);

        void onFailure();
    }
}
