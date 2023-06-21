package com.newsoft.nscustom.ext.value

import java.util.*

/**
 * Forged by edryn on 3/26/19.
 */

/**
 * Convert calendar month [Int] to [String] MMM
 */
fun Int.toMonthString(): String {
	return when (this) {
		Calendar.JANUARY -> "January"
		Calendar.FEBRUARY -> "February"
		Calendar.MARCH -> "March"
		Calendar.APRIL -> "April"
		Calendar.MAY -> "May"
		Calendar.JUNE -> "Jun"
		Calendar.JULY -> "July"
		Calendar.AUGUST -> "August"
		Calendar.SEPTEMBER -> "September"
		Calendar.OCTOBER -> "October"
		Calendar.NOVEMBER -> "November"
		Calendar.DECEMBER -> "December"
		else -> ""
	}
}

/**
 * Convert [Int] figures to words
 */
fun Int.toEnglishWords(): String {
	val units = arrayOf(
		"",
		"One",
		"Two",
		"Three",
		"Four",
		"Five",
		"Six",
		"Seven",
		"Eight",
		"Nine",
		"Ten",
		"Eleven",
		"Twelve",
		"Thirteen",
		"Fourteen",
		"Fifteen",
		"Sixteen",
		"Seventeen",
		"Eighteen",
		"Nineteen")
	val tens = arrayOf(
		"", // 0
		"", // 1
		"Twenty", // 2
		"Thirty", // 3
		"Forty", // 4
		"Fifty", // 5
		"Sixty", // 6
		"Seventy", // 7
		"Eighty", // 8
		"Ninety"    // 9
	)
	return when {
		this < 20 -> units[this]
		this < 100 -> tens[this / 10] + (if (this % 10 > 0) " " + (this % 10).toEnglishWords() else "")
		this < 1000 -> units[this / 100] + " Hundred" + (if (this % 100 > 0) " and " + (this % 100).toEnglishWords() else "")
		this < 1000000 -> (this / 1000).toEnglishWords() + " Thousand" + (if (this % 1000 > 0) " " + (this % 1000).toEnglishWords() else "")
		else -> (this / 1000000).toEnglishWords() + " Million" + (if (this % 1000000 > 0) " " + (this % 1000000).toEnglishWords() else "")
	}
}

fun Int.IPToString(): String {
	return (this and 0xFF).toString() + "." +
			(this shr 8 and 0xFF) + "." +
			(this shr 16 and 0xFF) + "." +
			(this shr 24 and 0xFF)
}