package com.newsoft.location_manager.gps

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
                listener.apply { onLocationResult(result.lastLocation!!) }

                if (result != null)
                    stop()
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            createRequest(),
            locationCallback!!,
            null
        )
    }

    private fun createRequest(): LocationRequest =
        // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
            setMinUpdateDistanceMeters(5000f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    interface LocationSimpleTrackerListener {
        fun onLocationResult(location: Location)
    }

    fun stop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback!! )
    }
}