package com.mclowicz.compass.service.orientation

import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.mclowicz.compass.data.model.CompassOrientation
import com.mclowicz.compass.data.model.GeoLocation
import com.mclowicz.compass.service.sharedPreferences.SharedPreferencesServiceJava
import com.mclowicz.compass.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.lang.Math.toDegrees
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class OrientationService @Inject constructor(
    private val sensorManager: SensorManager,
    private val sharedPreferencesService: SharedPreferencesServiceJava
) {
    private val R = FloatArray(9)
    private val I = FloatArray(9)
    private var azimuth = 0f
    private var lastPolesAzimuth = 0f
    private var lastDestinationAzimuth = 0f
    private var lastAccelerometer = FloatArray(3)
    private var lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    @ExperimentalCoroutinesApi
    fun fetchOrientation(): Flow<Resource<CompassOrientation>> = channelFlow {
        val currentPosition: GeoLocation = sharedPreferencesService.getCurrentLocation()
        val destinationPosition: GeoLocation = sharedPreferencesService.getDestinationLocation()
        val accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor === accelerometer) {
                    lowPass(event.values, lastAccelerometer)
                    lastAccelerometerSet = true
                } else if (event.sensor === magnetometer) {
                    lowPass(event.values, lastMagnetometer)
                    lastMagnetometerSet = true
                }
                if (lastAccelerometerSet && lastMagnetometerSet) {
                    if (SensorManager.getRotationMatrix(
                            R,
                            I,
                            lastAccelerometer,
                            lastMagnetometer
                        )
                    ) {
                        val compassOrientation = CompassOrientation()
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(R, orientation)
                        azimuth = (toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360
                        compassOrientation.polesDirection = azimuth
                        compassOrientation.lastPolesDirection = lastPolesAzimuth
                        lastPolesAzimuth = azimuth
                        val destinationAzimuth: Double = azimuth -
                                bearing(
                                    currentPosition.latitude,
                                    currentPosition.longitude,
                                    destinationPosition.latitude,
                                    destinationPosition.longitude
                                )
                        compassOrientation.destinationDirection = destinationAzimuth.toFloat()
                        compassOrientation.lastDestinationDirection = lastDestinationAzimuth
                        lastDestinationAzimuth = destinationAzimuth.toFloat()
                        channel.offer(Resource.Success(compassOrientation))
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener, accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            listener, magnetometer,
            SensorManager.SENSOR_DELAY_GAME
        )

        awaitClose {
            sensorManager.unregisterListener(listener, accelerometer)
            sensorManager.unregisterListener(listener, magnetometer)
            channel.close()
        }
    }

    private fun bearing(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Double {
        val latitude1 = Math.toRadians(startLat)
        val latitude2 = Math.toRadians(endLat)
        val longDiff = Math.toRadians(endLng - startLng)
        val y = sin(longDiff) * cos(latitude2)
        val x =
            cos(latitude1) * sin(latitude2) - sin(latitude1) * cos(latitude2) * cos(
                longDiff
            )
        return (toDegrees(atan2(y, x)) + 360) % 360
    }


    fun lowPass(input: FloatArray, output: FloatArray) {
        val alpha = 0.05f
        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
    }
}