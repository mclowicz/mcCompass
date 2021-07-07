package com.mclowicz.compass.features.settings

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.mclowicz.compass.R
import com.mclowicz.compass.data.sharedPreferences.SharedPreferencesService
import com.mclowicz.compass.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var sharedPreferencesService: SharedPreferencesService

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_layout, rootKey)

        val destination = sharedPreferencesService.getDestinationLocation()

        val latitudePreferences: EditTextPreference? =
            findPreference(Constants.DESTINATION_LATITUDE)
        latitudePreferences?.let { it ->
            it.apply {
                it.summary = destination.latitude.toString()
                setOnBindEditTextListener { editText ->
                    editText.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
                setOnPreferenceChangeListener { _, newValue ->
                    it.text = newValue.toString()
                    it.summary = newValue.toString()
                    sharedPreferencesService.saveDestinationLatitude(newValue.toString())
                    true
                }
            }
        }
        val longitudePreferences: EditTextPreference? =
            findPreference(Constants.DESTINATION_LONGITUDE)
        longitudePreferences?.let { it ->
            it.apply {
                it.summary = destination.longitude.toString()
                setOnBindEditTextListener { editText ->
                    editText.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
                setOnPreferenceChangeListener { _, newValue ->
                    it.text = newValue.toString()
                    it.summary = newValue.toString()
                    sharedPreferencesService.saveDestinationLongitude(newValue.toString())
                    true
                }
            }
        }
    }
}