package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kamadgirikisan.ui.login.quizLoginActivity;
import com.example.kamadgirikisan.ui.login.redempsignupActivity;
import com.example.kamadgirikisan.ui.login.redemptionLoginActivity;

import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ImageView quiz,redemption;
    Intent quizIntent, redempIntent;
    boolean doubleBackToExitPressedOnce = false;
    String url = "http://148.72.213.116:3000/api/ads/publish/all/";
    String[] ImagesArray ;
    RequestQueue queue;
    String id = "5f2ae7227239338311707460";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
        getAllAdvertisements();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SharedPreferences sharedprefrence = getApplicationContext().getSharedPreferences("kisanUser",MODE_PRIVATE);
                String userId = sharedprefrence.getString("userId","0");
                if(!userId.equals("0")) {
                    Intent intent = new Intent(MainActivity.this, homescreen.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    quizIntent = new Intent(MainActivity.this, loginActivity.class);
                    startActivity(quizIntent);
                    finish();

                }
            }
        }, 4000);


//        quiz = findViewById(R.id.quiz);
//        redemption = findViewById(R.id.redemption);
//
//
//        quiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences quizsharedprefrence = getApplicationContext().getSharedPreferences("quizuser",MODE_PRIVATE);
//                String userId = quizsharedprefrence.getString("userId","0");
//                if(!userId.equals("0")) {
//                    Intent intent = new Intent(MainActivity.this, quizHome.class);
//                    startActivity(intent);
//                }
//                else {
//                    quizIntent = new Intent(MainActivity.this, quizLoginActivity.class);
//                    startActivity(quizIntent);
//                }
//
////                Toast.makeText(MainActivity.this, "quiz clicked", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        redemption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences settings = getApplicationContext().getSharedPreferences("userRedmp", MODE_PRIVATE);
//                String userId = settings.getString("userId","0");
//                String name = settings.getString("name","unknown");
//                if(!userId.equals("0") && !name.equals("unknown")) {
////                    Toast.makeText(MainActivity.this, "entered"+userId+na, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, redmptionHome.class);
//                    startActivity(intent);
//                }
//                else {
//                    redempIntent = new Intent(MainActivity.this, redemptionLoginActivity.class);
//                    startActivity(redempIntent);
//                }
////                Toast.makeText(MainActivity.this, "redemption clicked", Toast.LENGTH_LONG).show();
//            }
//        });
    }

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }



    public void getAllAdvertisements() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

//                            response = response.replace("[","");
//                            response = response.replace("]","");
//                            ImagesArray = response.split(",");
                            sharedPreferences = getSharedPreferences("ads", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("adsArray", response);
                            editor.apply();
//                            Toast.makeText(MainActivity.this, "response" + ImagesArray.length+ImagesArray[0], Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("id", id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        queue.add(stringRequest);
        stringRequest.setTag("TAG");
    }


}