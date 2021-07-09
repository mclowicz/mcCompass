package com.mclowicz.compass.viewModel

import android.location.Location
import com.mclowicz.compass.data.model.CompassOrientation
import com.mclowicz.compass.features.compass.CompassViewModel
import com.mclowicz.compass.utils.BaseUnitTest
import com.mclowicz.compass.utils.Resource
import com.mclowicz.compass.utils.captureValues
import com.mclowicz.compass.utils.getValueForTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CompassViewModelShould : BaseUnitTest() {

    private val expectedLocation = mock<Resource<Location>>()
    private val expectedCompassOrientation = mock<Resource<CompassOrientation>>()
    private val expectedLocationError = mock<Resource.Error<Location>>()
    private val expectedCompassOrientationError = mock<Resource.Error<CompassOrientation>>()

    @Test
    fun `get current location from repository`() = runBlockingTest {
        val viewModel = mockSuccessfulCase()
        viewModel.currentLocation.getValueForTest()
        verify(repository, times(1)).getCurrentLocation()
    }

    @Test
    fun `get current compass orientation from repository`() = runBlockingTest {
        val viewModel = mockSuccessfulCase()
        viewModel.currentOrientation.getValueForTest()
        verify(repository, times(1)).getCurrentOrientation()
    }

    @Test
    fun `show spinner while loading`() = runBlockingTest {
        val viewModel = mockSuccessfulCase()
        viewModel.currentLocation.captureValues {
            viewModel.currentLocation.getValueForTest()
            assertEquals(expectedLocation, values[0])
        }
    }


    @Test
    fun `hide spinner after location load`() = runBlockingTest {
        val viewModel = mockSuccessfulCase()
        viewModel.currentLocation.captureValues {
            viewModel.currentLocation.getValueForTest()
            assertEquals(expectedLocation, values.last())
        }
    }

    @Test
    fun `emit error when receive error`() {
        val viewModel = mockErrorCase()
        assertEquals(expectedLocationError, viewModel.currentLocation.getValueForTest())
        assertEquals(expectedCompassOrientationError, viewModel.currentOrientation.getValueForTest())
    }

    private fun mockSuccessfulCase(): CompassViewModel {
        runBlockingTest {
            whenever(repository.getCurrentLocation()).thenReturn(
                flow {
                    emit(expectedLocation)
                }
            )
            whenever(repository.getCurrentOrientation()).thenReturn(
                flow {
                    emit(expectedCompassOrientation)
                }
            )
        }
        return CompassViewModel(repository)
    }

    private fun mockErrorCase(): CompassViewModel {
        runBlockingTest {
            whenever(repository.getCurrentLocation()).thenReturn(
                flow {
                    emit(expectedLocationError)
                }
            )
            whenever(repository.getCurrentOrientation()).thenReturn(
                flow {
                    emit(expectedCompassOrientationError)
                }
            )
        }
        return CompassViewModel(repository)
    }
}