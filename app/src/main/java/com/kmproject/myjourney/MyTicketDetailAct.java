package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MyTicketDetailAct extends AppCompatActivity {
    TextView titleWisata,lokasi,jumlahTicket,date,time,ketentuan;
    LinearLayout btnBack;

    DatabaseReference reference,reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");


        titleWisata = findViewById(R.id.titleTicket);
        lokasi = findViewById(R.id.lokasi);
        jumlahTicket = findViewById(R.id.jumlah_tiket);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        ketentuan = findViewById(R.id.short_desc_ticket);
        btnBack = findViewById(R.id.btn_back);


        //Mengambil data dari Firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titleWisata.setText(snapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(snapshot.child("lokasi").getValue().toString());
                date.setText(snapshot.child("date_wisata").getValue().toString());
                time.setText(snapshot.child("time_wisata").getValue().toString());
                ketentuan.setText(snapshot.child("ketentuan").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}