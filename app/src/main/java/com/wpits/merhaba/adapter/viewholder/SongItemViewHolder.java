package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.Utility;


public class SongItemViewHolder extends RecyclerView.ViewHolder {


    public LinearLayout parent,imgPlay;
    public TextView textViewSongName,textViewArtistName,textViewAlbum;
    public ImageView imageViewClass,addToFav;

    private final Context context;
    boolean isArabic = Utility.isArabic();

    public SongItemViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);
        textViewSongName = itemView.findViewById(R.id.textViewSongName);
        textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        textViewAlbum = itemView.findViewById(R.id.textViewAlbum);
        imageViewClass = itemView.findViewById(R.id.imageViewClass);

        imgPlay = itemView.findViewById(R.id.imgPlay);
        addToFav = itemView.findViewById(R.id.addToFav);


    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Song) {
            final Song model = (Song) data;
            itemView.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(model.getAlbumArt())
                    .into(imageViewClass,new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
            if(isArabic){
                textViewSongName.setText(model.getSongsNameAr());
                textViewArtistName.setText(model.getArtistNameAr());
                textViewAlbum.setText(model.getCategoryNameAr());

            }else{
                textViewSongName.setText(model.getSongName());
                textViewArtistName.setText(model.getArtistName());
                textViewAlbum.setText(model.getCategoryName());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallbacks.onAdapterItemClick(SongItemViewHolder.this,imgPlay,data,position);
                }
            });

            addToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallbacks.onAdapterItemClick(SongItemViewHolder.this,addToFav,data,position);
                }
            });
            if(model.getFavStatus()){
                addToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
            }else{
                addToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));

            }
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SongDetailActivity.open(context,model);
//                }
//            });
//            Picasso.get()
//                    .load(model.getAlbumArt())
//                    .into(imageViewClass);

        } else {
            itemView.setVisibility(View.GONE);
        }
    }




    }
