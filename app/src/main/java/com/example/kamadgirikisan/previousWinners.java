package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kamadgirikisan.ui.login.quizLoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class previousWinners extends AppCompatActivity {
    String url = "http://148.72.213.116:3000/api/quiz/lastQuizeWinners";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){ }
        setContentView(R.layout.activity_previous_winners);
        getPreviousWinners();
    }

   public void getPreviousWinners() {
       StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONObject jsonObject = new JSONObject(response.trim());
                           JSONArray winnersArray = jsonObject.getJSONArray("winner");
                               TableLayout prices = (TableLayout) findViewById(R.id.winners);
                               if (winnersArray.length() < 1) {
                                   AlertDialog.Builder builder1 = new AlertDialog.Builder(previousWinners.this);
                                   builder1.setMessage("No record found");
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
                                   return;
                               }
                               TextView h1 = new TextView(previousWinners.this);
                               h1.setText("किसान कोड ");
                               h1.setTextColor(Color.BLACK);
                               TextView h2 = new TextView(previousWinners.this);
                               h2.setText(" किसान का नाम ");
                               h2.setTextColor(Color.BLACK);
                               TextView h3 = new TextView(previousWinners.this);
                               h3.setText(" गाँव का नाम");
                               h3.setTextColor(Color.BLACK);
                               h1.setTextSize(16);
                               h1.setHeight(70);
                               h2.setTextSize(16);
                               h2.setHeight(70);
                               h3.setTextSize(16);
                               h3.setHeight(70);
                               TableRow th1 = new TableRow(previousWinners.this);
                               th1.addView(h1);
                               th1.addView(h2);
                               th1.addView(h3);
                               prices.addView(th1);
                               prices.setStretchAllColumns(true);
                           for (int i = 0; i < winnersArray.length(); i++) {
                               if(!winnersArray.getJSONObject(i).getString("user").equals("null")) {
                                   JSONObject object = winnersArray.getJSONObject(i).getJSONObject("user");
                                   String name = object.getString("name");
                                   String dealerCode = object.getString("dealerCode");
                                   String villageName = object.getString("address");
                                   TableRow tr = new TableRow(previousWinners.this);
                                   TextView c1 = new TextView(previousWinners.this);
                                   TextView c2 = new TextView(previousWinners.this);
                                   TextView c3 = new TextView(previousWinners.this);
                                   c2.setTextColor(Color.WHITE);
                                   c2.setTextSize(14);
                                   c2.setHeight(80);
                                   c1.setTextColor(Color.WHITE);
                                   c1.setTextSize(14);
                                   c1.setHeight(80);
                                   c3.setTextColor(Color.WHITE);
                                   c3.setTextSize(14);
                                   c3.setHeight(80);
                                   c3.setText(dealerCode);
                                   c1.setText(name);
                                   c2.setText(villageName);
                                   tr.addView(c3);
                                   tr.addView(c1);
                                   tr.addView(c2);
                                   prices.setStretchAllColumns(true);
                                   prices.addView(tr);
                               }
                               Toast.makeText(previousWinners.this, "if not executed"+i, Toast.LENGTH_SHORT).show();
                           }
                       }
                        catch (Exception e) {
                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(previousWinners.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
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