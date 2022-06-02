package com.newsoft.nscustom.ext.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter


fun setImage(
    context: Context?,
    image: ImageView,
    input: String?
) {
    Glide.with(context!!)
        .load(input)
        .override(image.width, image.height)
        .into(image)
}

fun setImage(
    context: Context?,
    image: ImageView,
    input: Uri?
) {
    Glide.with(context!!)
        .load(input)
        .override(image.width, image.height)
        .into(image)
}

fun setImageRadius(
    context: Context?,
    image: ImageView,
    input: String?,
    dimension: Int,
) {
    Glide.with(context!!)
        .asBitmap()
        .load(input)
        .centerCrop()
//                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_empty_image))
        .into(object : BitmapImageViewTarget(image) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.resources, resource)
//                        circularBitmapDrawable.isCircular = true
                circularBitmapDrawable.cornerRadius =
                    context.resources.getDimension(dimension)
                image.setImageDrawable(circularBitmapDrawable)
            }
        })
}

fun setImageDrawable(
    context: Context?,
    image: ImageView,
    input: String?
) {
    Glide.with(context!!)
        .asBitmap()
        .load(input)
        .centerCrop()
//                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_empty_image))
        .into(object : BitmapImageViewTarget(image) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.resources, resource)
                image.setImageDrawable(circularBitmapDrawable)
            }
        })
}

//fun setImageFromBitmap(
//    context: Context,
//    image: ImageView,
//    input: String?
//) {
//    Glide.with(context)
//        .asBitmap().load(input)
//        .listener(object : RequestListener<Bitmap?> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: Target<Bitmap?>?,
//                isFirstResource: Boolean
//            ): Boolean {
//                TODO("Not yet implemented")
//                return false
//            }
//
//            override fun onResourceReady(
//                resource: Bitmap?,
//                model: Any?,
//                target: Target<Bitmap?>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                image.setImageBitmap(resource)
//                return false
//            }
//
//        }
//        ).submit()
//}

//fun Context.createQRCode(str: String): Bitmap? {
////        int widthAndHeight = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
//    val charset = "UTF-8"
//    val hints: HashMap<EncodeHintType, String> = HashMap()
//    var matrix: BitMatrix? = null
//    try {
//        matrix = MultiFormatWriter().encode(
//            String(str.toByteArray(charset(charset))),
//            BarcodeFormat.QR_CODE, 600, 600, hints
//        )
//    } catch (e: UnsupportedEncodingException) {
//        e.printStackTrace()
//    }
//    val width: Int = matrix!!.width
//    val height: Int = matrix.height
//    val pixels = IntArray(width * height)
//    for (y in 0 until height) {
//        for (x in 0 until width) if (matrix.get(x, y)) pixels[y * width + x] = Color.BLACK
//    }
//    val bitmap = Bitmap.createBitmap(
//        width, height,
//        Bitmap.Config.ARGB_8888
//    )
//    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
//    val overlay = BitmapFactory.decodeResource(resources, R.drawable.ic_logo_qrcode)
////    return mergeBitmaps(overlay, bitmap)
//    return bitmap
//}

//fun mergeBitmaps(overlay: Bitmap, bitmap: Bitmap): Bitmap {
//    val height = bitmap.height
//    val width = bitmap.width
//    val combined = Bitmap.createBitmap(width, height, bitmap.config)
//    val canvas = Canvas(combined)
//    val canvasWidth = canvas.width
//    val canvasHeight = canvas.height
//    canvas.drawBitmap(bitmap, Matrix(), null)
//    val centreX = (canvasWidth - overlay.width) / 2
//    val centreY = (canvasHeight - overlay.height) / 2
//    canvas.drawBitmap(overlay, centreX.toFloat(), centreY.toFloat(), null)
//    return combined
//}

fun Context.createQRCode(
    qrCodeData: String,
    logoQrcode:Int
): Bitmap {
    var bitmapImage: Bitmap? = null
    val charset = "UTF-8"
    try {
        //generating qr code in bitmatrix type
        val matrix = MultiFormatWriter().encode(
            String(qrCodeData.toByteArray(charset(charset))),
            BarcodeFormat.QR_CODE, 600, 600
        )
        //converting bitmatrix to bitmap
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        // All are 0, or black, by default
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                pixels[offset + x] = if (matrix[x, y]) ResourcesCompat.getColor(
                    getResources(),
                    android.R.color.black,
                    null
                ) else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        //setting bitmap to image view
        val overlay = BitmapFactory.decodeResource(resources, logoQrcode)
//        imageViewBitmap.setImageBitmap(mergeBitmaps(overlay, bitmap))
        bitmapImage = mergeBitmaps(overlay, bitmap)
    } catch (er: Exception) {
        Log.e("QrGenerate", er.message!!)
    }
    return bitmapImage!!
}


fun mergeBitmaps(overlay: Bitmap, bitmap: Bitmap): Bitmap? {
    val height = bitmap.height
    val width = bitmap.width
    val combined = Bitmap.createBitmap(width, height, bitmap.config)
    val canvas = Canvas(combined)
    val canvasWidth = canvas.width
    val canvasHeight = canvas.height
    canvas.drawBitmap(bitmap, Matrix(), null)
    val centreX = (canvasWidth - overlay.width) / 2
    val centreY = (canvasHeight - overlay.height) / 2
    canvas.drawBitmap(overlay, centreX.toFloat(), centreY.toFloat(), null)
    return combined
}
