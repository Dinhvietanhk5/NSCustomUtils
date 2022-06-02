package com.newsoft.nscustom.ext.view

import android.widget.SeekBar


fun SeekBar.setChangeListener(
    onSyncChanged: ((Int) -> Unit)) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onSyncChanged(progress)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}
