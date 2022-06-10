package com.example.go4lunch_randa.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;

public class PlaceDetails {
    @SerializedName("result")
    private ResultDetails mResultDetails;


    public ResultDetails getResultDetails() {
        return mResultDetails;
    }

}
