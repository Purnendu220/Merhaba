package com.wpits.merhaba.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.wpits.merhaba.model.BannerData;
import com.wpits.merhaba.model.CitiListModel;
import com.wpits.merhaba.model.City;
import com.wpits.merhaba.model.album.Song;
import com.wpits.merhaba.remoteConfig.RemoteConfigure;
import com.wpits.merhaba.utility.Utility;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.GiftRequest;
import com.wpits.merhaba.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import phonenumberui.PhoneNumberActivity;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatEditText edtFullName;
    private RadioGroup radioGroup;
    private RadioButton radioMale,radioFemale;
    private AppCompatButton btnSendConfirmationCode;
    private String gender;
    private Context mContext;
    private AppCompatSpinner edtBirthYear,edtCity;
    private ArrayList<String> yearList= new ArrayList<>();
    private ArrayList<String> cityList= new ArrayList<>();

    int year=1950;
    String selectedYear,SelectedCity;
    CitiListModel city;
    boolean isArabic = Utility.isArabic();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;

        edtFullName = findViewById(R.id.edtFullName);
        edtBirthYear = findViewById(R.id.edtBirthYear);
        edtCity = findViewById(R.id.edtCity);
        btnSendConfirmationCode = findViewById(R.id.btnSendConfirmationCode);
        String bannerList =  RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigure.cityJson);
        city =JsonUtils.fromJson(bannerList, CitiListModel.class);
        yearList.add(mContext.getResources().getString(R.string.select_year));

        for (int i = 0; i < 120; i++) {
            yearList.add((year+i)+"");
        }
        for (City data:city.getCities()) {
            if(isArabic&&data.getCity_ar()!=null){
                cityList.add(data.getCity_ar());

            }else{
                cityList.add(data.getCity());

            }

        }

        radioGroup = findViewById(R.id.radioGroup);

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

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

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item_new, yearList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_new);
        edtBirthYear.setAdapter(adapter);
        edtBirthYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                 selectedYear = yearList.get(i);
                Toast.makeText(RegisterActivity.this, "Year Name: " + selectedYear, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter adapterCity = new ArrayAdapter<>(this, R.layout.simple_spinner_item_new, cityList);
        adapterCity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_new);
        edtCity.setAdapter(adapterCity);
        edtCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                return;
                }

                SelectedCity = cityList.get(i);
                Toast.makeText(RegisterActivity.this, "City Name: " + SelectedCity, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSendConfirmationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String fullName =   edtFullName.getText().toString() ;
             String birthName = selectedYear ;
             String city = SelectedCity; ;
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