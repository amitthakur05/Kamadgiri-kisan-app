package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.example.kamadgirikisan.ui.login.LoginViewModel;
import com.example.kamadgirikisan.ui.login.redemptionLoginActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {
    ProgressBar loadingProgressBar;
    HashMap<String, String> params = new HashMap<String, String>();
    String url = "http://148.72.213.116:3000/api/kisan/user/login";
    RequestQueue queue;
    Snackbar snackbar;
    EditText mobileNumber;
    Button submit;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){ }
        setContentView(R.layout.activity_login);
        submit = findViewById(R.id.submit);
        mobileNumber = findViewById(R.id.mobileNumber);
        loadingProgressBar = findViewById(R.id.progressBar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileNumber.getText().toString().length() <10){
                    Toast.makeText(loginActivity.this, "Please enter a valid phone number", Toast.LENGTH_LONG).show();
                }
                if(mobileNumber.getText().toString().trim().equals("")){
                    Toast.makeText(loginActivity.this, "Please enter phone number", Toast.LENGTH_LONG).show();
                }
                if(mobileNumber.getText().toString().length() ==10 && !mobileNumber.getText().toString().trim().equals("")) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    login();
                }
            }
        });
    }


    public void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingProgressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
//                            Toast.makeText(loginActivity.this, "response"+jsonObject.names().length()+"   "+jsonObject.get(jsonObject.names().getString(0)).toString(), Toast.LENGTH_LONG).show();

                            for (int i = 0; i < jsonObject.names().length(); i++) {
                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("200")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                                    for(int j=0; j<jsonArray.length(); j++) {
                                        if(jsonArray.length()>0) {
                                          String userId  = jsonArray.getJSONObject(j).getString("_id");
                                          String name = jsonArray.getJSONObject(j).getString("firstName");
//                                            Log.v("TAG",userId);
                                            Intent intent = new Intent(loginActivity.this,otpActivity.class);
                                            String token = (String) jsonObject.get(jsonObject.names().getString(i+1));
//                                            Toast.makeText(loginActivity.this, "token--------------------"+token, Toast.LENGTH_LONG).show();
                                            intent.putExtra("token",token);
                                            intent.putExtra("mobileNumber",mobileNumber.getText().toString());
                                            intent.putExtra("loginFlag",true);
                                            intent.putExtra("userId",userId);
                                            intent.putExtra("name",name);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }


                                }

                                 if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("401")) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(loginActivity.this);
                                    builder1.setMessage("Farmer Do not exist with entered mobile number. Do you want to register now")
                                            .setTitle( "Kamadgiri Kisan" );
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(loginActivity.this,registrationActivity.class);
                                                    intent.putExtra("mobileNumber",mobileNumber.getText().toString());
                                                    startActivity(intent);
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });
                                builder1.setNegativeButton(
                                        "NO",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingProgressBar.setVisibility(View.GONE);
                        snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        login();
                                    }
                                });

                        snackbar.show();
                    }
                }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("mobileNumber", mobileNumber.getText().toString());
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