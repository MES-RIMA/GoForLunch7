package com.example.go4lunch_randa.utils;

import static com.example.go4lunch_randa.utils.ShowToastSnack.showToast;

import android.util.Log;

import com.example.go4lunch_randa.R;
import com.example.go4lunch_randa.ui.HomeActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class UpdateMarkers {


    /**
     * USE OF LIVEDATA
     */

    public static void updateMarkers(GoogleMap map, HomeActivity mHomeActivity) {
        if (mHomeActivity.mLiveData.getValue() != null) {
            map.clear();

            if (mHomeActivity.mLiveData.getValue().size() > 0) {
                for (int i = 0; i < mHomeActivity.mLiveData.getValue().size(); i++) {
                    Double lat = mHomeActivity.mLiveData.getValue().get(i).getGeometry().getLocation().getLat();
                    Double lng = mHomeActivity.mLiveData.getValue().get(i).getGeometry().getLocation().getLng();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lng));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    Marker marker = map.addMarker(markerOptions);
                    marker.setTag(mHomeActivity.mLiveData.getValue().get(i).getPlaceId());
                    marker.showInfoWindow();

                }
            } else {
                showToast(mHomeActivity.getApplicationContext(), mHomeActivity.getResources().getString(R.string.no_restaurant), 1);
            }
            Log.e(Constants.TAG, "number of markers : " + mHomeActivity.mLiveData.getValue().size());
        }
    }
}
