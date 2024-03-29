package com.wpits.merhaba.activity.ui.player;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.novoda.sax.RootElement;
import com.novoda.sexp.RootTag;
import com.novoda.sexp.Streamer;
import com.novoda.sexp.finder.ElementFinder;
import com.squareup.picasso.Picasso;
import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.PlayerActivity;
import com.wpits.merhaba.databinding.PlayerFragmentBinding;
import com.wpits.merhaba.dialogs.CustomRateAlertDialog;
import com.wpits.merhaba.events.Events;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.AddToFavRequest;
import com.wpits.merhaba.model.Datum;
import com.wpits.merhaba.model.SongRatingResponse;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.AppConstant;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.GiftRequest;
import com.wpits.merhaba.utils.MySingleton;
import com.wpits.merhaba.utils.SubscribeModel;
import com.wpits.merhaba.utils.SubscriptionDataModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import phonenumberui.PhoneNumberActivity;

public class PlayerFragment extends Fragment implements View.OnClickListener {

   // private PlayerViewModel mViewModel;
    private Context mContext;
    private PlayerFragmentBinding binding;
    private static Song mSong;
    private int navigatedFrom;
    private boolean isSuffleOn;
    boolean isArabic = Utility.isArabic();
    private long totaltime;
    private long timeLeft;
    CountDownTimer timer;
    private String TAG = "FRAGMENT TAG";
    Dialog myDialog;
    ProgressDialog dialog;

    public static PlayerFragment newInstance(Song mSong,int position ) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstant.NAVIGATED_FROM_INT, position);
        args.putSerializable(AppConstant.SONG_DATA,mSong);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         binding = PlayerFragmentBinding.inflate(inflater,container,false);
        mContext = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PlayerViewModel.class);
        navigatedFrom = getArguments().getInt(AppConstant.NAVIGATED_FROM_INT, 0);
        mSong = (Song) getArguments().getSerializable(AppConstant.SONG_DATA);
        isSuffleOn =  ((PlayerActivity)getActivity()).isSuffleOn;
        myDialog = new Dialog(mContext,R.style.AdvanceDialogTheme);


        setupData();
        Log.d(TAG,mSong.getSongNameEn());

    }



    private void setupData(){

        binding.txtSongName.setText(isArabic?mSong.getSongsNameAr():mSong.getSongNameEn());
        binding.txtArtistName.setText(isArabic?mSong.getArtistNameAr():mSong.getArtistName());
        binding.txtRingtoneId.setText(String.format(mContext.getResources().getString(R.string.ringtone_id),mSong.getSongId()+""));
        binding.txtSongNameBottom.setText(isArabic?mSong.getSongsNameAr():mSong.getSongsNameAr());
        binding.txtArtistNameBottom.setText(isArabic?mSong.getArtistNameAr():mSong.getArtistName());


        binding.txtProgressTime.setText("");
        binding.txtTotalProgress.setText("");
        binding.imageRepeat.setOnClickListener(this);
        binding.imagePrev.setOnClickListener(this);
        binding.imagePlayNext.setOnClickListener(this);
        binding.imagePlayShuffle.setOnClickListener(this);
        binding.imagePlayPause.setOnClickListener(PlayerFragment.this);
        binding.imageGift.setOnClickListener(this);
        binding.imageGetIt.setOnClickListener(this);
        binding.imageFav.setOnClickListener(this);

        Picasso.get()
                   .load(mSong.getAlbumArt())
                    .into(binding.songImage);
        binding.imagePlayPause.setVisibility(View.VISIBLE);
        if(mSong.getFavStatus()){
            binding.imageFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_heart));
        }else{
            binding.imageFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_fav));

        }
        Log.d(TAG,mSong.getSongNameEn());
        binding.songRating.setVisibility(View.GONE);
        binding.rateSong.setOnClickListener(this);

    }

    private void toggleSuffle()
    { isSuffleOn = ((PlayerActivity)getActivity()).toggleSuffle();
    }

    public void play(final Song mSong){
        getSongRating(mSong);

        binding.progressBarDone.setVisibility(View.VISIBLE);
        binding.imagePlayPause.setVisibility(View.GONE);
        binding.jcplayer.kill();
        binding.jcplayer.setVisibility(View.GONE);
        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        JcAudio jcAudio;
        if(isArabic)
            jcAudio = JcAudio.createFromURL(mSong.getSongsNameAr(), mSong.getContentPathLocation());
        else
            jcAudio = JcAudio.createFromURL(mSong.getSongName(), mSong.getContentPathLocation());

        jcAudios.add(jcAudio);

        binding.jcplayer.initPlaylist(jcAudios, null);
        binding.jcplayer.playAudio(jcAudio);
        binding.jcplayer.setJcPlayerManagerListener(new JcPlayerManagerListener() {
            @Override
            public void onPreparedAudio(@NotNull JcStatus jcStatus) {

                audioPrepare(jcStatus);
            }

            @Override
            public void onCompletedAudio() {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        binding.jcplayer.setActivated(false);
                        binding.jcplayer.setVisibility(View.GONE);
                        binding.jcplayer.kill();
                        if(((PlayerActivity)getActivity()).isRepeatOn){
                            play(mSong);
                            return;
                        }
                        ((PlayerActivity)getActivity()).openNextSong();
                    }
                });
            }

            @Override
            public void onPaused(@NotNull JcStatus jcStatus) {
                Log.d("onPaused",jcStatus.getCurrentPosition()+"");

                timer.cancel();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.imagePlayPause.setImageDrawable(mContext.getDrawable(R.drawable.ic_play));

                    }
                });

            }

            @Override
            public void onContinueAudio(@NotNull JcStatus jcStatus) {
                Log.d("onContinueAudio",jcStatus.getCurrentPosition()+"");

                trackProgress();
            }

            @Override
            public void onPlaying(@NotNull JcStatus jcStatus) {
                Log.d("onPlaying",jcStatus.getCurrentPosition()+"");

            }

            @Override
            public void onTimeChanged(@NotNull JcStatus jcStatus) {
                Log.d("onTimeChanged",jcStatus.getCurrentPosition()+":"+jcStatus.getDuration());
                //trackProgress(jcStatus);


            }

            @Override
            public void onStopped(@NotNull JcStatus jcStatus) {

            }

            @Override
            public void onJcpError(@NotNull Throwable throwable) {

            }
        });
    }
    private void audioPrepare(final JcStatus jcStatus){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(dialog!=null)
                dialog.dismiss();

                binding.progressBarDone.setVisibility(View.GONE);
                binding.imagePlayPause.setVisibility(View.VISIBLE);
                binding.txtProgressTime.setText(Utility.timeinMinute(jcStatus.getCurrentPosition()));
                binding.txtTotalProgress.setText(Utility.timeinMinute(jcStatus.getDuration()));
                totaltime = jcStatus.getDuration();
                timeLeft = jcStatus.getDuration();
                trackProgress();
                binding.jcplayer.createNotification();
                binding.imagePlayPause.setImageDrawable(mContext.getDrawable(R.drawable.ic_pause));


            }
        });
    }

private void trackProgress(){
   timer =  new CountDownTimer(timeLeft, 1000) {

        public void onTick(long millisUntilFinished) {
            timeLeft =  millisUntilFinished;
            long timeElapsed = (totaltime-millisUntilFinished);
            float percentage =   (float) timeElapsed/totaltime;
            Log.d("Progress","Total Progress Percentage : - "+percentage*100);
            binding.txtProgressTime.setText(Utility.timeinMinute(timeElapsed));

            binding.materialSeekBar.setProgress(Math.round(percentage*100));

        }

        public void onFinish() {
            binding.materialSeekBar.setProgress(100);

        }

    }.start();




}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rateSong:
                if(PrefrenceManager.getInstance().isLoggedIn()){
                    Song song =((PlayerActivity)getActivity()).viewPagerAdapter.getCurrentSong(navigatedFrom);
                    new CustomRateAlertDialog(mContext,song).show();

                }else{
                    Intent i = new Intent(mContext, PhoneNumberActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
                break;
            case R.id.imageRepeat:
                ((PlayerActivity)getActivity()).toggleRepeat();
                Toast.makeText(mContext,((PlayerActivity)getActivity()).isRepeatOn?"Repeat is On":"Repeat is Off",Toast.LENGTH_LONG).show();
                if(((PlayerActivity)getActivity()).isRepeatOn){
                    binding.imageRepeat.setImageDrawable(mContext.getDrawable(R.drawable.ic_repeat_off));

                }else{
                    binding.imageRepeat.setImageDrawable(mContext.getDrawable(R.drawable.ic_repeat));

                }
                break;
            case R.id.imagePrev:
                binding.jcplayer.kill();
                ((PlayerActivity)getActivity()).playPrev();
                break;
            case R.id.imagePlayNext:
                binding.jcplayer.kill();
                ((PlayerActivity)getActivity()).playNext();
                break;
            case R.id.imagePlayShuffle:
                toggleSuffle();
                Toast.makeText(mContext,((PlayerActivity)getActivity()).isSuffleOn?"Shuffle is On":"Shuffle is Off",Toast.LENGTH_LONG).show();

                if(((PlayerActivity)getActivity()).isSuffleOn){
                    binding.imagePlayShuffle.setImageDrawable(mContext.getDrawable(R.drawable.ic_shuffle_off));

                }else{
                    binding.imagePlayShuffle.setImageDrawable(mContext.getDrawable(R.drawable.ic_shuffle));

                }
                break;
            case R.id.imagePlayPause:
                if(binding.jcplayer.isPlaying()){
                    binding.jcplayer.pause();
                    binding.imagePlayPause.setImageDrawable(mContext.getDrawable(R.drawable.ic_play));
                }else{
                    if(timer==null){
                        dialog = Utility.showProgress(mContext);
                        play(mSong);

                    }else{
                        binding.jcplayer.continueAudio();
                    }
                    binding.imagePlayPause.setImageDrawable(mContext.getDrawable(R.drawable.ic_pause));


                }
                break;
            case  R.id.imageGift:
                showGift(((PlayerActivity)getActivity()).getCurrentSong());
                break;
            case  R.id.imageGetIt:
                showPopup(binding.imageGetIt,((PlayerActivity)getActivity()).getCurrentSong());
                break;
            case R.id.imageFav:
                if(PrefrenceManager.getInstance().isLoggedIn()){


                    if(((PlayerActivity)getActivity()).getCurrentSong().getFavStatus()){
                    unfavRequest(((PlayerActivity)getActivity()).getCurrentSong());
                }else{
                    addToFavourite(((PlayerActivity)getActivity()).getCurrentSong());
                }
                }else{
                    Intent i = new Intent(mContext, PhoneNumberActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
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
         songImage = myDialog.findViewById(R.id.songImage);
        btnSub = (Button) myDialog.findViewById(R.id.btnSubscribe);
        btnGift = (Button) myDialog.findViewById(R.id.btnGift);

        if(isArabic)
            txtSongName.setText(song.getSongsNameAr());
        else
            txtSongName.setText(song.getSongNameEn());

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
        Picasso.get()
                .load(mSong.getAlbumArt())
                .into(songImage);
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(PrefrenceManager.getInstance().getUserMobile()+"",song.getSongId()+"");

            }
        });
    }


    private void subscribe(final String mobile, String songId){
        String url = ApplicationUrls.subscribe;
        url = url+ mobile+"/"+songId;
        String urlBalance = ApplicationUrls.balanceCheck;
        urlBalance = urlBalance +mobile;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        final JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("Subscription", "onResponse: ****"+response);
                try{
                    SubscribeModel model =  JsonUtils.fromJson(response.toString(), SubscribeModel.class);
                    if(model!=null&&model.getStatus().equalsIgnoreCase("successful")){
                        SubscriptionDataModel data =  JsonUtils.fromJson(model.getData(), SubscriptionDataModel.class);
                        Toast.makeText(mContext, data.getResultDescription(), Toast.LENGTH_LONG).show();
                        if(myDialog.isShowing()){
                            myDialog.dismiss();
                        }

                    }

                }catch (Exception e){
                    Toast.makeText(mContext, "Please Try Again!", Toast.LENGTH_LONG).show();

                }


                if(response!=null && (response.toString().toLowerCase().contains("success")) || response.toString().contains("User already registered")
                        || response.toString().contains("New user registered")){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Oops! Please check your internet connection & Try Again!", Toast.LENGTH_LONG).show();
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
        StringRequest req = new StringRequest(Request.Method.GET, urlBalance,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                       double balance=  getUserBalance(response);
                       if(balance>=100000){
                             phonenumberui.MySingleton.getInstance(mContext).addToRequest(jsonArrayRequest);

                       }else{
                           progressDialog.dismiss();
                           Toast.makeText(mContext, mContext.getResources().getString(R.string.do_not_have_enough_bal), Toast.LENGTH_LONG).show();
                           if(myDialog.isShowing()){
                               myDialog.dismiss();
                           }
                       }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        ){
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


        phonenumberui.MySingleton.getInstance(mContext).addToRequest(req);

    }

    private double getUserBalance(String response) {
        double balanceValue=0;

        try {
            InputStream stream = new ByteArrayInputStream(response.getBytes
                    (Charset.forName("UTF-8")));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = null;
            doc = dBuilder.parse(stream);
            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("QryUserBasicBalResponse");
            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                for (int j=0; j<node.getChildNodes().getLength(); j++) {
                    Node child =node.getChildNodes().item(j);
                    for (int k=0; k<child.getChildNodes().getLength(); k++) {
                        Node subCHild = child.getChildNodes().item(k);
                        if(subCHild.getNodeName().equalsIgnoreCase("BalanceDtoList")){
                            if(subCHild.getChildNodes()!=null&&subCHild.getChildNodes().getLength()>0){
                                Node balDTO= subCHild.getChildNodes().item(0);
                                for (int l=0; l<balDTO.getChildNodes().getLength(); l++) {
                                    Node balDtoItem = balDTO.getChildNodes().item(l);
                                    if(balDtoItem.getNodeName().equalsIgnoreCase("BalanceValue")){
                                        try{
                                            balanceValue =Double.parseDouble(balDtoItem.getChildNodes().item(0).getNodeValue());

                                        }catch (Exception e){

                                        }

                                    }


                                }
                            }
                        }


                    }


                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  balanceValue;
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
        String urlBalance = ApplicationUrls.balanceCheck;
        urlBalance = urlBalance +gifter;

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("giftee", giftee);
            jsonRequest.put("gifter", gifter);
            jsonRequest.put("mode", "GIFT");
            jsonRequest.put("rbtID", songId+"");
            jsonRequest.put("remdur", 1);
            jsonRequest.put("reminder", "false");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GiftRequest request = new GiftRequest("920001472","925331856","GIFT","917",1,"false");
        VolleyLog.DEBUG = true;
        VolleyLog.setTag("Volley");
        Log.isLoggable("Volley", Log.VERBOSE);
        Log.d("GIFT",JsonUtils.toJson(jsonRequest));

        final JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if(myDialog!=null&&myDialog.isShowing()){
                    myDialog.dismiss();
                }
                Toast.makeText(mContext,mContext.getResources().getString(R.string.song_gifted_successfully),Toast.LENGTH_LONG).show();
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
                Toast.makeText(mContext, "Oops! Please check your internet connection & Try Again!", Toast.LENGTH_LONG).show();
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
        StringRequest req = new StringRequest(Request.Method.GET, urlBalance,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        double balance=  getUserBalance(response);
                        if(balance>=100000){
                            MySingleton.getInstance(mContext).addToRequest(jsonArrayRequest);

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.do_not_have_enough_bal), Toast.LENGTH_LONG).show();
                            if(myDialog.isShowing()){
                                myDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        ){
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


        MySingleton.getInstance(mContext).addToRequest(req);


    }
    private void addToFavourite(final Song mSong){

        String x="https://www.marhaba.com.ly:18086/crbt/v1/favorites";
        // String jsonString = {"subscriber_id" : "Ronaldo", "top_content_id" : "soccer"};
        //  JSONObject jsonObject = new JSONObject("{\"subscriber_id\":\"+PrefrenceManager.getInstance().getUserMobile()+\",\"top_content_id\":\"++\"}");
        final ProgressDialog dialog = Utility.showProgress(mContext);
        AddToFavRequest request =  new AddToFavRequest(PrefrenceManager.getInstance().getUserMobile(),mSong.getId()+"");
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
                dialog.dismiss();
                try {
                    Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);
                    binding.imageFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_heart));
                    mSong.setFavStatus(true);
                    EventBus.getDefault().post(new Events.FavouritesEvent());



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.d("CategoryErrorResponse",error.toString());
                Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);

            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }
    private void unfavRequest(Song song){
        final ProgressDialog dialog = Utility.showProgress(mContext);

        String x="https://www.marhaba.com.ly:18086/crbt/v1/deleteFavorite";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("subscriber_id", PrefrenceManager.getInstance().getUserMobile());
            jsonRequest.put("top_content_id", song.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Fav Request", JsonUtils.toJson(jsonRequest));


        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.POST, x,jsonRequest , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CategoryResponse",response.toString());
                try {
                    Toast.makeText(mContext,"Song unmarked as Favourite",Toast.LENGTH_LONG);
                    dialog.dismiss();
                    binding.imageFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_fav));
                    mSong.setFavStatus(false);
                    EventBus.getDefault().post(new Events.FavouritesEvent());



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(mContext,"Song unmarked as Favourite",Toast.LENGTH_LONG);
                    dialog.dismiss();
                    binding.imageFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_fav));
                    mSong.setFavStatus(false);
                    EventBus.getDefault().post(new Events.FavouritesEvent());



                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("CategoryErrorResponse",error.toString());
                Toast.makeText(mContext,"Song marked as Favourite",Toast.LENGTH_LONG);

            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.jcplayer.kill();

        Log.d("onPageSelected","onDestroy position:- "+navigatedFrom);

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            Log.d("onPageSelected","setUserVisibleHint position:- "+navigatedFrom +"isVisibleToUser: "+isVisibleToUser);
            if(binding!=null)
            binding.jcplayer.kill();


        }

    }
    private void getSongRating(Song mSong){
        String x="https://www.marhaba.com.ly:18086/crbt/v1/getRatingByTopContentId/"+ mSong.getSongId();
        Log.d(TAG, "getSongRating: "+x);
        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.GET, x,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Rating Response",response.toString());
                try {
                    SongRatingResponse songResp = JsonUtils.fromJson(response.toString(),SongRatingResponse.class);
                    if(songResp!=null&&songResp.getData()!=null&&songResp.getData().size()>0){
                        Integer totalRating =0;
                        for (Datum data:songResp.getData()) {
                            totalRating +=data.getRating();
                        }
                        float avgRating = (float)(totalRating/songResp.getData().size());

                        binding.songRating.setVisibility(View.VISIBLE);
                        binding.songRating.setRating(avgRating);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RatingErrorResponse",error.toString());

            }

        }) {
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

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }
    private static class SimpleStreamer implements Streamer<String> {

        private final ElementFinder<String> elementFinder;
        private final String elementTag;

        public SimpleStreamer(ElementFinder<String> elementFinder, String elementTag) {
            this.elementFinder = elementFinder;
            this.elementTag = elementTag;
        }

        @Override
        public RootTag getRootTag() {
            return RootTag.create("novoda");
        }

        @Override
        public void stream(RootElement rootElement) {
            elementFinder.find(rootElement, elementTag);
        }

        @Override
        public String getStreamResult() {
            return elementFinder.getResultOrThrow();
        }
    }
}