package com.wpits.merhaba.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.wpits.merhaba.R;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.remoteConfig.RemoteConfigure;

import phonenumberui.PhoneNumberActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RemoteConfigure.getFirebaseRemoteConfig(this).fetchRemoteConfig();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(!PrefrenceManager.getInstance().isLoggedIn()){
                    Intent i = new Intent(SplashActivity.this, PhoneNumberActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashActivity.this, HomeActivityNew.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
