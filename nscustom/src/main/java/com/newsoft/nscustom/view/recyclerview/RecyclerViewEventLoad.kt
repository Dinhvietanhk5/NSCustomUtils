package com.newsoft.nscustom.view.recyclerview

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.newsoft.nscustom.R
import com.newsoft.nscustom.view.recyclerview.interface_adapter.RecyclerViewLoadMoreListener
import java.util.*

class RecyclerViewEventLoad : RecyclerView.OnScrollListener {

    private var visibleThreshold = 9
    private var isLoading = false
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    private var isRefreshing = false
    private var rvLayoutManager: RecyclerView.LayoutManager? = null
    private var swRefresh: SwipeRefreshLayout? = null
    private var recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener
    private var timer: Timer? = null
    var index = 0

    constructor(
        rvLayoutManager: RecyclerView.LayoutManager, swRefresh: SwipeRefreshLayout?,
        editText: EditText?, recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener
    ) {
        swRefresh?.let {
            this.swRefresh = it
        }
        this.rvLayoutManager = rvLayoutManager
        this.recyclerViewLoadMoreListener = recyclerViewLoadMoreListener
        initView(editText)
    }

    private fun initView(editText: EditText?) {
        editText?.let {edt->
            edt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun afterTextChanged(editable: Editable) {
                    timer?.let { t -> t.cancel() }
                    timer = Timer()
                    timer?.let { t ->
                        t.schedule(object : TimerTask() {
                            override fun run() {
                                index = 0
                                recyclerViewLoadMoreListener.onLoad(0)
                            }
                        }, 500L)
                    }
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            })
            edt.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    index = 0
                    recyclerViewLoadMoreListener.onLoad(0)
                    handled = true
                }
                handled
            }
        }
        swRefresh?.let {
            it.setColorSchemeResources(R.color.black)
            it.setOnRefreshListener {
                isRefreshing = true
                reload(0)
                reset()
            }
        }
        recyclerViewLoadMoreListener.onLoad(0)
    }


    fun reload(index: Int) {
        recyclerViewLoadMoreListener.onLoad(index)
    }

    fun setLoaded() {
        isLoading = false
    }

    fun reset() {
        swRefresh?.let {
            it.isRefreshing = false
        }
        isLoading = false
        visibleThreshold = 9
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) return

        totalItemCount = rvLayoutManager!!.itemCount

        when (rvLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    (rvLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                        null
                    )
                lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItem =
                    (rvLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItem =
                    (rvLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
        }

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            recyclerView.post {
                recyclerViewLoadMoreListener.onLoad(index)
            }
            isLoading = true
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}