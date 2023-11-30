package com.example.perfect_pitch_on_piano;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import android.Manifest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Profile_PictureActivity extends AppCompatActivity {
    Uri imageUri;
    boolean isLegal;

    ImageView profilePic;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        profilePic = (ImageView) findViewById(R.id.profilePic);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data_back) {
        super.onActivityResult(requestCode, resultCode, data_back);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION && resultCode == Activity.RESULT_OK) {
            if (data_back != null) {
                imageUri = data_back.getData();
                isLegal = true;
            }
            else {
                isLegal= false;
            }
        }
    }



    public void chooseFromGallery(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    public void showProfilePic(View view) {
        if(isLegal){
            profilePic.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this,"You must select an image",Toast.LENGTH_LONG).show();
        }

    }

    public void to_FBDB(View view) {
        Intent FBDBIntent = new Intent(this,FBDB_withText_Activity.class);
        startActivity(FBDBIntent);

    }
}