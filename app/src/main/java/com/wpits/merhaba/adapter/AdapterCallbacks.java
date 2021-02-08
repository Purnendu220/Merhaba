package com.wpits.merhaba.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by MyU10 on 3/15/2018.
 */

public interface AdapterCallbacks<Object> {
    void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position);

    void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position);

    void onShowLastItem();
}
