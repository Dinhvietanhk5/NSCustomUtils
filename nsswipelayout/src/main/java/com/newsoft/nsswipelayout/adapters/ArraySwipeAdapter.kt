package com.newsoft.nsswipelayout.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.newsoft.nsswipelayout.SwipeLayout
import com.newsoft.nsswipelayout.implments.SwipeItemMangerImpl
import com.newsoft.nsswipelayout.interfaces.SwipeAdapterInterface
import com.newsoft.nsswipelayout.interfaces.SwipeItemMangerInterface
import com.newsoft.nsswipelayout.util.Attributes

abstract class ArraySwipeAdapter<T> : ArrayAdapter<Any?>, SwipeItemMangerInterface,
    SwipeAdapterInterface {
    private val mItemManger = SwipeItemMangerImpl(this)

    constructor(context: Context?, resource: Int) : super(context!!, resource)
    constructor(context: Context?, resource: Int, textViewResourceId: Int) : super(
        context!!, resource, textViewResourceId
    )

    constructor(context: Context?, resource: Int, objects: Array<T>?) : super(
        context!!, resource, objects!!
    )

    constructor(
        context: Context?,
        resource: Int,
        textViewResourceId: Int,
        objects: Array<T>?
    ) : super(
        context!!, resource, textViewResourceId, objects!!
    )

    constructor(context: Context?, resource: Int, objects: List<T>?) : super(
        context!!, resource, objects!!
    )

    constructor(
        context: Context?,
        resource: Int,
        textViewResourceId: Int,
        objects: List<T>?
    ) : super(
        context!!, resource, textViewResourceId, objects!!
    )

    override fun notifyDatasetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getView(position, convertView, parent)
        mItemManger.bind(v, position)
        return v
    }

    override fun openItem(position: Int) {
        mItemManger.openItem(position)
    }

    override fun closeItem(position: Int) {
        mItemManger.closeItem(position)
    }

    override fun closeAllExcept(layout: SwipeLayout) {
        mItemManger.closeAllExcept(layout)
    }

    override fun closeAllItems() {
        mItemManger.closeAllItems()
    }

    override val openItems: List<Int?>?
        get() = mItemManger.openItems
    override val openLayouts: List<SwipeLayout?>?
        get() = mItemManger.openLayouts

    override fun removeShownLayouts(layout: SwipeLayout?) {
        mItemManger.removeShownLayouts(layout)
    }

    override fun isOpen(position: Int): Boolean {
        return mItemManger.isOpen(position)
    }

    override fun getMode(): Attributes.Mode? {
        return mItemManger.mode
    }

    override fun setMode(mode: Attributes.Mode) {
        mItemManger.mode = mode
    }
}
