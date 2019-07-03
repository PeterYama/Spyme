package com.example.spyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class selectionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);
    }
    public void triggerListeningScreen(View view){
        startActivity(new Intent(getApplicationContext(),listenScreen.class));
    }
    public void triggerRecordScreen(View view){
        startActivity(new Intent(getApplicationContext(),recordScreen.class));
    }
}
