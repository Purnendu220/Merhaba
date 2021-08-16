package com.wpits.merhaba.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.SongActivity;
import com.wpits.merhaba.model.album.Song;

import java.util.ArrayList;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.SingleItemRowholder> {

    private ArrayList<Song> itemsList;
    private Context mContext;
    ArrayList songAlbumList;

    public AlbumListAdapter(Context context, ArrayList itemsList) {

        this.itemsList = itemsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SingleItemRowholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowholder mh = new SingleItemRowholder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowholder holder, int i) {

        Song singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getCategoryName());
        holder.albumId=singleItem.getCategoryId();
         //songAlbumList= (ArrayList) singleItem.getSongs();

     //   String albumImageURL=ApplicationUrl.IpImageUrl +singleItem.getAlbumArt();
        //   String albumImageURL=ApplicationUrl.IpImageUrl+"image/" +singleItem.getAlbumTitle()+".jpg";  //VPN API 10.111.222.111
      //  Glide.with(mContext).load(albumImageURL).into(holder.itemImage);

    }



    @Override
    public int getItemCount() {
            return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowholder extends RecyclerView.ViewHolder{

        protected TextView tvTitle;

        protected ImageView itemImage;

        int albumId;

        public SingleItemRowholder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


          //          Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                    Intent songActivity=new Intent(v.getContext(), SongActivity.class);
                    Log.d("SongAlbumId", String.valueOf(albumId));
                    songActivity.putExtra("SONGARRAY",songAlbumList);
                    v.getContext().startActivity(songActivity);

                }
            });
        }
    }
}
