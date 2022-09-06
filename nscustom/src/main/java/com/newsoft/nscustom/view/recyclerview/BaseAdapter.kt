package com.newsoft.nscustom.view.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.newsoft.nscustom.view.recyclerview.interface_adapter.IViewHolder
import com.newsoft.nscustom.view.recyclerview.interface_adapter.OnAdapterListener
import com.newsoft.nscustom.view.recyclerview.interface_adapter.RecyclerViewLoadMoreListener
import java.util.*

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>() :
    RecyclerView.Adapter<VH>(),
    IViewHolder<T, VH> {

    protected var items: ArrayList<T>?
    protected var itemsCache: ArrayList<T>? = null
    var mOnAdapterListener: OnAdapterListener<T>? = null
    var viewHolder: VH? = null
    var context: Context? = null
    private var viewEmpty: View? = null
    private var recyclerViewEventLoad: RecyclerViewEventLoad? = null
    private var recyclerView: RecyclerView? = null
    private var recycleViewLayoutManagerEnums = RvLayoutManagerEnums.LinearLayout_VERTICAL
    private val TAG = "BaseAdapter"
    private var swRefresh: SwipeRefreshLayout? = null
    private var countTest = 0
    private var parent: ViewGroup?=null
    var isItemView = false //TODO: false itemview click, true không phải itemview click

    /**
     * init Adapter
     */
    init {
        items = ArrayList()
    }

    /**
     * setItems
     *  index = 0 -> setItem
     *  index != 0 -> addItem
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: ArrayList<T>?, index: Int) {
        recyclerViewEventLoad?.let {
            it.index = index
        }
        if (this.items == null) this.items = ArrayList()
        items?.let {
            if (it.size == 0 && index <= 10) {
                setEmptyItems()
                return
            }
        } ?: kotlin.run {
            if (index == 0)
                viewEmpty?.let { it.visibility = View.VISIBLE }
        }

        //TODO: trường hợp load more items null, index != 0
        if (items == null)
            return
        viewEmpty?.let { it.visibility = View.GONE }
        recyclerView?.let { it.visibility = View.VISIBLE }

        if (index <= 11) {
            this.items = items
            this.itemsCache = items
        } else {
            this.itemsCache!!.addAll(items)
            this.items!!.addAll(items)
        }
        notifyDataSetChanged()
        recyclerViewEventLoad?.setLoaded()
    }

    /**
     * setEmptyItems
     * check recyclerView visibility
     * check viewEmpty visibility
     */
    private fun setEmptyItems() {

//        swRefresh?.let { it.visibility = View.GONE }
        recyclerView!!.visibility = View.GONE
        viewEmpty?.let { it.visibility = View.VISIBLE }
    }

    fun getItems(position: Int): T {
        return items!![position]
    }

    /**
     * clearItem
     * index = 0
     */

    fun clearItem() {
        recyclerViewEventLoad?.let { it.index = 0 }
        items?.let { it.clear() }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            viewHolder = holder
            if (items!!.size > 0) onBindView(holder, items!![position], position, realCount())
            else if (countTest != 0)
                onBindView(holder, null, position, realCount())

            if (mOnAdapterListener != null && !isItemView) {
                holder!!.itemView.setOnClickListener {
                    mOnAdapterListener!!.onItemClick(
                        0,
                        if (items!!.size > 0) items!![position] else null, position
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        this.parent = parent
        return onCreateHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return realCount()
    }

    private fun realCount(): Int {
        return if (items!!.size == 0) countTest else items!!.size
    }

    fun setView(layout: Int): View {
        return LayoutInflater.from(context)
            .inflate(layout, parent, false)
    }


    /**
     * setCountItemTest
     * countTest == 0 viewEmpty VISIBLE & , recyclerView GONE
     * countTest != 0 viewEmpty GONE & , recyclerView VISIBLE
     */
    fun setCountItemTest(countTest: Int) {
        this.countTest = countTest
        if (countTest != 0) {
            viewEmpty?.let { it.visibility = View.GONE }
            recyclerView?.let { it.visibility = View.VISIBLE }
        } else {
            viewEmpty?.let { it.visibility = View.VISIBLE }
            recyclerView?.let { it.visibility = View.GONE }
        }
    }

    /**
     * onclick not itemview
     */
    fun setOnAdapterNotItemViewListener(listener: OnAdapterListener<T>?) {
        mOnAdapterListener = listener
        this.isItemView = true
    }

    /**
     * onclick itemview
     */
    fun setOnAdapterListener(listener: OnAdapterListener<T>?) {
        mOnAdapterListener = listener
        this.isItemView = false
    }

    /**
     * init RecyclerView
     */

    fun setRecyclerView(recyclerView: RecyclerView, type: RvLayoutManagerEnums) {
        recycleViewLayoutManagerEnums = type
        setLayoutManager(recyclerView, type)
    }

    fun setRecyclerView(recyclerView: RecyclerView, countTest: Int, type: RvLayoutManagerEnums) {
        recycleViewLayoutManagerEnums = type
        this.countTest = countTest
        if (countTest != 0) {
            viewEmpty?.let { it.visibility = View.GONE }
            recyclerView?.let { it.visibility = View.VISIBLE }
        } else {
            viewEmpty?.let { it.visibility = View.VISIBLE }
            recyclerView?.let { it.visibility = View.GONE }
        }
        setLayoutManager(recyclerView, type)
    }

    fun setRecyclerView(
        recyclerView: RecyclerView,
        viewEmpty: ViewGroup,
        type: RvLayoutManagerEnums
    ) {
        recycleViewLayoutManagerEnums = type
        this.viewEmpty = viewEmpty
        setLayoutManager(recyclerView, type)
    }

    fun setRecyclerView(recyclerView: RecyclerView, viewEmpty: View) {
        this.viewEmpty = viewEmpty
        setLayoutManager(recyclerView, RvLayoutManagerEnums.LinearLayout_VERTICAL)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        setLayoutManager(recyclerView, RvLayoutManagerEnums.LinearLayout_VERTICAL)
    }

    fun setRecyclerView(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        this.recyclerView = recyclerView
        recyclerView.adapter = this
        recyclerView.layoutManager = layoutManager
    }

    private fun setLayoutManager(recyclerView: RecyclerView, type: RvLayoutManagerEnums?) {
        when (type) {
            RvLayoutManagerEnums.LinearLayout_VERTICAL -> recyclerView.layoutManager =
                LinearLayoutManager(context)
            RvLayoutManagerEnums.LinearLayout_HORIZONTAL -> recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            RvLayoutManagerEnums.LinearLayout_INVALID_OFFSET -> recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.INVALID_OFFSET, false)
            RvLayoutManagerEnums.GridLayoutManager_spanCount1 -> recyclerView.layoutManager =
                GridLayoutManager(context, 1)
            RvLayoutManagerEnums.GridLayoutManager_spanCount2 -> recyclerView.layoutManager =
                GridLayoutManager(context, 2)
            RvLayoutManagerEnums.GridLayoutManager_spanCount3 -> recyclerView.layoutManager =
                GridLayoutManager(context, 3)
            RvLayoutManagerEnums.StaggeredGridLayoutManager_spanCount2 -> recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        }
        recyclerView.adapter = this
        this.recyclerView = recyclerView
//
//        recyclerView.visibility = View.GONE
//        viewEmpty?.let { it.visibility = View.GONE }
    }

    /**
     * set Load More SwipeRefreshLayout
     *
     * @param recyclerViewLoadMoreListener
     */
    fun setLoadData(
        recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener
    ) {
        setLoadData(null, null, recyclerViewLoadMoreListener)
    }

    /**
     * set Load More SwipeRefreshLayout
     *
     * @param swRefresh
     * @param recyclerViewLoadMoreListener
     */
    fun setLoadData(
        swRefresh: SwipeRefreshLayout?,
        recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener
    ) {
        setLoadData(swRefresh, null, recyclerViewLoadMoreListener)
    }

    /**
     * set Load More SwipeRefreshLayout, Edt search
     *
     * @param swRefresh
     * @param editText
     * @param recyclerViewLoadMoreListener
     */
    fun setLoadData(
        swRefresh: SwipeRefreshLayout?,
        editText: EditText?,
        recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener
    ) {
        this.swRefresh = swRefresh
//        if (recycleViewLayoutManagerEnums == RvLayoutManagerEnums.LinearLayout_VERTICAL || recycleViewLayoutManagerEnums == RvLayoutManagerEnums.LinearLayout_HORIZONTAL
//        ) {
        recyclerViewEventLoad = RecyclerViewEventLoad(
            recyclerView!!.layoutManager!!,
            swRefresh,
            editText,
            recyclerViewLoadMoreListener
        )
        recyclerView!!.addOnScrollListener(recyclerViewEventLoad!!)
//        } else {
//            Log.e(TAG, "Not set load more")
//        }
    }


    /**
     * resetLoadMore
     * clear loading
     */

    fun resetLoadMore() {
        recyclerViewEventLoad!!.reset()
    }

    /**
     * reload data
     * index = 0
     */
    fun reloadData() {
        recyclerViewEventLoad!!.reload(0)
    }


    fun destroy() {
        itemsCache!!.clear()
        items!!.clear()
        mOnAdapterListener = null
        viewHolder = null
        context = null
        viewEmpty = null
        recyclerViewEventLoad = null
        recyclerView = null
        swRefresh = null
        countTest = 0
        isItemView = false
    }

}