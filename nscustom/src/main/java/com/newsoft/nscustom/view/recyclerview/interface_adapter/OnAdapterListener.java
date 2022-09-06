package com.newsoft.nscustom.view.recyclerview.interface_adapter;

public interface OnAdapterListener<T> {

    /**
     * @param id defaults 0
     * @param item
     * @param position
     */
    void onItemClick(int id, T item, int position);
}
