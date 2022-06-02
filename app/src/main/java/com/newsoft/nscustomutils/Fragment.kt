package com.newsoft.nscustomutils

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newsoft.nscustom.ext.context.startActivityExt
import com.newsoft.nscustom.ext.context.switchFragmentBackStack
import kotlinx.android.synthetic.main.fragment.*

class Fragment : Fragment() {

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        btnIntentActivity.setOnClickListener {
//            requireActivity().startActivityExt("title" to "Intent Activity", IntentActivity::class)
        }
        btnIntentFragment.setOnClickListener {
            switchFragmentBackStack<IntentFragment>(R.id.container, "title" to "Intent Fragment")
        }
        btnGetFile.setOnClickListener {
            switchFragmentBackStack<GetFileFragment>(R.id.container, "title" to "Intent Fragment")
        }

    }
}