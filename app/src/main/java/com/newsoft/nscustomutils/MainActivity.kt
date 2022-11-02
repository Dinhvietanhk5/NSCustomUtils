package com.newsoft.nscustomutils

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.EditText
import com.newsoft.nscustom.ext.context.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        checkHideKeyboardOnTouchScreen(packed)
//        switchFragment(newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE))

//        handleFineLocationPermission({
//            Log.e("handleFineLocationPermission", " 1 ")
//        }, {
//            Log.e("handleFineLocationPermission", " 2 ")
//        }, {
//            Log.e("handleFineLocationPermission", " 3 ")
//        })
//
//        btnNext.setOnClickListener {
////            switchFragmentUpDown(
////                R.id.container,
////                newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE),
////                true
////            )
//            startActivityExt<IntentActivity>()
////            finishActivityExt()
//        }

        val adapter = TestAdapter()
        adapter.apply {
            setRecyclerView(rvList, countTest = 200)
        }

//        edt.setOnTouchListener(OnTouchListener { v, event ->
//            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
//            val DRAWABLE_RIGHT = 2
//            val DRAWABLE_BOTTOM = 3
//            if (event.action == MotionEvent.ACTION_UP) {
//                if (event.rawX <= edt.compoundDrawables[DRAWABLE_LEFT].bounds.width()) {
//                    // your action here
//                    Log.e("getCompoundDrawables", " ")
//                    return@OnTouchListener true
//                }
//            }
//            true
//        })
//        edt.setDrawableRightTouch {
//            Log.e("setDrawableRightTouch", " ")
//        }
//        edt.setOnTouchListener { _, event ->
//            val DRAWABLE_RIGHT = 2
//            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
//            val DRAWABLE_BOTTOM = 3
//            if (event.action == MotionEvent.ACTION_UP) {
//                if (event.rawX >= (edt.right - edt.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
//                    Log.e("setDrawableRightTouch"," ")
//                    true
//                }
//            }
//            false
//        }
//        btnNext.setOnClickListener {
//            handleWriteStoragePermission {
//                Log.e("handleCameraPermission", " ")
//            }
//        }
//        setDateFaceBook(btnNext,"2022-10-18T17:50:53.242Z",DefaultsUtils.DATE_FORMAT_TIME_ZONE)

        edt.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edt.right - edt.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    Log.e("setDrawableRightTouch", " DRAWABLE_RIGHT")
                    return@OnTouchListener true
                }
            }
            false
        })

        Log.e("MainActivity", " ")
    }


    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setDrawableRightTouch(setClickListener: () -> Unit) {
        this.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX <= this.compoundDrawables[DRAWABLE_LEFT].bounds.width()) {
                    setClickListener.invoke()
                    return@OnTouchListener true
                }
                if (event.rawX >= this.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    setClickListener.invoke()
                    return@OnTouchListener true
                }
            }
            true
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_FINISH_ACTIVITY) {
            val intet = data?.getIntExtra("intemnt", 0)
            Log.e("onActivityResultMain", " $resultCode $intet")
        }
    }
}