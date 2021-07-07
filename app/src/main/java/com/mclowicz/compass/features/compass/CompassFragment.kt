package com.mclowicz.compass.features.compass

import android.graphics.Typeface
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mclowicz.compass.R
import com.mclowicz.compass.data.model.CompassOrientation
import com.mclowicz.compass.data.model.GeoLocation
import com.mclowicz.compass.data.sharedPreferences.SharedPreferencesService
import com.mclowicz.compass.databinding.FragmentCompassBinding
import com.mclowicz.compass.utils.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class CompassFragment : Fragment(R.layout.fragment_compass) {

    private lateinit var binding: FragmentCompassBinding
    private val viewModel by viewModels<CompassViewModel>()

    @Inject
    lateinit var sharedPreferencesService: SharedPreferencesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCompassBinding.bind(view)
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.settingsFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initObservers() {
        with(viewModel) {
            currentLocation.observe(viewLifecycleOwner, {
                initLayoutContent(currentLocation)
                val currentLocation = GeoLocation(
                    it.latitude,
                    it.longitude
                )
                sharedPreferencesService.saveCurrentLocation(currentLocation)
            })
            currentOrientation.observe(viewLifecycleOwner, { currentOrientation ->
                updateCompass(currentOrientation)
            })
        }
    }

    private fun updateCompass(compassOrientation: CompassOrientation) {
        binding.imageMainHands.adjustArrow(
            compassOrientation.polesDirection!!,
            compassOrientation.lastPolesDirection!!
        )
        binding.imageMainDestination.adjustArrow(
            compassOrientation.destinationDirection!!,
            compassOrientation.lastDestinationDirection!!
        )
    }

    private fun View.adjustArrow(azimuth: Float, currentAzimuth: Float) {
        val animation = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 500
        animation.repeatCount = 0
        animation.fillAfter = true
        this.startAnimation(animation)
    }

    private fun initLayoutContent(currentLocation: LiveData<Location>) {
        val destinationLocation = sharedPreferencesService.getDestinationLocation()
        binding.apply {
            currentLocation.value?.let {
                locationProgressBar.isVisible = false
            }
            textCurrentLat.text = getString(
                R.string.fragment_compass_text_current_lat,
                currentLocation.value?.latitude?.format(6)
            )
            textCurrentLong.text = getString(
                R.string.fragment_compass_text_current_long,
                currentLocation.value?.longitude?.format(6)
            )
            textDestinationLat.text = getString(
                R.string.fragment_compass_text_destination_lat,
                destinationLocation.latitude.format(6)
            )
            textDestinationLong.text = getString(
                R.string.fragment_compass_text_destination_long,
                destinationLocation.longitude.format(6)
            )
        }
    }
}