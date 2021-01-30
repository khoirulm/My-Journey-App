package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterOneAct extends AppCompatActivity {
LinearLayout btnBack;
Button btnContinue;
EditText username, password, email_address;


DatabaseReference reference,reference_username;

String USERNAME_KEY = "usernamekey";
String username_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);
        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btnContinue);


        btnContinue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Ubah button menjadi Loading...
                btnContinue.setEnabled(false);
                btnContinue.setText("Loading...");

                final String xusername = username.getText().toString();
                final String xpassword = password.getText().toString();
                final String xemail = email_address.getText().toString();


                if (xusername.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username harus di isi", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    btnContinue.setText("SIGN IN");
                } else if (xpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password harus di isi", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    btnContinue.setText("SIGN IN");
                } else {
                    if (xemail.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Email address harus di isi", Toast.LENGTH_SHORT).show();
                        btnContinue.setEnabled(true);
                        btnContinue.setText("SIGN IN");
                    } else {
                        reference_username = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username.getText().toString());
                        reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Toast.makeText(getApplicationContext(),"Gunakan Username Lain", Toast.LENGTH_SHORT).show();
                                    btnContinue.setEnabled(true);
                                    btnContinue.setText("CONTINUE");

                                }
                                else{
                                    //Menyimpan data di local (handphone)
                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key, username.getText().toString());
                                    editor.apply();

                                    reference = FirebaseDatabase.getInstance()
                                            .getReference().child("Users").child(username.getText().toString());
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().child("username").setValue(username.getText().toString());
                                            snapshot.getRef().child("password").setValue(password.getText().toString());
                                            snapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                            snapshot.getRef().child("user_balance").setValue(800);

                                            Intent goToRegisterTwo = new Intent(RegisterOneAct.this, RegisterTwoAct.class);
                                            startActivity(goToRegisterTwo);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
             }
            });


        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

    }
}
