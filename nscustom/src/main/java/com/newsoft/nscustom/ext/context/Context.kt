package com.newsoft.nscustom.ext.context

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.newsoft.nscustom.constants.ResultCode
import com.newsoft.nscustom.ext.value.capitalize
import com.newsoft.nscustom.ext.value.precedeLinkWithHttp
import com.newsoft.nscustom.ext.value.precedeLinkWithHttps
//import com.pawegio.kandroid.fromApi


/**
 * Open [url] in browser
 */
fun Activity.openLinkInBrowser(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW, Uri.parse(
                try {
                    url.precedeLinkWithHttps()
                } catch (e: Exception) {
                    url.precedeLinkWithHttp()
                }
            )
        )
    )
}

/**
 * Open app in play store
 */
fun Context.openAppInPlayStore() {
    val uri = Uri.parse("market://details?id=$packageName")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    // To count with Play market backstack, After pressing back button,
    // to taken back to our application, we need to add following flags to intent.
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

/**
 * Get default status bar height
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

data class ButtonSpecs(
    val text: String? = null,
    val handler: (() -> Unit)? = null
)


/**
 * Open Gallery and select media
 */
fun Fragment.choosePhotoFromGallery(resultCode: Int?) {
    val galleryIntent = Intent(
        Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
    this.startActivityForResult(
        galleryIntent,
        resultCode ?: ResultCode.GALLERY_PICK
    )
}

/**
 * Open camera and take photo
 */
fun Fragment.takePhotoFromCamera(resultCode: Int?) {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    this.startActivityForResult(
        cameraIntent,
        resultCode ?: ResultCode.TAKE_PHOTO
    )
}

/**
 * Get Device Name
 */
fun getDeviceName(): String? {
    val manufacturer: String = Build.MANUFACTURER
    val model: String = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        capitalize(model)
    } else capitalize(manufacturer).toString() + " " + model
}



///**
// * Set Activity to fullscreen and status bar icons to black
// */
//@RequiresApi(Build.VERSION_CODES.M)
//fun Activity.setToFullScreenAndLightStatusBar() {
//    window.apply {
//        decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        fromApi(23, true) { decorView.systemUiVisibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR }
//        statusBarColor = Color.TRANSPARENT
//    }
//}
/**
 * Get color resource
 */
fun Context.getCompatColor(colorRes: Int) = ContextCompat.getColor(this, colorRes)

/**
 * Get drawable resource
 */
fun Context.getCompatDrawable(drawableRes: Int) =
    ContextCompat.getDrawable(this, drawableRes)


/**
 * Check if internet is available
 * returns [Boolean]
 */

//fun Context.isInternetAvailable(): Boolean {
//    val connectivityManager =
//        getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as  _root_ide_package_.android.net.ConnectivityManager
//    val netInfo = connectivityManager.activeNetworkInfo
//    return netInfo != null && netInfo.isConnected
//}

/**
 * Handle connection availability
 * @param available
 * @param unavailable
 */
//fun Context.internetConnectivityHandler(
//    available: (() -> Unit),
//    unavailable: (() -> Unit)? = null
//) {
//    if (isInternetAvailable()) available.invoke() else unavailable?.invoke()
//}
//
//
//fun Context.getConnectionType(): String {
//    if (!isInternetAvailable())
//        return "-"
//
//    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as _root_ide_package_.android.net.ConnectivityManager
//    return when (cm.activeNetworkInfo?.type) {
//        _root_ide_package_.android.net.ConnectivityManager.TYPE_WIFI -> "WiFi"
//        _root_ide_package_.android.net.ConnectivityManager.TYPE_MOBILE -> "Cellular"
//        else -> "-"
//    }
//}


//
//inline fun <reified T : Activity> View.startActivity(vararg params: Pair<String, Any?>) =
//    AnkoInternals.internalStartActivity(context, T::class.java, params)
//
//inline fun <reified T : Activity> Activity.startActivityForResult(
//    requestCode: Int,
//    vararg params: Pair<String, Any?>
//) =
//    AnkoInternals.internalStartActivityForResult(this, T::class.java, requestCode, params)
//
//inline fun <reified T: Service> Fragment.startService(vararg params: Pair<String, Any>) =
//    AnkoInternals.internalStartService(context!!, T::class.java, params)
//
//inline fun <reified T : Service> View.startService(vararg params: Pair<String, Any?>) =
//    AnkoInternals.internalStartService(context, T::class.java, params)
//
//inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
//    AnkoInternals.internalStopService(this, T::class.java, params)
//
//inline fun <reified T : Service> Fragment.stopService(vararg params: Pair<String, Any?>) =
//    AnkoInternals.internalStopService(context!!, T::class.java, params)
//
//inline fun <reified T : Service> View.stopService(vararg params: Pair<String, Any?>) =
//    AnkoInternals.internalStopService(context, T::class.java, params)
//
//inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
//    AnkoInternals.createIntent(this, T::class.java, params)
//
//inline fun <reified T: Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
//    AnkoInternals.createIntent(context!!, T::class.java, params)
//
//inline fun <reified T : Any> View.intentFor(vararg params: Pair<String, Any?>): Intent =
//    AnkoInternals.createIntent(context, T::class.java, params)