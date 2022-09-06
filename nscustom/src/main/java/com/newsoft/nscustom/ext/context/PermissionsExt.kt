package com.newsoft.nscustom.ext.context

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.fondesa.kpermissions.extension.*
import com.newsoft.nscustom.R
import com.newsoft.nscustom.view.cfalertdialog.CFAlertDialog
import org.jetbrains.anko.newTask
import java.util.*


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
fun Activity.handleFineLocationPermission(
    onAccepted: (() -> Unit)? = null,
    onDenied: (() -> Unit)? = null,
    openSetting: (() -> Unit)? = null
) {
    handlePermission(
        textPermission = "Vị trí",
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onAccepted = onAccepted,
        onDenied = onDenied,
        openSetting = openSetting
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

/**
 * Read phone state permission handler
 */
fun Activity.handleCallPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Call Phone State",
        permission = Manifest.permission.CALL_PHONE,
        onAccepted = onAccepted
    )
}

fun Activity.handlePermission(
    textPermission: String,
    permission: String,
    onAccepted: (() -> Unit)?,
    onDenied: (() -> Unit)? = null,
    openSetting: (() -> Unit)? = null
) {
    var timerStart = Calendar.getInstance().timeInMillis
    // permission handler
    val permissionRequest = permissionsBuilder(permission).build()
    permissionRequest.onAccepted {
        onAccepted?.invoke()
    }.onDenied {
        permissionRequest.send()
        onDenied?.invoke()
    }.onPermanentlyDenied {
        val timeNow = Calendar.getInstance().timeInMillis
        if (timeNow - timerStart < 500L) {
            val builder = CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Thông báo")
                .setMessage("Vui lòng cấp quyền $textPermission truy cập trong cài đặt. Đi tới quyền -> $textPermission -> cho phép")
                .addButton(
                    "Cài đặt",
                    ContextCompat.getColor(this, R.color.white),
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog, which ->
                    openSetting?.invoke()
                    val intent =
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", packageName, null)
                        )
                    startActivity(intent.newTask())
                    dialog.dismiss()
                }
                .addButton(
                    "Hủy",
                    ContextCompat.getColor(this, R.color.white),
                    ContextCompat.getColor(this, R.color.black),
                    CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog, which ->
                    dialog.dismiss()
                }
            builder.show()
        }
    }.onShouldShowRationale { _, permissionNonce ->
        val timeNow = Calendar.getInstance().timeInMillis
        if (timeNow - timerStart < 500L) {
            // request for permission
            val builder = CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Thông báo")
                .setMessage("Vui lòng cho phép truy cập $textPermission của thiết bị trong mục cài đặt để sử dụng chức năng này!")
                .addButton(
                    "Chấp nhận",
                    ContextCompat.getColor(this, R.color.white),
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog, which ->
                    timerStart = Calendar.getInstance().timeInMillis
                    permissionNonce.use()
                    dialog.dismiss()
                }
                .addButton(
                    "Hủy",
                    ContextCompat.getColor(this, R.color.white),
                    ContextCompat.getColor(this, R.color.black),
                    CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                ) { dialog, which ->
                    dialog.dismiss()
                }
            builder.show()
        }
    }.send()    // check
}