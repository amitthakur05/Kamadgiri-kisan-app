package com.example.kamadgirikisan.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kamadgirikisan.MainActivity;
import com.example.kamadgirikisan.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class redempsignupActivity extends AppCompatActivity {
    EditText phone, name, fatherName, village, postOffice, ksc;
    Button signup;
    RequestQueue queue;
    View parentLayout;
    Snackbar snackbar;
    HashMap<String, String> params = new HashMap<String, String>();
    String url = "http://148.72.213.116:3000/api/user/kkk/register";
//    Map<String, Object> params = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_redempsignup);

        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        fatherName = findViewById(R.id.fatherName);
        village = findViewById(R.id.village);
        postOffice = findViewById(R.id.postOffice);
        ksc = findViewById(R.id.ksc);
        signup = findViewById(R.id.button);
        parentLayout = findViewById(android.R.id.content);
//        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().toString().length() <10){
                    phone.setError("Please enter valid phone number");
                    phone.requestFocus();
                }
                if(phone.getText().toString().trim().equals("")){
                    phone.setError("Please enter phone number");
                    phone.requestFocus();
                }

                if(name.getText().toString().trim().equals("")){
                    name.setError("Please enter your name");
                    name.requestFocus();
                }
                if(fatherName.getText().toString().trim().equals("")){
                    fatherName.setError("Please enter father's name");
                    fatherName.requestFocus();
                }

                if(village.getText().toString().trim().equals("")){
                    village.setError("Please enter village name");
                    village.requestFocus();
                }

                if(postOffice.getText().toString().trim().equals("")){
                    postOffice.setError("Please enter post office name");
                    postOffice.requestFocus();
                }

                if(ksc.getText().toString().trim().equals("")){
                    ksc.setError("Please enter kisan sahayak code");
                    ksc.requestFocus();
                }
                if(ksc.getText().toString().length()<4){
                    ksc.setError("Please enter valid kisan sahayak code");
                    ksc.requestFocus();
                }
                if(phone.getText().toString().length() ==10 && !phone.getText().toString().trim().equals("") && !name.getText().toString().trim().equals("")
                && !fatherName.getText().toString().trim().equals("") && !village.getText().toString().trim().equals("") && !postOffice.getText().toString().trim().equals("")
                && !ksc.getText().toString().trim().equals("") && ksc.getText().toString().length() == 4) {
                    signUp();
                }
            }
        });
    }

    public void signUp() {
//        Toast.makeText(redempsignupActivity.this, "called!!!!!!!!", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //you will get your response in log
//                        Toast.makeText(redempsignupActivity.this, "Thank you for your post1" + response.toString(), Toast.LENGTH_SHORT).show();

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
//                                Snackbar snackbar = Snackbar
//                                        .make(parentLayout, "" + jsonObject.names().length() + "    " + jsonObject.get(jsonObject.names().getString(i)), Snackbar.LENGTH_LONG);
//                                snackbar.show();


//                                Toast.makeText(redempsignupActivity.this, "key = " + jsonObject.names().getString(i) + " value = " + jsonObject.get(jsonObject.names().getString(i)), Toast.LENGTH_SHORT).show();

                                         snackbar = Snackbar
                                        .make(parentLayout, "" + jsonObject.get(jsonObject.names().getString(i)), Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Okay", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent intent = new Intent(redempsignupActivity.this, MainActivity .class);
                                                startActivity(intent);
                                                finish();

//                                                Snackbar mSnackbar = Snackbar.make(mainLayout, "Message successfully deleted.", Snackbar.LENGTH_SHORT);
//                                                mSnackbar.show();
                                            }
                                        });
                                View snackbarView = snackbar.getView();
                                TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                                snackTextView.setMaxLines(7);

                                snackbar.show();

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
                        snackbar = Snackbar
                                .make(parentLayout, "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                      signUp();

//                                                Snackbar mSnackbar = Snackbar.make(mainLayout, "Message successfully deleted.", Snackbar.LENGTH_SHORT);
//                                                mSnackbar.show();
                                    }
                                });

                        snackbar.show();
                    }
                }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("mobileNumber", phone.getText().toString());
                params.put("name", name.getText().toString());
                params.put("fatherName", fatherName.getText().toString());
                params.put("address", village.getText().toString());
                params.put("postOffice", postOffice.getText().toString());
                params.put("dealerCode", ksc.getText().toString());
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


