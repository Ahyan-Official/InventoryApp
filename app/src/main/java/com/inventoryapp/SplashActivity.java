package com.inventoryapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class SplashActivity extends AppCompatActivity {

    private CardView top;
    private static int splashTimeOut=2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //init Views
        top=findViewById(R.id.top);

        //add delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);

        //animation
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);

        top.startAnimation(anim);
    }
}