package com.mclowicz.compass.utils

import com.mclowicz.compass.data.model.GeoLocation

object Constants {

    const val PREFERENCE_NAME = "location"
    const val CURRENT_LATITUDE = "currentLatitude"
    const val CURRENT_LONGITUDE = "currentLongitude"
    const val DESTINATION_LATITUDE = "destinationLatitude"
    const val DESTINATION_LONGITUDE = "destinationLongitude"
    val DEFAULT_CURRENT_GEOPOSITION = GeoLocation(82.8628, 135.0000)
    val DEFAULT_DESTINATION_GEOPOSITION = GeoLocation(46.8625, 103.8467)
}