package com.example.go4lunch_randa.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.go4lunch_randa.api.firebase.RestaurantsHelper;
import com.example.go4lunch_randa.models.googleplaces_gson.ResultDetails;
import com.google.firebase.auth.FirebaseAuth;

public class LikeButton {
    public static void likeRestaurant(ResultDetails result, Context context, TextView textView, String unlike, String liked) {
        if (result != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            RestaurantsHelper.createLike(result.getPlaceId(), FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(likeTask -> {
                if (likeTask.isSuccessful()) {
                    Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
                    textView.setText(unlike);
                }
            });
        } else {
            Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
        }
    }

    public static void dislikeRestaurant(ResultDetails result, Context context, TextView textView, String like, String disliked, String liked) {
        if (result != null) {
            RestaurantsHelper.deleteLike(result.getPlaceId(), FirebaseAuth.getInstance().getUid());
            textView.setText(like);
            Toast.makeText(context, disliked, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, liked, Toast.LENGTH_SHORT).show();
        }
    }
}
