package com.mclowicz.compass.data.sharedPreferences

import android.content.Context
import com.mclowicz.compass.data.model.GeoLocation
import com.mclowicz.compass.utils.Constants
import javax.inject.Inject

class SharedPreferencesService @Inject constructor(
    private val context: Context
) {

    val sharedPreferences by lazy {
        context.getSharedPreferences(
            Constants.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun saveCurrentLocation(geoLocation: GeoLocation) {
        with(sharedPreferences.edit()) {
            putFloat(Constants.CURRENT_LATITUDE, geoLocation.latitude.toFloat())
            putFloat(Constants.CURRENT_LONGITUDE, geoLocation.longitude.toFloat())
            apply()
        }
    }

    fun saveDestinationLatitude(latitude: String) {
        with(sharedPreferences.edit()) {
            putFloat(Constants.DESTINATION_LATITUDE, latitude.toFloat())
            apply()
        }
    }

    fun saveDestinationLongitude(longitude: String) {
        with(sharedPreferences.edit()) {
            putFloat(Constants.DESTINATION_LONGITUDE, longitude.toFloat())
            apply()
        }
    }

    fun getCurrentLocation(): GeoLocation {
        val currentLatitude = sharedPreferences.getFloat(
            Constants.CURRENT_LATITUDE,
            Constants.DEFAULT_CURRENT_GEOPOSITION.latitude.toFloat()
        ).toDouble()
        val currentLongitude = sharedPreferences.getFloat(
            Constants.CURRENT_LONGITUDE,
            Constants.DEFAULT_CURRENT_GEOPOSITION.longitude.toFloat()
        ).toDouble()
        return GeoLocation(
            currentLatitude,
            currentLongitude
        )
    }

    fun getDestinationLocation(): GeoLocation {
        val destinationLatitude = sharedPreferences.getFloat(
            Constants.DESTINATION_LATITUDE,
            Constants.DEFAULT_DESTINATION_GEOPOSITION.latitude.toFloat()
        ).toDouble()
        val destinationLongitude = sharedPreferences.getFloat(
            Constants.DESTINATION_LONGITUDE,
            Constants.DEFAULT_DESTINATION_GEOPOSITION.longitude.toFloat()
        ).toDouble()
        return GeoLocation(
            destinationLatitude,
            destinationLongitude
        )
    }
}