package com.newsoft.nsswipelayout.interfaces

import com.newsoft.nsswipelayout.SwipeLayout
import com.newsoft.nsswipelayout.util.Attributes

interface SwipeItemMangerInterface {
    fun openItem(position: Int)
    fun closeItem(position: Int)
    fun closeAllExcept(layout: SwipeLayout)
    fun closeAllItems()
    val openItems: List<Int?>?
    val openLayouts: List<SwipeLayout?>?
    fun removeShownLayouts(layout: SwipeLayout?)
    fun isOpen(position: Int): Boolean
    fun getMode(): Attributes?
    fun setMode(mode: Attributes)
}
