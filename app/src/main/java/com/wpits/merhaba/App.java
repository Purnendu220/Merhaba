package com.wpits.merhaba;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.wpits.merhaba.utility.PicassoImageLoadingService;

import ss.com.bannerslider.Slider;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Slider.init(new PicassoImageLoadingService(this));

    }
}
