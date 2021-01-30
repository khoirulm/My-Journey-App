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

public class SuccesRegisterAct extends AppCompatActivity {
    Button btnExplore;
    Animation btt,ttb,appSplash;
    ImageView icSucces;
    TextView title,subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_register);




        btnExplore = findViewById(R.id.btnExplore);
        icSucces = findViewById(R.id.ic_success);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);

        //load Animation
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this,R.anim.ttb);
        appSplash = AnimationUtils.loadAnimation(this,R.anim.app_splash);

        //run Animation
        icSucces.startAnimation(appSplash);
        title.startAnimation(ttb);
        subtitle.startAnimation(ttb);
        btnExplore.startAnimation(btt);


         btnExplore.setOnClickListener( new View.OnClickListener(){
           @Override
           public void onClick(View view){
               Intent intent = new Intent(SuccesRegisterAct.this,HomeAct.class);
               startActivity(intent);
               finish();
            }
        });

    }
}
