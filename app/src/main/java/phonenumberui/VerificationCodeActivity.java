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

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.wpits.merhaba.R;
import com.wpits.merhaba.MainActivity;
import com.wpits.merhaba.helper.JsonUtils;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.utils.ApplicationUrls;
import com.wpits.merhaba.utils.VerifyOtpModel;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VerificationCodeActivity extends AppCompatActivity {

    private Activity mActivity = VerificationCodeActivity.this;
    private AppCompatEditText etDigit1;
    private AppCompatEditText etDigit2;
    private AppCompatEditText etDigit3;
    private AppCompatEditText etDigit4;
    private AppCompatEditText etDigit5;
    private AppCompatEditText etDigit6;
    private AppCompatButton btnContinue;
    private AppCompatButton btnResendCode;
    private AppCompatTextView tvToolbarBack;
    private AppCompatTextView tvToolbarTitle;
    private AppCompatTextView tvCountDownTimer;
    private LinearLayout llContinue;
    private RelativeLayout rlResend;
    private ProgressBar pbVerify;
    private String strPhoneCode;
    private String strPhoneNumber;
    private String mVerificationId;
    private CountDownTimer countDownTimer;
    private String mobile;
    private String imei;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CheckPermissionAndStartIntent();

        setContentView(R.layout.activity_otp_verification_code);
        setUpUI();
        setUpToolBar();




        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(AppConstant.PhoneCode)) {
                strPhoneCode = getIntent().getStringExtra(AppConstant.PhoneCode);
            }
            if (getIntent().hasExtra(AppConstant.PhoneNumber)) {
                strPhoneNumber = getIntent().getStringExtra(AppConstant.PhoneNumber);
            }
//            tvToolbarBack.setText("< Edit Number");
//            tvToolbarTitle.setText(AppConstant.PLUS + strPhoneCode + " " + strPhoneNumber + "");
            mobile = strPhoneCode+strPhoneNumber;
        }
        startPhoneNumberVerification(AppConstant.PLUS + strPhoneCode + strPhoneNumber + "");

    }

    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(VerificationCodeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(VerificationCodeActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(VerificationCodeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //SEY SOMTHING LIKE YOU CANT ACCESS WITHOUT PERMISSION
        } else {

        }
    }









    private void setUpUI() {

        tvCountDownTimer = findViewById(R.id.tvCountDownTimer);

        etDigit1 = findViewById(R.id.etDigit1);
        etDigit2 = findViewById(R.id.etDigit2);
        etDigit3 = findViewById(R.id.etDigit3);
        etDigit4 = findViewById(R.id.etDigit4);
        etDigit5 = findViewById(R.id.etDigit5);
        etDigit6 = findViewById(R.id.etDigit6);
        rlResend = findViewById(R.id.rlResend);
        llContinue = findViewById(R.id.llContinue);
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
        pbVerify = findViewById(R.id.pbVerify);

        btnContinue = findViewById(R.id.btnContinue);

        btnResendCode = findViewById(R.id.btnResendCode);
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

    private boolean validate() {

        Log.d("validate","validate otp");

            if (TextUtils.isEmpty(etDigit1.getText().toString().trim())) {
                return false;
            } else if (TextUtils.isEmpty(etDigit2.getText().toString().trim())) {
                return false;
            } else if (TextUtils.isEmpty(etDigit3.getText().toString().trim())) {
                return false;
            } else if (TextUtils.isEmpty(etDigit4.getText().toString().trim())) {
                return false;
            } else if (TextUtils.isEmpty(etDigit5.getText().toString().trim())) {
                return true;
            } else if (TextUtils.isEmpty(etDigit6.getText().toString().trim())) {
                return true;
            }

        return true;
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

    private void setUpToolBar() {
        Toolbar mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
    }




    @Override
    public void onBackPressed() {
        //signOut();
        Intent intent = new Intent(mActivity, PhoneNumberActivity.class);
        intent.putExtra("TITLE", getResources().getString(R.string.app_name));
        intent.putExtra("PHONE_NUMBER", "");
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        sendOtp(phoneNumber);
        startCounter();
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

    @Override
    public void onStart() {
        super.onStart();
    }

    // [START resend_verification]


    public void sendOtp(String mobile){
        final ProgressDialog progressDialog = new ProgressDialog(VerificationCodeActivity.this);
        progressDialog.setMessage("Sending Otp...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = ApplicationUrls.otp;
        url = url+mobile.replaceAll("[^a-zA-Z0-9]", "")+"/ar";
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

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

    private void validateOtp(){

        mVerificationId = etDigit1.getText().toString().trim() +
                etDigit2.getText().toString().trim() +
                etDigit3.getText().toString().trim() +
                etDigit4.getText().toString().trim() ;

        String url = ApplicationUrls.validateOtp;
        url = url+mobile+"/"+mVerificationId;

       final ProgressDialog progressDialog = new ProgressDialog(VerificationCodeActivity.this);
        progressDialog.setMessage("Validating...");
        progressDialog.setCancelable(false);

        progressDialog.show();


        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(isActivityRunning(VerificationCodeActivity.class))
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
                    Intent mainIntent = new Intent(VerificationCodeActivity.this, MainActivity.class);
                    VerificationCodeActivity.this.startActivity(mainIntent);
                    VerificationCodeActivity.this.finish();
                }else if(response!=null && response.toString().contains("OTP does not matched")){
                    Toast.makeText(getApplicationContext(),"OTP does not matched.",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Mobile number verification failed. Please retry!",Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(VerificationCodeActivity.this, PhoneNumberActivity.class);
                    VerificationCodeActivity.this.startActivity(mainIntent);
                    VerificationCodeActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(isActivityRunning(VerificationCodeActivity.class))
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



    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDeviceDetails(){
        return System.getProperty("os.version")+"_"+ // OS version
        Build.VERSION.SDK +"_"+     // API Level
        Build.DEVICE+ "_"+           // Device
        Build.MODEL+"_"+            // Model
        Build.PRODUCT+"";
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
    }


