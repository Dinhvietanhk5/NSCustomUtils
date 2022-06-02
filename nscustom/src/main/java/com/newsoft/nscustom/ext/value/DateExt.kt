package com.newsoft.nscustom.ext.value

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import com.newsoft.nscustom.constants.Defaults
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.dateString() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    SimpleDateFormat(Defaults.DATE_TIME_SERVER_FORMAT, Locale.getDefault()).format(this).orEmpty()
} else {
    TODO("VERSION.SDK_INT < N")
}

fun Date.getFormattedTime() = SimpleDateFormat(Defaults.TIME_FORMAT_12, Locale.getDefault()).format(this).orEmpty()

fun Date.getFormattedDateTime() = SimpleDateFormat(Defaults.DATE_TIME_FORMAT_COMPLETE, Locale.getDefault()).format(this).orEmpty()

fun Date.getFormattedDate() = SimpleDateFormat(Defaults.DATE_FORMAT_COMPLETE, Locale.getDefault()).format(this).orEmpty()

fun Date.getSimpleFormattedDate() = SimpleDateFormat(Defaults.DATE_FORMAT_SIMPLE, Locale.getDefault()).format(this).orEmpty()

fun Date.getDefaultFormattedDate() = SimpleDateFormat(Defaults.DATE_FORMAT, Locale.getDefault()).format(this).orEmpty()

@SuppressLint("SimpleDateFormat")
fun convertDateSend(Date: String?): String {   //TODO: convert date hiển thị để gửi lên server
    val tz = Calendar.getInstance().timeZone
    var converted_date = ""
    try {
        val utcFormat = SimpleDateFormat(Defaults.DATE_FORMAT2)
        val date = utcFormat.parse(Date)

        val currentTFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        converted_date = currentTFormat.format(date)
    } catch (e: java.lang.Exception) {
        Log.e("convertDateUtcToString", "error" + e.message.toString())
    }
    return converted_date
}

@SuppressLint("SimpleDateFormat")
fun millisToDate(millis: Long): String {
    return SimpleDateFormat("dd-MM-yyyy").format(Date(millis))
}

@SuppressLint("SimpleDateFormat")
fun getDateNow(): String? {
    val c = Calendar.getInstance()
    val df = SimpleDateFormat(Defaults.DATE_FORMAT2)
    return df.format(c.time)
}
