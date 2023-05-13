package com.newsoft.nsbaseadapter.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.newsoft.nsbaseadapter.R

class NSRecyclerview : LinearLayout {


    var swRefresh: SwipeRefreshLayout? = null
    var recyclerView: RecyclerView? = null
    var imageEmpty: ImageView? = null
    var tvContentEmpty: TextView? = null
    var viewEmpty: LinearLayout? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context!!, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {

        initView(context!!, attrs)
    }

    private fun initView(
        context: Context, attrs: AttributeSet?
    ) {
        try {
            val viewRecyclerview =
                LayoutInflater.from(context).inflate(R.layout.view_recyclerview_utils, null, false)
            swRefresh = viewRecyclerview.findViewById(R.id.swRefresh)
            recyclerView = viewRecyclerview.findViewById(R.id.recyclerView)
            imageEmpty = viewRecyclerview.findViewById(R.id.imageEmpty)
            tvContentEmpty = viewRecyclerview.findViewById(R.id.tvContentEmpty)
            viewEmpty = viewRecyclerview.findViewById(R.id.viewEmpty)


            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.NSRecyclerview, 0, 0
            )


            val idImage = typedArray.getDrawable(R.styleable.NSRecyclerview_imageEmpty)
            val textEmpty = typedArray.getString(R.styleable.NSRecyclerview_textEmpty)

            val paddingTop = typedArray.getDimension(R.styleable.NSRecyclerview_paddingTopRecyclerview,0f).toInt()
            val paddingBottom = typedArray.getDimension(R.styleable.NSRecyclerview_paddingBottomRecyclerview,0f).toInt()
            val paddingStart = typedArray.getDimension(R.styleable.NSRecyclerview_paddingStartRecyclerview,0f).toInt()
            val paddingEnd = typedArray.getDimension(R.styleable.NSRecyclerview_paddingEndRecyclerview,0f).toInt()
            val clipToPadding = typedArray.getBoolean(R.styleable.NSRecyclerview_clipToPaddingRecyclerview,true)

            recyclerView!!.clipToPadding = clipToPadding
            recyclerView!!.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)

            if (idImage != null || textEmpty != null) {
                viewEmpty!!.visibility = View.VISIBLE
                imageEmpty?.setImageDrawable(idImage)
                tvContentEmpty?.text = textEmpty
            }

            addView(viewRecyclerview)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}