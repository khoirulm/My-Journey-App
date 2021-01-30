package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class TicketDetailAct extends AppCompatActivity {
    TextView titleTicket, locationTicket,
            photoSpotsTicket, wifiTicket,
            festivalTicket,shortDescTicket;
    ImageView headerTicket;

    DatabaseReference reference ;

    Button btnBuy;
    LinearLayout btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        Bundle bundle = getIntent().getExtras();
        final String jenisTicketBaru = bundle.getString("jenis_tiket");

        Toast.makeText(getApplicationContext(), "Jenis Tiket " + jenisTicketBaru,Toast.LENGTH_SHORT).show();

        btnBack = findViewById(R.id.btn_back);
        btnBuy = findViewById(R.id.btn_buy_now);
        titleTicket = findViewById(R.id.title_ticket);
        locationTicket = findViewById(R.id.location_ticket);
        photoSpotsTicket = findViewById(R.id.photo_spots_ticket);
        wifiTicket = findViewById(R.id.wifi_ticket);
        festivalTicket = findViewById(R.id.festival_ticket);
        shortDescTicket = findViewById(R.id.short_desc_ticket);
        headerTicket = findViewById(R.id.bgHeader);

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenisTicketBaru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titleTicket.setText(snapshot.child("nama_wisata").getValue().toString());
                locationTicket.setText(snapshot.child("lokasi").getValue().toString());
                photoSpotsTicket.setText(snapshot.child("is_photo_spot").getValue().toString());
                wifiTicket.setText(snapshot.child("is_wifi").getValue().toString());
                festivalTicket.setText(snapshot.child("is_festival").getValue().toString());
                shortDescTicket.setText(snapshot.child("short_desc").getValue().toString());
                Picasso.with(TicketDetailAct.this).load(snapshot.child("url_thumbnail")
                        .getValue().toString()).centerCrop().fit().into(headerTicket);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailAct.this, TicketCheckoutAct.class);
                intent.putExtra("jenis_tiket", jenisTicketBaru);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View v){
                onBackPressed();
            }
        });

    }
}
