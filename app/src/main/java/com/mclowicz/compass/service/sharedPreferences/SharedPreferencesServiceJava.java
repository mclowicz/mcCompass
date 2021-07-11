package com.mclowicz.compass.service.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.mclowicz.compass.data.model.GeoLocation;
import com.mclowicz.compass.utils.Constants;
import javax.inject.Inject;

public class SharedPreferencesServiceJava {

    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesServiceJava(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                Constants.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void saveCurrentLocation(GeoLocation geoLocation) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constants.CURRENT_LATITUDE, (float) geoLocation.getLatitude());
        editor.putFloat(Constants.CURRENT_LONGITUDE, (float) geoLocation.getLongitude());
        editor.apply();
    }

    public void saveDestinationLatitude(String latitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constants.DESTINATION_LATITUDE, Float.parseFloat(latitude));
        editor.apply();
    }

    public void saveDestinationLongitude(String longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constants.DESTINATION_LONGITUDE, Float.parseFloat(longitude));
        editor.apply();
    }

    public GeoLocation getCurrentLocation() {
        float currentLatitude = sharedPreferences.getFloat(
                Constants.CURRENT_LATITUDE,
                82.8628f
        );
        float currentLongitude = sharedPreferences.getFloat(
                Constants.CURRENT_LONGITUDE,
                135.0000f
        );
        return new GeoLocation(
                currentLatitude,
                currentLongitude
        );
    }

    public GeoLocation getDestinationLocation() {
        float destinationLatitude = sharedPreferences.getFloat(
                Constants.DESTINATION_LATITUDE,
                46.8625f
        );
        float destinationLongitude = sharedPreferences.getFloat(
                Constants.DESTINATION_LONGITUDE,
                103.8467f
        );
        return new GeoLocation(
                destinationLatitude,
                destinationLongitude
        );
    }
}
