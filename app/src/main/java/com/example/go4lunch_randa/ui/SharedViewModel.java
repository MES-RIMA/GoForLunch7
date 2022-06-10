package com.example.go4lunch_randa.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch_randa.models.googleplaces_gson.ResultDetails;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

public class SharedViewModel extends ViewModel {
    public MutableLiveData<LatLng> currentUserPosition = new MutableLiveData<>();
    public MutableLiveData<List<ResultDetails>> mList = new MutableLiveData<>();

    public void updateCurrentUserPosition(LatLng latLng) {

        currentUserPosition.setValue(latLng);
    }

    public LatLng getCurrentUserPosition() {

        return currentUserPosition.getValue();
    }

    public List<ResultDetails> getList() {
        return mList.getValue();
    }

    public String getCurrentUserPositionFormatted() {
        String location = Objects.requireNonNull(currentUserPosition.getValue()).toString().replace("lat/lng: (", "");
        return location.replace(")", "");
    }
}
