package com.newsoft.nscustom.ext.value

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.TextView
import com.newsoft.nscustom.constants.DefaultsUtils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun convertDate(
    date: String,
    formatDateIn: String = DefaultsUtils.DATE_FORMAT_TIME_ZONE,
    formatDateOut: String = DefaultsUtils.DATE_FORMAT3
): String {
    val tz = Calendar.getInstance().timeZone
    var converted_date = ""
    try {
        val utcFormat = SimpleDateFormat(formatDateIn)
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        utcFormat.toLocalizedPattern()
        val dateOut = utcFormat.parse(date)!!
        val currentTFormat: DateFormat = SimpleDateFormat(formatDateOut)
        currentTFormat.timeZone = TimeZone.getTimeZone(tz.id)
        converted_date = currentTFormat.format(dateOut)
    } catch (e: java.lang.Exception) {
        Log.e("convertDateUtcToString", "error" + e.message)
    }
    return converted_date
}

@SuppressLint("SimpleDateFormat")
fun convertStringDateToLongDate(
    millis: Long,
    dateFormat: String = DefaultsUtils.DATE_FORMAT
): String {
    return SimpleDateFormat(dateFormat).format(Date(millis))
}

@SuppressLint("SimpleDateFormat")
fun getStringDateNow(dateFormat: String = DefaultsUtils.DATE_FORMAT): String? {
    val c = Calendar.getInstance()
    val df = SimpleDateFormat(dateFormat)
    return df.format(c.time)
}

fun secondsToString(seconds: Int): String {
    // convert giờ:phút theo int
    return String.format("%02d:%02d", seconds / 60, seconds % 60)
}

fun getLongDateNow(): Long {
    val date = Date(System.currentTimeMillis())
    return date.time / 1000
}

@SuppressLint("SimpleDateFormat")
fun convertDateStringToDateLong(st_time: String, formatDate: String): Long {
    // lấy thời gian theo string dạng long
    val df = SimpleDateFormat(formatDate)
    df.timeZone = TimeZone.getTimeZone("UTC")
    df.toLocalizedPattern() // lấy giờ theo vị trí
    return try {
        val time = df.parse(st_time)!!
        time.time / 1000
    } catch (e: ParseException) {
        0
    }
}

@SuppressLint("SimpleDateFormat")
fun convertStringDateToDate(
    time: String,
    formatDate: String = DefaultsUtils.DATE_FORMAT_TIME_ZONE2
): Date? {
    val utcFormat = SimpleDateFormat(formatDate)
    utcFormat.timeZone = TimeZone.getTimeZone("UTC")
    utcFormat.toLocalizedPattern()
    return try {
        utcFormat.parse(time)
    } catch (e: ParseException) {
        Log.e("getDateToString", "error" + e.message)
        null
    }
}

// lấy thứ trong tuần
fun getDayOfWeek(time: String, formatDateIn: String): String {
    val calendar = Calendar.getInstance()
    calendar.time = convertStringDateToDate(time = time, formatDateIn)!!
    return calendar[Calendar.DAY_OF_WEEK].toString()
}

@SuppressLint("SetTextI18n")
fun setDateFaceBook(
    textView: TextView,
    time: String,
    formatDateIn: String = DefaultsUtils.DATE_FORMAT_TIME_ZONE,
    formatDateOut: String = DefaultsUtils.DATE_FORMAT3,
) {
    try {
        val time_total = getLongDateNow() - convertDateStringToDateLong(time, formatDateIn)
        //        Log.e("getTimeToString",""+getTimeToString(time));
        var set_times = ""
        if (time_total < 3600) {
            // phút:giây
            set_times = secondsToString(time_total.toInt())
            val minute = set_times.split(":".toRegex()).toTypedArray()
            try {
                if (minute[0] == "00" || minute[0].toInt() < 0) textView.text = "vừa xong"
                else textView.text = Integer.valueOf(minute[0]).toString() + " phút trước"
            } catch (e: Exception) {
                textView.text = "vừa xong"
                e.printStackTrace()
            }

        } else if (time_total < 86400) {
            // giờ:phút
            set_times = secondsToString(time_total.toInt() / 60)
            val hours = set_times.split(":".toRegex()).toTypedArray()
            textView.text = Integer.valueOf(hours[0]).toString() + " giờ trước"
        } else {
//                Log.e("time",""+time);
//            Log.e("convertDateUtcToStringtime",convertDateUtcToString(time));
            val day =
                convertDate(time, formatDateIn, formatDateOut).substring(6, 16).split("/".toRegex())
                    .toTypedArray()
            if (time_total < 172800) // 1 ngày
                textView.text = "hôm qua" else textView.text =
                "Thứ " + getDayOfWeek(time, formatDateIn) + ", " + Integer.valueOf(
                    day[0]
                ) + " Thg " + +day[1].toInt()

//            Log.e("textView",""+day[0] +" "+ day[1]);
//            Log.e("năm",""+Integer.valueOf(day[0]) + " Thg "+ +Integer.valueOf(day[1])+", "+day[2]);
//            Log.e("tuần",""+Integer.valueOf(day[0]) + " Thg "+ +Integer.valueOf(day[1]));
        }
        //        else if (time_total >= 604800){
//            // ngày trong tháng
//            String day [] = convertDateUtcToString(time).substring(7,11).split("/");
//
//            textView.setText(Integer.valueOf(day[0]) + " Thg "+ +Integer.valueOf(day[1]));
//        }else if (time_total >= 2592000){
//            // tháng
//        }
    } catch (ignored: Exception) {
    }
}