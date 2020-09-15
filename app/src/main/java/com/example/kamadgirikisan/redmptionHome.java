package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class redmptionHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_redmption_home);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userRedmp", MODE_PRIVATE);
        String userId = settings.getString("userId","0");
        String name = settings.getString("name","unknown");
        Toast.makeText(this, "userId    "+userId+""+name, Toast.LENGTH_SHORT).show();
    }
}