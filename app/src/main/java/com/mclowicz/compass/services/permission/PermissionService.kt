package com.mclowicz.compass.services.permission

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PermissionService @Inject constructor(
    private val context: Context
) {
    private fun checkFinePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarsePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun appHasPermissions() = flow {
        emit(checkFinePermission() && checkCoarsePermission())
    }
}