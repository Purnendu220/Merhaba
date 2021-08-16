package com.wpits.merhaba.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wpits.merhaba.MainActivity;
import com.wpits.merhaba.R;
import com.wpits.merhaba.model.album.Song;

public class LoginActivity extends BaseActivity {

    Button otpVerify;
    Dialog myDialog;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDialog = new Dialog(this);
        mContext = this;

        otpVerify=findViewById(R.id.otpVerify);

        otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }


    private void showGift( final Song song){
        TextView txtclose;
        Button  btnGift;
        final AppCompatEditText edtGifteeNumber;
        myDialog.setContentView(R.layout.layout_verification_code);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        btnGift = (Button) myDialog.findViewById(R.id.btnGift);
        edtGifteeNumber = myDialog.findViewById(R.id.etPhoneNumber);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });



    }

}
