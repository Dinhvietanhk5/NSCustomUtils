package com.newsoft.nscustom.view.recyclerview.interface_adapter;

import android.view.ViewGroup;

public interface IViewHolder <T, VH> {

    /**
     * @return XViewHolder
     */
    VH onCreateHolder(ViewGroup parent, int viewType);

    /**
     * @param holder   XViewHolder
     * @param item
     * @param position
     * @param size
     */
    void onBindView(VH holder, T item, int position, int size);

}
