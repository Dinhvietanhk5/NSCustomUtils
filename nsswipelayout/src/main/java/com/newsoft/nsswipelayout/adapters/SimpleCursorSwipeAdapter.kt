package com.newsoft.nsswipelayout.adapters

import android.content.Context
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import com.newsoft.nsswipelayout.SwipeLayout
import com.newsoft.nsswipelayout.implments.SwipeItemMangerImpl
import com.newsoft.nsswipelayout.interfaces.SwipeAdapterInterface
import com.newsoft.nsswipelayout.interfaces.SwipeItemMangerInterface
import com.newsoft.nsswipelayout.util.Attributes

abstract class SimpleCursorSwipeAdapter : SimpleCursorAdapter, SwipeItemMangerInterface,
    SwipeAdapterInterface {
    private val mItemManger = SwipeItemMangerImpl(this)

    protected constructor(
        context: Context?,
        layout: Int,
        c: Cursor?,
        from: Array<String?>?,
        to: IntArray?,
        flags: Int
    ) : super(context, layout, c, from, to, flags)

    protected constructor(
        context: Context?,
        layout: Int,
        c: Cursor?,
        from: Array<String?>?,
        to: IntArray?
    ) : super(context, layout, c, from, to)

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
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
