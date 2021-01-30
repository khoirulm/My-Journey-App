package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class SignInAct extends AppCompatActivity {
    Button btnSignin;
    TextView createNew;
    EditText xpassword,xusername;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignin = findViewById(R.id.btnSigin);
        createNew = findViewById(R.id.creaateNew);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);


        //Perpindahan activity
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCreateNew = new Intent(SignInAct.this, RegisterOneAct.class);
                startActivity(goToCreateNew);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Ubah button menjadi Loading...
                btnSignin.setEnabled(false);
                btnSignin.setText("Loading...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Usernam Kosong",Toast.LENGTH_SHORT).show();
                    btnSignin.setEnabled(true);
                    btnSignin.setText("SIGN IN");
                }else {
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Pasword Kosong", Toast.LENGTH_SHORT).show();
                        btnSignin.setEnabled(true);
                        btnSignin.setText("SIGN IN");
                    }else {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    //Ambil Data password dari firebase
                                    String passworFromFirebase = snapshot.child("password").getValue().toString();

                                    //validasi password dengan firebase
                                    if(password.equals(passworFromFirebase)){

                                        //Simpan username (key) pada lokal
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();

                                        //Perpindahan Activity
                                        Intent goToHome = new Intent(SignInAct.this, HomeAct.class);
                                        startActivity(goToHome);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Password Salah", Toast.LENGTH_SHORT).show();
                                        //Ubah button menjadi Loading...
                                        btnSignin.setEnabled(true);
                                        btnSignin.setText("SIGN IN");
                                    }


                                }
                                else{
                                    //Ubah button menjadi SIGNIN
                                    btnSignin.setEnabled(true);
                                    btnSignin.setText("SIGN IN");
                                    Toast.makeText(getApplicationContext(),"Username dan Pasword Salah", Toast.LENGTH_SHORT).show();
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
    }
}
