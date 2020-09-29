package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class quiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e) {

        }
        setContentView(R.layout.activity_quiz);
    }
}