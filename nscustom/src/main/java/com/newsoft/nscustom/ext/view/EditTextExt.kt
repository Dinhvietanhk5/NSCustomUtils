package com.newsoft.nscustom.ext.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.coroutines.*

/**
 * Check if blank
 */
fun EditText.isBlank(): Boolean = text.toString() == ""

/**
 * Add a text change listener
 * execute [afterTextChanged] when the [EditText]'s text changes
 */
fun EditText.onTextChangeListener(
    beforeTextChanged: ((String) -> Unit)? = null,
    onTextChanged: ((String) -> Unit)? = null,
    afterTextChanged: ((String) -> Unit)? = null
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChanged?.invoke(p0.toString())
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged?.invoke(p0.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged?.invoke(editable.toString())
        }
    })
}


fun Context.setCusor(editText: EditText) {
    editText.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}


var EditText.value
    get() = this.text.toString()
    set(value) {
        this.setText(value)
    }


fun EditText.doAfterTextChangedDelayed(delayMillis: Long = 500, input: (String) -> Unit) {
    var lastInput = ""
    var debounceJob: Job? = null
    val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            if (editable != null) {
                val newtInput = editable.toString()
                debounceJob?.cancel()
                if (lastInput != newtInput) {
                    lastInput = newtInput
                    debounceJob = uiScope.launch {
                        delay(delayMillis)
                        if (lastInput == newtInput) {
                            input(newtInput)
                        }
                    }
                }
            }
        }

        override fun beforeTextChanged(cs: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onClickDrawerEnd(listener: () -> Unit) {
    setOnTouchListener(View.OnTouchListener { _, event ->
//        val DRAWABLE_LEFT = 0
//        val DRAWABLE_TOP = 1
        val DRAWABLE_RIGHT = 2
//        val DRAWABLE_BOTTOM = 3
        try {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= right - compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    listener.invoke()
                    return@OnTouchListener true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        false
    })
}