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
import com.newsoft.nscustom.ext.view.setOnClickBounceEffect
import com.newsoft.nscustom.view.recyclerview.interface_adapter.IViewHolder
import com.newsoft.nscustom.view.recyclerview.interface_adapter.OnAdapterListener
import com.newsoft.nscustom.view.recyclerview.interface_adapter.RecyclerViewLoadMoreListener

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>() :
    RecyclerView.Adapter<VH>(),
    IViewHolder<T, VH> {

    var items: ArrayList<T>

    //    protected var itemsCache: ArrayList<T>? = null
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
      private var isItemView = false //TODO: false itemview click, true không phải itemview click
    private var isReload = true  //TODO: false khong reload, true reload
    private var isBounceEffect = false

    /**
     * init Adapter
     */
    init {
        items = ArrayList()
    }

    fun requireContextAdapter(): Context {
        return context ?: throw IllegalStateException("Fragment $this not attached to a context.")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<T>?, index: Int) {
        try {
            setItems(items as ArrayList<T>, index)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * setItems
     *  index = 0 -> setItem
     *  index != 0 -> addItem
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: ArrayList<T>?, index: Int) {
        try {
            recyclerViewEventLoad?.let {
                it.index = index
            }

            if (items == null || items.size == 0) {
                if (index <= 10) setEmptyItems()
//            recyclerViewEventLoad?.let {
//                it.index = index - 10
//            }
                return
            }
            if (this.items == null)
                this.items = ArrayList()

            viewEmpty?.let { it.visibility = View.GONE }
            recyclerView?.let { it.visibility = View.VISIBLE }

            if (index <= 11) {
                this.items = items
//            this.itemsCache = items
            } else {
//            this.itemsCache!!.addAll(items)
                this.items.addAll(items)
            }
            notifyDataSetChanged()
            recyclerViewEventLoad?.setLoaded()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: T, index: Int = -1) {
        try {
            if (index == -1)
                this.items.add(item)
            else
                this.items.add(index, item)
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: ArrayList<T>, index: Int = -1) {
        try {
            if (index == -1)
                this.items.addAll(items)
            else
                this.items.addAll(index, items)
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        return items[position]
    }

    /**
     * clearItem
     * index = 0
     */

    fun clearItem() {
        recyclerViewEventLoad?.let { it.index = 0 }
        items.let { it.clear() }
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            viewHolder = holder
            if (items.size > 0) onBindView(holder, items[position], position, realCount())
            else if (countTest != 0)
                onBindView(holder, null, position, realCount())

            val checkNullItem = if (items.size > 0) items[position] else null

            if (mOnAdapterListener != null && !isItemView) {
                if (isBounceEffect)
                    holder.itemView.setOnClickBounceEffect {
                        mOnAdapterListener!!.onItemClick(0, checkNullItem, position)
                    }
                else
                    holder.itemView.setOnClickListener {
                        mOnAdapterListener!!.onItemClick(0, checkNullItem, position)
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
        return if (items.size == 0) countTest else items.size
    }

    fun setView(layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, parent, false)
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
        recyclerViewEventLoad = RecyclerViewEventLoad(
            recyclerView!!.layoutManager!!,
            swRefresh,
            editText,
            recyclerViewLoadMoreListener
        )
        recyclerView?.addOnScrollListener(recyclerViewEventLoad!!)
    }


    /**
     * init NSRecyclerview
     */

    fun setRecyclerView(
        nsRecyclerView: NSRecyclerview,
        type: RvLayoutManagerEnums = RvLayoutManagerEnums.LinearLayout_VERTICAL,
        countTest: Int = 0
    ) {
        recyclerView = nsRecyclerView.recyclerView
        viewEmpty = nsRecyclerView.viewEmpty
        swRefresh = nsRecyclerView.swRefresh

        val layout = when (type) {
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

        setRecyclerView(nsRecyclerView, layout, countTest)
    }

    fun setRecyclerView(
        nsRecyclerView: NSRecyclerview,
        layoutManager: RecyclerView.LayoutManager,
        countTest: Int? = 0
    ) {
        viewEmpty = nsRecyclerView.viewEmpty
        swRefresh = nsRecyclerView.swRefresh
        recyclerView = nsRecyclerView.recyclerView


        viewEmpty?.let {
            this.viewEmpty = it
        }
        setCountItemTest(countTest!!)
        this.recyclerView = nsRecyclerView.recyclerView
        this.recyclerView!!.adapter = this
        this.recyclerView!!.layoutManager = layoutManager
    }

    /**
     * set Load More NSRecyclerview, Edt search
     *
     * @param editText
     * @param recyclerViewLoadMoreListener
     */
    fun setLoadData(
        editText: EditText? = null,
        recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener,
    ) {
        if (!isReload) {
            swRefresh?.apply {
                isRefreshing = false
                isEnabled = false
            }
        }
        recyclerViewEventLoad = RecyclerViewEventLoad(
            recyclerView!!.layoutManager!!,
            swRefresh,
            editText,
            recyclerViewLoadMoreListener
        )
        recyclerView?.addOnScrollListener(recyclerViewEventLoad!!)
    }

    fun disableReload() {
        isReload = false
    }

    fun setItemView(isItemView: Boolean) {
        this.isItemView = isItemView
    }

    fun setBounceEffect(isBounceEffect: Boolean) {
        this.isBounceEffect = isBounceEffect
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
//            itemsCache?.clear()
            items?.clear()
            mOnAdapterListener = null
            viewHolder = null
            context = null
            viewEmpty = null
            recyclerViewEventLoad = null
            recyclerView = null
            countTest = 0
            isItemView = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}