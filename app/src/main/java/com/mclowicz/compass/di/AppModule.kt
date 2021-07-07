package com.mclowicz.compass.di

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mclowicz.compass.data.location.LocationService
import com.mclowicz.compass.data.orientation.OrientationService
import com.mclowicz.compass.data.permission.PermissionService
import com.mclowicz.compass.data.sharedPreferences.SharedPreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application) = SharedPreferencesService(app)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application) =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    @Singleton
    fun provideSensorManager(app: Application) =
        app.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    fun providePermissionsService(app: Application) = PermissionService(app)

    @Provides
    @Singleton
    fun provideLocationService(fusedLocationProviderClient: FusedLocationProviderClient) =
        LocationService(fusedLocationProviderClient)

    @Provides
    @Singleton
    fun provideOrientationService(
        sensorManager: SensorManager,
        sharedPreferencesService: SharedPreferencesService
    ) = OrientationService(sensorManager, sharedPreferencesService)
}