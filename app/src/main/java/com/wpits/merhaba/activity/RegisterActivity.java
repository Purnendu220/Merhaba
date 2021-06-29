package com.wpits.merhaba.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.Gson;
import com.wpits.merhaba.R;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.GiftRequest;
import com.wpits.merhaba.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import phonenumberui.PhoneNumberActivity;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatEditText edtFullName,edtBirthYear,edtCity;
    private RadioGroup radioGroup;
    private RadioButton radioMale,radioFemale;
    private AppCompatButton btnSendConfirmationCode;
    private String gender;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtFullName = findViewById(R.id.edtFullName);
        edtBirthYear = findViewById(R.id.edtBirthYear);
        edtCity = findViewById(R.id.edtCity);
        btnSendConfirmationCode = findViewById(R.id.btnSendConfirmationCode);

        radioGroup = findViewById(R.id.radioGroup);

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        mContext = this;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                  case  R.id.radioMale:
                      gender = "MALE";
                    break;
                    case  R.id.radioFemale:
                        gender = "FEMALE";

                        break;
                }
            }
        });
        btnSendConfirmationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String fullName =   edtFullName.getText().toString() ;
             String birthName = edtBirthYear.getText().toString() ;
             String city = edtCity.getText().toString() ;
             if(fullName==null||fullName.trim().length()==0){
                 Toast.makeText(RegisterActivity.this, getResources().getString(R.string.fullname_required), Toast.LENGTH_SHORT).show();
          return;
             }
                if(birthName==null||birthName.trim().length()==0){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.birthyear_required), Toast.LENGTH_SHORT).show();
                    return;

                }
                if(city==null||city.trim().length()==0){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.city_required), Toast.LENGTH_SHORT).show();
                    return;

                }
                if(gender==null||gender.trim().length()==0){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.gender_required), Toast.LENGTH_SHORT).show();
                    return;

                }
                sendData(fullName,birthName,city);
            }
        });


    }
    private void sendData(String fullName, String birthYear,String city){
        String url = ApplicationUrls.customerDetail;

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("fullName", fullName);
            jsonRequest.put("birthDayYear", birthYear);
            jsonRequest.put("gender", gender);
            jsonRequest.put("city", city);
            jsonRequest.put("subscriber", PrefrenceManager.getInstance().getUserMobile());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyLog.DEBUG = true;
        VolleyLog.setTag("Volley");
        Log.isLoggable("Volley", Log.VERBOSE);
        Log.d("GIFT", JsonUtils.toJson(jsonRequest));

        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Toast.makeText(mContext,mContext.getResources().getString(R.string.customer_detail_successfully),Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(mContext, HomeActivityNew.class);
                startActivity(mainIntent);
                finish();

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
        MySingleton.getInstance(mContext).addToRequest(jsonArrayRequest);


    }

}