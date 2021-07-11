package com.mclowicz.compass.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mclowicz.compass.data.repository.CompassRepository
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseUnitTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val repository: CompassRepository = mock()

    abstract fun mockSuccessfulCase(): Any
    abstract fun mockFailureCase(): Any
}