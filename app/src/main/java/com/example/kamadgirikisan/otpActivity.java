package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class otpActivity extends AppCompatActivity {
    String url = "http://148.72.213.116:3000/api/kisan/user/verifyregister";
    String authToken;
    EditText otp_EditText;
    TextView time_left;
    Button submitOtp;
    Snackbar snackbar;
    RequestQueue queue;
    Boolean isLoginActivity;
    String userId,name;
    private SharedPreferences mPreferences;
    String mobileNumber,userLocation,pinoCode;
    HashMap<String, String> params = new HashMap<String, String>();
    String login_url = "http://148.72.213.116:3000/api/kisan/user/login";
    String register_url = "http://148.72.213.116:3000/api/kisan/user/register";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        time_left = findViewById(R.id.timeleft);
        Bundle extras= getIntent().getExtras();
        if (extras != null) {
            authToken = "Bearer "+extras.getString("token");
            mobileNumber = extras.getString("mobileNumber");
            name = extras.getString("name");
            if(extras.getBoolean("loginFlag")) {
                isLoginActivity = true;
                userId = extras.getString("userId");

                Log.v("TAG",userId);
            }
            else {
                userLocation = extras.getString("location");
                pinoCode = extras.getString("pinCode");
                isLoginActivity = false;
            }
            getTimeout();

//            Toast.makeText(this, "token"+authToken, Toast.LENGTH_SHORT).show();
        }

        otp_EditText = findViewById(R.id.otp);
        submitOtp = findViewById(R.id.submitOtp);

//        time_left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        time_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoginActivity) {
                    Log.v("TAG","Inside login otp");
                    getLoginOtp();
                    getTimeout();
                }
                if(!isLoginActivity) {
                    Log.v("TAG","Inside register otp");
                    getRegisterOtp();
                    getTimeout();
                }
               Log.v("TAG","Clicked--------");
            }
        });

        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp_EditText.getText().toString().length() <5){
                    Toast.makeText(otpActivity.this, "Please enter a valid OTP", Toast.LENGTH_LONG).show();
                }
                if(otp_EditText.getText().toString().trim().equals("")){
                    Toast.makeText(otpActivity.this, "Please enter the OTP", Toast.LENGTH_LONG).show();
                }
                if(otp_EditText.getText().toString().length() >=5 && !otp_EditText.getText().toString().trim().equals("")) {
                    submit();
                }
            }
        });


    }

    public void getTimeout() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                time_left.setClickable(false);
                time_left.setEnabled(false);
                time_left.setVisibility(View.VISIBLE);
                time_left.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                time_left.setClickable(true);
                time_left.setEnabled(true);
                time_left.setText("Resend");

            }

        }.start();
    }

    public void submit() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
                            for (int i = 0; i < jsonObject.names().length(); i++) {
                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("200")) {
                                    if(isLoginActivity) {
                                        mPreferences = getSharedPreferences("kisanUser", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = mPreferences.edit();
                                        editor.putString("userId", userId);
                                        editor.putString("name",name);
                                        editor.apply();
                                        Toast.makeText(otpActivity.this,
                                                "OTP verification Successful",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(otpActivity.this,homescreen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (!isLoginActivity) {
                                       String newUserId = jsonObject.getJSONObject("user").getJSONObject("user").getString("_id");
                                        String newUserName = jsonObject.getJSONObject("user").getJSONObject("user").getString("firstName");
                                        mPreferences = getSharedPreferences("kisanUser", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = mPreferences.edit();
                                        editor.putString("userId", newUserId);
                                        editor.putString("name",newUserName);
                                        editor.apply();
                                        Toast.makeText(otpActivity.this,"OTP verification Successful",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(otpActivity.this,homescreen.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("401")) {
                                    Toast.makeText(otpActivity.this,"Incorrect otp",Toast.LENGTH_LONG).show();
                                }
                            }
                            Log.v("TAG",response);
//                                Toast.makeText(otpActivity.this, "response++++++++++"+response, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.v("TAG",e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("TAG",error.toString());
                        Toast.makeText(otpActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();
//                        snackbar = Snackbar
//                                .make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Retry", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        submit();
//                                    }
//                                });
//
//                        snackbar.show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new LinkedHashMap<>();
//                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", authToken);
                Log.v("TAG",headers.toString());
//                Toast.makeText(otpActivity.this, "headers"+headers, Toast.LENGTH_LONG).show();
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();

                params.put("otp", otp_EditText.getText().toString());
                Log.v("TAG",params.toString());
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


    public void getLoginOtp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
//                            Toast.makeText(loginActivity.this, "response"+jsonObject.names().length()+"   "+jsonObject.get(jsonObject.names().getString(0)).toString(), Toast.LENGTH_LONG).show();
                            for (int i = 0; i < jsonObject.names().length(); i++) {
                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("200")) {
                                    Log.v("TAG",jsonObject.getString("token"));
                                    authToken= "Bearer "+jsonObject.getString("token");
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
                        snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getLoginOtp();
                                    }
                                });

                        snackbar.show();
                    }
                }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("mobileNumber", mobileNumber);
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

    public void getRegisterOtp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
                            for (int i = 0; i < jsonObject.names().length(); i++) {
                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("200")) {
                                    authToken= "Bearer "+jsonObject.getString("token");
                                }
                            }
//                                Toast.makeText(registrationActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getRegisterOtp();
                                    }
                                });

                        snackbar.show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("firstName",name);
                params.put("mobileNumber", mobileNumber);
                params.put("location", userLocation);
                params.put("pinCode", pinoCode);

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