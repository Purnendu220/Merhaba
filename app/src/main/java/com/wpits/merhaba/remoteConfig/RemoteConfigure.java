package com.wpits.merhaba.remoteConfig;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.wpits.merhaba.R;


public class RemoteConfigure {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    private Context context;
    public static RemoteConfigure firebaseRemoteConfigHelper;
    private String TAG = "RemoteConfigure";

    public static final String bannerJson="BANNER_JSON";
    public static final String top_twenty_en="Top_Twenty_En";
    public static final String top_twenty_ar="Top_Twenty_Ar";
    public static final String new_arrival_en="New_Arrival_En";
    public static final String new_arrival_ar="New_Arrival_Ar";
    public static final String crash_app="Crash_App";
    public static final String cityJson="city_list";





    public static RemoteConfigure getFirebaseRemoteConfig(Context mContext) {
        if (firebaseRemoteConfigHelper == null) {
            firebaseRemoteConfigHelper = new RemoteConfigure(mContext);
        }
        return firebaseRemoteConfigHelper;
    }
    public RemoteConfigure(Context mContext) {
        this.context = mContext;

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
         configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_configure_defaults);

    }

    public void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);


                        }
                    }
                });




    }




    public String getRemoteConfigValue(String key){
        return mFirebaseRemoteConfig.getString(key);
    }
}