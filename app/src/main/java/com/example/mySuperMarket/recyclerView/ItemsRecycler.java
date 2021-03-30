package com.example.mySuperMarket.recyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mySuperMarket.R;
import com.example.mySuperMarket.Utils;
import com.example.mySuperMarket.objects.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ItemsRecycler extends  RecyclerView.Adapter<ItemsRecycler.ViewHolder>{


    private ArrayList<Item> items=new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userName;

    public ItemsRecycler(ArrayList<Item> items,String userName)
    {
        this.items=items;
        this.userName=userName;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);

        return new ItemsRecycler.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.price.setText("₪"+items.get(position).getPrice()+"");
        holder.desc.setText(""+items.get(position).getDesc());
        Glide.with(holder.itemView.getContext()).load(items.get(position).getImage()).into(holder.image);
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(Utils.USERS_COLLECTION).document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String>ids= (ArrayList<String>) task.getResult().get("ids");
                        ids.add(items.get(position).getId());
                        db.collection(Utils.USERS_COLLECTION).document(userName).update("ids",ids);

                    }
                });
            }
        });

        /*holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.collection(Utils.USERS_COLLECTION).document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ArrayList<String>ids= (ArrayList<String>) task.getResult().get("ids");
                            ids.add(items.get(position).getId());
                            db.collection(Utils.USERS_COLLECTION).document(userName).update("ids",ids);

                        }
                    });
                }else{
                    db.collection(Utils.USERS_COLLECTION).document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ArrayList<String>ids= (ArrayList<String>) task.getResult().get("ids");
                            ids.remove(items.get(position).getId());
                            db.collection(Utils.USERS_COLLECTION).document(userName).update("ids",ids);

                        }
                    });
                }
            }
        });*/


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
        public Button addToCart;



        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            desc=itemView.findViewById(R.id.desc);
            price=itemView.findViewById(R.id.price_of_item);
            name=itemView.findViewById(R.id.name_of_item);
            image=itemView.findViewById(R.id.product_image);
            addToCart=itemView.findViewById(R.id.add_to_cart);


        }
    }
}
