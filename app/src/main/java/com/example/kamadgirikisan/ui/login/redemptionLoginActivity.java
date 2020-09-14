package com.example.kamadgirikisan.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
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
import com.example.kamadgirikisan.ui.login.LoginViewModel;
import com.example.kamadgirikisan.ui.login.LoginViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

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
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    kvcCode.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });
//
//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
////                        passwordEditText.getText().toString());
//            }
//        };
////        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        kvcCode.addTextChangedListener(afterTextChangedListener);
//        kvcCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
////                    loginViewModel.login(usernameEditText.getText().toString(),
////                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

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
//                Toast.makeText(redemptionLoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }

    public void login() {
//        Toast.makeText(redempsignupActivity.this, "called!!!!!!!!", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //you will get your response in log
//                        Toast.makeText(redemptionLoginActivity.this, "Thank you for your post1" + response.toString(), Toast.LENGTH_SHORT).show();
                        loadingProgressBar.setVisibility(View.GONE);

//                        Iterator<String> iter = json.keys();
//                        while (iter.hasNext()) {
//                            String key = iter.next();
//                            try {
//                                Object value = json.get(key);
//                            } catch (JSONException e) {
//                                // Something went wrong!
//                            }
//                        }
//                        if (user.equals("") || pass.equals("")) {
//
//                            Toast.makeText(getApplicationContext(), "username or password is empty", Toast.LENGTH_LONG).show();
//                        } else if (!response.equals("empty")) {
//                            Log.e("isempty", "yes");
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());

                            for (int i = 0; i < jsonObject.names().length(); i++) {
////                                Snackbar snackbar = Snackbar
////                                        .make(parentLayout, "" + jsonObject.names().length() + "    " + jsonObject.get(jsonObject.names().getString(i)), Snackbar.LENGTH_LONG);
////                                snackbar.show();
//
//
//                                Toast.makeText(redemptionLoginActivity.this, "key = " + jsonObject.names().getString(i) + " value = " + jsonObject.get(jsonObject.names().getString(i)), Toast.LENGTH_SHORT).show();

//                            if(jsonObject.names().getString(i)=="[userDetails]") {
//                                Toast.makeText(redemptionLoginActivity.this, "true", Toast.LENGTH_SHORT).show();
//                            }
//                            else {
                                snackbar = Snackbar
                                        .make(findViewById(android.R.id.content), "succesful" + jsonObject, Snackbar.LENGTH_LONG)
                                        .setAction("Okay", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

//                                                Intent intent = new Intent(redemptionLoginActivity.this, MainActivity.class);
//                                                startActivity(intent);
//                                                finish();

//                                                Snackbar mSnackbar = Snackbar.make(mainLayout, "Message successfully deleted.", Snackbar.LENGTH_SHORT);
//                                                mSnackbar.show();
                                            }
                                        });
                                View snackbarView = snackbar.getView();
                                TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                snackTextView.setMaxLines(7);
                                snackbar.show();
//                            }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();

                        }
//
//                            try {
//                                Toast.makeText(redempsignupActivity.this, "new array"+response, Toast.LENGTH_SHORT).show();
//                                JSONArray array = new JSONArray(response);
//                                Toast.makeText(redempsignupActivity.this, "new array"+array, Toast.LENGTH_SHORT).show();
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONArray array1 = array.getJSONObject(i).getJSONArray("user");
//                                    for (int j = 0; j < array1.length(); j++) {
//
//
////                                        startActivity(intent);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            Log.e("isempty", "else");
//                            Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
//                        }
//

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

//                                                Snackbar mSnackbar = Snackbar.make(mainLayout, "Message successfully deleted.", Snackbar.LENGTH_SHORT);
//                                                mSnackbar.show();
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