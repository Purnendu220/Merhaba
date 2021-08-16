package com.wpits.merhaba.activity;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.appcompat.app.AppCompatActivity;

import com.wpits.merhaba.helper.PrefrenceManager;
import com.wpits.merhaba.utils.ContextUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale localeToSwitchTo = new Locale(PrefrenceManager.getInstance().getUserLanguage());
        ContextWrapper localeUpdatedContext = ContextUtils.updateLocale(newBase, localeToSwitchTo);
        super.attachBaseContext(localeUpdatedContext);
    }

}
