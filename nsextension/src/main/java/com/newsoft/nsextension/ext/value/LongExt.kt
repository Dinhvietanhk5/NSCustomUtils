package com.newsoft.nsextension.ext.value

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Forged by edryn on 3/26/19.
 */

fun Long.isCallValid(): Boolean = Calendar.getInstance().timeInMillis in this..this + 30000

fun Long.secondsToMillis(): Long = this * 1000

fun Long.minutesToMillis(): Long = secondsToMillis() * 60

fun Long.hoursToMillis(): Long = minutesToMillis() * 60

fun Long.millisToReadableElapsedTime(): String =
	try {
		val s = this / 1000
		val m = s / 60
		val h = m / 60
		val d = h / 24

		if (m < 1) "$s second${if (s == 1.toLong()) "" else "s"} ago"
		if (h < 1) "$m minute${if (m == 1.toLong()) "" else "s"} ago"
		if (d < 1) "$h hour${if (h == 1.toLong()) "" else "s"}, ${(m % 60)} minute${if ((m % 60) == 1.toLong()) "" else "s"} ago"
		"$d day${if (d == 1.toLong()) "" else "s"}, ${(h % 24)} hour${if ((h % 24) == 1.toLong()) "" else "s"}, ${(m % 60)} minute${if ((m % 60) == 1.toLong()) "" else "s"} ago"
	} catch (e: Exception) {
		this.toString()
	}

fun Long.formatToDigitalClock(): String {
	val hours = TimeUnit.MILLISECONDS.toHours(this).toInt() % 24
	val minutes = TimeUnit.MILLISECONDS.toMinutes(this).toInt() % 60
	val seconds = TimeUnit.MILLISECONDS.toSeconds(this).toInt() % 60
	return when {
		hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
		minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
		seconds > 0 -> String.format("00:%02d", seconds)
		else -> {
			"00:00"
		}
	}
}