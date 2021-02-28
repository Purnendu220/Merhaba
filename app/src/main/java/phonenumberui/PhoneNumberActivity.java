// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package phonenumberui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import phonenumberui.countrycode.Country;
import phonenumberui.countrycode.CountryUtils;
import phonenumberui.utility.Utility;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.wpits.merhaba.MainActivity;
import com.wpits.merhaba.R;
import com.wpits.merhaba.activity.BaseActivity;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.VerifyOtpModel;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class PhoneNumberActivity extends BaseActivity {

    private AppCompatEditText etCountryCode;
    private AppCompatEditText etPhoneNumber;
    private AppCompatButton btnSendConfirmationCode;
    private ImageView imgFlag;
    private AppCompatTextView tvToolbarTitle;
    private Activity mActivity = PhoneNumberActivity.this;
    private PhoneNumberUtil mPhoneUtil;
    private Country mSelectedCountry;
    private static final int COUNTRYCODE_ACTION = 1;
    private static final int VERIFICATION_ACTION = 2;
    public String title = "";
    Dialog myDialog;
    Context mContext;
    private ProgressBar pbVerify;
    String mobile;

    private String mVerificationId;
    private LinearLayout llContinue;
    private RelativeLayout rlResend;
    AppCompatEditText etDigit1;
    AppCompatEditText etDigit2;
    AppCompatEditText etDigit3;
    AppCompatEditText etDigit4;
    AppCompatEditText etDigit5;
    AppCompatEditText etDigit6;
    AppCompatButton btnContinue;
    AppCompatButton btnResendCode;
    AppCompatTextView tvCountDownTimer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_phone_number);

        myDialog = new Dialog(this,R.style.AdvanceDialogTheme);
        mContext = this;


        setUpUI();
       // setUpToolBar();
       // etPhoneNumber.setText("911624663");
       // mobile = mSelectedCountry.getPhoneCode()+etPhoneNumber.getText().toString().trim();
       // sendOtp(AppConstant.PLUS+mobile);
       // showOtPScreen();

    }


    private void setUpUI() {
        etCountryCode = findViewById(R.id.etCountryCode);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        imgFlag = findViewById(R.id.flag_imv);
        btnSendConfirmationCode = findViewById(R.id.btnSendConfirmationCode);
        //tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        mPhoneUtil = PhoneNumberUtil.createInstance(mActivity);

//
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String countryISO = tm.getNetworkCountryIso();
        String countryNumber = "";
        String countryName = "";
        Utility.log(countryISO);

        Country country = new Country(getString(R.string.country_libya_code),
                getString(R.string.country_libya_number),
                getString(R.string.country_libya_name));
        this.mSelectedCountry = country;
        etCountryCode.setText("+" + country.getPhoneCode() + "");
        imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
        Utility.log(countryNumber);

        /*if(!TextUtils.isEmpty(countryISO))
        {
            for (Country country : CountryUtils.getAllCountries(mActivity)) {
                if (countryISO.toLowerCase().equalsIgnoreCase(country.getIso().toLowerCase())) {
                    countryNumber = country.getPhoneCode();
                    countryName = country.getName();
                    break;
                }
            }
            Country country = new Country(countryISO,
                    countryNumber,
                    countryName);
            this.mSelectedCountry = country;
            etCountryCode.setText("+" + country.getPhoneCode() + "");
            imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
            Utility.log(countryNumber);
        }
       /* else {
            Country country = new Country(getString(R.string.country_united_states_code),
                    getString(R.string.country_united_states_number),
                    getString(R.string.country_united_states_name));
            this.mSelectedCountry = country;
            etCountryCode.setText("+" + country.getPhoneCode() + "");
            imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
            Utility.log(countryNumber);
        }*/

        //setPhoneNumberHint();


        etCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyBoardFromView(mActivity);
                etPhoneNumber.setError(null);
                Intent intent = new Intent(mActivity, CountryCodeActivity.class);
                intent.putExtra("TITLE", getResources().getString(R.string.app_name));
                startActivityForResult(intent, COUNTRYCODE_ACTION);
            }
        });
        btnSendConfirmationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyBoardFromView(mActivity);
                etPhoneNumber.setError(null);
                if (validate()) {
                     mobile = mSelectedCountry.getPhoneCode()+etPhoneNumber.getText().toString().trim();
                    sendOtp(AppConstant.PLUS+mobile);

//                    Intent verificationIntent = new Intent(mActivity, VerificationCodeActivity.class);
//                    verificationIntent.putExtra(AppConstant.PhoneNumber, etPhoneNumber.getText().toString().trim());
//                    verificationIntent.putExtra(AppConstant.PhoneCode, mSelectedCountry.getPhoneCode() + "");
//                    verificationIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//                    startActivity(verificationIntent);
//                    finish();
                }
            }
        });

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra("PHONE_NUMBER")) {
                etPhoneNumber.setText(getIntent().getStringExtra("PHONE_NUMBER"));
                etPhoneNumber.setSelection(etPhoneNumber.getText().toString().trim().length());
            }
        }
    }

    private void setUpToolBar() {
        Toolbar mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(ContextCompat.getColor(mActivity, R.color.backgroundLight));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void setPhoneNumberHint() {
        if (mSelectedCountry != null) {
            Phonenumber.PhoneNumber phoneNumber =
                    mPhoneUtil.getExampleNumberForType(mSelectedCountry.getIso().toUpperCase(),
                            PhoneNumberUtil.PhoneNumberType.MOBILE);
            if (phoneNumber != null) {
                String format = mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                if (format.length() > mSelectedCountry.getPhoneCode().length())
                    etPhoneNumber.setHint(
                            format.substring((mSelectedCountry.getPhoneCode().length() + 1), format.length()));
            }
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())) {
            etPhoneNumber.setError("Please enter phone number");
            etPhoneNumber.requestFocus();
            return false;
        } else if (!isValid()) {
            etPhoneNumber.setError("Please enter valid phone number");
            etPhoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUNTRYCODE_ACTION) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.hasExtra(AppConstant.COUNTRY)) {
                        Country country = (Country) data.getSerializableExtra(AppConstant.COUNTRY);
                        this.mSelectedCountry = country;
                        setPhoneNumberHint();
                        etCountryCode.setText("+" + country.getPhoneCode() + "");
                        imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
                    }
                }
            }
        } else if (requestCode == VERIFICATION_ACTION) {
            if (data != null) {

            }
        }
    }

    public boolean isValid() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        return phoneNumber != null && mPhoneUtil.isValidNumber(phoneNumber) &&  !phoneNumber.getRawInput().startsWith("+");
    }

    public Phonenumber.PhoneNumber getPhoneNumber() {
        try {
            String iso = null;
            if (mSelectedCountry != null) {
                iso = mSelectedCountry.getIso().toUpperCase();
            }
            return mPhoneUtil.parse(etPhoneNumber.getText().toString().trim(), iso);
        } catch (NumberParseException ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public void sendOtp(String mobile){
        final ProgressDialog progressDialog = new ProgressDialog(PhoneNumberActivity.this);
        progressDialog.setMessage("Sending Otp...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = ApplicationUrls.otp;
        url = url+mobile.replaceAll("[^a-zA-Z0-9]", "")+"/ar";
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                showOtPScreen();
                Log.d("SendOtp", "onResponse: ****"+response);
                Gson gson=new Gson();
                Toast.makeText(getApplicationContext(), "OTP is sent to your mobile number.", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Opps! Please Try Again", Toast.LENGTH_LONG).show();
                finish();

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

        MySingleton.getInstance(getApplicationContext()).addToRequest(jsonArrayRequest);
    }

    private void showOtPScreen( ){
        myDialog.setContentView(R.layout.layout_verification_code);
        setUpUIDialog();
        startCounter();
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }
    private void setUpUIDialog() {

        tvCountDownTimer = myDialog.findViewById(R.id.tvCountDownTimer);

        etDigit1 = myDialog.findViewById(R.id.etDigit1);
        etDigit2 = myDialog.findViewById(R.id.etDigit2);
        etDigit3 = myDialog.findViewById(R.id.etDigit3);
        etDigit4 = myDialog.findViewById(R.id.etDigit4);
        etDigit5 = myDialog.findViewById(R.id.etDigit5);
        etDigit6 = myDialog.findViewById(R.id.etDigit6);
        rlResend = myDialog.findViewById(R.id.rlResend);
        llContinue = myDialog.findViewById(R.id.llContinue);
        pbVerify = myDialog.findViewById(R.id.pbVerify);
        btnContinue = myDialog.findViewById(R.id.btnContinue);
        btnResendCode = myDialog.findViewById(R.id.btnResendCode);
        llContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnContinue.isClickable()) {
                    btnContinue.performClick();
                    if(validate()){
                        validateOtp();

                    }else{
                        Toast.makeText(getApplicationContext(),"Please enter OTP than validate.",Toast.LENGTH_SHORT).show();

                    }

//                    Toast.makeText(getApplicationContext(),"Mobile number verification successful.",Toast.LENGTH_SHORT).show();
//                    Intent mainIntent = new Intent(VerificationCodeActivity.this, Language.class);
//                    VerificationCodeActivity.this.startActivity(mainIntent);
//                    VerificationCodeActivity.this.finish();
//                    PrefrenceManager.getInstance().setUserMobile(mobile);

                }
            }
        });

        rlResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(mobile);
            }
        });
        pbVerify = myDialog.findViewById(R.id.pbVerify);

        btnContinue = myDialog.findViewById(R.id.btnContinue);

        btnResendCode = myDialog.findViewById(R.id.btnResendCode);
        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(mobile);
            }
        });

        setButtonContinueClickbleOrNot();
       /* tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });*/
        etDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit2.requestFocus();
                }
            }
        });
        etDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit3.requestFocus();
                } else {
                    etDigit1.requestFocus();
                }
            }
        });
        etDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit4.requestFocus();
                } else {
                    etDigit2.requestFocus();
                }
            }
        });
        etDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit5.requestFocus();
                } else {
                    etDigit3.requestFocus();
                }
            }
        });
        etDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                    etDigit6.requestFocus();
                } else {
                    etDigit4.requestFocus();
                }
            }
        });
        etDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setButtonContinueClickbleOrNot();
                if (editable.toString().length() == 1) {
                } else {
                    etDigit5.requestFocus();
                }
            }
        });

        etDigit1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                } else {
                    if (etDigit1.getText().toString().trim().length() == 1) {
                        etDigit2.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit2.getText().toString().trim().length() == 0)
                        etDigit1.requestFocus();
                } else {
                    if (etDigit2.getText().toString().trim().length() == 1) {
                        etDigit3.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit3.getText().toString().trim().length() == 0)
                        etDigit2.requestFocus();
                } else {
                    if (etDigit3.getText().toString().trim().length() == 1) {
                        etDigit4.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit4.getText().toString().trim().length() == 0)
                        etDigit3.requestFocus();
                } else {
                    if (etDigit4.getText().toString().trim().length() == 1) {
                        etDigit5.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit5.getText().toString().trim().length() == 0)
                        etDigit4.requestFocus();
                } else {
                    if (etDigit5.getText().toString().trim().length() == 1) {
                        etDigit6.requestFocus();
                    }
                }
                return false;
            }
        });
        etDigit6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etDigit6.getText().toString().trim().length() == 0)
                        etDigit5.requestFocus();
                }
                return false;
            }
        });

        btnContinue.setOnClickListener(new ButtonClickListener());

        /*btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){} {
                //Utility.hideKeyBoardFromView(mActivity);
                if(validate()) {
                    Log.d("validate", "Call validate");
                    Intent mainIntent = new Intent(VerificationCodeActivity.this, MainActivity.class);
                    VerificationCodeActivity.this.startActivity(mainIntent);
                    VerificationCodeActivity.this.finish();
                    validateOtp();
                }

            }
        });
*/    }
    private void validateOtp(){

        mVerificationId = etDigit1.getText().toString().trim() +
                etDigit2.getText().toString().trim() +
                etDigit3.getText().toString().trim() +
                etDigit4.getText().toString().trim() ;

        String url = ApplicationUrls.validateOtp;
        url = url+mobile+"/"+mVerificationId;

        final ProgressDialog progressDialog = new ProgressDialog(PhoneNumberActivity.this);
        progressDialog.setMessage("Validating...");
        progressDialog.setCancelable(false);

        progressDialog.show();


        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    progressDialog.dismiss();

                Log.d("Verify OTP", "onResponse: ****"+response);
                Gson gson=new Gson();
                if(response!=null && (response.toString().toLowerCase().contains("success")) || response.toString().contains("User already registered")
                        || response.toString().contains("New user registered")){

                    SharedPreferences settings = getSharedPreferences("PREFERENCE", MODE_PRIVATE);

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isFirstRun", false);
                    editor.putBoolean("isFirebaseIdUpdated", false);
                    editor.putString("mobile",mobile);
                    editor.commit();
                    PrefrenceManager.getInstance().setUserMobile(mobile);
                    PrefrenceManager.getInstance().setFirstRun(false);
                    PrefrenceManager.getInstance().setFirebaseIdUpdated(false);
                    PrefrenceManager.getInstance().setIsLoggedIn(true);
                    PrefrenceManager.getInstance().setUserData(response.toString());



                    Toast.makeText(getApplicationContext(),"Mobile number verification successful.",Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(PhoneNumberActivity.this, MainActivity.class);
                    PhoneNumberActivity.this.startActivity(mainIntent);
                    PhoneNumberActivity.this.finish();
                }else if(response!=null && response.toString().contains("OTP does not matched")){
                    Toast.makeText(getApplicationContext(),"OTP does not matched.",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Mobile number verification failed. Please retry!",Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(PhoneNumberActivity.this, PhoneNumberActivity.class);
                    PhoneNumberActivity.this.startActivity(mainIntent);
                    PhoneNumberActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                try {
                    String responseBody = new String( error.networkResponse.data, "utf-8" );
                    VerifyOtpModel data =  JsonUtils.fromJson(responseBody, VerifyOtpModel.class);
                    Toast.makeText(getApplicationContext(),data.getData(),Toast.LENGTH_SHORT).show();
                    Log.d("verifyOtp", "userLogin: " + responseBody);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }




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
        }

        ) {
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

        MySingleton.getInstance(getApplicationContext()).addToRequest(jsonArrayRequest);
    }

    private void setButtonContinueClickbleOrNot() {
        if (!validate()) {
            llContinue.setAlpha(.5f);
            btnContinue.setClickable(false);
        } else {
            llContinue.setAlpha(1.0f);
            btnContinue.setClickable(true);
        }
    }
    class ButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v){} {
            //Utility.hideKeyBoardFromView(mActivity);
            if(validate()) {
                Log.d("validate", "Call validate");


            }

        }

    }
    protected Boolean isActivityRunning(Class activityClass)
    {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
    private void startCounter() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvCountDownTimer.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvCountDownTimer.setText("");
                btnResendCode.setEnabled(true);

            }

        };
        countDownTimer.start();
    }
}
