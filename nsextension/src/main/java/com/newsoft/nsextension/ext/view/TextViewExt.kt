package com.newsoft.nsextension.ext.view

import android.widget.TextView

/**
 * Sanitize text
 */
fun TextView.text() = text.toString().trim()
