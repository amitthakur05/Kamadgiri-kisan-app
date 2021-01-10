package com.example.kamadgirikisan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class productDataAdapter extends RecyclerView.Adapter<productDataAdapter.ViewHolder> {
    private ArrayList<productModel> products;
    private Context context;

    public productDataAdapter(Context context, ArrayList<productModel> products) {
        this.products = products;
        this.context = context;
    }


    @Override
    public productDataAdapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( productDataAdapter.ViewHolder viewHolder, int position) {
        Log.v("PICASSSO LOG",products.get(position).getImageUrl());
        Picasso.get().load(products.get(position).getImageUrl()).into(viewHolder.product_image);
        viewHolder.product_name.setText(products.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView product_name;
        private ImageView product_image;
        public ViewHolder(View view) {
            super(view);

            product_name = view.findViewById(R.id.product_name);
            product_image =  view.findViewById(R.id.product_image);

        }
    }
}
