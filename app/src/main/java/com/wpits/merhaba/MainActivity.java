package com.wpits.merhaba;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.wpits.merhaba.activity.LoginActivity;
import com.wpits.merhaba.adapter.CategoryViewDataAdapter;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.model.category.Category;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean isArabic = false;
    private Toolbar mToolbar;
    DrawerLayout mDraweLayout;
    NavigationView navigationView;
    View header;
    RelativeLayout headerRelativeLayout;
    ImageView headerImage;
    TextView headerName,headerEmai, banner;

    CategoryViewDataAdapter adapter;
    RecyclerView my_recycler_view;

    Dialog myDialog;

    TableLayout tableLayout;

    ArrayList<Category> allSampleData;
    ImageView loader1, loader2;

    ArrayList<Song> songContentList = new ArrayList<Song>();
    SearchView searchView;
    JcPlayerView jcplayerView;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isArabic)
            setContentView(R.layout.activity_main_ar);
        else
            setContentView(R.layout.activity_main);

        mContext=this;

        mToolbar =findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        mDraweLayout = findViewById(R.id.drawerLayout);
        header=navigationView.inflateHeaderView(R.layout.header);
        headerRelativeLayout = header.findViewById(R.id.header_relative_layout);
        headerImage = header.findViewById(R.id.imageView);
        headerName=header.findViewById(R.id.txvName);
        headerEmai=header.findViewById(R.id.txvEmail);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        loader1 = findViewById(R.id.loader1);
        loader2 = findViewById(R.id.loader2);
        searchView = findViewById(R.id.search);
       // searchView.setBackgroundResource(R.drawable.round_corner);
        if(isArabic)
            searchView.setQueryHint("ابحث في طريبة");
        else
        searchView.setQueryHint("Search Tarhiba");
        searchView.setIconifiedByDefault(false);
        myDialog = new Dialog(this);
        banner = findViewById(R.id.banner);

        if(isArabic) {


            banner.setText("عزيزي المشترك انت مشترك حاليا");

            banner.setText(banner.getText()+"\n"+"النشيد الوطني الليبي");

            banner.setText(banner.getText()+"\nاشتراكك ينتهي في 30-07-2020");
            banner.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            banner.setTextSize(20);
            banner.setTypeface(ResourcesCompat.getFont(this, R.font.fs), Typeface.NORMAL);
            banner.setVisibility(View.GONE);
        }

        jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);


        setUpNavigationDrawerMenu();
        intiViews();
        getCategory();



    }

    private void intiViews() {
        loader1.setVisibility(View.VISIBLE);
        loader2.setVisibility(View.VISIBLE);
        allSampleData = new ArrayList<Category>();
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        adapter = new CategoryViewDataAdapter(MainActivity.this, allSampleData, this);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    public void setTableHeaders(){

        TableRow row;
        ImageView button, getit;
        TextView t1, t3, t4, t5, t6;
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f);

        getit = new ImageView(getApplicationContext());
        getit.setImageResource(R.drawable.phonehand_icon);
        getit.setLayoutParams(params);

        button = new ImageView(getApplicationContext());
        button.setImageResource(R.drawable.play_icon);
       button.setLayoutParams(params);


        t3 = new TextView(getApplicationContext());
        // t3.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        if(isArabic)
            t3.setText("المؤدي");
        else
           t3.setText("Performer");
        t3.setTextSize(18);

        t3.setTextColor(Color.WHITE);
        t3.setLayoutParams(params);

        t4= new TextView(getApplicationContext());
        t4.setTextSize(18);

        if(isArabic)
            t4.setText("الألبوم");
        else
         t4.setText("Album"); //
        t4.setTextColor(Color.WHITE);
        t4.setLayoutParams(params);
        t4.setGravity(Gravity.CENTER_HORIZONTAL );

        t5= new TextView(getApplicationContext());
        t5.setTextColor(Color.WHITE);
        t5.setTextSize(18);

        t5.setGravity(Gravity.CENTER_HORIZONTAL );
        if(isArabic)
            t5.setText("اسم الرنة"); //
        else
            t5.setText("Tarhiba Name"); //اسم الرنة
        t5.setLayoutParams(params);

        t6= new TextView(getApplicationContext());
        t6.setTextColor(Color.WHITE);
        t6.setTextSize(14);
        t6.setGravity(Gravity.CENTER_HORIZONTAL );
        if(isArabic)
           t6.setText("رمز الرنة"); //
        else
            t6.setText("Tarhiba ID"); //رمز الرنة
        t6.setLayoutParams(params);


        //t1.setTypeface(null, Typeface.NORMAL);
        t3.setTypeface(ResourcesCompat.getFont(this, R.font.fs), Typeface.NORMAL);
        t4.setTypeface(ResourcesCompat.getFont(this, R.font.fs), Typeface.NORMAL);
        t5.setTypeface(ResourcesCompat.getFont(this, R.font.fs), Typeface.NORMAL);
        t6.setTypeface(ResourcesCompat.getFont(this, R.font.fs), Typeface.NORMAL);


        row = new TableRow(getApplicationContext());
        row.setPadding(0,5*dip,0,0);
        row.setBackgroundColor(getResources().getColor(R.color.menuColor));

        row.addView(getit);
        row.addView(button);
        row.addView(t3);
        row.addView(t4);
        row.addView(t5);
        row.addView(t6);


        tableLayout.addView(row, new TableLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
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

                    }

                    my_recycler_view.setAdapter(adapter);

                    loader1.setVisibility(View.INVISIBLE);

                    populateSongs(1, "The Top 20 Tarhiba", "أفضل 20 رنة");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CategoryErrorResponse",error.toString());
            }
        });

        MySingleton.getInstance(getApplication()).addToRequest(categoryRequest);
    }

    private List<Song> albumApi(final int categoryId) {
        final List<Song> songsList = new ArrayList<Song>();
        String album_url = "https://www.marhaba.com.ly:18083/topContent/topContentByCtgId?ctgId="+categoryId;
        JsonObjectRequest albumRequest=new JsonObjectRequest(Request.Method.GET, album_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response --->",response.toString());

                int catCategoryId=categoryId;

                try {
                    JSONArray data = response.getJSONArray("data");
                    Log.d("Songs ", response.toString());

                    for(int i=0;i<data.length();i++){
                        JSONObject songContent=data.getJSONObject(i);
                        //JSONObject categoryContent=categoryList.getJSONObject("album");
                        Log.d("Songs", songContent.toString());

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

                        Log.d("Songs", categoryId+" "+catCategoryId);

                        if(catCategoryId == categoryId) {
                            songsList.add(song);

                        }
                    }


                    TableRow row;
                    ImageView button, getit;
                    TextView t1, t3, t4, t5, t6;
                    //Converting to dip unit
                    int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            (float) 1, getResources().getDisplayMetrics());
                    for (int i=0; i< songsList.size(); i++){
                        final Song song = songsList.get(i);
                        Log.d("***song***",song.getArtistName()+" SOng ID *** "+song.getSongId());
                        row = new TableRow(getApplicationContext());
                        row.setPadding(0,2*dip,2*dip,0);
                        if(i % 2 == 0){
                            row.setBackgroundColor(Color.LTGRAY);
                        }


                        getit = new ImageView(getApplicationContext());
                        if(isArabic)
                            getit.setImageResource(R.drawable.get_it_ar);
                        else
                            getit.setImageResource(R.drawable.get_it);
                        if(i % 2 == 0)
                            getit.setBackgroundColor(Color.LTGRAY);
                        else
                        getit.setBackgroundColor(Color.WHITE);
                        getit.setPadding(0*dip, 0, 1*dip, 0);
                        getit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPopup(v,song);
                            }
                        });


                        button = new ImageView(getApplicationContext());
                        if(isArabic)
                            button.setImageResource(R.drawable.play_now_ar);
                        else
                            button.setImageResource(R.drawable.play_now);
                        if(i % 2 == 0)
                            button.setBackgroundColor(Color.LTGRAY);
                        else
                        button.setBackgroundColor(Color.WHITE);
                        //button.setPadding(5*dip, 0, 0, 0);


                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // if(!jcplayerView.isPlaying()) {

                                    jcplayerView.kill();
                                    jcplayerView.setVisibility(View.VISIBLE);
                                    ArrayList<JcAudio> jcAudios = new ArrayList<>();
                                    JcAudio jcAudio;
                                    if(isArabic)
                                       jcAudio = JcAudio.createFromURL(song.getSongsNameAr(), song.getContentPathLocation());
                                    else
                                        jcAudio = JcAudio.createFromURL(song.getSongName(), song.getContentPathLocation());
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

                                  //  jcplayerView.setVisibility(View.VISIBLE);

                             //   }else{

                             //   }

                            }
                        });

                        t3 = new TextView(getApplicationContext());
                        t3.setTextColor(Color.BLACK);
                       // t3.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                        if(isArabic)
                            t3.setText(song.getArtistNameAr());
                        else
                            t3.setText(song.getArtistName());
                        t3.setTextSize(16);
                        t3.setWidth(50 * dip);
                        t3.setPadding(0*dip, 0, 2*dip, 0);


                        t4= new TextView(getApplicationContext());
                        t4.setTextColor(Color.BLACK);
                        t4.setTextSize(16);
                      //  t4.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                        t4.setWidth(50 * dip);
                        if(isArabic)
                            t4.setText(song.getCategoryNameAr());
                        else
                            t4.setText(song.getCategoryName());
                        t4.setPadding(0*dip, 0, 2*dip, 0);

                        t5= new TextView(getApplicationContext());
                        t5.setTextColor(Color.BLACK);
                        t5.setTextSize(16);
                        t5.setWidth(50 * dip);
                      //  t5.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                        if(isArabic)
                            t5.setText(song.getSongsNameAr());
                        else
                            t5.setText(song.getSongName());
                        t5.setPadding(0*dip, 0, 2*dip, 0);

                        t6= new TextView(getApplicationContext());
                        t6.setTextColor(Color.WHITE);
                        t6.setBackgroundColor(getResources().getColor(R.color.menuColor));
                        t6.setTextSize(16);
                        t6.setGravity(Gravity.CENTER_HORIZONTAL );
                        t6.setEms(3);
                       // t6.setWidth(5 * dip);

                        t6.setText(song.getSongId()+"");
                        t6.setPadding(2*dip, 0, 2*dip, 0);


                        //t1.setTypeface(null, Typeface.NORMAL);
                        t3.setTypeface(null, Typeface.NORMAL);
                        t4.setTypeface(null, Typeface.NORMAL);
                        t5.setTypeface(null, Typeface.NORMAL);
                        t6.setTypeface(null, Typeface.NORMAL);


                        row.addView(getit);
                        row.addView(button);
                        row.addView(t3);
                        row.addView(t4);
                        row.addView(t5);
                        row.addView(t6);


                        tableLayout.addView(row, new TableLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    }

                    loader2.setVisibility(View.INVISIBLE);



                    //category.setAllItemsInSection(albumContentList);
                    //   Log.d("QWERTY",albumContents.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error-->",error.getLocalizedMessage());
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
        MySingleton.getInstance(getApplication()).addToRequest(albumRequest);

        return songsList;
    }


    public void populateSongs(int categoryId, String categoryName, String categoryName_ar) {

        tableLayout.removeAllViews();

        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        setTableHeaders();
        loader2.setVisibility(View.VISIBLE);
        if(isArabic)
            searchView.setQueryHint(categoryName_ar);
        else
            searchView.setQueryHint("Search "+categoryName);

        List<Song> songsList = albumApi(categoryId);

        Log.d("Populate songs--> ",""+songsList.size());

    }



    private void setUpNavigationDrawerMenu() {

        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,
                mDraweLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDraweLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    public void showPopup(View v, final Song song) {
        TextView txtclose,txtCategoryName,txtSongName;
        Button btnSub, btnGift;
        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtSongName =(TextView) myDialog.findViewById(R.id.txtSongName);
        txtCategoryName =(TextView) myDialog.findViewById(R.id.txtCategoryName);

        btnSub = (Button) myDialog.findViewById(R.id.btnSubscribe);
        btnGift = (Button) myDialog.findViewById(R.id.btnGift);

        if(isArabic)
            txtSongName.setText(song.getSongsNameAr());
        else
            txtSongName.setText(song.getSongName());

        if(isArabic)
            txtCategoryName.setText(song.getCategoryNameAr());
        else
            txtCategoryName.setText(song.getCategoryName());


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            myDialog.dismiss();
            showGift(song);
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(PrefrenceManager.getInstance().getUserMobile()+"",song.getSongId()+"");

            }
        });
    }

    private void showGift( final Song song){
        TextView txtclose;
        Button  btnGift;
        final AppCompatEditText edtGifteeNumber;
        myDialog.setContentView(R.layout.gift);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        btnGift = (Button) myDialog.findViewById(R.id.btnGift);
        edtGifteeNumber = myDialog.findViewById(R.id.etPhoneNumber);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        try {
            btnGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edtGifteeNumber.getText().toString().isEmpty()){
                        edtGifteeNumber.setError(mContext.getString(R.string.required_error));
                    }
                    else{
      gift(edtGifteeNumber.getText().toString().replaceAll("[^a-zA-Z0-9]", ""),song);
                    }

                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void gift(String giftee,Song song){
        Integer songId = song.getSongId();
        String gifter = PrefrenceManager.getInstance().getUserMobile()+"";
        String url = ApplicationUrls.gift;

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        GiftRequest request = new GiftRequest(giftee,gifter,"GIFT",songId+"",1,"false");
        VolleyLog.DEBUG = true;
         VolleyLog.setTag("Volley");
Log.isLoggable("Volley", Log.VERBOSE);

        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("Subscription", "onResponse: ****"+response);
                Gson gson=new Gson();
                //User already registered
                //New user registered
                if(response!=null && (response.toString().toLowerCase().contains("success")) || response.toString().contains("User already registered")
                        || response.toString().contains("New user registered")){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Oops! Please check your internet connection & Try Again!", Toast.LENGTH_LONG).show();
                //finish();

                if (error instanceof NetworkError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("sendOtp", "userLogin: " + error);
                } else if (error instanceof ServerError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof AuthFailureError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof ParseError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof NoConnectionError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                }
            }
        }){
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
        };
        phonenumberui.MySingleton.getInstance(getApplicationContext()).addToRequest(jsonArrayRequest);


    }


    private void subscribe(final String mobile, String songId){
        String url = ApplicationUrls.subscribe;
        url = url+ mobile+"/"+songId;

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("Subscription", "onResponse: ****"+response);
                try{
                    SubscribeModel model =  JsonUtils.fromJson(response.toString(), SubscribeModel.class);
                    if(model!=null&&model.getStatus().equalsIgnoreCase("successful")){
                        SubscriptionDataModel data =  JsonUtils.fromJson(model.getData(), SubscriptionDataModel.class);
                        Toast.makeText(getApplicationContext(), data.getResultDescription(), Toast.LENGTH_LONG).show();
                        if(myDialog.isShowing()){
                            myDialog.dismiss();
                        }

                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Please Try Again!", Toast.LENGTH_LONG).show();

                }


                if(response!=null && (response.toString().toLowerCase().contains("success")) || response.toString().contains("User already registered")
                        || response.toString().contains("New user registered")){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Oops! Please check your internet connection & Try Again!", Toast.LENGTH_LONG).show();
                //finish();

                if (error instanceof NetworkError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("sendOtp", "userLogin: " + error);
                } else if (error instanceof ServerError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof AuthFailureError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof ParseError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof NoConnectionError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                } else if (error instanceof TimeoutError) {
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fgh", "userLogin: " + error);
                }
            }
        }){
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
        };

        phonenumberui.MySingleton.getInstance(getApplicationContext()).addToRequest(jsonArrayRequest);
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
}
