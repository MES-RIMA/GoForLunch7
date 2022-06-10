package com.example.go4lunch_randa.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {
    @SerializedName("open_now")
    private Boolean mOpenNow;
    @SerializedName("periods")
    private List<Period> mPeriods;


    public Boolean getOpenNow() {
        return mOpenNow;
    }

    public List<Period> getPeriods() {
        return mPeriods;
    }
}
