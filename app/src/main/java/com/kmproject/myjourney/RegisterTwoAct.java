package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoAct extends AppCompatActivity {

    LinearLayout btnBack;
    Button btnContinue,btnAddPhoto;
    ImageView photoRegister;
    EditText bio, nama_lengkap;

    Uri photoLocation;
    Integer photoMax = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();

        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btnContinue);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        photoRegister = findViewById(R.id.pic_photo_register_user);
        bio = findViewById(R.id.bio);
        nama_lengkap = findViewById(R.id.nama_lengkap);

        btnAddPhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                findPhoto();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Ubah State menjadi loading
                btnContinue.setEnabled(false);
                btnContinue.setText("Loading...");

                final String xbio = bio.getText().toString();
                final String xnama = nama_lengkap.getText().toString();

                if (xnama.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama harus di isi", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    btnContinue.setText("SIGN IN");
                } else {
                    if (xbio.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Bio harus di isi", Toast.LENGTH_SHORT).show();
                        btnContinue.setEnabled(true);
                        btnContinue.setText("SIGN IN");
                    } else {
                        //Menyimpan di Firebase
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

                        //Validai untuk file (apakah ada?)

                        if (photoLocation != null) {
                            final StorageReference storageReference1 =
                                    storage.child(System.currentTimeMillis() + "." + getFileExtension(photoLocation));
                                      storageReference1.putFile(photoLocation)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    String uri_photo = uri.toString();
                                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                                    reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                                    reference.getRef().child("bio").setValue(bio.getText().toString());

                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    //Berpindah halaman
                                                    Intent goToSuccess = new Intent(RegisterTwoAct.this, SuccesRegisterAct.class);
                                                    startActivity(goToSuccess);

                                                }
                                            });

                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Photo harus di isi", Toast.LENGTH_SHORT).show();
                            btnContinue.setEnabled(true);
                            btnContinue.setText("SIGN IN");
                        }
                        }

                    }
                }
        });

    }


    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photoMax);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photoMax && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            photoLocation = data.getData();
            Picasso.with(this).load(photoLocation).centerCrop().fit().into( photoRegister);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
