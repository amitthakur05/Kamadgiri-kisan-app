package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class products extends AppCompatActivity  {
    String url = "http://148.72.213.116:3000/api/products/find/";
    RequestQueue queue;
    Snackbar snackbar;
    ProgressBar productsProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_products);
        productsProgressbar= findViewById(R.id.productsProgressbar);
        getAllProducts();
    }


    public  void getAllProducts() {
        productsProgressbar.setVisibility(View.VISIBLE);
        final ArrayList<productModel> productsModel = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.trim());
//                            Toast.makeText(products.this, "response"+jsonArray+jsonArray.length(), Toast.LENGTH_LONG).show();
                            for(int i=0; i<jsonArray.length(); i++) {
                                productModel products = new productModel();
                                Log.v("TEST PTOOOOOOO",jsonArray.getJSONObject(i).getString("name"));
                                products.setName(jsonArray.getJSONObject(i).getString("name"));
                                products.setImageUrl(url+jsonArray.getJSONObject(i).getString("name"));
                                productsModel.add(products);
                                Log.v("PRODUCTS+++++++++++++++",products.toString());

                            }
                            initViews(productsModel);

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
                                        getAllProducts();
                                    }
                                });

                        snackbar.show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        queue.add(stringRequest);
        stringRequest.setTag("TAG");

    }


    private void initViews(ArrayList<productModel> productsModel){
//        Log.v("TAG","called");
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

//        ArrayList<productModel> productsModel = getAllProducts();
//        Toast.makeText(this, "res/++++++++"+productsModel.size(), Toast.LENGTH_SHORT).show();

        productDataAdapter adapter = new productDataAdapter(getApplicationContext(),productsModel);
        recyclerView.setAdapter(adapter);
        productsProgressbar.setVisibility(View.INVISIBLE);

    }
}