package com.wpits.merhaba.adapter.viewholder;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.SongListAdapterNew;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.model.category.Category;
import com.wpits.merhaba.remoteConfig.RemoteConfigure;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.CirclePagerIndicatorDecoration;
import com.wpits.merhaba.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class SongsViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewRecomended;
    public RecyclerView recyclerViewRecommendedCategories;
    public GifImageView loader2;


    private final Context context;
    boolean isArabic = Utility.isArabic;

    public SongsViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);
        textViewRecomended= itemView.findViewById(R.id.textViewRecomended);
        recyclerViewRecommendedCategories = itemView.findViewById(R.id.recyclerViewRecommendedCategories);
        loader2 = itemView.findViewById(R.id.loader2);

    }
    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Category) {
            Category model = (Category) data;
            itemView.setVisibility(View.VISIBLE);
            if(position == 0){
                if(isArabic){

                    textViewRecomended.setText( RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigure.top_twenty_ar));

                }else{
                    textViewRecomended.setText( RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigure.top_twenty_en));

                }
            }
            if(position == 1){
                if(isArabic){
                    textViewRecomended.setText( RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigure.new_arrival_ar));

                }else{
                    textViewRecomended.setText( RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigure.new_arrival_en));


                }
            }

            albumApi(model.getId(),adapterCallbacks);








        } else {
            itemView.setVisibility(View.GONE);
        }
    }
    private void initRecyclerView(List<Song> list,AdapterCallbacks adapterCallbacks){
        recyclerViewRecommendedCategories.setOnFlingListener(null);

        SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recyclerViewRecommendedCategories);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);

        SongListAdapterNew adapter = new SongListAdapterNew(context,0,false,adapterCallbacks);
        adapter.addAllClass(list);
        recyclerViewRecommendedCategories.setHasFixedSize(true);
        //recyclerViewRecommendedCategories.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewRecommendedCategories.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRecommendedCategories.setAdapter(adapter);
        recyclerViewRecommendedCategories.addItemDecoration(new CirclePagerIndicatorDecoration());

    }

    private List<Song> albumApi(final int categoryId, final AdapterCallbacks adapterCallbacks) {
        final List<Song> songsList = new ArrayList<Song>();
        String album_url = "https://www.marhaba.com.ly:18083/topContent/topContentByCtgId?ctgId="+categoryId;
        JsonObjectRequest albumRequest=new JsonObjectRequest(Request.Method.GET, album_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                int catCategoryId=categoryId;

                try {
                    JSONArray data = response.getJSONArray("data");

                    for(int i=0;i<data.length();i++){
                        JSONObject topContent=data.getJSONObject(i);
                        //JSONObject categoryContent=categoryList.getJSONObject("album");
                        JSONObject songContent = topContent.getJSONObject("topContent");
                        String favStatus = topContent.getString("favStatus");

                        int id = songContent.getInt("id");
                        int songId =songContent.getInt("songId");
                        String songName =songContent.getString("songName");
                        int categoryId=songContent.getInt("categoryId");
                        String categoryName = songContent.getString("category");
                        String categoryNameAr=   songContent.getString("categoryNameAr");
                        String songsNameAr=   songContent.getString("songsNameAr");
                        String artistName=   songContent.getString("artistName");
                        String artistNameAr=   songContent.getString("artistNameAr");
                        String albumArt=   songContent.getString("albumArt");
                        String contentPathLocation=   songContent.getString("contentPathLocation");

                        Song song=new Song();
                        song.setId(id);
                        song.setAlbumArt(albumArt);
                        song.setArtistName(artistName);
                        song.setArtistNameAr(artistNameAr);
                        song.setCategoryId(categoryId);
                        song.setCategoryName(categoryName);
                        song.setCategoryNameAr(categoryNameAr);
                        song.setSongName(songName);
                        song.setSongsNameAr(songsNameAr);
                        song.setContentPathLocation(contentPathLocation);
                        song.setSongId(songId);
                        song.setFavStatus(Utility.getFavStatus(favStatus));

                        Log.d("Songs", categoryId+" "+catCategoryId);

                        if(catCategoryId == categoryId) {
                            songsList.add(song);

                        }
                    }
                    initRecyclerView(songsList,adapterCallbacks);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loader2.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error-->",error.getLocalizedMessage());
                loader2.setVisibility(View.GONE);

            }

        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        }
                ;
        MySingleton.getInstance(context).addToRequest(albumRequest);

        return songsList;
    }



}