package com.mclowicz.compass.data.repository

import com.mclowicz.compass.data.location.LocationService
import com.mclowicz.compass.data.orientation.OrientationService
import com.mclowicz.compass.data.permission.PermissionService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CompassRepository @Inject constructor(
    private val permissionService: PermissionService,
    private val locationService: LocationService,
    private val orientationService: OrientationService
) {

    fun checkPermissions() = permissionService.appHasPermissions()
    fun getCurrentLocation() = locationService.fetchUpdates()
    fun getCurrentOrientation() = orientationService.fetchOrientation()
}