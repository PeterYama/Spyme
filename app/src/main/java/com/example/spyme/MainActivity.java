package com.example.spyme;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Intent triggerSelectionScreen;
    Intent triggerMainScreen;
    private String EMAIL;
    LoginButton loginButton;
    static private int REQUEST_PERMISSION_CODE;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Toast.makeText(MainActivity.this,"User Logged In",Toast.LENGTH_LONG).show();
        }else{
            signInAnonymously();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        REQUEST_PERMISSION_CODE  = 1000;
        EMAIL  = "email";
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        final String EMAIL = "email";

        callbackManager = CallbackManager.Factory.create();

        triggerSelectionScreen = new Intent(getApplicationContext(),selectionScreen.class);
        triggerMainScreen = new Intent(getApplicationContext(),MainActivity.class);
        checkPermissionFromDevice();
        requestPermissions();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
    }

    private void signInAnonymously(){
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override public void onSuccess(AuthResult authResult) {
                    authResult.getUser();

            }
        }) .addOnFailureListener(this, new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "signInAnonymously:FAILURE", exception);
            }
        });
    }


    public void triggerSelectionScreen(View view){

        startActivity(triggerSelectionScreen);

    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(this,new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {

        int write_external_storage_result = ContextCompat.checkSelfPermission
                (this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission
                (this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch(requestCode){
            case 1000:
            {
                if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


}
