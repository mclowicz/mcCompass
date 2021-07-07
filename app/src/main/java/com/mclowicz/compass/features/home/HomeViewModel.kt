package com.mclowicz.compass.features.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mclowicz.compass.data.repository.CompassRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    repository: CompassRepository
) : ViewModel() {

    val appHasPermissions = repository.checkPermissions().asLiveData()
}