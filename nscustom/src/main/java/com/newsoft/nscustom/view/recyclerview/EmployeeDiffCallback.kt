package com.newsoft.nscustom.view.recyclerview

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import java.util.*


class EmployeeDiffCallback<T>(oldEmployeeList: ArrayList<T>, newEmployeeList: ArrayList<T>) :
    DiffUtil.Callback() {

    private val mOldEmployeeList: ArrayList<T> = oldEmployeeList
    private val mNewEmployeeList: ArrayList<T> = newEmployeeList

    override fun getOldListSize(): Int {
        return mOldEmployeeList.size
    }

    override fun getNewListSize(): Int {
        return mNewEmployeeList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldEmployeeList[oldItemPosition]  == mNewEmployeeList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldEmployeeList[oldItemPosition]  == mNewEmployeeList[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}