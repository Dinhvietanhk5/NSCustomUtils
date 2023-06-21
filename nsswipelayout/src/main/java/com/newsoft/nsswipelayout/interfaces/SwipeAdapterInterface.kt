package com.newsoft.nsswipelayout.interfaces

interface SwipeAdapterInterface {
    fun getSwipeLayoutResourceId(position: Int): Int
    fun notifyDatasetChanged()
}
