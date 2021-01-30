package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileAct extends AppCompatActivity {
    Button editProfil,btnSignOut;
    TextView namaLengkap,bio;
    ImageView photoProfile,btnBack;


    DatabaseReference reference,reference2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView myTicketPlace;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getUsernameLocal();


        editProfil = findViewById(R.id.btn_edit);
        namaLengkap = findViewById(R.id.nama_lengkap);
        bio         = findViewById(R.id.bio);
        myTicketPlace = findViewById(R.id.myticket_place);
        photoProfile = findViewById(R.id.photoProfile);
        btnBack = findViewById(R.id.btnBack);
        btnSignOut = findViewById(R.id.btnSignOut);


        myTicketPlace.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namaLengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());
                Picasso.with(MyProfileAct.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photoProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goToEditProfile = new Intent (MyProfileAct.this, EditProfilAct.class);
                startActivity(goToEditProfile);
            }
        });

        reference2 = FirebaseDatabase.getInstance()
                .getReference().child("MyTicket").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    MyTicket p = snapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(MyProfileAct.this, list);
                myTicketPlace.setAdapter(ticketAdapter) ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Menghapus value / isi data pada local
                SharedPreferences sharedPreferences  = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                //Pindah Activity ke SignIn
                Intent goToSignIn = new Intent (MyProfileAct.this, SignInAct.class);
                startActivity(goToSignIn);
                finishAffinity();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHome = new Intent(MyProfileAct.this, HomeAct.class);
                startActivity(goToHome);
                finishAffinity();
            }
        });

    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }

}