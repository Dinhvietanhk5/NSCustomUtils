package com.newsoft.nscustom.ext.context


import android.content.Context
import android.util.Base64
import java.io.File

/** App Data **/

fun Context.clearAppData() {
    val cache = cacheDir
    val appDir = cache.parent?.let { File(it) }
    if (appDir!!.exists()) {
        val children = appDir.list()
        for (d in children!!) {
            if (d != "lib") {
                deleteDirectory(File(appDir, d))
//                e("File /data/data/APP_PACKAGE/$d DELETED")
            }
        }
    }
}

fun Context.deleteDirectory(dir: File): Boolean {
    if (dir.isDirectory) {
        for (i in dir.list()!!.indices) {
            val success = try {
                deleteDirectory(File(dir, dir.list()!![i]))
            } catch (e: ArrayIndexOutOfBoundsException) {
                false
            }
            if (!success) {
                return false
            }
        }
    }
    return dir.delete()
}

fun File.toBase64String(): String {
    return Base64.encodeToString(this.readBytes(), Base64.NO_WRAP)
}

fun createOrExistsDir(file: File?): File? {
    file != null && if (!file.exists()) file.mkdirs() else file.isDirectory
    return file
}