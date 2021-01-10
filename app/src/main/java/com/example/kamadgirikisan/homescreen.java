package com.example.kamadgirikisan;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kamadgirikisan.ui.login.redemptionLoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

public class homescreen extends AppCompatActivity {
    RequestQueue queue;
    String id = "5f2ae7227239338311707460";
    String[] ImagesArray;
    Intent  redempIntent;
    String adsUrl = "http://148.72.213.116:3000/api/ads/files/findAll/";
    ImageView products,redemption,feedback,history;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fa = this;
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_homescreen);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ads", MODE_PRIVATE);
        String response = sharedPreferences.getString("adsArray","0");
        response = response.replace("[","");
        response = response.replace("]","");
        response = response.replace("\"","");
        ImagesArray = response.split(",");
//        Toast.makeText(this,// "array"+ImagesArray[0]+"arr data", Toast.LENGTH_SHORT).show();
        final CarouselView carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(ImagesArray.length);
        carouselView.setImageListener(imageListener);

        products = findViewById(R.id.products);
        redemption =findViewById(R.id.redemption);
        feedback = findViewById(R.id.feedback);
        history = findViewById(R.id.history);


        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(homescreen.this, products.class);
                startActivity(intent);
            }
        });

        redemption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("userRedmp", MODE_PRIVATE);
                String userId = settings.getString("userId","0");
                String name = settings.getString("name","unknown");
                Log.v("TAG",userId);
                Log.v("TAG",name);

                    Intent intent = new Intent(homescreen.this, redmptionHome.class);
                    startActivity(intent);

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homescreen.this,feedback.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homescreen.this,labelHistory.class);
                startActivity(intent);


            }
        });

    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(adsUrl+ImagesArray[position]).into(imageView);
        }
    };


}