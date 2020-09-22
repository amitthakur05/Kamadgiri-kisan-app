package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.DrawableMarginSpan;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class labelHistory extends AppCompatActivity {
    String url = "http://148.72.213.116:3000/api/redimption/get/serialNo";
    RequestQueue queue;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_label_history);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("userRedmp", MODE_PRIVATE);
        userId = settings.getString("userId","0");
        getUserHistory();
    }

   public void getUserHistory() {
//       Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
       StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
//                           Toast.makeText(labelHistory.this, "data" + response, Toast.LENGTH_LONG).show();

                           JSONObject jsonObject = new JSONObject(response.trim());

                           JSONArray items = jsonObject.getJSONArray("items");

//                           Toast.makeText(labelHistory.this, "data" + items.length(), Toast.LENGTH_LONG).show();
                           TableLayout prices = (TableLayout)findViewById(R.id.labelNo);
                           if(items.length()<1) {
                               TextView data = new TextView(labelHistory.this);
                               data.setText(" No record found ");
                               data.setTextColor(Color.rgb(255,69,0));
//                           h2.setBackgroundColor(Color.WHITE);
                               data.setTextSize(18);
                               data.setHeight(80);
                               TableRow th3 =  new TableRow(labelHistory.this);
                               th3.addView(data);
//                           th1.setBackgroundColor(Color.YELLOW);
                               prices.addView(th3);
                               prices.setPadding(140,100,10,10);
                               return;
                           }
                           TextView h1 = new TextView(labelHistory.this);
                           h1.setText(" Label No. ");
                           h1.setTextColor(Color.BLACK);
//                           h1.setBackgroundColor(Color.WHITE);
                           TextView h2 = new TextView(labelHistory.this);
                           h2.setText(" Status ");
                           h2.setTextColor(Color.BLACK);
                           TextView h3 = new TextView(labelHistory.this);
                           h3.setText(" S.no. ");
                           h3.setTextColor(Color.BLACK);
//                           h2.setBackgroundColor(Color.WHITE);
                           h1.setTextSize(16);
                           h1.setHeight(80);
                           h2.setTextSize(16);
                           h2.setHeight(80);
                           h3.setTextSize(16);
                           h3.setHeight(80);
                           TableRow th1 =  new TableRow(labelHistory.this);

                           th1.addView(h3);
                           th1.addView(h1);
                           th1.addView(h2);
//                           th1.setBackgroundColor(Color.YELLOW);
                           prices.addView(th1);
                           prices.setStretchAllColumns(true);
//                           prices.
//                           prices.bringToFront();
//                           tbrow0.addView(tv0);
                           for(int i=0; i<items.length(); i++) {
                               JSONObject object = items.getJSONObject(i);
//                               if(jsonObject.names().getString(i).equals("items")) {
                               String status = object.getString("status");
                               String serialNo = object.getString("serialNo");
                               TableRow tr =  new TableRow(labelHistory.this);
                               TextView c1 = new TextView(labelHistory.this);
                               TextView c2 = new TextView(labelHistory.this);
                               TextView c3 = new TextView(labelHistory.this);

                               c2.setTextColor(Color.WHITE);
                               c2.setTextSize(16);
                               c2.setHeight(80);
                               c1.setTextColor(Color.WHITE);
                               c1.setTextSize(16);
                               c1.setHeight(80);
                               c3.setTextColor(Color.WHITE);
                               c3.setTextSize(16);
                               c3.setHeight(80);
                               c3.setText(""+i);
                               c1.setText(serialNo);
                               c2.setText(status);
//                               tr.setBackgroundColor(Color.CYAN);
                               tr.addView(c3);
                               tr.addView(c1);
                               tr.addView(c2);

//                               tr.setTextDirection(View.TEXT_ALIGNMENT_CENTER);
                               prices.addView(tr);

                               Toast.makeText(labelHistory.this, "data"+status+serialNo , Toast.LENGTH_LONG).show();

//                                   JSONArray jsonArray = new JSONArray(jsonObject.get(jsonObject.names().getString(i)));

//                                   Toast.makeText(labelHistory.this, "data" +jsonArray.length(), Toast.LENGTH_LONG).show();

//                                   jsonObject(jsonObject.names().getString(i))
//                               }
                           }
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(labelHistory.this, "Something went wrong" , Toast.LENGTH_SHORT).show();
                   }
               })
       {

           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new LinkedHashMap<>();
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

}