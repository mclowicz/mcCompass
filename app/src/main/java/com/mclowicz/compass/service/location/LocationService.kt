package com.mclowicz.compass.service.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.mclowicz.compass.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationService @Inject constructor(
    private val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    @ExperimentalCoroutinesApi
    fun fetchUpdates(): Flow<Resource<Location>> = channelFlow {
        channel.offer(Resource.Loading())
        val locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECS)
            fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_UPDATE_INTERVAL_SECS)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                channel.offer(Resource.Success(location))
            }
        }
        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose {
            client.removeLocationUpdates(callBack)
            channel.close()
        }
    }

    companion object {
        private const val UPDATE_INTERVAL_SECS = 10L
        private const val FASTEST_UPDATE_INTERVAL_SECS = 2L
    }
}