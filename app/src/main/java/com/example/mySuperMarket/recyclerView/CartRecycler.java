package com.example.mySuperMarket.recyclerView;



import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mySuperMarket.R;

import com.example.mySuperMarket.objects.Item;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CartRecycler extends  RecyclerView.Adapter<CartRecycler.ViewHolder>{


    private ArrayList<Item> items=new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userName;

    public CartRecycler(ArrayList<Item> items,String userName)
    {
        this.items=items;
        this.userName=userName;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout,parent,false);

        return new CartRecycler.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.price.setText("₪"+items.get(position).getPrice());
        Glide.with(holder.itemView.getContext()).load(items.get(position).getImage()).into(holder.image);
        holder.desc.setText(items.get(position).getCartDesc());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;
        public ImageView image;
        public TextView desc;



        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            desc=itemView.findViewById(R.id.desc_cart);
            price=itemView.findViewById(R.id.price);
            name=itemView.findViewById(R.id.name_of_product);
            image=itemView.findViewById(R.id.product_image_cart);


        }
    }
}
