package com.example.gosh;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity2 extends AppCompatActivity {

    EditText mDays;
    Button setbutton;
    String numDays;
    private final MediaPlayer mp2 = new MediaPlayer();
    private void playAudio2(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp2.reset();
            mp2.setOnPreparedListener(MediaPlayer::start);
            mp2.setDataSource(new FileInputStream(mp3File).getFD());
            mp2.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDays = findViewById(R.id.getdays);

        setbutton = findViewById(R.id.okbut);

        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numDays = mDays.getText().toString();
                setbutton.setBackgroundColor(Color.parseColor("#008080"));
                Log.i("DAYS",numDays);
                Python py3 = Python.getInstance();
                PyObject pyf3 = py3.getModule("t2"); //name of file
                PyObject obj3 = pyf3.callAttr("reminder",MainActivity.b,numDays);
                Log.i("REMINDER",obj3.toString());
                Amplify.Predictions.convertTextToSpeech(
                        "Frequency has been set",
                        result -> playAudio2(result.getAudioData()),
                        error -> Log.e("MyAmplifyApp", "Conversion failed", error)
                );

            }
        });
    }
}