package com.newsoft.nscustom.ext.context

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.newsoft.nscustom.R
import com.newsoft.nscustom.ext.context.launcher_result.BetterActivityResult
import com.newsoft.nscustom.ext.value.fromJsonArray
import java.io.Serializable


const val RESULT_FINISH_ACTIVITY = 110
const val RESULT_START_ACTIVITY = 111

/**
 * Extensions for simpler launching of Activities
 */

inline fun <reified T : Activity> Activity.startActivityExtFinish(
    vararg params: Pair<String, Any>,
    requestCode: Int = RESULT_START_ACTIVITY,
    options: Bundle? = null,
) {
    val intent = Intent(this, T::class.java)
    intent.putDataExtras(*params)
    startActivityForResult(intent, requestCode, options)
    finish()
}

inline fun <reified T : Activity> Activity.startActivityExtFinish(
    vararg params: Pair<String, Any>,
    requestCode: Int = RESULT_START_ACTIVITY,
) {
    val intent = Intent(this, T::class.java)
    intent.putDataExtras(*params)
    startActivityForResult(intent, requestCode)
    finish()
}

inline fun <reified T : Activity> Activity.startActivityExt(
    vararg params: Pair<String, Any>,
    requestCode: Int = RESULT_START_ACTIVITY,
) {
    val intent = Intent(this, T::class.java)
    intent.putDataExtras(*params)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Activity.startActivityExt(
    activityLauncher: BetterActivityResult<Intent, ActivityResult>,
    vararg params: Pair<String, Any>,
    crossinline onActivityResult: (ActivityResult) -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.putDataExtras(*params)
    activityLauncher.launch(intent) {
        onActivityResult.invoke(it)
    }
}

inline fun <reified T : Activity> View.startActivityExt(
    vararg params: Pair<String, Any>,
    requestCode: Int = RESULT_START_ACTIVITY,
    options: Bundle? = null,
) {
    val intent = Intent(context, T::class.java)
    intent.putDataExtras(*params)
    (context as Activity).startActivityForResult(intent, requestCode, options)
}

/**
 *  New Fragment In Activity
 */

fun AppCompatActivity.switchFragmentUpDown(
    container: Int,
    fragment: Fragment?,
    isBackTask: Boolean
) {
    val tag = fragment!!.javaClass.simpleName
    hideSoftKeyboard()
    val fragmentTransaction =
        supportFragmentManager
            .beginTransaction().setCustomAnimations(
                R.animator.slide_in_bottom,
                R.animator.slide_out_top,
                R.animator.slide_in_top,
                R.animator.slide_out_bottom
            )
            .replace(container, fragment, tag)
    if (isBackTask) fragmentTransaction.addToBackStack(tag)
    fragmentTransaction.commit()
}

/**
 *  New Fragment In Activity
 */

fun AppCompatActivity.switchFragment(
    container: Int,
    fragment: Fragment?,
    isBackTask: Boolean
) {
    val tag = fragment!!.javaClass.simpleName
    hideSoftKeyboard()
    val fragmentTransaction =
        supportFragmentManager
            .beginTransaction().setCustomAnimations(
                R.animator.slide_in_left, R.animator.slide_out_left,
                R.animator.slide_out_right, R.animator.slide_in_right
            )
            .replace(container, fragment, tag)
    if (isBackTask) fragmentTransaction.addToBackStack(tag)
    fragmentTransaction.commit()
}

/**
 *  New Fragment In Activity Not BackStack
 */

fun AppCompatActivity.switchFragmentNotBackStack(
    container: Int,
    fragment: Fragment?
) {
    switchFragment(container, fragment, false)
}

/**
 *  New Fragment In Activity BackStack
 */

fun AppCompatActivity.switchFragmentBackStack(
    container: Int,
    fragment: Fragment?
) {
    switchFragment(container, fragment, true)
}

/**
 *  New Fragment In Activity
 */

fun AppCompatActivity.switchFragment(container: Int, fragment: Fragment) {
    val tag = fragment.javaClass.simpleName
    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    val fragment1 = supportFragmentManager.findFragmentByTag(tag)
    if (fragment1 != null && fragment1.isAdded) { // if the fragment is already in container
        ft.show(fragment1)
    } else { // fragment needs to be added to frame container
        ft.add(container, fragment, tag)
    }
    val fragments =
        supportFragmentManager.fragments
    if (fragments.size > 0) {
        for (frag in fragments) {
            if (frag !== fragment1) {
                if (frag.isAdded) ft.hide(frag)
            }
        }
    }
    ft.commit()
}


/**
 *  New Fragment In Activity
 */

fun AppCompatActivity.switchFragment(container: ViewGroup, fragment: Fragment) {
    val tag = fragment.javaClass.simpleName
    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    val fragment1 = supportFragmentManager.findFragmentByTag(tag)
    if (fragment1 != null && fragment1.isAdded) { // if the fragment is already in container
        ft.show(fragment1)
    } else { // fragment needs to be added to frame container
        ft.add(container.id, fragment, tag)
    }
    val fragments =
        supportFragmentManager.fragments
    if (fragments.size > 0) {
        for (frag in fragments) {
            if (frag !== fragment1) {
                if (frag.isAdded) ft.hide(frag)
            }
        }
    }
    ft.commit()
}

/**
 * Finish Activity For Result
 */

fun Activity.finishActivityForResultExt(
    vararg params: Pair<String, Any>,
    requestCode: Int = RESULT_OK
) {
    val intent = Intent()
    intent.putDataExtras(*params)
    setResult(requestCode, intent)
    finishActivityExt()
}

/**
 * Finish Activity Dismiss KeyBroad
 */

fun Activity.finishActivityExt() {
    hideSoftKeyboard()
    finish()
}


/**
 * Put Intent Activity
 */

fun Intent.putDataExtras(vararg params: Pair<String, Any>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
            is Serializable -> putExtra(key, value)
        }
    }
    return this
}


/**
 * Get Intent Activity
 */

inline fun <reified T> Bundle.getDataExtra(key: String): T? {
    return try {
        getSerializable(key) as? T
    } catch (e: Exception) {
        e.printStackTrace()
        "" as? T
    }
}

inline fun <reified T> Activity.getDataExtras(key: String, defaultValue: Any): T {
    return try {
        intent.getSerializableExtra(key) as T
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("DataExtras error:", "$key ${e.message}")
        (defaultValue as? T)!!
    }
}

/**
 * Get Json Intent Activity
 */

fun <T> Intent.fromJsonExtra(key: String, classOfT: Class<T>?): T {
    val json: String? = getStringExtra(key)
    return Gson().fromJson(json, classOfT)
}

/**
 * Get Array Json Intent Activity
 */

fun <T> Intent.fromJsonArrayExtra(key: String, classOfT: Class<T>?): ArrayList<T> {
    val json: String? = getStringExtra(key)
    return fromJsonArray(json, classOfT)
}

/**
 * Get Intent String Activity
 */

fun Activity.getStringExt(key: String): String {
    return try {
        getDataExtras(key, "") as String
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * Get color resource
 */
fun Activity.getCompatColor(colorRes: Int) = ContextCompat.getColor(this, colorRes)

/**
 * Get drawable resource
 */
fun Activity.getCompatDrawable(drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

/**
 * set Transparent in Activity
 */
fun Activity.setTransparentActivity() {
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    val attrib = window.attributes
    attrib.layoutInDisplayCutoutMode =
        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
}
