package com.newsoft.nscustomutils

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.newsoft.nscustom.constants.DefaultsUtils
import com.newsoft.nscustom.ext.context.*
import com.newsoft.nscustom.ext.value.convertDateSend
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
//            switchFragmentUpDown(
//                R.id.container,
//                newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE),
//                true
//            )
            startActivityExt<IntentActivity>()
//            finishActivityExt()
        }

        Log.e(
            "MainActivity",
            "${convertDateSend("2022-10-04T16:43:39+0700","yyyy-MM-dd'T'HH:mm:ssZ", DefaultsUtils.DATE_FORMAT2)}"
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_FINISH_ACTIVITY) {
            val intet = data?.getIntExtra("intemnt", 0)
            Log.e("onActivityResultMain", " $resultCode $intet")

        }
    }
}