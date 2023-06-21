//package com.newsoft.nscustomutils
//
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.View
//import com.newsoft.nsextension.ext.context.*
//
//class MainFragment : BaseFragment<Fra>() {
//
//    var type = TypeConnectEnums.CONNECT
//    override fun onCreate() {
//        type = getDataExtras("type", TypeConnectEnums.CONNECT)
//    }
//
//    override fun onViewCreated(view: View) {
//        Log.e("type", type.toString())
//
////        btnIntentActivity.setOnClickListener {
//////            requireActivity().handleWriteStoragePermission {
//////                Log.e("handleCameraPermission", " ")
//////            }
//////            requireActivity().startActivityExt<IntentActivity>("title" to "Intent Activity")
////        }
////        btnIntentFragment.setOnClickListener {
//////            switchFragmentBackStack<IntentFragment>(R.id.container, "title" to "Intent Fragment")
////        }
////        btnGetFile.setOnClickListener {
//////            switchFragmentBackStack<GetFileFragment>(R.id.container, "title" to "Intent Fragment")
////        }
//
//    }
//}