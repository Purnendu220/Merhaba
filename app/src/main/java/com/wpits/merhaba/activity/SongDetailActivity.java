package com.wpits.merhaba.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wpits.merhaba.R;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.GiftRequest;
import com.wpits.merhaba.utils.SubscribeModel;
import com.wpits.merhaba.utils.SubscriptionDataModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongDetailActivity extends AppCompatActivity implements View.OnClickListener {
ImageView songImage;
LinearLayout addToCart,palySong,addToFav,giftFriend;
TextView txtSongName,songId,txtArtistName,txtCategoryName;
Toolbar mToolbar;
Song mSong;
Dialog myDialog;
    boolean isArabic = Utility.isArabic;
  Context mContext;
  JcPlayerView jcplayerView;

    public static void open(Context context, Song mSong){
        Intent i =  new Intent(context,SongDetailActivity.class);
        i.putExtra("SONG",mSong);

        context.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        mSong = (Song) getIntent().getSerializableExtra("SONG");
        myDialog = new Dialog(this);
        mContext = this;
        songImage=findViewById(R.id.songImage);
        addToCart=findViewById(R.id.addToCart);
        palySong=findViewById(R.id.palySong);
        addToFav=findViewById(R.id.addToFav);
        giftFriend=findViewById(R.id.giftFriend);
        txtSongName=findViewById(R.id.txtSongName);
        songId=findViewById(R.id.songId);
        txtArtistName=findViewById(R.id.txtArtistName);
        txtCategoryName=findViewById(R.id.txtCategoryName);
        mToolbar=findViewById(R.id.songToolbar);
        jcplayerView = findViewById(R.id.jcplayer);

        addToCart.setOnClickListener(this);
        palySong.setOnClickListener(this);
        addToFav.setOnClickListener(this);
        giftFriend.setOnClickListener(this);

        initToolbar();
        if(isArabic){
            txtSongName.setText(mSong.getSongsNameAr());
            songId.setText(mSong.getSongId()+"");
            txtArtistName.setText(mSong.getArtistNameAr());
            txtCategoryName.setText(mSong.getCategoryNameAr());
        }else{
            txtSongName.setText(mSong.getSongName());
            songId.setText(mSong.getSongId()+"");
            txtArtistName.setText(mSong.getArtistName());
            txtCategoryName.setText(mSong.getCategoryName());
        }

        Picasso.get()
                .load(mSong.getAlbumArt())
                .into(songImage);


    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void play(){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addToCart:
                showPopup(v,mSong);
                break;
            case R.id.palySong:
                play();
                break;
            case R.id.addToFav:
                break;
            case R.id.giftFriend:
                showGift(mSong);
                break;

        }



    }
    public void showPopup(View v, final Song song) {
        TextView txtclose,txtCategoryName,txtSongName;
        Button btnSub, btnGift;
        ImageView songImage;
        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtSongName =(TextView) myDialog.findViewById(R.id.txtSongName);
        txtCategoryName =(TextView) myDialog.findViewById(R.id.txtCategoryName);
        songImage = (ImageView)myDialog.findViewById(R.id.songImage);


        btnSub = (Button) myDialog.findViewById(R.id.btnSubscribe);
        btnGift = (Button) myDialog.findViewById(R.id.btnGift);

        Picasso.get()
                .load(song.getAlbumArt())
                .into(songImage);
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

//        btnGift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//                showGift(song);
//            }
//        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                subscribe(PrefrenceManager.getInstance().getUserMobile()+"",song.getSongId()+"");

            }
        });
    }


    private void subscribe(final String mobile, String songId){
        String url = ApplicationUrls.subscribe;
        url = url+ mobile+"/"+songId;

        final ProgressDialog progressDialog = new ProgressDialog(SongDetailActivity.this);
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

        final ProgressDialog progressDialog = new ProgressDialog(SongDetailActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jcplayerView.kill();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}