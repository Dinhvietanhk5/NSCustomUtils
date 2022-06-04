package com.newsoft.nscustom.ext.context

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.newsoft.nscustom.ext.value.fromJson
import com.newsoft.nscustom.ext.value.fromJsonArray
import java.io.Serializable

/**
 * New Instance Fragment
 */

inline fun <reified T : Fragment> newInstance(vararg params: Pair<String, Any>): T =
    T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }

/**
 * New Instance Fragment Back Stack
 */

inline fun <reified T : Fragment> Fragment.switchFragmentBackStack(
    container: Int,
    vararg params: Pair<String, Any>
) {
    val fragment = T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }
    (requireActivity() as AppCompatActivity).switchFragmentBackStack(container, fragment)
}

/**
 * New Instance Fragment Not Back Stack
 */

inline fun <reified T : Fragment> Fragment.switchFragmentNotBackStack(
    container: Int,
    vararg params: Pair<String, Any>
) {
    val fragment = T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }
    (requireActivity() as AppCompatActivity).switchFragmentNotBackStack(container, fragment)
}


/**
 *  New Fragment In Activity Not BackStack
 */

fun Fragment.switchFragmentNotBackStack(
    container: Int,
    fragment: Fragment?
) {
    (requireActivity() as AppCompatActivity).switchFragmentNotBackStack(container, fragment)
}

/**
 *  New Fragment In Activity BackStack
 */

fun Fragment.switchFragmentBackStack(
    container: Int,
    fragment: Fragment?
) {
    (requireActivity() as AppCompatActivity).switchFragmentBackStack(container, fragment)
}

/**
 * BackStack Fragment
 */

fun Fragment.backStack() {
    requireActivity().hideSoftKeyboard()
    requireActivity().supportFragmentManager.popBackStack()
}

/**
 * Get Intent Activity
 */

fun <T> Fragment.getDataExtras(key: String, defaultValue: Any): T {
    var serializable: Serializable? = null
    return try {
        requireArguments().getSerializable(key)?.let { serializable = it }
        (serializable as T)!!
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("DataExtras error:", "$key ${e.message}")
        (defaultValue as? T)!!
    }
}

/**
 * Get Json argument from bundle using [key]
 */

fun <T> Bundle.fromJsonExtra(key: String, classOfT: Class<T>?): T {
    val json: String? = getString(key)
    return fromJson(json, classOfT)
}

fun <T> Bundle.fromJsonArrayExtra(key: String, classOfT: Class<T>?): ArrayList<T> {
    val json: String? = getString(key)
    return fromJsonArray(json, classOfT)
}

/**
 * Get color resource
 */
fun Fragment.getCompatColor(colorRes: Int) = ContextCompat.getColor(requireContext(), colorRes)

/**
 * Get drawable resource
 */
fun Fragment.getCompatDrawable(drawableRes: Int) =
    ContextCompat.getDrawable(requireContext(), drawableRes)