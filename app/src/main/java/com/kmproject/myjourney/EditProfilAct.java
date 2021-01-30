package com.kmproject.myjourney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SnapHelper;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfilAct extends AppCompatActivity {

    EditText xNama,xBio,xUsername,xPassword,xEmail;
    Button btnAdd,btnSaveProfile;
    LinearLayout btnBack;
    ImageView editPhoto;

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
        setContentView(R.layout.activity_edit_profil);

        getUsernameLocal();


        xNama = findViewById(R.id.editNama);
        xBio = findViewById(R.id.editBio);
        xUsername = findViewById(R.id.editUsername);
        xPassword = findViewById(R.id.editPassword);
        xEmail = findViewById(R.id.editEmail);
        btnAdd = findViewById(R.id.buttonAdd);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnBack = findViewById(R.id.btn_back);
        editPhoto = findViewById(R.id.pic_edit_photo);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                xNama.setText(snapshot.child("nama_lengkap").getValue().toString());
                xBio.setText(snapshot.child("bio").getValue().toString());
                xUsername.setText(snapshot.child("username").getValue().toString());
                xPassword.setText(snapshot.child("password").getValue().toString());
                xEmail.setText(snapshot.child("email_address").getValue().toString());
                Picasso.with(EditProfilAct.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(editPhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mengubah button menjandi loading
                btnSaveProfile.setEnabled(false);
                btnSaveProfile.setText("Loading...");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("nama_lengkap").setValue(xNama.getText().toString());
                        snapshot.getRef().child("bio").setValue(xBio.getText().toString());
                        snapshot.getRef().child("username").setValue(xUsername.getText().toString());
                        snapshot.getRef().child("password").setValue(xPassword.getText().toString());
                        snapshot.getRef().child("email_address").setValue(xEmail.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (photoLocation != null){
                    final StorageReference storageReference =
                            storage.child(System.currentTimeMillis() + "." + getFileExtension(photoLocation));
                            storageReference.putFile(photoLocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           String uri_photo = uri.toString();
                                           reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                       }
                                   }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Uri> task) {
                                       }
                                   });
                                }
                            });
                }
                Intent goToMyProfile = new Intent(EditProfilAct.this, MyProfileAct.class);
                startActivity(goToMyProfile);
                finish();
            }
        });

    }
    String getFileExtension( Uri uri){
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

        if(requestCode == photoMax && resultCode == RESULT_OK && data != null && data.getData() != null){
            photoLocation = data.getData();
            Picasso.with(this).load(photoLocation).centerCrop().fit().into(editPhoto);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}