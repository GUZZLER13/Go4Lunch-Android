package com.guzzler.go4lunch_p7.utils;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.ui.MainActivity;

import static android.content.ContentValues.TAG;

public class UpdateMarkers {


    /**
     * USE OF LIVEDATA
     */

    public static void updateMarkers(GoogleMap map, MainActivity mMainActivity) {
        if (mMainActivity.mLiveData.getValue() != null) {
            map.clear();

            if (mMainActivity.mLiveData.getValue().size() > 0) {
                for (int i = 0; i < mMainActivity.mLiveData.getValue().size(); i++) {
                    Double lat = mMainActivity.mLiveData.getValue().get(i).getGeometry().getLocation().getLat();
                    Double lng = mMainActivity.mLiveData.getValue().get(i).getGeometry().getLocation().getLng();
                    // String title = mMainActivity.mLiveData.getValue().get(i).getName();
                    // markerOptions.title(title);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lng));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    Marker marker = map.addMarker(markerOptions);
                    marker.setTag(mMainActivity.mLiveData.getValue().get(i).getPlaceId());
                    marker.showInfoWindow();

                }
            } else {
                Toast.makeText(mMainActivity.getApplicationContext(), mMainActivity.getResources().getString(R.string.no_restaurant), Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "number of markers : " + mMainActivity.mLiveData.getValue().size());
        }

    }

}
