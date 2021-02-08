package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;



/**
 * Created by MyU10 on 3/10/2018.
 */

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    public EmptyViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

    }

    public void bind() {
        itemView.setVisibility(View.GONE);
    }
}
