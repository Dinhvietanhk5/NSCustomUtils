package com.newsoft.nsextension.ext.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

class TabLayoutOnTabSelected(private val tabSelected: (TabLayout.Tab) -> Unit = {}) : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
        tabSelected(tab)
    }
    override fun onTabUnselected(tab: TabLayout.Tab) {}
    override fun onTabReselected(tab: TabLayout.Tab) {}
}

fun TabLayout.setTintWith(color: Int?) {
    val colorList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_selected),
            intArrayOf(android.R.attr.state_selected)
        ),
        intArrayOf(
            color ?: Color.WHITE,
            Color.WHITE
        )
    )
    this.apply {
        tabIconTint = colorList
        tabTextColors = colorList
    }
}

fun TabLayout.setTabsBgWith(context: Context, background: Int) {
    for (i in 0 until this.tabCount) {
        this.getTabAt(i)?.view?.background = ContextCompat.getDrawable(context, background)
    }
}