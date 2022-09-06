package com.newsoft.nscustomutils

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newsoft.nscustom.ext.context.*
import kotlinx.android.synthetic.main.fragment.*

class MainFragment : Fragment() {

    var type = TypeConnectEnums.CONNECT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = getDataExtras("type", TypeConnectEnums.CONNECT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireActivity().checkHideKeyboardOnTouchScreen(view)
        Log.e("type", type.toString())

        btnIntentActivity.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
//            requireActivity().startActivityExt<IntentActivity>("title" to "Intent Activity")
        }
        btnIntentFragment.setOnClickListener {
            switchFragmentBackStack<IntentFragment>(R.id.container, "title" to "Intent Fragment")
        }
        btnGetFile.setOnClickListener {
            switchFragmentBackStack<GetFileFragment>(R.id.container, "title" to "Intent Fragment")
        }

    }
}