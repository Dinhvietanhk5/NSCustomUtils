package com.newsoft.location_manager.gps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


fun Context.isGpsOn(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.turnGPSOn() {
    val poke = Intent()
    poke.setClassName(
        "com.android.settings",
        "com.android.settings.widget.SettingsAppWidgetProvider"
    )
    poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
    poke.data = Uri.parse("3")
    sendBroadcast(poke)
}

@SuppressLint("MissingPermission")
fun Activity.checkLocation(locationResult: ((Location) -> Unit)) {
    try {

        if (!isGpsOn()) {
            requestDeviceLocationSettings(locationResult)
            return
        }
        getLocationAsset(locationResult)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Lấy lat long, lắng nghe GPS bật
 */
fun Context.getLocationAsset(
    onLocationResult: (Location) -> Unit,
    locationResult: ((Boolean) -> Unit)? = null
) {
    val locationSimpleTracker = LocationSimpleTracker(this, object :
        LocationSimpleTracker.LocationSimpleTrackerListener {
        override fun onLocationResult(location: Location) {
            onLocationResult.invoke(location)
        }
    })

    locationSimpleTracker.let { location ->
        location.detectGPS { isOpen ->
            locationResult?.invoke(isOpen)
            if (isOpen) location.stop()
        }
    }
}


var REQUEST_CHECK_SETTINGS = 1103
var REQUEST_GPS_NOT_OPEN = 1023

/**
 * offGPS -> Hiển thị hộp thoại báo cho user bật định vị
 */

fun Activity.requestDeviceLocationSettings(
    onGPS: (Location) -> Unit,
    offGPS: (() -> Unit)? = null,
) {
    try {

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
            setMinUpdateDistanceMeters(5000f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            try {
//                val response = task.getResult(ApiException::class.java)
//                onGPS.invoke()
                val state = locationSettingsResponse.locationSettingsStates
                val label =
                    "GPS >> (Present: ${state!!.isGpsPresent}  | Usable: ${state.isGpsUsable} ) \n\n" +
                            "Network >> ( Present: ${state.isNetworkLocationPresent} | Usable: ${state.isNetworkLocationUsable} ) \n\n" +
                            "Location >> ( Present: ${state.isLocationPresent} | Usable: ${state.isLocationUsable} )"
                Log.d("addOnSuccessListener", label)

                getLocationAsset(onGPS)

            } catch (exception: ApiException) {
                Log.v(" Failed ", exception.statusCode.toString())
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        //TODO: Cài đặt vị trí không hài lòng. Nhưng có thể được khắc phục bằng cách hiển thị người dùng một hộp thoại. Truyền tới một ngoại lệ có thể giải quyết được.
                        val resolvable = exception as ResolvableApiException
                        //TODO: Hiển thị hộp thoại bằng cách gọi startResolutionForResult () và kiểm tra kết quả trong onActivityResult ().
                        try {
                            resolvable.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                            getLocationAsset(onGPS)
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.d("addOnSuccessListener", " 3")
                    }
                }
                offGPS?.invoke()
            }
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                //TODO: Cài đặt vị trí không hài lòng, nhưng điều này có thể được khắc phục bằng cách hiển thị cho người dùng một hộp thoại.
                try {
                    //TODO: Hiển thị hộp thoại bằng cách gọi startResolutionForResult () và kiểm tra kết quả trong onActivityResult ().
                    exception.startResolutionForResult(
                        this,
                        REQUEST_GPS_NOT_OPEN
                    )

                    getLocationAsset(onGPS)

                } catch (sendEx: IntentSender.SendIntentException) {
                    offGPS?.invoke()
                }
            }
        }

    } catch (e: Exception) {
        offGPS?.invoke()
        e.printStackTrace()
    }
}
