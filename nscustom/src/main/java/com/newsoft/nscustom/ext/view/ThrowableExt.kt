package com.newsoft.nscustom.ext.view

import org.json.JSONObject


fun Throwable.getMessage() : String {
    return if (this.message.isNullOrEmpty())
        "Unavailable. Please try again later."
    else
        return try { JSONObject(this.message.orEmpty()).getString("message") }
        catch (e: Exception) { this.message.orEmpty() }
}