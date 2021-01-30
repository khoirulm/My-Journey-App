package com.kmproject.myjourney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {

    Animation app_splash,btt;
    ImageView appLogo;
    TextView subtitle;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        getUsernameLocal();

        //load Animation
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);

        //load element
        appLogo = findViewById(R.id.appLogo);
        subtitle =findViewById(R.id.subtitle);

        //Run Animation
        appLogo.startAnimation(app_splash);
        subtitle.startAnimation(btt);


    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");

        //Memeriksa apakah sudah pernah login sebelumnya
        if(username_key_new.isEmpty()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent goToGetStarted = new Intent(SplashAct.this, GetStartedAct.class);
                    startActivity(goToGetStarted);
                    finish();
                }
            },2000);
        }
        else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent goToHome = new Intent(SplashAct.this, HomeAct.class);
                    startActivity(goToHome);
                    finish();
                }
            },2000);
        }

    }
}
