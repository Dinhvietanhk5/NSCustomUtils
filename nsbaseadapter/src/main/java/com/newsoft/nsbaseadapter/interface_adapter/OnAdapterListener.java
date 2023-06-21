package com.newsoft.nsbaseadapter.interface_adapter;

public interface OnAdapterListener<T> {

    /**
     * @param id defaults 0
     * @param item
     * @param position
     */
    void onItemClick(int id, T item, int position);
}
