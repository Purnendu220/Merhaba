package com.wpits.merhaba.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.gson.Gson;
import com.wpits.merhaba.MainActivity;
import com.wpits.merhaba.R;
import com.wpits.merhaba.adapter.AdapterCallbacks;
import com.wpits.merhaba.adapter.AdapterGridView;
import com.wpits.merhaba.adapter.CategoryViewDataAdapter;
import com.wpits.merhaba.adapter.HomeAdapter;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.CategoryListModel;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.model.category.Category;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.GiftRequest;
import com.wpits.merhaba.utils.MySingleton;
import com.wpits.merhaba.utils.SubscribeModel;
import com.wpits.merhaba.utils.SubscriptionDataModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterCallbacks<Object> {

        boolean isArabic = Utility.isArabic;
        private Toolbar mToolbar;
        DrawerLayout mDraweLayout;
        NavigationView navigationView;
        View header;
        RelativeLayout headerRelativeLayout;
        ImageView headerImage;
        TextView headerName,headerEmai;

        RecyclerView recyclerViewMain;
        List<Category> allSampleData = new ArrayList<>();

        Context mContext;
        HomeAdapter homeAdapter;
        GifImageView loader2;
        JcPlayerView jcplayerView;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(isArabic)
                setContentView(R.layout.activity_home);
            else
                setContentView(R.layout.activity_home);

            mContext=this;

            mToolbar =findViewById(R.id.toolbar);
            navigationView = findViewById(R.id.navigation_view);
            mDraweLayout = findViewById(R.id.drawerLayout);
            header=navigationView.inflateHeaderView(R.layout.header);
            headerRelativeLayout = header.findViewById(R.id.header_relative_layout);
            headerImage = header.findViewById(R.id.imageView);
            headerName=header.findViewById(R.id.txvName);
            headerEmai=header.findViewById(R.id.txvEmail);
            recyclerViewMain = findViewById(R.id.recyclerViewMain);
            loader2 = findViewById(R.id.loader2);
            jcplayerView = findViewById(R.id.jcplayer);



            setUpNavigationDrawerMenu();
            initRecyclerView();
            getCategory();



        }

    private void initRecyclerView(){
        homeAdapter = new HomeAdapter(mContext,0,false,this);
        recyclerViewMain.setHasFixedSize(false);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewMain.setAdapter(homeAdapter);
    }



        private void getCategory() {
            String x="https://www.marhaba.com.ly:18086/crbt/v1/category/List";

            JsonArrayRequest categoryRequest=new JsonArrayRequest(Request.Method.GET, x, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Category category;
                    Log.d("CategoryResponse",response.toString());
                    try {


                        //JSONArray ca=response.getJSONArray("category");
                        for(int i=0;i<response.length();i++){
                            if(i>7){
                                break;
                            }

                            JSONObject categoryObject=response.getJSONObject(i);
                            category=new Category();
                            category.setId(categoryObject.getInt("id"));
                            category.setCategoryName(categoryObject.getString("categoryName"));
                            category.setCategoryNameAr(categoryObject.getString("categoryName"));
                            categoryObject.getString("categoryNameAr");
                            // albumApi(category.getId());
                            Log.d("CategoryResponse2", String.valueOf(categoryObject.getInt("id")));
                            Log.d("CategoryResponse -->", category.getCategoryName()+" "+category.getId());
                            allSampleData.add(category);
                            if(i<2){
                              homeAdapter.addClass(category);
                            }

                        }
                        homeAdapter.addCategoryListModel(new CategoryListModel(allSampleData));

                       homeAdapter.notifyDataSetChanged();
                        loader2.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("CategoryErrorResponse",error.toString());
                    loader2.setVisibility(View.GONE);

                }
            });

            MySingleton.getInstance(getApplication()).addToRequest(categoryRequest);
        }



        public void loadSongs(int catId){
            //albumApi(catId);
            //populateSongs(songContentList);
        }

        private String getSubscriptionStatus(String msisdn){

            return null;
        }

        private void setUpNavigationDrawerMenu() {

            ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,
                    mDraweLayout,
                    mToolbar,
                    R.string.drawer_open,
                    R.string.drawer_close);

            mDraweLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        }





        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()) {

//            case R.id.profile:
//                /*Intent intent=new Intent(MainActivity.this,Profile.class);
//                //  int userId=getIntent().getIntExtra("USERID",0);
//                Log.d("USerID",""+userId);
//                intent.putExtra("PROFILEUSERID",userId);
//                startActivity(intent);*/
//                break;
//            case R.id.setting_nw:
//               /* Intent intent1=new Intent(MainActivity.this,Settings.class);
//                intent1.putExtra("MSISDN",getIntent().getStringExtra("MSISDN"));
//                startActivity(intent1);*/
//                break;

                case R.id.shareApp:
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String sAux = "\nLet me recommend you this application\n\n";
                        sAux = sAux + "https://play.google.com/store/apps/details?id=the.package.id \n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "choose one"));
                    } catch(Exception e) {
                        //e.toString();
                    }
                    break;



                case R.id.logout:
                    // Toast.makeText(MainActivity.this,"Logout",Toast.LENGTH_SHORT).show();
                    Intent logoutIntent = new Intent(this, LoginActivity.class);
                    logoutIntent.putExtra("finish", true); // if you are checking for this in your other Activities
                    PrefrenceManager.getInstance().clearData();
                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(logoutIntent);
                    finish();
                    break;


            }


            closeDrawer();
            openDrawer();
            return true;
        }

        private void openDrawer() {
            mDraweLayout.openDrawer(GravityCompat.START);
        }

        private void closeDrawer() {
            mDraweLayout.closeDrawer(GravityCompat.START);
            //start will make the drawer open from left
        }


        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()){
            case R.id.imgPlay:
                final Song data = (Song) model;
                play(data);

                break;

            case R.id.addToFav:


                break;
        }

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

