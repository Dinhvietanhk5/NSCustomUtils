package com.newsoft.nscustom.ext.context.gps

import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.activity.result.contract.ActivityResultContract

class LocationSettingsContract : ActivityResultContract<Nothing, Nothing>() {

    override fun createIntent(context: Context, input: Nothing?): Intent {
        return Intent(ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Nothing? {
        return null
    }
}