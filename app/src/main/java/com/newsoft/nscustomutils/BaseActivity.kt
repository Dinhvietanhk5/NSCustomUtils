package com.newsoft.nscustomutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newsoft.nscustom.ext.context.switchFragment

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    public fun switchFragment(mainFragment: MainFragment) {
        switchFragment(R.id.container, mainFragment)
    }

    public fun switchFragment(mainFragment: MainFragment, isTaskBack: Boolean) {
        switchFragment(R.id.container, mainFragment, isTaskBack)
    }

}