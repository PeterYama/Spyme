package com.example.spyme;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;

public class listenScreen extends Activity {

    private ListView listView;
    private String[] mobileArray = {"First Audio","Second Audio","Third Audio","Forth Audio",
            "Fifth Audio","...","...","..."};
    private StorageReference mReference = null;
    private String downloadLinkString = null;
    FirebaseAuth mAuth;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_screen);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseStorage.getInstance().getReference();
        listView = (ListView) findViewById(R.id.mobile_list);
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listen_screen, R.id.label,mobileArray);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case(0):
                        startListening();
                        break;
                    case(1):
                        break;
                }

            }
        });

        if (mAuth == null)
        {
            settingAudioSource();

        }else {
            signInAnonymously();
        }

    }

    private void startListening() {

        final MediaPlayer mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(downloadLinkString);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Toast.makeText(listenScreen.this,"Playing",Toast.LENGTH_LONG).show();
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(listenScreen.this,"Ended",Toast.LENGTH_LONG).show();
                    mp.stop();
                    mp.release();
                }

            });
            mMediaPlayer.prepare();
        } catch (IOException e) {
            String message = e.toString();
            Toast.makeText(listenScreen.this, message, Toast.LENGTH_SHORT).show();
        }
        

    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                settingAudioSource();
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(listenScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void settingAudioSource(){


            mReference.child("Audio").child("user_recording.3gp").getDownloadUrl().continueWithTask(new Continuation<Uri, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<Uri> task) throws Exception {
                    if(task.isSuccessful()){
                        Toast.makeText(listenScreen.this, "Wait for completion", Toast.LENGTH_SHORT).show();
                    }
                    return task;
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> link) {
                    downloadLinkString = link.getResult().toString();
                    Toast.makeText(listenScreen.this,"Completed",Toast.LENGTH_LONG).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(listenScreen.this,"Failed"+message,Toast.LENGTH_LONG).show();
                }
            });
    }
}