package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.Utility;

public class SongVerticalItemViewHolder extends RecyclerView.ViewHolder {


    public LinearLayout parent,imgPlay;

     ImageView imageViewClass,addToFav;
     TextView textViewSongName,textViewArtistName;
    private final Context context;
    boolean isArabic = Utility.isArabic();
    int showInScreen;

    public SongVerticalItemViewHolder(View itemView,int showInScreen) {
        super(itemView);

        context = itemView.getContext();
        this.showInScreen = showInScreen;

        itemView.setClickable(true);
        textViewSongName = itemView.findViewById(R.id.textViewSongName);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        imgPlay = itemView.findViewById(R.id.imgPlay);
        addToFav = itemView.findViewById(R.id.addToFav);

        imageViewClass = itemView.findViewById(R.id.imageViewClass);




    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Song) {
            final Song model = (Song) data;
            itemView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(model.getAlbumArt())
                    .into(imageViewClass);
            if (isArabic) {
                String songName = model.getSongNameAr()!=null?model.getSongNameAr():model.getSongsNameAr();
                String categoryName;

                if(showInScreen==100){
                    categoryName = model.getCategoryName();
                    songName = songName+"~"+categoryName;
                }
                textViewSongName.setText(songName);
                textViewArtistName.setText(model.getArtistNameAr());

            }
            else{
                String songName = model.getSongName();

                String categoryName;

                if(showInScreen==100){
                    categoryName = model.getCategoryName();
                    songName = songName+"~"+categoryName;
                }
                textViewSongName.setText(songName);
                textViewArtistName.setText(model.getArtistName());
            }



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallbacks.onAdapterItemClick(SongVerticalItemViewHolder.this,imgPlay,data,position);
                }
            });

            addToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallbacks.onAdapterItemClick(SongVerticalItemViewHolder.this,addToFav,data,position);
                }
            });
            if(model.getFavStatus()){
                addToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
            }else{
                addToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));

            }



        } else {
            itemView.setVisibility(View.GONE);
        }
    }




}
