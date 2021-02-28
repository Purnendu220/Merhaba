package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.AdapterGridView;
import com.wpits.merhaba.model.CategoryListModel;
import com.wpits.merhaba.utility.Utility;

import pl.droidsonroids.gif.GifImageView;


public class CategoryViewHoder extends RecyclerView.ViewHolder {

    public TextView textViewRecomended;
    public RecyclerView recyclerViewRecommendedCategories;
    public GifImageView loader2;

    public LinearLayout parent;

    private final Context context;
    boolean isArabic = Utility.isArabic();

    public CategoryViewHoder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);
        textViewRecomended= itemView.findViewById(R.id.textViewRecomended);
        recyclerViewRecommendedCategories = itemView.findViewById(R.id.recyclerViewRecommendedCategories);
        loader2 = itemView.findViewById(R.id.loader2);



    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof CategoryListModel) {
            CategoryListModel model = (CategoryListModel) data;
            itemView.setVisibility(View.VISIBLE);
            initRecyclerView(model,adapterCallbacks);
            loader2.setVisibility(View.GONE);
            if(isArabic){
               textViewRecomended.setText("التصنيفات");
            }else{
                textViewRecomended.setText("Categories");

            }






        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView(CategoryListModel model,AdapterCallbacks adapterCallbacks){
        AdapterGridView adapter = new AdapterGridView(context,0,false,adapterCallbacks);
        adapter.addAllClass(model.getCategories());
        recyclerViewRecommendedCategories.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewRecommendedCategories.setLayoutManager(layoutManager);
        recyclerViewRecommendedCategories.setAdapter(adapter);
    }
}