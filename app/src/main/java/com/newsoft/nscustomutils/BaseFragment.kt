package com.newsoft.nscustomutils

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.newsoft.nscustom.ext.context.checkHideKeyboardOnTouchScreen
import com.newsoft.nscustom.ext.context.hideSoftKeyboard
import com.trello.rxlifecycle.components.support.RxAppCompatActivity


abstract class BaseFragment(@LayoutRes private val layoutRes: Int) : Fragment(layoutRes) {

    var activityRx: RxAppCompatActivity? = null

    abstract fun onCreate()
    abstract fun onViewCreated(view: View)

    fun requireActivityRx(): RxAppCompatActivity {
        return activityRx!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRx = requireActivity() as RxAppCompatActivity
        onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().hideSoftKeyboard()
        requireActivity().checkHideKeyboardOnTouchScreen(view)
        onViewCreated(view)
    }


}
