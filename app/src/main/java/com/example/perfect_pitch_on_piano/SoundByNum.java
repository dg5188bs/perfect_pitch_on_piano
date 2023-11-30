package com.example.perfect_pitch_on_piano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SoundByNum extends AppCompatActivity {
    MediaPlayer mediaPlayer_do,mediaPlayer_re,mediaPlayer_mi,mediaPlayer_fa,
    mediaPlayer_sol,mediaPlayer_la,mediaPlayer_si;
    EditText editTextNum;
    String strOfeditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_by_num);
        editTextNum = (EditText) findViewById(R.id.editTextNumber2);
        mediaPlayer_do = MediaPlayer.create(SoundByNum.this,R.raw.do_note);
        mediaPlayer_re = MediaPlayer.create(SoundByNum.this,R.raw.re_note);
        mediaPlayer_mi = MediaPlayer.create(SoundByNum.this,R.raw.mi_note);
        mediaPlayer_fa = MediaPlayer.create(SoundByNum.this,R.raw.fa_note);
        mediaPlayer_sol = MediaPlayer.create(SoundByNum.this,R.raw.sol_note);
        mediaPlayer_la = MediaPlayer.create(SoundByNum.this,R.raw.la_note);
        mediaPlayer_si = MediaPlayer.create(SoundByNum.this,R.raw.si_note);
    }

    public void Play_sound(View view) {
        strOfeditText = editTextNum.getText().toString();
        switch (strOfeditText){
            case "1":
                mediaPlayer_do.start();
                break;
            case "2":
                mediaPlayer_re.start();
                break;
            case "3":
                mediaPlayer_mi.start();
                break;
            case "4":
                mediaPlayer_fa.start();
                break;
            case "5":
                mediaPlayer_sol.start();
                break;
            case "6":
                mediaPlayer_la.start();
                break;
            case "7":
                mediaPlayer_si.start();
                break;   
            default:
                Toast.makeText(this,"Incorrect input, Please enter a number between one and seven",Toast.LENGTH_LONG).show();
        }
    }

    public void next2(View view) {
        Intent toFinalActivity = new Intent(this,Sound_intensity.class);
        startActivity(toFinalActivity);
    }
}