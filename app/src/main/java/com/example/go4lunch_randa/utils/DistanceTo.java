package com.example.go4lunch_randa.utils;

import static com.example.go4lunch_randa.utils.Constants.EARTH_RADIUS_KM;

import com.example.go4lunch_randa.models.googleplaces_gson.ResultDetails;
import com.example.go4lunch_randa.ui.HomeActivity;

import java.util.Objects;

public class DistanceTo {
    public static double distanceTo(ResultDetails resultDetails, HomeActivity mHomeActivity) {
        double lat1Rad = Math.toRadians(Objects.requireNonNull(mHomeActivity.mShareViewModel.currentUserPosition.getValue()).latitude);
        double lat2Rad = Math.toRadians(resultDetails.getGeometry().getLocation().getLat());
        double deltaLonRad = Math.toRadians(resultDetails.getGeometry().getLocation().getLng() - mHomeActivity.mShareViewModel.currentUserPosition.getValue().longitude);

        return 1000 * Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
    }
}
