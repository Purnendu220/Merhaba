package com.wpits.merhaba.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.SongsListViewAllAdapter;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.MySingleton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phonenumberui.PhoneNumberActivity;
import pl.droidsonroids.gif.GifImageView;

public class SongActivity extends AppCompatActivity implements AdapterCallbacks<Object> {

    RecyclerView songRecycler;
    Toolbar mToolbar;
    GifImageView loader2;
    SongsListViewAllAdapter songsListAdapter;
    Context mContext;
    JcPlayerView jcplayerView;
    boolean isArabic = Utility.isArabic;
    int categoryId;

    public static void open(Context context,int categoryId){
         Intent i =  new Intent(context,SongActivity.class);
         i.putExtra("CAT",categoryId);

         context.startActivity(i);

     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

         categoryId = getIntent().getIntExtra("CAT",0);

        mContext = this;
        songRecycler=findViewById(R.id.songRecyclerView);
        mToolbar=findViewById(R.id.songToolbar);
        loader2 = findViewById(R.id.loader2);
        jcplayerView = findViewById(R.id.jcplayer);

        songsListAdapter=new SongsListViewAllAdapter(mContext,0,false,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        songRecycler.setLayoutManager(mLayoutManager);
        songRecycler.setItemAnimator(new DefaultItemAnimator());
        songRecycler.setAdapter(songsListAdapter);
        loader2.setVisibility(View.VISIBLE);
        albumApi(categoryId);
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private List<Song> albumApi(final int categoryId) {
        loader2.setVisibility(View.VISIBLE);

        final List<Song> songsList = new ArrayList<Song>();
        String album_url = "https://www.marhaba.com.ly:18083/topContent/topContentByCtgId?ctgId="+categoryId;
        JsonObjectRequest albumRequest=new JsonObjectRequest(Request.Method.GET, album_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                songsListAdapter.clearAll();
                Log.d("Response --->",response.toString());

                int catCategoryId=categoryId;

                try {
                    JSONArray data = response.getJSONArray("data");
                    Log.d("Songs ", response.toString());

                    for(int i=0;i<data.length();i++){

                        JSONObject topContent=data.getJSONObject(i);
                        //JSONObject categoryContent=categoryList.getJSONObject("album");
                        Log.d("Songs", topContent.toString());
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

                    songsListAdapter.addAllClass(songsList);
                    songsListAdapter.notifyDataSetChanged();
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
        MySingleton.getInstance(mContext).addToRequest(albumRequest);

        return songsList;
    }



    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
      switch (view.getId()){
          case R.id.imgPlay:
              Song mSong = (Song) model;
                PlayerActivity.open(mContext,0,categoryId,mSong);
                break;
          case R.id.addToFav:
              Song song = (Song) model;
              if(PrefrenceManager.getInstance().isLoggedIn()){
                  if(song.getFavStatus()){
                      unfavRequest(song);
                  }else{
                      addToFavourite(song);

                  }

              }else{
                  Intent i = new Intent(SongActivity.this, PhoneNumberActivity.class);
                  startActivity(i);
                  finish();
              }

              break;
      }
    }

    private void addToFavourite(Song mSong){
        final ProgressDialog dialog = Utility.showProgress(mContext);
        String x="https://www.marhaba.com.ly:18086/crbt/v1/favorites";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("subscriber_id", PrefrenceManager.getInstance().getUserMobile());
            jsonRequest.put("top_content_id", mSong.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Fav Request", JsonUtils.toJson(jsonRequest));

        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.POST, x,jsonRequest , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CategoryResponse",response.toString());
                try {

                    Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);



                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                albumApi(categoryId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
                loader2.setVisibility(View.GONE);
                dialog.dismiss();

            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }
    private void unfavRequest(Song mSong){
        final ProgressDialog dialog = Utility.showProgress(mContext);

        String x="https://www.marhaba.com.ly:18086/crbt/v1/deleteFavorite";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("subscriber_id", PrefrenceManager.getInstance().getUserMobile());
            jsonRequest.put("top_content_id", mSong.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Fav Request", JsonUtils.toJson(jsonRequest));
        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.GET, x,jsonRequest , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CategoryResponse",response.toString());
                try {




                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                albumApi(categoryId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
                loader2.setVisibility(View.GONE);
                dialog.dismiss();

            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }



    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
    private void play(Song mSong){
        jcplayerView.kill();
        jcplayerView.setVisibility(View.VISIBLE);
        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        JcAudio jcAudio;
        if(isArabic)
            jcAudio = JcAudio.createFromURL(mSong.getSongsNameAr(), mSong.getContentPathLocation());
        else
            jcAudio = JcAudio.createFromURL(mSong.getSongName(), mSong.getContentPathLocation());
        jcAudios.add(jcAudio);

        jcplayerView.initPlaylist(jcAudios, null);
        jcplayerView.playAudio(jcAudio);
        jcplayerView.setJcPlayerManagerListener(new JcPlayerManagerListener() {
            @Override
            public void onPreparedAudio(@NotNull JcStatus jcStatus) {

            }

            @Override
            public void onCompletedAudio() {
                jcplayerView.setActivated(false);
                jcplayerView.setVisibility(View.INVISIBLE);
                jcplayerView.kill();
            }

            @Override
            public void onPaused(@NotNull JcStatus jcStatus) {
                jcplayerView.setActivated(false);
                jcplayerView.setVisibility(View.INVISIBLE);
                jcplayerView.kill();
            }

            @Override
            public void onContinueAudio(@NotNull JcStatus jcStatus) {

            }

            @Override
            public void onPlaying(@NotNull JcStatus jcStatus) {

            }

            @Override
            public void onTimeChanged(@NotNull JcStatus jcStatus) {

            }

            @Override
            public void onStopped(@NotNull JcStatus jcStatus) {

            }

            @Override
            public void onJcpError(@NotNull Throwable throwable) {

            }
        });
    }

}
