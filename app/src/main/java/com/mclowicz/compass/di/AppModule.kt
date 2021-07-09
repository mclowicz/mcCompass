package com.mclowicz.compass.di

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mclowicz.compass.services.location.LocationService
import com.mclowicz.compass.services.orientation.OrientationService
import com.mclowicz.compass.services.permission.PermissionService
import com.mclowicz.compass.services.sharedPreferences.SharedPreferencesServiceJava
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
    fun provideSharedPreferencesJava(app: Application) = SharedPreferencesServiceJava(app)

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
        sharedPreferencesService: SharedPreferencesServiceJava
    ) = OrientationService(sensorManager, sharedPreferencesService)
}