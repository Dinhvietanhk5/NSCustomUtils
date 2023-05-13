package com.newsoft.nscustomutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.newsoft.nscustom.ext.context.*
import com.newsoft.nscustom.ext.context.launcher_result.BetterActivityResult
import com.newsoft.nsdialog.CFAlertDialog


class MainActivity : BaseActivity() {

//    protected val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
//        BetterActivityResult.r

    protected val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    @SuppressLint("ClickableViewAccessibility", "WrongThread")
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
//        image.setOnClickListener {
////            switchFragmentUpDown(
////                R.id.container,
////                newInstance<MainFragment>("type" to TypeConnectEnums.NEW_INVITE),
////                true
////            )
////            startActivityExt<IntentActivity>()
////            finishActivityExt()
//            Log.e("validate","${edt.validate()}")
//
//        }

//        val imageStream = this.resources.openRawResource(R.raw.joda_atlantic_faroe)
////        val b = BitmapFactory.decodeStream(imageStream)
////
////        val d: Drawable = BitmapDrawable(b)
////
////        val bitmapDrawable = d as BitmapDrawable
////        val bitmap = bitmapDrawable.bitmap
////        val stream = ByteArrayOutputStream()
////        bitmap.compress(CompressFormat.PNG, 100, stream) //use the compression format of your need
//
//        val d = Drawable.createFromStream(imageStream, "res/raw/joda_atlantic_faroe")
//
//        image.setImageDrawable(d)
//
//        Log.e("formatStringNumBer","${formatStringNumBer(10000L ,".")}")
//
//
//        val adapter = TestAdapter()
//        adapter.apply {
//            setRecyclerView(rvList, countTest = 200)
//            setOnAdapterListener { id, item, pos ->
//                Log.e("setOnAdapterListener", " ")
//            }
//            setLoadData {
//                Log.e("setLoadData", " ")
//            }
//        }
//        edtMoney.setMaxMoney(100000000000,"Tiền bị giới hạn")
//

        val mStartForResult =
            registerForActivityResult(StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    // Handle the Intent
                }
            }
//        handleFineLocationPermission(onAccepted = {
//            Log.e("handleFineLocationPermission", " ")
//            checkLocation { location ->
//                Log.e("location", "${location.latitude} ${location.longitude}")
//            }
//
//        }, null, null)
//        btnNext.setOnClickListener {
//            handleFineLocationPermission(onAccepted = {
//                Log.e("handleFineLocationPermission", " ")
//                checkLocation { location ->
//                    Log.e("location", "${location.latitude} ${location.longitude}")
//                }
//
//            }, null, { scope, deniedList, beforeRequest ->
//                onPermissionExplanation({
//                    scope.getChainTask().requestAgain(deniedList)
//                }, {
//
//                })
//
//            })
//
////            val intent = Intent(this,IntentActivity::class.java)
////            mStartForResult.launch(intent)
//
////            startActivityExt<IntentActivity>(activityLauncher) { result ->
////                Log.e("result", "${result.resultCode}")
////            }
//
//
////            startActivityExt<IntentActivity> { result ->
////                Log.e("result", "${result.resultCode}")
////            }
//        }


//        val builder = CFAlertDialog.Builder(this)
//            .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//            .addButton(
//                "Resend request",
//                ContextCompat.getColor(this, R.color.purple_500),
//                ContextCompat.getColor(this, R.color.teal_700),
//                R.style.Dialog,
//                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
//                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
//            ) { dialog, which ->
//                dialog.dismiss()
//            }.addButton(
//                "Resend request",
//                ContextCompat.getColor(this, R.color.purple_500),
//                ContextCompat.getColor(this, R.color.teal_700),
//                R.style.Dialog2,
//                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
//                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
//            ) { dialog, which ->
//                dialog.dismiss()
//            }
//
//        builder.show()


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

//        edt.setOnTouchListener(OnTouchListener { v, event ->
//            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
//            val DRAWABLE_RIGHT = 2
//            val DRAWABLE_BOTTOM = 3
//            if (event.action == MotionEvent.ACTION_UP) {
//                if (event.rawX >= edt.right - edt.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
//                    // your action here
//                    Log.e("setDrawableRightTouch", " DRAWABLE_RIGHT")
//                    return@OnTouchListener true
//                }
//            }
//            false
//        })

    }


    fun Context.onPermissionExplanation(
        onChangeToWays: (() -> Unit),
        onSkip: (() -> Unit)
    ) {
        try {
            val view =
                LayoutInflater.from(this).inflate(R.layout.dialog_check_permission, null, false)

            val dialogRating = CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setHeaderView(view)
                .create()

            val btnChangeToWays = view.findViewById<TextView>(R.id.btnChangeToWays)
            val btnSkip = view.findViewById<TextView>(R.id.btnSkip)

            btnChangeToWays!!.setOnClickListener {
                dialogRating.dismiss()
                onChangeToWays.invoke()
            }
            btnSkip!!.setOnClickListener {
                dialogRating.dismiss()
                onSkip.invoke()
            }
            dialogRating.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setDrawableRightTouch(setClickListener: () -> Unit) {
        this.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
//            val DRAWABLE_BOTTOM = 3
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