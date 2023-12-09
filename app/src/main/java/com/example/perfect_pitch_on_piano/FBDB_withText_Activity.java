package com.example.perfect_pitch_on_piano;

import static com.example.perfect_pitch_on_piano.FBref.refText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FBDB_withText_Activity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    DBText dbText;
    String str;
    String textid,text;
    String str2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbdbwith_text);
        editText = (EditText) findViewById(R.id.editTextText);
        textView = (TextView) findViewById(R.id.textView2);
        textid = "your text";
        text = "";
        dbText = new DBText(text,textid);
        refText.child(textid).setValue(text);
        ValueEventListener stuListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                str2 = "";
                for (DataSnapshot data : snapshot.getChildren()) {
                    String value = data.getValue(String.class);
                    str2 = value ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refText.addValueEventListener(stuListener);
    }

    public void save(View view) {
        str = editText.getText().toString();
        refText.child(textid).setValue(str);
    }

    public void showText(View view) {
        textView.setText(str2);
    }

    public void next(View view) {
        Intent toSoundActivity = new Intent(this,SoundByNum.class);
        startActivity(toSoundActivity);
    }
}