package com.example.go4lunch_randa.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class SharedViewModel extends ViewModel {
    public MutableLiveData<LatLng> currentUserPosition = new MutableLiveData<>();


    public void updateCurrentUserPosition(LatLng latLng) {
        currentUserPosition.setValue(latLng);
    }

    public LatLng getCurrentUserPosition() {
        return currentUserPosition.getValue();
    }

}

