package com.newsoft.nscustom.ext.value

import java.lang.Exception
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Format a double to set a number of [digits]
 * to the right of decimal point
 */
fun Double.format(digits: Int = 2): String = String.format(Locale.US, "%.${digits}f", this)

/**
 * Get the whole number of a double
 */
fun Double.toWholeNumber() = toString().split(".").first().toInt()


fun Double.formatToGBP(): String = "£${this.format()}"
fun Double.formatToGBPWithPlus(): String = "+£${this.format()}"

fun Double.formatNumber(number: Int): Double? {
    return try {
        if (number < 0 && number > -1000 || number in 1..999) {
            return number.toDouble()
        }
        val formatter: NumberFormat = DecimalFormat("###,###")
        var resp = formatter.format(number.toLong())
        resp = resp.replace(",".toRegex(), ".")
        resp.toDouble()
    } catch (e: Exception) {
        0.0
    }
}
