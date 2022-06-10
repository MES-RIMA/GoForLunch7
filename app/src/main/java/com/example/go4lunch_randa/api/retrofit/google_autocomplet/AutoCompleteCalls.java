package com.example.go4lunch_randa.api.retrofit.google_autocomplet;

import static com.example.go4lunch_randa.utils.Constants.API_KEY;
import static com.example.go4lunch_randa.utils.Constants.RADIUS;

import androidx.annotation.Nullable;

import com.example.go4lunch_randa.models.google_autocomplete_gson.AutoCompleteResult;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteCalls {
    private static final String types = "establishment";
    static String language = "fr";

    public static void fetchAutoCompleteResult(AutoCompleteCalls.Callbacks callbacks, String input, String location) {

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        AutoCompleteService googleAutoComplete = AutoCompleteService.retrofit.create(AutoCompleteService.class);

        Call<AutoCompleteResult> call = googleAutoComplete.getAutoComplete(input, types, language, location, RADIUS, true, API_KEY);
        call.enqueue(new Callback<AutoCompleteResult>() {

            @Override
            public void onResponse(@NotNull Call<AutoCompleteResult> call, @NotNull Response<AutoCompleteResult> response) {
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<AutoCompleteResult> call, @NotNull Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    public interface Callbacks {
        void onResponse(@Nullable AutoCompleteResult autoCompleteResult);

        void onFailure();
    }
}
