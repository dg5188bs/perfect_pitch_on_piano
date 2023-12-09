package com.example.perfect_pitch_on_piano;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Profile_PictureActivity extends AppCompatActivity {
    ImageView profilePic;
    public static final int PIC_IMAGE_REQUEST = 50;
    public StorageReference storageReference,imageReference;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PIC_IMAGE_REQUEST) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
            else {
                chooseFromGallery2();
            }
        }
    }

    public void showProfilePic(View view) {
            if (imageReference != null) {
                imageReference.getBytes(1920 * 1080).addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profilePic.setImageBitmap(bitmap);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                });
            } else {
                Toast.makeText(this, "The image did not upload or was not selected", Toast.LENGTH_LONG).show();
            }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data_back) {
        super.onActivityResult(requestCode, resultCode, data_back);
        if (requestCode == PIC_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data_back != null) {
                imageUri = data_back.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] imageData = byteArrayOutputStream.toByteArray();
                    imageReference = storageReference.child("image/"+ System.currentTimeMillis()+".jpg");
                    UploadTask uploadTask = imageReference.putBytes(imageData);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this,"image uploaded successfully",Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this,"failed upload image",Toast.LENGTH_LONG).show();
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(this,"you must select an image",Toast.LENGTH_LONG).show();

            }
        }
    }


        public void chooseFromGallery(View view) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PIC_IMAGE_REQUEST);
            }
            else {
                chooseFromGallery2();
            }
        }
    public void chooseFromGallery2() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PIC_IMAGE_REQUEST);
    }

    public void to_FBDB(View view) {
        Intent FBDBIntent = new Intent(this, FBDB_withText_Activity.class);
        startActivity(FBDBIntent);
    }

    }

