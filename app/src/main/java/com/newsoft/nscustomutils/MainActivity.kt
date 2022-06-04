package com.newsoft.nscustomutils

import android.os.Bundle
import com.newsoft.nscustom.ext.context.newInstance

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchFragment(newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE))
    }
}