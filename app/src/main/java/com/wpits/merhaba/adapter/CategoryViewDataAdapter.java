package com.wpits.merhaba.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wpits.merhaba.MainActivity;
import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.AlbumsActivity;

import com.wpits.merhaba.model.category.Category;

import java.util.ArrayList;

public class CategoryViewDataAdapter extends RecyclerView.Adapter<CategoryViewDataAdapter.ItemRowHolder> {

    private ArrayList<Category> dataList;
    private Context mContext;
    private MainActivity obj;

    public CategoryViewDataAdapter(Context mContext, ArrayList<Category> dataList, MainActivity obj) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.obj = obj;
    }


    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getCategoryName();
        final String sectionName_ar = dataList.get(i).getCategoryNameAr();
        final int categoryId=dataList.get(i).getId();
        Log.d("XYZABC " , String.valueOf(categoryId));
        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();


//           Log.d("ABC " ,singleSectionItems.toString());

        itemRowHolder.itemTitle.setText(sectionName_ar);
        if(categoryId == 1)
        itemRowHolder.catImageButton.setImageResource(R.drawable.slide1);
        else if(categoryId == 2)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide2);
        else if(categoryId == 3)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide3);
        else if(categoryId == 4)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide4);
        else if(categoryId == 5)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide5);
        else if(categoryId == 6)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide6);
        else if(categoryId == 7)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide7);
        else if(categoryId == 8)
            itemRowHolder.catImageButton.setImageResource(R.drawable.slide8);


        int categoryid=dataList.get(i).getId();


        AlbumListAdapter itemListDataAdapter = new AlbumListAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);

        itemRowHolder.catImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Category Click ID -- > ",""+v.getId()+" "+categoryId);
                //populateSongs(categoryId);
                obj.populateSongs(categoryId, sectionName,sectionName_ar);


            }
        });

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent albumActivity=new Intent(v.getContext(), AlbumsActivity.class);
              //  albumActivity.putExtra("CategoryID",categoryId);
                v.getContext().startActivity(albumActivity);
                //          Toast.makeText(v.getContext(), "click event on more, "+sectionName +categoryId , Toast.LENGTH_SHORT).show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder  extends RecyclerView.ViewHolder{

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected TextView btnMore;

        protected ImageButton catImageButton;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (TextView) view.findViewById(R.id.btnMore);
            this.catImageButton = view.findViewById(R.id.catImage);
        }
    }
}
