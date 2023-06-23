package com.newsoft.nsextension.ext.context

import android.view.Window
import android.view.WindowManager

fun Window.setStatusBarWith(color: Int) {
    this.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}