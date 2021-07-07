package com.mclowicz.compass.features.compass

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mclowicz.compass.data.repository.CompassRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CompassViewModel @ViewModelInject constructor(
    repository: CompassRepository
) : ViewModel() {

    val currentLocation = repository.getCurrentLocation().asLiveData()
    val currentOrientation = repository.getCurrentOrientation().asLiveData()
}