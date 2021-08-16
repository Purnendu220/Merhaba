package com.wpits.merhaba.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.wpits.merhaba.R;
import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.remoteConfig.RemoteConfigure;

import phonenumberui.PhoneNumberActivity;

public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RemoteConfigure.getFirebaseRemoteConfig(this).fetchRemoteConfig();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(!PrefrenceManager.getInstance().isShowAppIntro()){
                    Intent i = new Intent(SplashActivity.this, HomeActivityNew.class);
                    startActivity(i);
                    finish();
                }else{
                    PrefrenceManager.getInstance().setShowAppIntro(false);
                    Intent i = new Intent(SplashActivity.this, AppIntroActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
