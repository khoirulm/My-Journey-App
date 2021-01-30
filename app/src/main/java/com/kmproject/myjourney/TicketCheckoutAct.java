package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {
    Button btnPayNow;
    LinearLayout btnBack;
    ImageView btnMinus, btnPlus,noticeUang;
    TextView textBalance, textPrice, textTicket, titleTicket, locationTicket, ketentuan;
    Integer valueTicket = 1;
    Integer myBalance = 0;
    Integer valuePrice = 0;
    Integer totalPrice = 0;
    Integer sisaBalance = 0;

    String dateWisata = "";
    String timeWisata = "";


    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    //Generate kode Unik untuk setiap ticket
    Integer kodeUnik = new Random().nextInt();




    DatabaseReference reference,reference2,reference3,reference4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();



        //mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String jenisTicketBaru = bundle.getString("jenis_tiket");

        btnPayNow = findViewById(R.id.btnPayNow);
        btnBack = findViewById(R.id.btn_back);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        textBalance = findViewById(R.id.myBalance);
        textPrice = findViewById(R.id.myPrice);
        textTicket = findViewById(R.id.textTicket);
        noticeUang = findViewById(R.id.notice_uang);
        titleTicket = findViewById(R.id.nama_wisata);
        locationTicket = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);



        //Setting value baru untuk beberapa komponen
        textTicket.setText(valueTicket.toString());

        //set Default
        btnMinus.animate().alpha(0).setDuration(300).start();
        btnMinus.setEnabled(false);
        noticeUang.setVisibility(View.GONE);




        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myBalance = Integer.valueOf(snapshot.child("user_balance").getValue().toString());
                textBalance.setText("US$" + myBalance + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenisTicketBaru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titleTicket.setText(snapshot.child("nama_wisata").getValue().toString());
                locationTicket.setText(snapshot.child("lokasi").getValue().toString());
                ketentuan.setText(snapshot.child("ketentuan").getValue().toString());
                dateWisata = snapshot.child("date_wisata").getValue().toString();
                timeWisata = snapshot.child("time_wisata").getValue().toString();
                valuePrice = Integer.valueOf(snapshot.child("harga_tiket").getValue().toString());
                textPrice.setText("US$" + valuePrice + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        btnPlus.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
                valueTicket+=1;
                textTicket.setText(valueTicket.toString());

                if (valueTicket > 1){
                    btnMinus.animate().alpha(1).setDuration(300).start();
                    btnMinus.setEnabled(true);
                }
               totalPrice = valuePrice * valueTicket;
               textPrice.setText("US$" + totalPrice + "");
               if(totalPrice > myBalance) {
                   btnPayNow.animate().translationY(250).alpha(0).setDuration(350).start();
                   btnPayNow.setEnabled(false);
                   textBalance.setTextColor(Color.parseColor("#D1206B"));//Merah
                   noticeUang.setVisibility(View.VISIBLE);
               }
           }
        });

        btnMinus.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               valueTicket-=1;
               textTicket.setText(valueTicket.toString());
               if (valueTicket < 2){
                   btnMinus.animate().alpha(0).setDuration(300).start();
                   btnMinus.setEnabled(false);
               }
               totalPrice = valuePrice * valueTicket;
               textPrice.setText("US$" + totalPrice + "");
                    if(totalPrice < myBalance){
                        btnPayNow.animate().translationY(0).alpha(1).setDuration(350).start();
                        btnPayNow.setEnabled(true);
                        noticeUang.setVisibility(View.GONE);
                        textBalance.setTextColor(Color.parseColor("#203DD1"));//Biru
               }
           }
        });


        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("MyTicket").child(username_key_new)
                        .child(titleTicket.getText().toString() + kodeUnik);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference3.getRef().child("id_ticket").setValue(titleTicket.getText().toString()+kodeUnik);
                        reference3.getRef().child("nama_wisata").setValue(titleTicket.getText().toString());
                        reference3.getRef().child("lokasi").setValue(locationTicket.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valueTicket.toString());
                        reference3.getRef().child("data_wisata").setValue(dateWisata);
                        reference3.getRef().child("time_wisata").setValue(timeWisata);

                        Intent buySuccess = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(buySuccess);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sisaBalance = myBalance - totalPrice;
                        reference4.getRef().child("user_balance").setValue(sisaBalance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");

    }
}
