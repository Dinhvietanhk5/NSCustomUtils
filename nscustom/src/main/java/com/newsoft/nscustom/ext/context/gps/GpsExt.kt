package com.newsoft.nscustom.ext.context.gps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.newsoft.nscustom.view.cfalertdialog.CFAlertDialog


fun Context.isGpsOn(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.turnGPSOn() {
    val provider =
        Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
//    if (!provider.contains("gps")) { //if gps is disabled
    val poke = Intent()
    poke.setClassName(
        "com.android.settings",
        "com.android.settings.widget.SettingsAppWidgetProvider"
    )
    poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
    poke.data = Uri.parse("3")
    sendBroadcast(poke)
//    }
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

//@SuppressLint("MissingPermission")
//fun Activity.getLocationAsset(
//    locationResult: ((Location) -> Unit)
//) {
////    val mLocationManager = getSystemService(RxAppCompatActivity.LOCATION_SERVICE) as LocationManager
////    mLocationManager.requestLocationUpdates(
////        LocationManager.GPS_PROVIDER,
////        0L,
////        0f
////    ) {
////        Log.e("getLocationAsset", " 1")
////        locationResult.invoke(it)
////    }
//
//    Log.e("getLocationAsset"," ")
//
//    var location: Location? = null
//
//    val locationManager =
//        getSystemService(RxAppCompatActivity.LOCATION_SERVICE) as LocationManager
//    val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//    lastKnownLocation?.let {
//        Log.e("getLocationAsset 1"," ${it.latitude}")
//        location = it
//        locationResult.invoke(it)
//    }
//
////    if (location == null) {
//        val fusedLocationClient =
//            LocationServices.getFusedLocationProviderClient(this)
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                location?.let {
//                    Log.e("getLocationAsset 2"," ${it.latitude}")
//                    locationResult.invoke(location)
//                }
//            }
////    }
//}


var enableLocationRetryCount = 1
inline fun Fragment.enableGPS(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
): ActivityResultLauncher<Nothing> = registerForActivityResult(
    LocationSettingsContract()
) {
    if (enableLocationRetryCount <= 2) {
        onLocationGranted.invoke()
        enableLocationRetryCount++
    } else {
        onDenied.invoke()
        enableLocationRetryCount = 1
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

inline fun FragmentActivity.enableGPS(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
): ActivityResultLauncher<Nothing> = registerForActivityResult(
    LocationSettingsContract()
) {
    if (enableLocationRetryCount <= 2) {
        onLocationGranted.invoke()
        enableLocationRetryCount++
    } else {
        onDenied.invoke()
        enableLocationRetryCount = 1
    }
}

var REQUEST_CHECK_SETTINGS = 1103
var REQUEST_GPS_NOT_OPEN = 1023

fun Activity.requestDeviceLocationSettings(
    onGPS: (Location) -> Unit,
    offGPS: (() -> Unit)? = null
) {
    try {

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

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
                dialogNotificationLocation()
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
                    dialogNotificationLocation()
                    offGPS?.invoke()
                }
            }
        }
    } catch (e: Exception) {
        offGPS?.invoke()
        e.printStackTrace()
    }
}

/**
 * Hiển thị hộp thoại báo cho user bật định vị
 */
fun Context.dialogNotificationLocation() {
    val builder = CFAlertDialog.Builder(this)
        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
        .setTitle("Thông báo")
        .setMessage("Vui lòng bật định vị trên thiết bị để sử dụng dịch vụ !")
    builder.show()
}