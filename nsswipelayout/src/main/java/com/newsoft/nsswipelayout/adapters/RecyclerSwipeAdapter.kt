package com.newsoft.nsswipelayout.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsoft.nsswipelayout.SwipeLayout
import com.newsoft.nsswipelayout.implments.SwipeItemMangerImpl
import com.newsoft.nsswipelayout.interfaces.SwipeAdapterInterface
import com.newsoft.nsswipelayout.interfaces.SwipeItemMangerInterface
import com.newsoft.nsswipelayout.util.Attributes

abstract class RecyclerSwipeAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>(),
    SwipeItemMangerInterface, SwipeAdapterInterface {
    var mItemManger = SwipeItemMangerImpl(this)
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract override fun onBindViewHolder(viewHolder: VH, position: Int)
    override fun notifyDatasetChanged() {
        super.notifyDataSetChanged()
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

    override fun getMode(): Attributes? {
        return mItemManger.mode
    }

    override fun setMode(mode: Attributes) {
        mItemManger.mode = mode
    }
}
