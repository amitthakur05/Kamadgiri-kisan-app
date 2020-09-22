package com.example.kamadgirikisan;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.kamadgirikisan.ui.login.redemptionLoginActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class redmptionHome extends AppCompatActivity {
TextView userName;
EditText labelNo;
Snackbar snackbar;
Button submit,previousLabelNo,logout;
String userId;
RequestQueue queue;
ProgressBar progressBar;
String url = "http://148.72.213.116:3000/api/redimption/addSerialNo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_redmption_home);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userRedmp", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","0");
        String name = sharedPreferences.getString("name","unknown");
        userName = findViewById(R.id.textView16);
        labelNo = findViewById(R.id.editTextTextPersonName2);
        submit = findViewById(R.id.button4);
        userName.setText(name.toUpperCase());
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        previousLabelNo = findViewById(R.id.button3);
        logout = findViewById(R.id.logout);

        previousLabelNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(redmptionHome.this,labelHistory.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(labelNo.getText().toString().length()<6) {
                    labelNo.setError("please enter a valid label number");
                    labelNo.requestFocus();
                }
                 if (labelNo.getText().toString().trim().equals("")) {
                    labelNo.setError("Enter the label number");
                    labelNo.requestFocus();
                }
                if(labelNo.getText().toString().length()>5 && !labelNo.getText().toString().trim().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    submit();
//                    Toast.makeText(redmptionHome.this, "label no"+labelNo.getText().toString()+"       "+userId, Toast.LENGTH_SHORT).show();
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().remove("userId").commit();
                sharedPreferences.edit().remove("name").commit();
                finish();
//                Intent intent = new Intent(redmptionHome.this,redemptionLoginActivity.class);
//                startActivity(intent);
            }
        });



    }
    public void submit() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());

                            for (int i = 0; i < jsonObject.names().length(); i++) {
//                                Toast.makeText(redmptionHome.this, ""+response, Toast.LENGTH_SHORT).show();
                                if(jsonObject.names().getString(i).equals("msg")) {
                                    snackbar = Snackbar.make(findViewById(android.R.id.content), ""+jsonObject.get(jsonObject.names().getString(i)), Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Okay", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    labelNo.setText("");
                                                }
                                            });
                                    snackbar.show();
                                    View snackbarView = snackbar.getView();
                                    TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                    snackTextView.setMaxLines(7);
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
//                        loadingProgressBar.setVisibility(View.GONE);
//                        Toast.makeText(redemptionLoginActivity.this, "error"+error, Toast.LENGTH_SHORT).show();
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
                params.put("serialNo", labelNo.getText().toString());
                params.put("user", userId);
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
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(redmptionHome.this,MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}