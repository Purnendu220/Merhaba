package com.wpits.merhaba.activity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.wpits.merhaba.R;
import com.wpits.merhaba.utility.Utility;

public class AppIntroActivity extends AppIntro2 {
    boolean isArabic = Utility.isArabic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isArabic){
            addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_layout_three));
            addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_layout_two));
            addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_layout_one));


        }else{
            addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_layout_one));
            addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_layout_two));
            addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.intro_layout_three));
        }


        setIndicatorColor(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.textColorAccent)
        );
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent i = new Intent(AppIntroActivity.this, HomeActivityNew.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDonePressed( Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent i = new Intent(AppIntroActivity.this, HomeActivityNew.class);
        startActivity(i);
        finish();
    }
}