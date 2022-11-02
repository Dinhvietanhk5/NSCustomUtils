package com.newsoft.nscustomutils

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.newsoft.nscustom.ext.context.switchFragment
import com.trello.rxlifecycle.components.support.RxAppCompatActivity


open class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    public fun switchFragment(mainFragment: MainFragment) {
//        switchFragment(R.id.container, mainFragment)
    }

    public fun switchFragment(mainFragment: MainFragment, isTaskBack: Boolean) {
//        switchFragment(R.id.container, mainFragment, isTaskBack)
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if (currentFocus != null) {
//            val imm: InputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//        return super.dispatchTouchEvent(ev)
//    }

}