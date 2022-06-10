package com.example.go4lunch_randa.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location mLocation;

    public Location getLocation() {
        return mLocation;
    }

}
