package com.newsoft.nscustom.ext.view

import android.widget.TextView

/**
 * Sanitize text
 */
fun TextView.text() = text.toString().trim()
