package com.example.gosh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.models.LanguageType;
import com.amplifyframework.predictions.models.TextFormatType;
import com.amplifyframework.predictions.result.IdentifyTextResult;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;


import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE=1000;
    private static final int PICK_IMAGE=1;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    // added this line for tourguide
    private static final String SHOWCASE_ID = "SingleUseCode";

    Button mButtonCapture;

    Button mReplay;
    Button mUpload;
    Button mRegister; //Record use
    public static String b=null;

    String a="hovid med";
    ImageView mImageView;

    Uri image_uri;
    Uri image_uri2;
    Bitmap bitmap=null;
    TextView dispText;
    AtomicReference<String> response;
    private final MediaPlayer mp = new MediaPlayer();


    private String showThere(String samp){
        //dispText.setText(samp);
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("t2"); //name of file
        PyObject obj = pyf.callAttr("test",samp);
        Log.i("PYTHON",obj.toString());

        Amplify.Predictions.convertTextToSpeech(
                obj.toString(),
                result -> playAudio(result.getAudioData()),
                error -> Log.e("MyAmplifyApp", "Conversion failed", error)
        );
        return obj.toString();
    }
    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPython();
        setContentView(R.layout.activity_main);

        // --------------------- COPY FROM HERE --------------------------------------------
        // Show Tutorial Button
        Button mShowTutor = findViewById(R.id.show_tutor_btn);


        // Reset Tutorial Button (Merged with Show Tutorial for now
        // Button mResetTutor = findViewById(R.id.reset_tutor_btn);

        mShowTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorSequence(0);
                //reset the one time use id
                MaterialShowcaseView.resetSingleUse(MainActivity.this, SHOWCASE_ID);
            }

        });

        // Reset Tutorial Button (now merged with Show Tutorial, but makes the show tutorial need to be clicked twice to work)
        /*
        mResetTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialShowcaseView.resetSingleUse(MainActivity.this, SHOWCASE_ID);
                Toast.makeText(MainActivity.this, "Sequence showcase has been resetted", Toast.LENGTH_SHORT).show();
            }
        });
*/

// --------------------------- UNTIL HERE IS TUTORIAL CODE ------------------------------------


        Amplify.Predictions.translateText(
                "Capture Image or Upload Image to read",
                LanguageType.ENGLISH,
                LanguageType.SPANISH,
                result -> Log.i("MyAmplifyApp", result.getTranslatedText()),
                error -> Log.e("MyAmplifyApp", "Translation failed", error)
        );

        Amplify.Predictions.convertTextToSpeech(
                "Capture Image or Upload Image to read",
                result -> playAudio(result.getAudioData()),
                error -> Log.e("MyAmplifyApp", "Conversion failed", error)
        );
        mImageView = findViewById(R.id.image_view);
        /*mDays = findViewById(R.id.getdays);

        mImageView = findViewById(R.id.image_view);

        setbutton = findViewById(R.id.okbut);

        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numDays = mDays.getText().toString();
                Log.i("DAYS",numDays);
                Python py3 = Python.getInstance();
                PyObject pyf3 = py3.getModule("t2"); //name of file
                PyObject obj3 = pyf3.callAttr("reminder",b,numDays);
                Log.i("REMINDER",obj3.toString());

            }
        });
        */
        mRegister = findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open2();
            }
        });
        mUpload = findViewById(R.id.upload_image_btn);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Python py2 = Python.getInstance();
                PyObject pyf2 = py2.getModule("t2"); //name of file
                PyObject obj2 = pyf2.callAttr("eat",b);
                Log.i("PYTHONEAT",obj2.toString());

                Amplify.Predictions.convertTextToSpeech(
                        "Medication consumption time has been recorded",
                        result -> playAudio(result.getAudioData()),
                        error -> Log.e("MyAmplifyApp", "Conversion failed", error)
                );
            }

        });
        mReplay = findViewById(R.id.replay_image_btn);
        mReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b != null){
                    Amplify.Predictions.convertTextToSpeech(
                            b,
                            result -> playAudio(result.getAudioData()),
                            error -> Log.e("MyAmplifyApp", "Conversion failed", error)
                    );
                }
                else{
                    Amplify.Predictions.convertTextToSpeech(
                            "Please capture image again",
                            result -> playAudio(result.getAudioData()),
                            error -> Log.e("MyAmplifyApp", "Conversion failed", error)
                    );
                }

            }
        });

        mButtonCapture = findViewById(R.id.capture_image_btn);



        mButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else {
                        openCamera();
                    }
                }
                else {
                    openCamera();

                }
            }




        });

    }

    public void open2() {
        Intent act2 = new Intent(this, MainActivity2.class);
        startActivity(act2);
    }


    private void openCamera() {
        ContentValues values =new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mImageView.setImageURI(image_uri);
            Log.i("MyHealth","HEREEEEEE");
            /*BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();*/

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                Log.i("MyHealth","wehereee");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null){

                Log.i("IMAGE", String.valueOf(bitmap.getClass()));
                //dispText = findViewById(R.id.textA);
                //response = detectText(bitmap);
                Toast.makeText(this,"Reading Image",Toast.LENGTH_SHORT).show();
                Amplify.Predictions.identify(
                        TextFormatType.PLAIN,
                        bitmap,
                        result -> {
                            IdentifyTextResult identifyResult = (IdentifyTextResult) result;
                            //dispText.setText(identifyResult.getFullText());
                            a=identifyResult.getFullText();
                            b=showThere(a);

                            //Toast.makeText(this,"GOOD",Toast.LENGTH_LONG).show();
                            Log.i("MyAmplifyApp", identifyResult.getFullText());
                            //dispText.setText(a);

                        },
                        error -> Log.e("MyAmplifyApp", "Identify text failed", error)
                );

                //detectText(bitmap);
                //dispText.setText((CharSequence) response);
            }
            //dispText.setText(a);
            //Toast.makeText(this,"Converting To Speech",Toast.LENGTH_SHORT).show();
            Amplify.Predictions.convertTextToSpeech(
                    "Extracting and converting data from image to speech",
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e("MyAmplifyApp", "Conversion failed", error)
            );

        }

    }



    private void initPython(){
        if(!Python.isStarted()) Python.start(new AndroidPlatform(this));
    }



    // From here down: Show and Reset Tutorial Functions
    private void showTutorSequence(int millis) {

        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(millis); //set the delay of each sequence using millis variable

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID); // create the material showcase sequence

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        }); // set the listener of the sequence order to know we are in which position

        sequence.setConfig(config); //set the showcase config to the sequence.

                sequence.addSequenceItem(mButtonCapture, "Press this to take a photo", "GOT IT"); // add view for the first sequence, in this case it is a button.

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mImageView)
                        .setDismissText("OK")
                        .setContentText("Photos you take are shown here")
                        .withRectangleShape(true)
                        .build()
        ); // add view for the second sequence, in this case it is a textview.

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mReplay)
                        .setDismissText("GOT IT")
                        .setContentText("You can replay the audio you just heard")
                        .withCircleShape()
                        .build()
        ); // add view for the third sequence, in this case it is a checkbox.
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mRegister)
                        .setDismissText("GOT IT")
                        .setContentText("View and edit your medicines list")
                        .withCircleShape()
                        .build()
        );

       // uncomment this sequence once the record button stops crashing

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mUpload)
                        .setDismissText("GOT IT")
                        .setContentText("Record your intake of medicine at the current time")
                        .withCircleShape()
                        .build()
        );

        sequence.start(); //start the sequence showcase

    }
}