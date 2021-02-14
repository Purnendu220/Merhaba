package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.SongActivity;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.model.category.Category;
import com.wpits.merhaba.utility.Utility;

public class GridItemViewHolder extends RecyclerView.ViewHolder {


    public LinearLayout parent;
    public ImageView imageViewClass;
    public TextView textViewCategoryName;

    private final Context context;
    boolean isArabic = Utility.isArabic;

    public GridItemViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);
        imageViewClass = itemView.findViewById(R.id.imageViewClass);
        textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);



    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Category) {
            final Category model = (Category) data;
            itemView.setVisibility(View.VISIBLE);
            if(isArabic){
                textViewCategoryName.setText(model.getCategoryNameAr());
            }else{
                textViewCategoryName.setText(model.getCategoryName());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongActivity.open(context,model.getId());

                  //  PlayerActivity.open(context,0,model.getId());

                }
            });
//      itemView.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//
//              SongActivity.open(context,model.getId());
//          }
//      });
            switch (model.getId()){
                case 1:
                    imageViewClass.setImageResource(R.drawable.ic_cat_islamic_ico);

                    break;
                case 2:
                    imageViewClass.setImageResource(R.drawable.ic_cat_patriotic_ico);

                    break;
                case 3:
                    imageViewClass.setImageResource(R.drawable.ic_cat_islamic_ico);//islamic

                    break;
                case 4:
                    imageViewClass.setImageResource(R.drawable.ic_cat_patriotic_ico);//national

                    break;
                case 5:
                    imageViewClass.setImageResource(R.drawable.ic_cat_social_ico);//social

                    break;
                case 6:
                    imageViewClass.setImageResource(R.drawable.ic_cat_athletics_ico);// sports

                    break;
                case 7:
                    imageViewClass.setImageResource(R.drawable.comedy);

                    break;
                case 8:
                    imageViewClass.setImageResource(R.drawable.miscellaneous);

                    break;



            }








        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView(){



    }
}