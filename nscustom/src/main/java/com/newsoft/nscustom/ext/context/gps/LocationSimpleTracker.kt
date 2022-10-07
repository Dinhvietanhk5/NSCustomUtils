package com.newsoft.nscustom.ext.context.gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*

data class LocationSimpleTracker(
    val context: Context,
    val listener: LocationSimpleTrackerListener
) {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun detectGPS(onGPSChanged: (Boolean) -> Unit) {
        locationCallback = object : LocationCallback() {

            override fun onLocationAvailability(var1: LocationAvailability) {
                onGPSChanged(var1.isLocationAvailable)
            }

            override fun onLocationResult(result: LocationResult) {
                Log.d("onLocationResult", " ")
                listener.apply { onLocationResult(result.lastLocation!!) }

                if (result != null)
                    stop()
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            buildLocationRequest(),
            locationCallback!!,
            null
        )
    }

    private fun buildLocationRequest(): LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000 //5 seconds
        fastestInterval = 5000 //5 seconds
        maxWaitTime = 1000 //1 seconds
    }

    interface LocationSimpleTrackerListener {
        fun onLocationResult(location: Location)
    }

    fun stop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback!! )
    }
}