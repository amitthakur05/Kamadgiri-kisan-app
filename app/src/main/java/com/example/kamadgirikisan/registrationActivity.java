package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class registrationActivity extends AppCompatActivity implements LocationListener{

    Button Submit;
    EditText Name,pincode,userLocation;
    LocationManager locationManager;
    ProgressBar loadingProgressBar;
    HashMap<String, String> params = new HashMap<String, String>();
    String url = "http://148.72.213.116:3000/api/kisan/user/register";
    RequestQueue queue;
    Snackbar snackbar;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_registration);

        Bundle extras= getIntent().getExtras();
        if (extras != null) {
            mobileNumber = extras.getString("mobileNumber");
        }
//        Toast.makeText(this, ""+mobileNumber, Toast.LENGTH_SHORT).show();
        Submit = findViewById(R.id.registerSubmit);
        userLocation = findViewById(R.id.location);
        Name = findViewById(R.id.fullname);
        pincode = findViewById(R.id.pincode);
        loadingProgressBar = findViewById(R.id.registerprogressbar);

        if(isGpsEnabled()==false) {
            displayPromptForEnablingGPS(registrationActivity.this);
            if(isGpsEnabled()==true) {
                showProgressBar();
                getLocation();
            }
        }

        if(isGpsEnabled()==true) {
           showProgressBar();
            getLocation();
        }

        if(ContextCompat.checkSelfPermission(registrationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(registrationActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Name.getText().toString().equals("")) {
                    Toast.makeText(registrationActivity.this, "Please enter the user name", Toast.LENGTH_SHORT).show();
                }
                if(userLocation.getText().toString().equals("")) {
                    Toast.makeText(registrationActivity.this, "Please enter the user location", Toast.LENGTH_SHORT).show();
                }
                if(pincode.getText().toString().equals("")) {
                    Toast.makeText(registrationActivity.this, "Please enter the pin code", Toast.LENGTH_SHORT).show();
                }
                if(Name.getText().toString().length()<3) {
                    Toast.makeText(registrationActivity.this, "Please enter a valid user name", Toast.LENGTH_SHORT).show();
                }
                if(userLocation.getText().toString().length()<5) {
                    Toast.makeText(registrationActivity.this, "Please enter a valid location", Toast.LENGTH_SHORT).show();
                }
                if(pincode.getText().toString().length()<5) {
                    Toast.makeText(registrationActivity.this, "Please enter  a valid pin code", Toast.LENGTH_SHORT).show();
                }
                if(!Name.getText().toString().equals("") && !userLocation.getText().toString().equals("") &&
                        !pincode.getText().toString().equals("") && Name.getText().toString().length()>3 && userLocation.getText().toString().length()>4
                  && pincode.getText().toString().length()>4) {
                    register();
                }

            }
        });
    }


    public void register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.trim());
                            for (int i = 0; i < jsonObject.names().length(); i++) {
                                if(jsonObject.get(jsonObject.names().getString(i)).toString().equals("200")) {
                                    Intent intent = new Intent(registrationActivity.this,otpActivity.class);
                                    String token = (String) jsonObject.get(jsonObject.names().getString(i+1));
                                    intent.putExtra("token",token);
                                    intent.putExtra("firstName", Name.getText().toString());
                                    intent.putExtra("mobileNumber", mobileNumber);
                                    intent.putExtra("location", userLocation.getText().toString());
                                    intent.putExtra("pinCode", pincode.getText().toString());
                                    intent.putExtra("loginFlag",false);
                                    startActivity(intent);
                                    finish();
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
                        loadingProgressBar.setVisibility(View.GONE);
                        snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        register();
                                    }
                                });

                        snackbar.show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new LinkedHashMap<>();
                params.put("firstName", Name.getText().toString());
                params.put("mobileNumber", mobileNumber);
                params.put("location", userLocation.getText().toString());
                params.put("pinCode", pincode.getText().toString());

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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        Toast.makeText(this, "result call", Toast.LENGTH_SHORT).show();
        if(resultCode ==0){
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(provider != null && !provider.isEmpty()){
                showProgressBar();
                getLocation();
            }else{

            }
        }
    }


   public void showProgressBar() {
       loadingProgressBar.setVisibility(View.VISIBLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
               WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private boolean isGpsEnabled()
    {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER)&&service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public static void displayPromptForEnablingGPS(final Activity activity)
    {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "GPS is disabled in your device. Please enable GPS to use services";

        builder.setMessage(message)
                .setTitle( "Kamadgiri Kisan" )
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivityForResult(new Intent(action),0);
                                d.dismiss();


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
//        Toast.makeText(this, "get location called", Toast.LENGTH_SHORT).show();
        try {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

//                    Toast.makeText(registrationActivity.this, "Enter the location manually", Toast.LENGTH_SHORT).show();
                    return;
                }
            }, 5000);
//            Toast.makeText(this, "get location called", Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 5, (LocationListener) registrationActivity.this);


        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, ""+location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();

        try {

            Geocoder geocoder = new Geocoder(registrationActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            userLocation.setText(address);
            loadingProgressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {

    }


}