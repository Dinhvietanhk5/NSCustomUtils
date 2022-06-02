package com.newsoft.nscustom.ext.value

import android.content.Context
import android.util.Log
import java.lang.Exception
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Convert pixel to density pixel value
 */
fun Number.pxToDp(context: Context) = this.toFloat() / context.resources.displayMetrics.density

/**
 * Convert density pixel to pixel value
 */
fun Number.dpToPx(context: Context) = this.toFloat() * context.resources.displayMetrics.density

/**
 * Convert pixel to scaled pixel value
 */
fun Number.pxToSp(context: Context) =
    this.toFloat() / context.resources.displayMetrics.scaledDensity

/**
 * Convert scaled pixel to pixel value
 */
fun Number.spToPx(context: Context) =
    this.toFloat() * context.resources.displayMetrics.scaledDensity

fun formatNumber(number: Int): String? {
    return try {
        if (number < 0 && number > -1000 || number in 1..999) {
            return number.toString()
        }
        val formatter: NumberFormat = DecimalFormat("###,###")
        var resp = formatter.format(number.toLong())
        resp = resp.replace(",".toRegex(), ".")
        resp
    } catch (e: Exception) {
        Log.e("formatNumber", e.message!!)
        "0"
    }
}