package com.newsoft.nscustom.ext.context

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

inline fun <reified T> Fragment.getDataExtras(key: String, defaultValue: Any): T {
    var dataIntent = Any()
    try {
        when (T::class) {
            Byte::class,
            Char::class,
            Long::class,
            Float::class,
            Short::class,
            Double::class,
            Boolean::class,
            String::class,
            Serializable::class,
            CharSequence::class,
            Int::class -> {
                requireArguments().getSerializable(key)?.let { dataIntent = it } ?: kotlin.run {
                    dataIntent = defaultValue
                }
            }
            Bundle::class -> requireArguments().getBundle(key)?.let { dataIntent = it }
                ?: kotlin.run {
                    dataIntent = Bundle()
                }
            IntArray::class -> requireArguments().getIntArray(key)?.let { dataIntent = it }
            ByteArray::class -> requireArguments().getByteArray(key)?.let { dataIntent = it }
            CharArray::class -> requireArguments().getCharArray(key)?.let { dataIntent = it }
            LongArray::class -> requireArguments().getLongArray(key)?.let { dataIntent = it }
                ?: kotlin.run { dataIntent = LongArray(0) }
            FloatArray::class -> requireArguments().getFloatArray(key)?.let { dataIntent = it }
                ?: kotlin.run { dataIntent = FloatArray(0) }
            Parcelable::class -> requireArguments().getParcelableArray(key)?.let { dataIntent = it }
            ShortArray::class -> requireArguments().getShortArray(key)?.let { dataIntent = it }
            DoubleArray::class -> requireArguments().getDoubleArray(key)?.let { dataIntent = it }
                ?: kotlin.run { dataIntent = DoubleArray(0) }
            BooleanArray::class -> requireArguments().getBooleanArray(key)?.let { dataIntent = it }
                ?: kotlin.run { dataIntent = BooleanArray(0) }
            CharSequence::class -> requireArguments().getCharArray(key)?.let { dataIntent = it }
//             Array<*> -> dataIntent = {
//                when {
//                    defaultValue.isArrayOf<String>() ->
//                        requireArguments().getStringArrayExtra(key)?.let { defaultValue as Array<String> = it }
//                    defaultValue.isArrayOf<Parcelable>() ->
//                        putExtra(key, value as Array<Parcelable?>)
//                    defaultValue.isArrayOf<CharSequence>() ->
//                        putExtra(key, value as Array<CharSequence?>)
//                    else -> putExtra(key, defaultValue)
//                }
//            }
//            is Serializable -> requireArguments().getSerializableExtra(key)?.let { (defaultValue as Serializable) =it }
        }
        return dataIntent as T

    } catch (e: Exception) {
        e.printStackTrace()
        dataIntent = defaultValue
        Log.e("DataExtras error:", "$key exist")
        return dataIntent as T
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