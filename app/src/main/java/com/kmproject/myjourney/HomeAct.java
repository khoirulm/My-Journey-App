package com.kmproject.myjourney;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {

    LinearLayout btnPisa,btnPagoda,btnCandi,btnTorri,btnMonas,btnSphinx;
    CircleView btnProfile;
    ImageView photo_home_user;
    TextView bio, nama_lengkap, user_balance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        btnProfile = findViewById(R.id.btn_to_profile);
        btnPisa = findViewById(R.id.btn_pisa);
        btnPagoda = findViewById(R.id.btn_pagoda);
        btnCandi = findViewById(R.id.btn_candi);
        btnMonas = findViewById(R.id.btn_monas);
        btnTorri = findViewById(R.id.btn_torri);
        btnSphinx = findViewById(R.id.btn_sphinx);

        photo_home_user = findViewById(R.id.photo_home_user);
        bio = findViewById(R.id. bio);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        user_balance = findViewById(R.id.user_balance);


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());
                user_balance.setText("US$ " + snapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeAct.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString()).centerCrop()
                        .fit().into(photo_home_user);
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });

        btnPisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent pisaTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                pisaTicket.putExtra("jenis_tiket","Pisa");
                startActivity(pisaTicket);
            }
        });

        btnTorri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent torriTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                torriTicket.putExtra("jenis_tiket", "Torri");
                startActivity(torriTicket);
            }
        });

        btnPagoda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent pagodaTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                pagodaTicket.putExtra("jenis_tiket", "Pagoda");
                startActivity(pagodaTicket);
            }
        });

        btnCandi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent candiTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                candiTicket.putExtra("jenis_tiket","Candi");
                startActivity(candiTicket);
            }
        });

        btnMonas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent monasTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                monasTicket.putExtra("jenis_tiket", "Monas");
                startActivity(monasTicket);
            }
        });

        btnSphinx.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sphinxTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                sphinxTicket.putExtra("jenis_tiket", "Sphinx");
                startActivity(sphinxTicket);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent goToProfile = new Intent(HomeAct.this, MyProfileAct.class);
                startActivity(goToProfile);
            }
        });

    }
        public void getUsernameLocal(){
            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
            username_key_new = sharedPreferences.getString(username_key,"");
        }


}
