package com.newsoft.nscustomutils

import android.os.Bundle
import android.util.Log
import com.newsoft.nscustom.ext.context.checkHideKeyboardOnTouchScreen
import com.newsoft.nscustom.ext.context.handleFineLocationPermission
import com.newsoft.nscustom.ext.context.newInstance
import com.newsoft.nscustom.ext.context.switchFragmentUpDown
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        checkHideKeyboardOnTouchScreen(packed)
//        switchFragment(newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE))

        handleFineLocationPermission({
            Log.e("handleFineLocationPermission", " 1 ")
        }, {
            Log.e("handleFineLocationPermission", " 2 ")
        }, {
            Log.e("handleFineLocationPermission", " 3 ")
        })
        btnNext.setOnClickListener {
            switchFragmentUpDown(
                R.id.container,
                newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE),
                true
            )
        }

    }
}