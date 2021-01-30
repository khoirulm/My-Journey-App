package com.kmproject.myjourney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessBuyTicketAct extends AppCompatActivity {
    Button  myDashboard,btnViewTicket;
    Animation btt,ttb,appSplash;
    TextView textTitle,textSubtitle;
    ImageView icSucces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        myDashboard = findViewById(R.id.btnMyDhasboard);
        btnViewTicket = findViewById(R.id.btnViewTicket);
        icSucces = findViewById(R.id.ic_success);
        textTitle = findViewById(R.id.textTitle);
        textSubtitle = findViewById(R.id.textSubtitle);

        //load Animation
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this,R.anim.ttb);
        appSplash = AnimationUtils.loadAnimation(this,R.anim.app_splash);

        //run Animation
        icSucces.startAnimation(appSplash);
        textTitle.startAnimation(ttb);
        textSubtitle.startAnimation(ttb);
        myDashboard.startAnimation(btt);
        btnViewTicket.startAnimation(btt);

        btnViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToProfile = new Intent(SuccessBuyTicketAct.this, MyProfileAct.class);
                startActivity(goToProfile);
            }
        });

        myDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(SuccessBuyTicketAct.this, HomeAct.class);
                startActivity(goHome);
            }
        });

    }
}
