package com.example.spyme;

import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.*;
import java.io.File;
import java.io.IOException;

public class recordScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private MediaRecorder myMediaRecorder;
    private String myAudioFile;
    private StorageReference mStorageReference;
    private StorageReference ref;
    private Uri firstUri;
    private FirebaseAuth.AuthStateListener mAuthListener;
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_screen);
        myAudioFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/user_recording.3gp";
        mStorageReference = FirebaseStorage.getInstance().getReference();
        ref = mStorageReference.child("Audio").child("user_recording.3gp");
        mAuth =FirebaseAuth.getInstance();

        f = new File(myAudioFile);
        firstUri = Uri.fromFile(f);
        setupMediaRecorder();

        try{
            myMediaRecorder.prepare();
            Toast.makeText(this,"Recording...",Toast.LENGTH_SHORT);

        }catch (IOException w){
            w.printStackTrace();
            Toast.makeText(this, "Couldn't start recording",Toast.LENGTH_SHORT);
        }
        myMediaRecorder.start();
    }

    private void setupMediaRecorder(){

        myMediaRecorder = new MediaRecorder();
        myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myMediaRecorder.setOutputFile(myAudioFile);
    }
    public void triggerStopRecording(View view){

        myMediaRecorder.stop();
        myMediaRecorder.release();
        myMediaRecorder = null;
        finish();
        uploadTask();
    }
    private void signInAnonymously(){
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override public void onSuccess(AuthResult authResult) {
                Log.e("TAG", "signInAnonymously:FAILURE");
                uploadFile();
            }
        }) .addOnFailureListener(this, new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "signInAnonymously:FAILURE", exception);
            }
        });
    }

    protected void uploadTask(){
        if(mAuth.getCurrentUser()!=null){
            uploadFile();
        }else{
            signInAnonymously();
        }

    }
    protected void uploadFile(){


        ref.putFile(firstUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(recordScreen.this,"Success",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(recordScreen.this,"Failed",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(recordScreen.this,"Uploading",Toast.LENGTH_LONG).show();
                    }
                });
    }

}
