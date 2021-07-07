package com.mclowicz.compass.utils

fun Double.format(digits: Int) = "%.${digits}f".format(this)