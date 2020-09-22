package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.kamadgirikisan.ui.login.quizLoginActivity;
import com.example.kamadgirikisan.ui.login.redempsignupActivity;
import com.example.kamadgirikisan.ui.login.redemptionLoginActivity;


public class MainActivity extends AppCompatActivity {
    ImageView quiz,redemption;
    Intent quizIntent, redempIntent;
    boolean doubleBackToExitPressedOnce = false;

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
                SharedPreferences settings = getApplicationContext().getSharedPreferences("userRedmp", MODE_PRIVATE);
                String userId = settings.getString("userId","0");
                String name = settings.getString("name","unknown");
                if(!userId.equals("0") && !name.equals("unknown")) {
//                    Toast.makeText(MainActivity.this, "entered"+userId+na, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, redmptionHome.class);
                    startActivity(intent);
                }
                else {
                    redempIntent = new Intent(MainActivity.this, redemptionLoginActivity.class);
                    startActivity(redempIntent);
                }
//                Toast.makeText(MainActivity.this, "redemption clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}