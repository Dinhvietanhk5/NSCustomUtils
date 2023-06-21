package com.newsoft.nsswipelayout.implments

import android.view.View
import com.newsoft.nsswipelayout.SimpleSwipeListener
import com.newsoft.nsswipelayout.SwipeLayout
import com.newsoft.nsswipelayout.interfaces.SwipeAdapterInterface
import com.newsoft.nsswipelayout.interfaces.SwipeItemMangerInterface
import com.newsoft.nsswipelayout.util.Attributes

/**
 * SwipeItemMangerImpl is a helper class to help all the adapters to maintain open status.
 */
class SwipeItemMangerImpl(swipeAdapterInterface: SwipeAdapterInterface?) :
    SwipeItemMangerInterface {
    var mode = Attributes.Mode.Single
    val INVALID_POSITION = -1
    protected var mOpenPosition = INVALID_POSITION
    protected var mOpenPositions: MutableSet<Int?> = HashSet()
    protected var mShownLayouts: MutableSet<SwipeLayout?> = HashSet()
    protected var swipeAdapterInterface: SwipeAdapterInterface

    init {
        requireNotNull(swipeAdapterInterface) { "SwipeAdapterInterface can not be null" }
        this.swipeAdapterInterface = swipeAdapterInterface
    }

    override fun getMode(): Attributes.Mode? {
        return mode
    }

    override fun setMode(mode: Attributes.Mode) {
        this.mode = mode
        mOpenPositions.clear()
        mShownLayouts.clear()
        mOpenPosition = INVALID_POSITION
    }

    fun bind(view: View, position: Int) {
        val resId = swipeAdapterInterface.getSwipeLayoutResourceId(position)
        val swipeLayout = view.findViewById<View>(resId) as SwipeLayout
            ?: throw IllegalStateException("can not find SwipeLayout in target view")
        if (swipeLayout.getTag(resId) == null) {
            val onLayoutListener: OnLayoutListener = OnLayoutListener(position)
            val swipeMemory: SwipeMemory = SwipeMemory(position)
            swipeLayout.addSwipeListener(swipeMemory)
            swipeLayout.addOnLayoutListener(onLayoutListener)
            swipeLayout.setTag(resId, ValueBox(position, swipeMemory, onLayoutListener))
            mShownLayouts.add(swipeLayout)
        } else {
            val valueBox = swipeLayout.getTag(resId) as ValueBox
            valueBox.swipeMemory.setPosition(position)
            valueBox.onLayoutListener.setPosition(position)
            valueBox.position = position
        }
    }

    override fun openItem(position: Int) {
        if (mode == Attributes.Mode.Multiple) {
            if (!mOpenPositions.contains(position)) mOpenPositions.add(position)
        } else {
            mOpenPosition = position
        }
        swipeAdapterInterface.notifyDatasetChanged()
    }

    override fun closeItem(position: Int) {
        if (mode == Attributes.Mode.Multiple) {
            mOpenPositions.remove(position)
        } else {
            if (mOpenPosition == position) mOpenPosition = INVALID_POSITION
        }
        swipeAdapterInterface.notifyDatasetChanged()
    }

    override fun closeAllExcept(layout: SwipeLayout) {
        for (s in mShownLayouts) {
            if (s !== layout) s!!.close()
        }
    }

    override fun closeAllItems() {
        if (mode == Attributes.Mode.Multiple) {
            mOpenPositions.clear()
        } else {
            mOpenPosition = INVALID_POSITION
        }
        for (s in mShownLayouts) {
            s!!.close()
        }
    }

    override fun removeShownLayouts(layout: SwipeLayout?) {
        mShownLayouts.remove(layout)
    }

    override val openItems: List<Int?>?
        get() = if (mode == Attributes.Mode.Multiple) {
            ArrayList(mOpenPositions)
        } else {
            listOf(mOpenPosition)
        }
    override val openLayouts: List<SwipeLayout?>?
        get() = ArrayList(mShownLayouts)

    override fun isOpen(position: Int): Boolean {
        return if (mode == Attributes.Mode.Multiple) {
            mOpenPositions.contains(position)
        } else {
            mOpenPosition == position
        }
    }

    internal inner class ValueBox(
        var position: Int,
        var swipeMemory: SwipeMemory,
        var onLayoutListener: OnLayoutListener
    )

    internal inner class OnLayoutListener(private var position: Int) : SwipeLayout.OnLayout {
        fun setPosition(position: Int) {
            this.position = position
        }

        override fun onLayout(v: SwipeLayout) {
            if (isOpen(position)) {
                v.open(false, false)
            } else {
                v.close(false, false)
            }
        }
    }

    internal inner class SwipeMemory(private var position: Int) : SimpleSwipeListener() {
        override fun onClose(layout: SwipeLayout?) {
            if (mode == Attributes.Mode.Multiple) {
                mOpenPositions.remove(position)
            } else {
                mOpenPosition = INVALID_POSITION
            }
        }

        override fun onStartOpen(layout: SwipeLayout) {
            if (mode == Attributes.Mode.Single) {
                closeAllExcept(layout)
            }
        }

        override fun onOpen(layout: SwipeLayout) {
            if (mode == Attributes.Mode.Multiple) mOpenPositions.add(
                position
            ) else {
                closeAllExcept(layout)
                mOpenPosition = position
            }
        }

        fun setPosition(position: Int) {
            this.position = position
        }
    }
}
