package com.newsoft.nscustomutils

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.newsoft.nscustom.view.recyclerview.BaseAdapter
import kotlinx.android.synthetic.main.item_string.view.*

class TestAdapter :
    BaseAdapter<String?, TestAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle = view.tvTitle
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(setView(R.layout.item_string))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindView(
        holder: ViewHolder?,
        item: String?,
        position: Int,
        size: Int
    ) {
//        holder!!.btnChooseVideo!!.setOnClickListener {
//            positionSelector = position
//            mOnAdapterListener?.onItemClick(0, item, position)
//            notifyDataSetChanged()
//        }
        holder!!.tvTitle.text = "test $position"
//        holder.btnChooseVideo.setBackgroundColor(
//            ContextCompat.getColor(
//                requireContextAdapter(),
//                if (positionSelector == position) R.color.colorPrimary
//                else R.color.gray3
//            )
//        )
    }

}