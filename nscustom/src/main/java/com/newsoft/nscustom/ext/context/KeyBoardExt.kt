package com.newsoft.nscustom.ext.context

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Open soft keyboard and focus [view]
 */
fun Activity.showSoftKeyboard(view: View? = null) {
    view?.requestFocus()
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

/**
 * Hide soft keyboard
 */
fun Activity.hideSoftKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    try {
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    } catch (e: Exception) {
    }
}

/**
 * Hide keyboard when another uneditable view is touched
 */
fun Context.hidesSoftInputOnTouch(rootLayout: ViewGroup) {
    rootLayout.apply {
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.rootView.windowToken, 0)
            }
        }
    }
}

/**
 * Listen for keyboard state changes
 */
fun Context.softInputStateChangeListener(viewGroup: ViewGroup, shown: () -> Unit, hidden: () -> Unit) {
    with(viewGroup) {
        viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            viewGroup.getWindowVisibleDisplayFrame(r)

            val screenHeight = this.rootView.height
            val heightDiff = viewGroup.rootView.height - (r.bottom - r.top)
            if (heightDiff > screenHeight * 0.15) {
                shown()
            } else {
                hidden()
            }
        }
    }
}

/**
 * ẩn bàn phím khi click vào màn hình (ko click vào edt)
 */

@SuppressLint("ClickableViewAccessibility")
fun Activity.checkHideKeyboardOnTouchScreen(view: View){
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            hideSoftKeyboard()
            false
        }
    }
}