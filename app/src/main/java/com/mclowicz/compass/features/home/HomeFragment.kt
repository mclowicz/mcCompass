package com.mclowicz.compass.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mclowicz.compass.R
import com.mclowicz.compass.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        initObservers()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            when (grantResults.isNotEmpty() && grantResults[GRANT_RESULT_INDEX] == PackageManager.PERMISSION_GRANTED) {
                true -> launchCompassFragment()
                else -> requestPermission()
            }
        }
    }

    private fun initObservers() {
        with(viewModel) {
            appHasPermissions.observe(viewLifecycleOwner, { hasPermissions ->
                when (hasPermissions) {
                    true -> launchCompassFragment()
                    else -> requestPermission()
                }
            })
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_PERMISSION_CODE
        )
    }

    private fun launchCompassFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToCompassFragment()
        findNavController().navigate(action)
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1
        private const val GRANT_RESULT_INDEX = 0
    }
}