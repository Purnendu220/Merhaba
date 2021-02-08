package com.wpits.merhaba.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wpits.merhaba.model.album.Content;

import java.util.ArrayList;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongViewHolder> {

    ArrayList<Content> songarraylist=new ArrayList<>();

    public SongsListAdapter(ArrayList<Content> songarrayList1) {
        this.songarraylist = songarraylist;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        public SongViewHolder(View itemView) {
            super(itemView);
        }
    }
}
