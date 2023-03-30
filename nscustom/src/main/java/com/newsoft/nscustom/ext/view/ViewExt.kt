package com.newsoft.nscustom.ext.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.SystemClock
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.marginTop
import com.jakewharton.rxbinding.view.RxView
import com.newsoft.nscustom.view.cfalertdialog.utils.ViewUtil
import java.util.concurrent.TimeUnit


/**
 * Set double tap listener
 */
@SuppressLint("ClickableViewAccessibility")
fun View.setOnDoubleTapListener(onDoubleTap: () -> Unit) {
    val gestureDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                onDoubleTap.invoke()
                return true
            }
        })
    setOnTouchListener { _, event -> return@setOnTouchListener gestureDetector.onTouchEvent(event) }
}

/**
 * Set safe click listener
 */
fun View.setOnSafeClickListener(onSafeClick: (View?) -> Unit) {
    val safeClickListener = OnSafeClick(1000) { onSafeClick(it) }
    setOnClickListener(safeClickListener)
}

/**
 * set alpha touch feedback
 */
@SuppressLint("ClickableViewAccessibility")
fun View.setAlphaTouchFeedback() {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.alpha = 0.5f
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1f
            }
        }
        false
    }
}

class OnSafeClick(private var defaultInterval: Int, private val onSafeClick: (View?) -> Unit) :
    View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}

fun View.setMarginTop(marginTop: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        marginTop,
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    this.layoutParams = layoutParams
}

fun View.setMarginStart(marginStart: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        marginStart,
        layoutParams.topMargin,
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    this.layoutParams = layoutParams
}

fun View.setMarginEnd(marginEnd: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.marginStart,
        marginEnd,
        layoutParams.bottomMargin
    )
    this.layoutParams = layoutParams
}

fun View.setMarginBottom(bottomMargin: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.marginStart,
        layoutParams.topMargin,
        layoutParams.rightMargin,
        bottomMargin
    )
    this.layoutParams = layoutParams
}

/**
 * Hide soft keyboard
 */
fun View.hideSoftKeyboard() {
    val inputMethodManager =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    try {
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
    }
}

/**
 * Bounce Animation
 */
fun View.bounceAnimation() = AnimatorSet().apply {
    duration = 1000
    playTogether(
        ObjectAnimator.ofFloat(
            this@bounceAnimation,
            "translationY",
            0f,
            0f,
            -60f,
            0f,
            50f,
            -40f,
            0f,
            30f,
            -20f,
            0f,
            0f
        )
    )
}

/**
 * Bounce Effect
 */
fun View.setOnClickBounceEffect(view: ((View) -> Unit)) {
    ViewUtil.addBounceEffect(this)
    setOnClickListener {
        view.invoke(this@setOnClickBounceEffect)
    }
}

fun View.setOnClickRxViewListener(duration: Long = 1, onListener: () -> Unit) {
    RxView.clicks(this)
        .throttleFirst(duration, TimeUnit.SECONDS) // chỉ cho phép click sau mỗi 1 giây
        .subscribe {
            onListener.invoke()
        }
}