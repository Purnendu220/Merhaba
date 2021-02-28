package com.wpits.merhaba.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wpits.merhaba.R;
import com.wpits.merhaba.databinding.DialogRateClassBinding;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.model.favResponse.DataDTO;
import com.wpits.merhaba.model.favResponse.FavResponseDTO;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CustomRateAlertDialog extends Dialog implements OnClickListener {
    private Context mContext;
    private Song model;
    boolean isArabic = Utility.isArabic();
    private DialogRateClassBinding dataBinding;
    private int songRating = 0;


    public CustomRateAlertDialog(@NonNull Context context, @NonNull Song model) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.model=model;
        init(context);
    }
    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dataBinding = DialogRateClassBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        CharSequence text = String.format(mContext.getString(R.string.how_was_this_song),isArabic?model.getSongsNameAr():model.getSongNameEn());
        dataBinding.textViewHowWasClass.setText(text);
        setListeners();


    }

   private void setListeners(){
       dataBinding.textViewNotNow.setOnClickListener(this);
       dataBinding.ratingStar1.setOnClickListener(this);
       dataBinding.ratingStar2.setOnClickListener(this);
       dataBinding.ratingStar3.setOnClickListener(this);
       dataBinding.ratingStar4.setOnClickListener(this);
       dataBinding.ratingStar5.setOnClickListener(this);


   }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewNotNow:{
                addRatingToSong(model);
                dismiss();

            }
            break;
            case R.id.ratingStar1:{
                setStarRating(1);
            }
            break;
            case R.id.ratingStar2: {
                setStarRating(2);
            }
            break;
            case R.id.ratingStar3:{
                setStarRating(3);
                }
            break;
            case R.id.ratingStar4:{
                setStarRating(4);
                }
            break;
            case R.id.ratingStar5:{
                setStarRating(5);
                }
            break;
        }

    }
    public void setStarRatingsFilled(Context context, View... view) {
        for (View v : view) {
                ((ImageView) v).setImageResource(R.drawable.star_filled);
        }
    }
    public void setStarRatingsBlank(Context context, View... view) {
        for (View v : view) {
            ((ImageView) v).setImageResource(R.drawable.star_blank);
        }
    }
    private void setStarRating(int rating){
        switch (rating){
            case 1:
                setStarRatingsFilled(mContext,dataBinding.ratingStar1);
                setStarRatingsBlank(mContext,dataBinding.ratingStar2,dataBinding.ratingStar3,dataBinding.ratingStar4,dataBinding.ratingStar5);
                break;
            case 2:
                setStarRatingsFilled(mContext,dataBinding.ratingStar1,dataBinding.ratingStar2);
                setStarRatingsBlank(mContext,dataBinding.ratingStar3,dataBinding.ratingStar4,dataBinding.ratingStar5);
                break;
            case 3:
                setStarRatingsFilled(mContext,dataBinding.ratingStar1,dataBinding.ratingStar2,dataBinding.ratingStar3);
                setStarRatingsBlank(mContext,dataBinding.ratingStar4,dataBinding.ratingStar5);
                break;
            case 4:
                setStarRatingsFilled(mContext,dataBinding.ratingStar1,dataBinding.ratingStar2,dataBinding.ratingStar3,dataBinding.ratingStar4);
                setStarRatingsBlank(mContext,dataBinding.ratingStar5);
                break;
            case 5:
                setStarRatingsFilled(mContext,dataBinding.ratingStar1,dataBinding.ratingStar2,dataBinding.ratingStar3,dataBinding.ratingStar4,dataBinding.ratingStar5);
                break;
        }
        dataBinding.textViewRatingCount.setText(rating+" of 5 star");
        songRating = rating;


    }


    private void addRatingToSong(Song mSong){
        final ProgressDialog dialog = Utility.showProgress(mContext);

        String x="https://www.marhaba.com.ly:18086/crbt/v1/trackRating";
        // String jsonString = {"subscriber_id" : "Ronaldo", "top_content_id" : "soccer"};
        //  JSONObject jsonObject = new JSONObject("{\"subscriber_id\":\"+PrefrenceManager.getInstance().getUserMobile()+\",\"top_content_id\":\"++\"}");
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("rating", songRating);
            jsonRequest.put("top_content_id", mSong.getSongId());
            jsonRequest.put("remarks", dataBinding.edtRemark.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Rating Request", JsonUtils.toJson(jsonRequest));


        JsonObjectRequest addToFavRequestRequest=new JsonObjectRequest(Request.Method.POST, x,jsonRequest , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Rating Response",response.toString());
                try {
                    Toast.makeText(mContext,"Song rated successfully",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,"Error While Rating song "+error.toString(),Toast.LENGTH_LONG).show();

                Log.d("RatingErrorResponse",error.toString());
                dialog.dismiss();
                dismiss();


            }
        });

        MySingleton.getInstance(mContext).addToRequest(addToFavRequestRequest);
    }


}