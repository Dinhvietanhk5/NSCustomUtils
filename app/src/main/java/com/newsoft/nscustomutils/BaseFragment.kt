package com.newsoft.nscustomutils

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.newsoft.nsextension.ext.context.checkHideKeyboardOnTouchScreen
import com.newsoft.nsextension.ext.context.hideSoftKeyboard
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.trello.rxlifecycle4.components.support.RxFragment
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<T : ViewBinding> : RxFragment() {

    var dataResult: ((Int, Int, Intent) -> Unit)? = null
    var activity: RxAppCompatActivity? = null
    var savedInstanceState: Bundle? = null

    private var _binding: T? = null
    val binding: T get() = _binding!!

    abstract fun onCreate()
    abstract fun onViewCreated(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as RxAppCompatActivity
        onCreate()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, layoutInflater, container, false) as T
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.savedInstanceState = savedInstanceState
        requireActivity().hideSoftKeyboard()
        requireActivity().checkHideKeyboardOnTouchScreen(view)
        onViewCreated(view)
    }
//
//    fun ViewTopBinding.initViewTopFragment(
//        category: String? = null,
//        gravity: Int? = Gravity.CENTER
//    ) {
//        category?.let { tvCategory.text = it }
//        gravity?.let { tvCategory.gravity = it }
//        btnBack.setOnClickListener {
//            requireActivity().supportFragmentManager.popBackStack()
//        }
//    }

    //TODO trả về file data Intent
    fun setDataIntentResult(dataResult: ((Int, Int, Intent) -> Unit)? = null) {
        this.dataResult = dataResult
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dataResult?.let { it.invoke(requestCode, resultCode, data!!) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
