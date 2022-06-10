package com.example.go4lunch_randa.api.retrofit;

import static com.example.go4lunch_randa.utils.Constants.API_KEY;

import androidx.annotation.Nullable;

import com.example.go4lunch_randa.models.googleplaces_gson.PlaceDetails;
import com.example.go4lunch_randa.models.googleplaces_gson.ResultDetails;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlaceDetailsCalls {

    // 2 - Public method to start fetching nearby restaurants
    public static void fetchPlaceDetails(Callbacks callbacks, String place_id) {

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GooglePlaceDetailsService googlePlaceDetailsService = GooglePlaceDetailsService.retrofit.create(GooglePlaceDetailsService.class);

        // 2.3 - Create the call on googlePlaceSearchService
        Call<PlaceDetails> call = googlePlaceDetailsService.getDetails(place_id, API_KEY);

        // 2.4 - Start the call
        call.enqueue(new Callback<PlaceDetails>() {

            @Override
            public void onResponse(@NotNull Call<PlaceDetails> call, @NotNull Response<PlaceDetails> response) {

                // 2.5 - Call the proper callback used in controller
                if (callbacksWeakReference.get() != null) {
                    PlaceDetails placeDetails = response.body();
                    assert placeDetails != null;
                    ResultDetails resultDetails = placeDetails.getResultDetails();
                    callbacksWeakReference.get().onResponse(resultDetails);
                }
            }

            @Override
            public void onFailure(@NotNull Call<PlaceDetails> call, @NotNull Throwable t) {

                // 2.5 - Call the proper callback used in controller
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ResultDetails resultDetails);

        void onFailure();
    }
}
