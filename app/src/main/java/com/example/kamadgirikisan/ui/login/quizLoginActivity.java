package com.example.kamadgirikisan.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.example.kamadgirikisan.R;
import com.example.kamadgirikisan.quizHome;
import com.example.kamadgirikisan.ui.login.LoginViewModel;
import com.example.kamadgirikisan.ui.login.LoginViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class quizLoginActivity extends AppCompatActivity {
    ProgressBar loadingProgressBar;
    Button loginButton;
    EditText mobileNumber, ksc, name, villageName;
    String url ="http://148.72.213.116:3000/api/user/register";
    Snackbar snackbar;
    RequestQueue queue;
    SharedPreferences quizsharedPreferences;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_quiz_login);
        mobileNumber = findViewById(R.id.editTextPhone);
        ksc = findViewById(R.id.password);
        name = findViewById(R.id.editTextTextPersonName);
        villageName = findViewById(R.id.editTextTextPostalAddress);
        loginButton = findViewById(R.id.logbtn);
        loadingProgressBar = findViewById(R.id.loading);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(quizLoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                if(mobileNumber.getText().toString().trim().equals("")) {
                    mobileNumber.setError("please enter mobile number");
                    mobileNumber.requestFocus();
                }
                if(mobileNumber.getText().toString().length()<10) {
                    mobileNumber.setError("please enter valid mobile number");
                }
                if(ksc.getText().toString().trim().equals("")) {
                    ksc.setError("please enter kisan sahayak code");
                    ksc.requestFocus();
                }
                if(ksc.getText().toString().length()<4) {
                    ksc.setError("please enter valid kisan sahayak code");
                    ksc.requestFocus();
                }
                if(name.getText().toString().trim().equals("")) {
                    name.setError("please enter the farmer name");
                    ksc.requestFocus();
                }
                if(villageName.getText().toString().trim().equals("")) {
                    villageName.setError("please enter the village name");
                    villageName.requestFocus();
                }
                if(!mobileNumber.getText().toString().trim().equals("") && mobileNumber.getText().toString().length()==10 && !ksc.getText().toString().trim().equals("")
                        && ksc.getText().toString().length()==4 && !name.getText().toString().trim().equals("") && !villageName.getText().toString().trim().equals("")) {
//                    Toast.makeText(quizLoginActivity.this, "true", Toast.LENGTH_SHORT).show();
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
                        try {
                            loadingProgressBar.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(response.trim());
                            for(int i=0; i<jsonObject.length(); i++) {

                                if(jsonObject.length()==1 && jsonObject.names().getString(i).equals("msg")) {

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(quizLoginActivity.this);
                                    builder1.setMessage(""+jsonObject.get(jsonObject.names().getString(0)));
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                                if(jsonObject.length()>1) {
                                    if(jsonObject.names().getString(i).equals("_id")) {
//                                        Toast.makeText(quizLoginActivity.this, "" + jsonObject.get(jsonObject.names().getString(i)).toString(), Toast.LENGTH_SHORT).show();
                                        quizsharedPreferences = getSharedPreferences("quizuser", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = quizsharedPreferences.edit();
                                        editor.putString("userId", jsonObject.get(jsonObject.names().getString(i)).toString());
                                        editor.apply();

                                        Toast.makeText(quizLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(quizLoginActivity.this, quizHome.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
//                            Toast.makeText(quizLoginActivity.this, "data" + jsonObject.length(), Toast.LENGTH_LONG).show();
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
                params.put("mobileNumber", mobileNumber.getText().toString());
                params.put("dealerCode", ksc.getText().toString());
                params.put("name", name.getText().toString());
                params.put("address",villageName.getText().toString());
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