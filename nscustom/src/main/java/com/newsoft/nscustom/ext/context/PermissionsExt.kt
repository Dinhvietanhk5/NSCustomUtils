package com.newsoft.nscustom.ext.context

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.newsoft.nscustom.R
import com.newsoft.nscustom.view.cfalertdialog.CFAlertDialog
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import org.jetbrains.anko.newTask


//TODO ----------------------------------------------------------------- Activity ---------------------------------------------------------------------------------------------
/**
 * Camera Activity permission handler
 */
fun FragmentActivity.handleCameraPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Camera",
        permissions = arrayOf(Manifest.permission.CAMERA),
        onAccepted = onAccepted
    )
}

/**
 * Contacts Activity permission handler
 */
fun FragmentActivity.handleReadContactsPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Read Contacts",
        permissions = arrayOf(Manifest.permission.READ_CONTACTS),
        onAccepted = onAccepted
    )
}

/**
 * NFC Activity permission handler
 */
fun FragmentActivity.handleNFCPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "NFC",
        permissions = arrayOf(Manifest.permission.NFC),
        onAccepted = onAccepted
    )
}

/**
 * Audio Activity permission handler
 */
fun FragmentActivity.handleRecordAudioPermissions(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Record Audio",
        permissions = arrayOf(Manifest.permission.RECORD_AUDIO),
        onAccepted = onAccepted
    )
}

/**
 * Location Activity permission handler
 */
fun FragmentActivity.handleFineLocationPermission(
    onAccepted: (() -> Unit)? = null,
    onDenied: (() -> Unit)? = null,
    openSetting: (() -> Unit)? = null
) {
    handlePermission(
        "Vị trí",
        permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        onAccepted = onAccepted,
        onDenied = onDenied,
        openSetting = openSetting
    )
}

/**
 * SMS Activity permission handler
 */
fun FragmentActivity.handleSendSMSPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Send SMS",
        permissions = arrayOf(Manifest.permission.SEND_SMS),
        onAccepted = onAccepted
    )
}

/**
 * Read phone state Activity permission handler
 */
fun FragmentActivity.handleReadPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Read Phone State",
        permissions = arrayOf(Manifest.permission.READ_PHONE_STATE),
        onAccepted = onAccepted
    )
}

/**
 * Read phone state Activity permission handler
 */
fun FragmentActivity.handleCallPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Call Phone State",
        permissions = arrayOf(Manifest.permission.CALL_PHONE),
        onAccepted = onAccepted
    )
}

/**
 * Write Storage Activity permission handler
 */
fun FragmentActivity.handleWriteStoragePermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    handlePermission(
        "Write Storage",
        *permissions,
        onAccepted = onAccepted,
    )
}

fun FragmentActivity.handlePermission(
    textPermission: String,
    vararg permissions: String,
    onAccepted: (() -> Unit)?,
    onDenied: (() -> Unit)? = null,
    openSetting: (() -> Unit)? = null
) {
    RxPermissions(this)
        .requestEachCombined(*permissions)
        .subscribe { permission: Permission ->  // will emit 2 Permission objects
            if (permission.granted) {
                // `allow.name` được cấp!
                onAccepted?.invoke()
            } else if (permission.shouldShowRequestPermissionRationale) {
                // Bị từ chối cho phép mà không hỏi lại lần nữa
                onDenied?.invoke()
            } else {
                // Bị từ chối quyền với yêu cầu không bao giờ lặp lại
                // Cần đi đến cài đặt
                onOpenDialogSetting(textPermission, openSetting)
            }
        }
}

//TODO ----------------------------------------------------------------- Fragment ---------------------------------------------------------------------------------------------


/**
 * Camera Fragment permission handler
 */
fun Fragment.handleCameraPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Camera",
        permissions = arrayOf(Manifest.permission.CAMERA),
        onAccepted = onAccepted
    )
}

/**
 * Contacts Fragment permission handler
 */
fun Fragment.handleReadContactsPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Read Contacts",
        permissions = arrayOf(Manifest.permission.READ_CONTACTS),
        onAccepted = onAccepted
    )
}

/**
 * NFC Fragment permission handler
 */
fun Fragment.handleNFCPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "NFC",
        permissions = arrayOf(Manifest.permission.NFC),
        onAccepted = onAccepted
    )
}

/**
 * Audio Fragment permission handler
 */
fun Fragment.handleRecordAudioPermissions(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Record Audio",
        permissions = arrayOf(Manifest.permission.RECORD_AUDIO),
        onAccepted = onAccepted
    )
}

/**
 * Location Fragment permission handler
 */
fun Fragment.handleFineLocationPermission(
    onAccepted: (() -> Unit)? = null,
    onDenied: (() -> Unit)? = null,
    openSetting: (() -> Unit)? = null
) {
    handlePermission(
        "Vị trí",
        permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        onAccepted = onAccepted,
        onDenied = onDenied,
        openSetting = openSetting
    )
}

/**
 * SMS Fragment permission handler
 */
fun Fragment.handleSendSMSPermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Send SMS",
        permissions = arrayOf(Manifest.permission.SEND_SMS),
        onAccepted = onAccepted
    )
}

/**
 * Read phone state Fragment permission handler
 */
fun Fragment.handleReadPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Read Phone State",
        permissions = arrayOf(Manifest.permission.READ_PHONE_STATE),
        onAccepted = onAccepted
    )
}

/**
 * Read phone state Fragment permission handler
 */
fun Fragment.handleCallPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    handlePermission(
        textPermission = "Call Phone State",
        permissions = arrayOf(Manifest.permission.CALL_PHONE),
        onAccepted = onAccepted
    )
}

/**
 * Write Storage Fragment permission handler
 */
fun Fragment.handleWriteStoragePermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    handlePermission(
        "Write Storage",
        *permissions,
        onAccepted = onAccepted,
    )
}

fun Fragment.handlePermission(
    textPermission: String,
    vararg permissions: String,
    onAccepted: (() -> Unit)?,
    onDenied: (() -> Unit)? = null,
    openSetting: (() -> Unit)? = null
) {
    RxPermissions(this)
        .requestEachCombined(*permissions)
        .subscribe { permission: Permission ->  // will emit 2 Permission objects
            if (permission.granted) {
                // `allow.name` được cấp!
                onAccepted?.invoke()
            } else if (permission.shouldShowRequestPermissionRationale) {
                // Bị từ chối cho phép mà không hỏi lại lần nữa
                onDenied?.invoke()
            } else {
                // Bị từ chối quyền với yêu cầu không bao giờ lặp lại
                // Cần đi đến cài đặt
                this.requireContext().onOpenDialogSetting(textPermission, openSetting)
            }
        }
}


private fun Context.onOpenDialogSetting(
    textPermission: String,
    openSetting: (() -> Unit)? = null
) {
    val builder = CFAlertDialog.Builder(this)
        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
        .setTitle("Thông báo")
        .setMessage("Vui lòng cấp quyền $textPermission truy cập trong cài đặt. Đi tới quyền -> $textPermission -> cho phép")
        .addButton(
            "Cài đặt",
            ContextCompat.getColor(
                this,
                R.color.cfdialog_positive_button_color
            ),
            ContextCompat.getColor(this, R.color.bg_btn_asset),
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
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.bg_btn_asset),
            CFAlertDialog.CFAlertActionStyle.NEGATIVE,
            CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
        ) { dialog, which ->
            dialog.dismiss()
        }
    builder.show()
}



