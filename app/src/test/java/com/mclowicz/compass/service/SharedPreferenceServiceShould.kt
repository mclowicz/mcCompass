package com.mclowicz.compass.service

import android.content.Context
import android.content.SharedPreferences
import com.mclowicz.compass.data.model.GeoLocation
import com.mclowicz.compass.data.sharedPreferences.SharedPreferencesService
import com.mclowicz.compass.utils.Constants
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class SharedPreferenceServiceShould {

    private val context: Context = mock()
    private val sharedPreferences: SharedPreferences = mock()
    private val sharedPreferencesEditor: SharedPreferences.Editor = mock()
    private val expectedLocation: GeoLocation = GeoLocation(0.0, 0.0)
    private lateinit var sharedPreferencesService: SharedPreferencesService

    @Before
    fun init() {
        sharedPreferencesService = SharedPreferencesService(context)
        `when`(context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
    }

    @Test
    fun `save current location`()  {
        sharedPreferencesService.saveCurrentLocation(expectedLocation)
        verify(sharedPreferences, times(1)).edit()
    }

    @Test
    fun `save destination latitude`() {
        val latitude = "432.34"
        sharedPreferencesService.saveDestinationLatitude(latitude)
        verify(sharedPreferences, times(1)).edit()
    }

    @Test
    fun `save destination longitude`() {
        val longitude = "34.32423"
        sharedPreferencesService.saveDestinationLongitude(longitude)
        verify(sharedPreferences, times(1)).edit()
    }

    @Test
    fun `get destination location`() {
        val destinationLocation = sharedPreferencesService.getDestinationLocation()
        assertEquals(expectedLocation, destinationLocation)
    }

    @Test
    fun `get current location`() {
        val currentLocation = sharedPreferencesService.getCurrentLocation()
        assertEquals(expectedLocation, currentLocation)
    }
}