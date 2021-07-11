package com.mclowicz.compass.data.repository

import com.mclowicz.compass.service.location.LocationService
import com.mclowicz.compass.service.orientation.OrientationService
import com.mclowicz.compass.service.permission.PermissionService
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