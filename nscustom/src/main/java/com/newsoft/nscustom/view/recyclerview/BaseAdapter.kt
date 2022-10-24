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

    var items: ArrayList<T>?
    protected var itemsCache: ArrayList<T>? = null
    var mOnAdapterListener: OnAdapterListener<T>? = null
    var viewHolder: VH? = null
    private var context: Context? = null
    private var viewEmpty: View? = null
    private var recyclerViewEventLoad: RecyclerViewEventLoad? = null
    private var recyclerView: RecyclerView? = null
    private val TAG = "BaseAdapter"
    private var swRefresh: SwipeRefreshLayout? = null
    private var countTest = 0
    private var parent: ViewGroup? = null
    var isItemView = false //TODO: false itemview click, true không phải itemview click

    /**
     * init Adapter
     */
    init {
        items = ArrayList()
    }

    fun requireContextAdapter(): Context {
        return context ?: throw IllegalStateException("Fragment $this not attached to a context.")
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
        if (this.items == null)
            this.items = ArrayList()

        if (items == null || items.size == 0) {
            if (index <= 10)
                setEmptyItems()
//            recyclerViewEventLoad?.let {
//                it.index = index - 10
//            }
            return
        }

        viewEmpty?.let { it.visibility = View.GONE }
        recyclerView?.let { it.visibility = View.VISIBLE }

        if (index <= 11) {
            this.items = items
//            this.itemsCache = items
        } else {
//            this.itemsCache!!.addAll(items)
            this.items!!.addAll(items)
        }
        notifyDataSetChanged()
        recyclerViewEventLoad?.setLoaded()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: T?) {
        this.items!!.add(item!!)
        notifyDataSetChanged()
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
                holder.itemView.setOnClickListener {
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
        return LayoutInflater.from(context).inflate(layout, parent, false)
    }

    /**
     * setCountItemTest
     * countTest == 0 viewEmpty VISIBLE & , recyclerView GONE
     * countTest != 0 viewEmpty GONE & , recyclerView VISIBLE
     */
    private fun setCountItemTest(countTest: Int) {
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
     * onClick not itemview
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

    fun setRecyclerView(
        recyclerView: RecyclerView,
        viewEmpty: View? = null,
        type: RvLayoutManagerEnums = RvLayoutManagerEnums.LinearLayout_VERTICAL,
        countTest: Int = 0
    ) {
        var layout: RecyclerView.LayoutManager = LinearLayoutManager(context)
        layout = when (type) {
            RvLayoutManagerEnums.LinearLayout_VERTICAL -> LinearLayoutManager(context)
            RvLayoutManagerEnums.LinearLayout_HORIZONTAL -> LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            RvLayoutManagerEnums.LinearLayout_INVALID_OFFSET -> LinearLayoutManager(
                context,
                LinearLayoutManager.INVALID_OFFSET,
                false
            )
            RvLayoutManagerEnums.GridLayoutManager_spanCount1 -> GridLayoutManager(context, 1)
            RvLayoutManagerEnums.GridLayoutManager_spanCount2 -> GridLayoutManager(context, 2)
            RvLayoutManagerEnums.GridLayoutManager_spanCount3 -> GridLayoutManager(context, 3)
            RvLayoutManagerEnums.StaggeredGridLayoutManager_spanCount2 -> StaggeredGridLayoutManager(
                2,
                LinearLayout.VERTICAL
            )
        }
        setRecyclerView(recyclerView, viewEmpty, layout, countTest)
    }

    fun setRecyclerView(
        recyclerView: RecyclerView,
        viewEmpty: View? = null,
        layoutManager: RecyclerView.LayoutManager,
        countTest: Int = 0
    ) {
        viewEmpty?.let {
            this.viewEmpty = it
        }
        setCountItemTest(countTest)
        this.recyclerView = recyclerView
        this.recyclerView!!.adapter = this
        this.recyclerView!!.layoutManager = layoutManager
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
        swRefresh: SwipeRefreshLayout? = null,
        editText: EditText? = null,
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
        recyclerView?.addOnScrollListener(recyclerViewEventLoad!!)
//        } else {
//            Log.e(TAG, "Not set load more")
//        }
    }


    /**
     * resetLoadMore
     * clear loading
     */

    fun resetLoadMore() {
        recyclerViewEventLoad?.let { it.reset() }
    }

    /**
     * reload data
     * index = 0
     */
    fun reloadData() {
        recyclerViewEventLoad?.let { it.reload(0) }
    }


    fun destroy() {
        try {
            itemsCache?.clear()
            items?.clear()
            mOnAdapterListener = null
            viewHolder = null
            context = null
            viewEmpty = null
            recyclerViewEventLoad = null
            recyclerView = null
            swRefresh = null
            countTest = 0
            isItemView = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}