package com.newsoft.nscustom.ext.context

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.fondesa.kpermissions.extension.*

/**
 * Camera permission handler
 */
fun Activity.handleCameraPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Camera",
        permission = Manifest.permission.CAMERA,
        onAccepted = onAccepted
    )
}

/**
 * Contacts permission handler
 */
fun Activity.handleReadContactsPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Read Contacts",
        permission = Manifest.permission.READ_CONTACTS,
        onAccepted = onAccepted
    )
}

/**
 * NFC permission handler
 */
fun Activity.handleNFCPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "NFC",
        permission = Manifest.permission.NFC,
        onAccepted = onAccepted
    )
}

/**
 * Audio permission handler
 */
fun Activity.handleRecordAudioPermissions(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Record Audio",
        permission = Manifest.permission.RECORD_AUDIO,
        onAccepted = onAccepted
    )
}

/**
 * Write Storage permission handler
 */
fun Activity.handleWriteStoragePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Write Storage",
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onAccepted = onAccepted
    )
}

/**
 * Location permission handler
 */
fun Activity.handleFineLocationPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Location",
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onAccepted = onAccepted
    )
}

/**
 * SMS permission handler
 */
fun Activity.handleSendSMSPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Send SMS",
        permission = Manifest.permission.SEND_SMS,
        onAccepted = onAccepted
    )
}

/**
 * Read phone state permission handler
 */
fun Activity.handleReadPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Read Phone State",
        permission = Manifest.permission.READ_PHONE_STATE,
        onAccepted = onAccepted
    )
}

fun Activity.handlePermission(
    textPermission: String,
    permission: String,
    onAccepted: (() -> Unit)?
) {
    // permission handler
    val permissionRequest = permissionsBuilder(permission).build()
    permissionRequest.onAccepted {
        onAccepted?.invoke()
    }.onDenied {
        permissionRequest.send()
    }.onPermanentlyDenied {
        // prompt user to update permissions in settings
//        showIndefiniteSnackbar(
//            message = "$textPermission access required. Go to Permissions -> Switch $textPermission ON",
//            actionText = "GO TO\nSETTINGS",
//            actionHandler = {
//                val intent =
//                    Intent(
//                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                        Uri.fromParts("package", packageName, null)
//                    )
//                startActivity(intent.newTask())
//            })
    }.onShouldShowRationale { _, permissionNonce ->
        // request for permission
//        showIndefiniteSnackbar(
//            message = "$textPermission access required",
//            actionText = "REQUEST\nPERMISSION",
//            actionHandler = { permissionNonce.use() })
    }.send()    // check
}
