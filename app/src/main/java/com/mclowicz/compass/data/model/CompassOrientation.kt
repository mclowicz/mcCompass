package com.mclowicz.compass.data.model

data class CompassOrientation(
    var destinationDirection: Float? = null,
    var lastDestinationDirection: Float? = null,
    var polesDirection: Float? = null,
    var lastPolesDirection: Float? = null
)