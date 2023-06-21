package com.newsoft.nsbaseadapter.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.newsoft.nsbaseadapter.R

class NSEmptyRecyclerView : LinearLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {


        val typedArray = context!!.theme.obtainStyledAttributes(
            attrs,
            R.styleable.NSRecyclerview, 0, 0
        )
        val idImage = typedArray.getDrawable(R.styleable.NSRecyclerview_imageEmpty)
        val textEmpty = typedArray.getString(R.styleable.NSRecyclerview_textEmpty)

        val imageEmpty = ImageView(context)
        val tvContentEmpty = TextView(context)

        if (idImage != null || textEmpty != null) {
            imageEmpty.setImageDrawable(idImage)
            tvContentEmpty.text = textEmpty
            tvContentEmpty.typeface = Typeface.defaultFromStyle(Typeface.ITALIC);
        }
        tvContentEmpty.setPadding(0, 30, 0, 0)

        addView(imageEmpty)
        addView(tvContentEmpty)
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        orientation = VERTICAL
        gravity = Gravity.CENTER
    }
}