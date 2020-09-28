package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class quizHome extends AppCompatActivity {
    TextView textView;
    String userId;
    Button previouWinner,playQuiz;
    RequestQueue queue;
    String getQuizUrl = "http://148.72.213.116:3000/api/quiz/";
    String getAnnouncementUrl = "http://148.72.213.116:3000/api/admin/announce/get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_quiz_home);
        textView= findViewById(R.id.marq);
        textView.setSelected(true);
        previouWinner = findViewById(R.id.prevoiuswinner);
        getActiveQuiz();
        getAnnouncement();
        previouWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quizHome.this, previousWinners.class);
                startActivity(intent);
            }
        });
        SharedPreferences quizsharedprefrence = getApplicationContext().getSharedPreferences("quizuser",MODE_PRIVATE);
        userId = quizsharedprefrence.getString("userId","0");
    }

    public void getActiveQuiz() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getQuizUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray quiz = new JSONArray(response.trim());
//                            Toast.makeText(quizHome.this, "respo"+response, Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(quizHome.this, "Something went wrong..", Toast.LENGTH_SHORT);
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        queue.add(stringRequest);
        stringRequest.setTag("TAG");
    }

    public void getAnnouncement() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAnnouncementUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(quizHome.this, "ressss"+response, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     Toast.makeText(quizHome.this, "Something went wrong..",Toast.LENGTH_SHORT).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        queue.add(stringRequest);
        stringRequest.setTag("TAG");
    }

}