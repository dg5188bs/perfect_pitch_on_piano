package com.example.perfect_pitch_on_piano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email,password;
    Intent registerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        registerIntent = new Intent(LogInActivity.this,Profile_PictureActivity.class);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(registerIntent);
        }
    }


    public void Register(View view) {
        if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(LogInActivity.this, "register failed", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(registerIntent);
                            } else {
                                Toast.makeText(LogInActivity.this, "register failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }
}