package com.example.kamadgirikisan.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.kamadgirikisan.MainActivity;
import com.example.kamadgirikisan.R;
import com.example.kamadgirikisan.redmptionHome;
import com.example.kamadgirikisan.ui.login.LoginViewModel;
import com.example.kamadgirikisan.ui.login.LoginViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class redemptionLoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    ProgressBar loadingProgressBar;
    HashMap<String, String> params = new HashMap<String, String>();
    String url = "http://148.72.213.116:3000/api/user/kkk/login";
    RequestQueue queue;
    View Layout;
    Snackbar snackbar;
    EditText mobileNumber,kvcCode;
    Button loginButton;
    private SharedPreferences mPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_redemption_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

         mobileNumber = findViewById(R.id.editTextPhone2);
         kvcCode = findViewById(R.id.password);
         loginButton= findViewById(R.id.button2);
         loadingProgressBar = findViewById(R.id.loading);
        final TextView register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(redemptionLoginActivity.this,redempsignupActivity.class);
                startActivity(intent);
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                }
                if (loginFormState.getPasswordError() != null) {
                    kvcCode.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileNumber.getText().toString().length() <10){
                    mobileNumber.setError("Please enter valid phone number");
                    mobileNumber.requestFocus();
                }
                if(mobileNumber.getText().toString().trim().equals("")){
                    mobileNumber.setError("Please enter phone number");
                    mobileNumber.requestFocus();
                }
                if(kvcCode.getText().toString().trim().equals("")){
                    kvcCode.setError("Please enter kisan sahayak code");
                    kvcCode.requestFocus();
                }
                if(kvcCode.getText().toString().length()<5){
                    kvcCode.setError("Please enter valid kisan sahayak code");
                    kvcCode.requestFocus();
                }
                if(mobileNumber.getText().toString().length() ==10 && !mobileNumber.getText().toString().trim().equals("") &&
                        !kvcCode.getText().toString().trim().equals("") && kvcCode.getText().toString().length() == 5) {
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

                            for (int i = 0; i < jsonObject.names().length(); i++) {
                            if(jsonObject.get(jsonObject.names().getString(i)).equals("200")) {
                                    String details = jsonObject.get(jsonObject.names().getString(i+1)).toString().replace("[","");
                                    details = details.replace("]","");
                                JSONObject userDetails = new JSONObject(details);
                                try {
                                    for (int j = 0; j < userDetails.names().length(); j++) {
                                        if (userDetails.names().getString(j).equals("_id")) {
                                            mPreferences = getSharedPreferences("userRedmp", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = mPreferences.edit();
                                            editor.putString("userId", userDetails.get(userDetails.names().getString(j)).toString());
                                            editor.apply();

                                        }
                                        if (userDetails.names().getString(j).equals("name")) {
                                            SharedPreferences.Editor editor = mPreferences.edit();
                                            editor.putString("name", userDetails.get(userDetails.names().getString(j)).toString());
                                            editor.apply();
                                            Toast.makeText(redemptionLoginActivity.this, "Login Successful" + userDetails.get(userDetails.names().getString(j)).toString(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(redemptionLoginActivity.this, redmptionHome.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    Toast.makeText(redemptionLoginActivity.this, "Something went wrong..", Toast.LENGTH_LONG).show();

                                }


                            }
                            else if(jsonObject.get(jsonObject.names().getString(i)).equals("401")) {
                                snackbar = Snackbar
                                        .make(findViewById(android.R.id.content), ""+jsonObject.get(jsonObject.names().getString(i+1)), Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Okay", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });
                                View snackbarView = snackbar.getView();
                                TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                snackTextView.setMaxLines(7);
                                snackbar.show();
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
//                        Toast.makeText(redemptionLoginActivity.this, "error"+error, Toast.LENGTH_SHORT).show();
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
                params.put("kkCode", kvcCode.getText().toString());
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