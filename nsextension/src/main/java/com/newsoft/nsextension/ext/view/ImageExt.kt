package com.newsoft.nsextension.ext.view

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget


fun setImage(
    context: Context?,
    image: ImageView,
    input: String?,
    empty_image: Int
) {
    Glide.with(context!!)
        .load(input)
        .override(image.width, image.height)
        .placeholder(ContextCompat.getDrawable(context, empty_image))
        .into(image)
}

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

fun setImage(
    context: Context?,
    image: ImageView,
    input: Uri?,
    empty_image: Int
) {
    Glide.with(context!!)
        .load(input)
        .override(image.width, image.height)
        .placeholder(ContextCompat.getDrawable(context, empty_image))
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
    input: String?,
    ic_empty_image: Int
) {
    Glide.with(context!!)
        .asBitmap()
        .load(input)
        .centerCrop()
        .placeholder(ContextCompat.getDrawable(context, ic_empty_image))
        .into(object : BitmapImageViewTarget(image) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.resources, resource)
                image.setImageDrawable(circularBitmapDrawable)
            }
        })
}