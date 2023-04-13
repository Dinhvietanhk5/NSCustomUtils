package com.newsoft.nscustom.ext

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import com.newsoft.permission.PermissionX
import com.newsoft.permission.callback.ExplainReasonCallback
import com.newsoft.permission.request.ExplainScope
import com.newsoft.permission.request.ForwardScope


/**
 * Camera permission handler
 */
fun AppCompatActivity.handleCameraPermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.CAMERA)
    handlePermissionExplainBeforeRequest(
        *permissions, onAccepted = onAccepted
    )
}

/**
 * Contacts permission handler
 */
fun AppCompatActivity.handleReadContactsPermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.READ_CONTACTS)
    handlePermissionExplainBeforeRequest(
        *permissions,
        onAccepted = onAccepted
    )
}

/**
 * NFC permission handler
 */
fun AppCompatActivity.handleNFCPermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.NFC)
    handlePermissionExplainBeforeRequest(
        *permissions, onAccepted = onAccepted
    )
}

/**
 * Audio permission handler
 */
fun AppCompatActivity.handleRecordAudioPermissions(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    handlePermissionExplainBeforeRequest(
        *permissions,
        onAccepted = onAccepted
    )
}

/**
 * Write Storage permission handler
 */
fun AppCompatActivity.handleWriteStoragePermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    handlePermissionExplainBeforeRequest(
        *permissions,
        onAccepted = onAccepted
    )
}

/**
 * Location permission handler
 */
fun AppCompatActivity.handleFineLocationPermission(
    onAccepted: (() -> Unit)? = null,
    onDenied: (() -> Unit)? = null,
    onExplainRequest: ((ExplainScope, List<String>, Boolean) -> Unit)? = null
) {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    handlePermissionExplainBeforeRequest(
        *permissions,
        onAccepted = onAccepted,
        onDenied = onDenied,
        onExplainRequest = onExplainRequest
    )
}

/**
 * SMS permission handler
 */
fun AppCompatActivity.handleSendSMSPermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.SEND_SMS)
    handlePermissionExplainBeforeRequest(
        *permissions,
        onAccepted = onAccepted
    )
}

/**
 * Read phone state permission handler
 */
fun AppCompatActivity.handleReadPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
    handlePermissionExplainBeforeRequest(*permissions, onAccepted = onAccepted)
}

/**
 * Read phone state permission handler
 */
fun AppCompatActivity.handleCallPhoneStatePermission(onAccepted: (() -> Unit)? = null) {
    val permissions = arrayOf(Manifest.permission.CALL_PHONE)
    handlePermissionExplainBeforeRequest(*permissions, onAccepted = onAccepted)
}

/**
 * Explain Before Request
 */

@SuppressLint("NewApi")
fun AppCompatActivity.handlePermissionExplainBeforeRequest(
    vararg permissions: String,
    onAccepted: (() -> Unit)?,
    onDenied: (() -> Unit)? = null,
    onExplainRequest: ((ExplainScope, List<String>, Boolean) -> Unit)? = null,
    onForwardToSettings: ((ForwardScope, List<String>) -> Unit)? = null,
    messageExplainRequestReason: String = "Permission needs following permissions to continue",
    messageForwardToSettings: String = "You need to allow necessary permissions in Settings manually",
    deny: String = "Deny",
    allow: String = "Allow",
    ok: String = "OK",
    cancel: String = "Cancel",
) {
    PermissionX.init(this).permissions(*permissions).explainReasonBeforeRequest()
        .explainReasonBeforeRequest()
        .onExplainRequestReason { scope, deniedList, beforeRequest ->
            if (onExplainRequest != null)
                onExplainRequest.invoke(scope, deniedList, beforeRequest)
            else
                scope.showRequestReasonDialog(deniedList, messageExplainRequestReason, allow, deny)
        }.onForwardToSettings { scope, deniedList ->
            if (onForwardToSettings != null)
                onForwardToSettings.invoke(scope, deniedList)
            else
                scope.showForwardToSettingsDialog(
                    deniedList,
                    messageForwardToSettings,
                    ok,
                    cancel
                )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                onAccepted?.invoke()
            } else {
//                requestPermissions(deniedList.toTypedArray(), 1)
                onDenied?.invoke()
            }
        }

}

/**
 * Explain After Request
 */

@SuppressLint("NewApi")
fun AppCompatActivity.handlePermissionExplainAfterRequest(
    vararg permissions: String,
    onAccepted: (() -> Unit)?,
    onDenied: (() -> Unit)? = null,
    onExplainRequest: ((ExplainScope, List<String>) -> Unit)? = null,
    onForwardToSettings: ((ForwardScope, List<String>) -> Unit)? = null,
    messageExplainRequestReason: String = "Permission needs following permissions to continue",
    messageForwardToSettings: String = "You need to allow necessary permissions in Settings manually",
    deny: String = "Deny",
    allow: String = "Allow",
    ok: String = "OK",
    cancel: String = "Cancel",
) {
    PermissionX.init(this).permissions(*permissions).explainReasonBeforeRequest()
        .onExplainRequestReason { scope, deniedList ->
            if (onExplainRequest != null)
                onExplainRequest.invoke(scope, deniedList)
            else
                scope.showRequestReasonDialog(deniedList, messageExplainRequestReason, allow, deny)
        }.onForwardToSettings { scope, deniedList ->
            if (onForwardToSettings != null)
                onForwardToSettings.invoke(scope, deniedList)
            else
                scope.showForwardToSettingsDialog(
                    deniedList,
                    messageForwardToSettings,
                    ok,
                    cancel
                )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                onAccepted?.invoke()
            } else {
                requestPermissions(deniedList.toTypedArray(), 1)
                onDenied?.invoke()
            }
        }

}

@SuppressLint("NewApi")
fun AppCompatActivity.handlePermission(
    vararg permissions: String,
    onAccepted: (() -> Unit)?,
    onDenied: (() -> Unit)? = null
) {
    PermissionX.init(this)
        .permissions(*permissions)
        .request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                onAccepted?.invoke()
            } else {
                requestPermissions(deniedList.toTypedArray(), 1)
                onDenied?.invoke()
            }
        }

}