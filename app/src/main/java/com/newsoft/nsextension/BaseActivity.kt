package com.newsoft.nsextension

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.viewbinding.ViewBinding
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity


abstract class BaseActivity<T : ViewBinding>(private val bindingFactory: (LayoutInflater) -> T) :
    RxAppCompatActivity() {

    var dataResult: ((Int, Int, Intent?) -> Unit)? = null
    var savedInstanceState: Bundle? = null
    var isAutoCheckHiderKeyboard = true

    private var _binding: T? = null
    val binding: T get() = _binding!!


    abstract fun onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setTransparentActivity()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        this.savedInstanceState = savedInstanceState
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
//        hideSoftKeyboard()
        onCreate()
    }


    /**
     * check hide keyboard on touch screen
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null && isAutoCheckHiderKeyboard) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun getTokenFirebase() {
//        if (AppLayerManager.getInstance().tokenFirebase.isEmpty())
//            FirebaseMessaging.getInstance().token
//                .addOnCompleteListener(OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                        return@OnCompleteListener
//                    }
//
//                    // Get new FCM registration token
//                    val token = task.result
//
//                    // Log and toast
//                    Log.e("TokenFirebase", token)
//                    AppLayerManager.getInstance().tokenFirebase = token
//                })
    }
//
//    fun ViewTopBinding.initViewTopFragment(
//        category: String? = null,
//        gravity: Int? = Gravity.CENTER
//    ) {
//        category?.let { tvCategory.text = it }
//        gravity?.let { tvCategory.gravity = it }
//        btnBack.setOnClickListener {
//            finish()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
//        hideSoftKeyboard()
        _binding = null
    }

    //TODO trả về file data Intent
    fun setDataIntentResult(dataResult: ((Int, Int, Intent?) -> Unit)? = null) {
        this.dataResult = dataResult
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            dataResult?.let { it.invoke(requestCode, resultCode, data) }
            for (fragment in supportFragmentManager.fragments)
                fragment.onActivityResult(requestCode, resultCode, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}