 package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {

    String url = "http://148.72.213.116:3000/api/contactUs/sendMail";
    Snackbar snackbar;
    RequestQueue queue;
    EditText feedbacktext;
    String name;
    Button submitfeedback;
    ProgressBar feedbackProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_feedback);
        feedbacktext = findViewById(R.id.feedbacktext);
        SharedPreferences sharedprefrence = getApplicationContext().getSharedPreferences("kisanUser",MODE_PRIVATE);
        name = sharedprefrence.getString("name","0");
        submitfeedback = findViewById(R.id.submitfeedback);
        feedbackProgressbar = findViewById(R.id.feedbackProgressbar);

        submitfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedbacktext.getText().toString().length()<10) {
                    Toast.makeText(feedback.this, "Please enter a valid feedback", Toast.LENGTH_LONG).show();
                }
                if(feedbacktext.getText().toString().length()>10) {
                    feedbackProgressbar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    submit();
                }
            }
        });
    }


    public void submit() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            feedbackProgressbar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Log.v("TAG",response);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(feedback.this);
                            builder1.setMessage("Thank you for your valuable feedback.");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
//                            JSONObject jsonObject = new JSONObject(response.trim());
//                            for (int i = 0; i < jsonObject.names().length(); i++) {
//                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("200")) {
//                                    Intent intent = new Intent(feedback.this,otpActivity.class);
//                                    String token = (String) jsonObject.get(jsonObject.names().getString(i+1));
//                                    Toast.makeText(feedback.this, "token--------------------"+token, Toast.LENGTH_LONG).show();
//                                    intent.putExtra("token",token);
//                                    startActivity(intent);
//                                }
//                            }
//                                Toast.makeText(registrationActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        loadingProgressBar.setVisibility(View.GONE);
                        snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        submit();
                                    }
                                });

                        snackbar.show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("name",name);
                params.put("email", "kamadgiri24365@gmail.com");
                params.put("subject", "KAMADGIRI KISAN MOBILE USER FEEDBACK");
                params.put("message", feedbacktext.getText().toString());

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