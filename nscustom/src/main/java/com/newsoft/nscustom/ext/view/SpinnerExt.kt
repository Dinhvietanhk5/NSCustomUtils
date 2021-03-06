package com.newsoft.nscustom.ext.view

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner


fun Spinner.onItemSelectedListener (
    onItemSelected: ((Int) -> Unit)) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(position)
        }
    }
}
