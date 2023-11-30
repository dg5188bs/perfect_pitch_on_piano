package com.example.perfect_pitch_on_piano;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sound_intensity extends AppCompatActivity {
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private ExecutorService executorService;
    private AudioRecord audioRecord;
    private ProgressBar intensityProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_intensity);
        intensityProgressBar = findViewById(R.id.intensityProgressBar);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeAudioRecord();
                startRecording();
            } else {
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeAudioRecord() {
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT,
                AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT));
    }

    private void startRecording() {
        executorService.execute(() -> {
            short[] buffer = new short[AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)];
            audioRecord.startRecording();
            while (!isDestroyed()) {
                int readSize = audioRecord.read(buffer, 0, buffer.length);
                if (readSize > 0) {
                    double intensity = calculateSoundIntensity(buffer, readSize);
                    updateUI(intensity);
                }
            }
        });
    }

    private double calculateSoundIntensity(short[] buffer, int readSize) {
        double sum = 0;
        for (int i = 0; i < readSize; i++) {
            sum += Math.abs(buffer[i]);
        }
        double averageAmplitude = sum / readSize;
        return averageAmplitude;
    }

    private void updateUI(double soundIntensity) {
        runOnUiThread(() -> {
            int normalizedIntensity = (int) (soundIntensity * 100.0 / Short.MAX_VALUE);
            intensityProgressBar.setProgress(normalizedIntensity);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }

        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void micIntensity(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            initializeAudioRecord();
            startRecording();
        }
    }
}