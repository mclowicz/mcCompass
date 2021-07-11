package com.mclowicz.compass.viewModel

import com.mclowicz.compass.features.home.HomeViewModel
import com.mclowicz.compass.utils.BaseUnitTest
import com.mclowicz.compass.utils.captureValues
import com.mclowicz.compass.utils.getValueForTest
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelShould: BaseUnitTest() {

    @Test
    fun `get permissions from repository`() = runBlockingTest {
        val viewModel = mockSuccessfulCase()
        viewModel.appHasPermissions.getValueForTest()
        verify(repository, times(1)).checkPermissions()
    }

    @Test
    fun `should grant access on a given permission`() = runBlockingTest {
        val viewModel = mockSuccessfulCase()
        viewModel.appHasPermissions.captureValues {
            viewModel.appHasPermissions.getValueForTest()
            assertEquals(true, values[0])
        }
    }

    @Test
    fun `should not grant access on a not given permission`() = runBlockingTest {
        val viewModel = mockFailureCase()
        viewModel.appHasPermissions.captureValues {
            viewModel.appHasPermissions.getValueForTest()
            assertEquals(false, values[0])
        }
    }

    override fun mockSuccessfulCase(): HomeViewModel {
        runBlockingTest {
            whenever(repository.checkPermissions()).thenReturn(
                flow {
                    emit(true)
                }
            )
        }
        return HomeViewModel(repository)
    }

    override fun mockFailureCase(): HomeViewModel {
        runBlockingTest {
            whenever(repository.checkPermissions()).thenReturn(
                flow {
                    emit(false)
                }
            )
        }
        return HomeViewModel(repository)
    }
}