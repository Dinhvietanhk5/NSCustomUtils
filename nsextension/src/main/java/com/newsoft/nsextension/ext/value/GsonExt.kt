package com.newsoft.nsextension.ext.value

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import java.lang.reflect.Type

/**
 * Convert an Object to pretty json string
 */
fun Any.toPrettyJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

//fun Any.toJson(): String = GsonBuilder().create().toJson(this)

fun Any.toJson(`object`: Any?): String {
    return if (`object` == null) {
        "null"
    } else Gson().toJson(`object`)
}

fun <T> fromJson(json: String?, typeOfT: Type?): T {
    return Gson().fromJson(json, typeOfT)
}

fun <T> fromJson(json: String?, classOfT: Class<T>?): T {
    return Gson().fromJson(json, classOfT)
}

fun <T> fromJsonArray(json: String?, classOfT: Class<T>?): ArrayList<T> {
    val item = ArrayList<T>()
    val jsonpath = JSONArray(json)
    for (i in 0 until jsonpath.length())
        item.add(Gson().fromJson(jsonpath.getString(i), classOfT))
    return item
}