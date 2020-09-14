package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.kamadgirikisan.ui.login.quizLoginActivity;
import com.example.kamadgirikisan.ui.login.redempsignupActivity;
import com.example.kamadgirikisan.ui.login.redemptionLoginActivity;


public class MainActivity extends AppCompatActivity {
    ImageView quiz,redemption;
    Intent quizIntent, redempIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
        quiz = findViewById(R.id.quiz);
        redemption = findViewById(R.id.redemption);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizIntent = new Intent(MainActivity.this, quizLoginActivity.class);
                startActivity(quizIntent);
//                Toast.makeText(MainActivity.this, "quiz clicked", Toast.LENGTH_LONG).show();
            }
        });

        redemption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redempIntent = new Intent(MainActivity.this, redemptionLoginActivity.class );
                startActivity(redempIntent);
//                Toast.makeText(MainActivity.this, "redemption clicked", Toast.LENGTH_LONG).show();
            }
        });
    }


}