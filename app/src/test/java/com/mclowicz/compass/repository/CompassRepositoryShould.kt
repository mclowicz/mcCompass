package com.mclowicz.compass.repository

import android.location.Location
import com.mclowicz.compass.data.model.CompassOrientation
import com.mclowicz.compass.data.repository.CompassRepository
import com.mclowicz.compass.service.location.LocationService
import com.mclowicz.compass.service.orientation.OrientationService
import com.mclowicz.compass.service.permission.PermissionService
import com.mclowicz.compass.utils.BaseUnitTest
import com.mclowicz.compass.utils.Resource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CompassRepositoryShould: BaseUnitTest() {

    private val permissionService: PermissionService = mock<PermissionService>()
    private val locationService: LocationService = mock<LocationService>()
    private val orientationService: OrientationService = mock<OrientationService>()
    private val expectedLocation = mock<Resource<Location>>()
    private val expectedCompassOrientation = mock<Resource<CompassOrientation>>()
    private val expectedLocationError = mock<Resource.Error<Location>>()
    private val expectedCompassOrientationError = mock<Resource.Error<CompassOrientation>>()

    @Test
    fun `get permission from service`() = runBlockingTest {
        val repository = mockSuccessfulCase()
        repository.checkPermissions()
        verify(permissionService, times(1)).appHasPermissions()
    }

    @Test
    fun `get current location from service`() = runBlockingTest {
        val repository = mockSuccessfulCase()
        repository.getCurrentLocation()
        verify(locationService, times(1)).fetchUpdates()
    }

    @Test
    fun `get current orientation from service`() = runBlockingTest {
        val repository = mockSuccessfulCase()
        repository.getCurrentOrientation()
        verify(orientationService, times(1)).fetchOrientation()
    }

    @Test
    fun `propagate errors when occurs`() = runBlockingTest {
        val repository = mockFailureCase()

        repository.checkPermissions()
        repository.getCurrentOrientation()
        repository.getCurrentLocation()

        assertEquals(false, repository.checkPermissions().first())
        assertEquals(expectedLocationError, repository.getCurrentLocation().first())
        assertEquals(expectedCompassOrientationError, repository.getCurrentOrientation().first())
    }

    override fun mockFailureCase(): CompassRepository {
        whenever(permissionService.appHasPermissions()).thenReturn(
            flow {
                emit(false)
            }
        )
        whenever(locationService.fetchUpdates()).thenReturn(
            flow {
                emit(expectedLocationError)
            }
        )
        whenever(orientationService.fetchOrientation()).thenReturn(
            flow {
                emit(expectedCompassOrientationError)
            }
        )
        return CompassRepository(permissionService, locationService, orientationService)
    }

    override fun mockSuccessfulCase(): CompassRepository {
        whenever(permissionService.appHasPermissions()).thenReturn(
            flow {
                emit(true)
            }
        )
        whenever(locationService.fetchUpdates()).thenReturn(
            flow {
                emit(expectedLocation)
            }
        )
        whenever(orientationService.fetchOrientation()).thenReturn(
            flow {
                emit(expectedCompassOrientation)
            }
        )
        return CompassRepository(permissionService, locationService, orientationService)
    }
}