package com.kmproject.myjourney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {

 Button btnSigIn, btnNewAccount;
 ImageView appLogo;
 TextView introApp;
 Animation btt, ttb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //load Animation
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);



        btnSigIn = findViewById(R.id.btnSign);
        btnNewAccount = findViewById(R.id.btnNewAccount);
        appLogo = findViewById(R.id.appLogo);
        introApp = findViewById(R.id.intro_app);

        //run Animation
        appLogo.startAnimation(ttb);
        introApp.startAnimation(ttb);
        btnSigIn.startAnimation(btt);
        btnNewAccount.startAnimation(btt);



        btnSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View view){
                Intent intent = new Intent(GetStartedAct.this, SignInAct.class);
                startActivity(intent);
            }
        });

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(GetStartedAct.this, RegisterOneAct.class);
                startActivity(intent);
            }
        });
    }
}
