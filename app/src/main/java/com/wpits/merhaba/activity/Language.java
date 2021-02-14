package com.wpits.merhaba.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.wpits.merhaba.MainActivity;
import com.wpits.merhaba.R;
import com.wpits.merhaba.helper.LocaleHelper;

public class Language extends AppCompatActivity {

    ImageView arabLang,englishLang;
    SharedPreferences langaugePref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

      /*  Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Libyana.ttf",true);*/

        arabLang=findViewById(R.id.arab);
        englishLang=findViewById(R.id.eng);

        langaugePref=getApplicationContext().getSharedPreferences("marhaba", Context.MODE_PRIVATE);
        editor=langaugePref.edit();

        Log.d("Choose language",LocaleHelper.getLanguage(Language.this));

        englishLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   editor.clear();
                //   editor.putString("LANG","en");
                LocaleHelper.setLocale(Language.this,"en");
                editor.putString("lang","en");
                editor.commit();
                Intent i = new Intent(Language.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        arabLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   editor.clear();
                LocaleHelper.setLocale(Language.this,"ar");
                //  editor.putString("LANG","ar-LY");
                editor.putString("lang","ar");
                editor.commit();
                Intent i = new Intent(Language.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
