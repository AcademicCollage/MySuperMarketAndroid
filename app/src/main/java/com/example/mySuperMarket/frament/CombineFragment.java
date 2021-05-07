package com.example.mySuperMarket.frament;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mySuperMarket.NavigationActivity;
import com.example.mySuperMarket.R;
import com.example.mySuperMarket.Utils;
import com.example.mySuperMarket.objects.Item;
import com.example.mySuperMarket.objects.User;
import com.example.mySuperMarket.recyclerView.CartRecycler;
import com.example.mySuperMarket.recyclerView.ItemsRecycler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.mySuperMarket.Utils.CART_TYPE;
import static com.example.mySuperMarket.Utils.ITEMS;
import static com.example.mySuperMarket.Utils.ITEMS_TYPE;


public class CombineFragment extends Fragment {
    private float totalSumOfItems=0;
    private int totalItems=0;
    private ArrayList<Item> cartItemsArrayList=new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPreferences;
    private ArrayList<Item> itemsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences=getContext().getSharedPreferences(Utils.USER_DATA,Context.MODE_PRIVATE);


        return inflater.inflate(R.layout.fragment_combine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loadItems();
        searchItem();
    }
    private void loadCartItems() {
        db.collection(Utils.USERS_COLLECTION).document(sharedPreferences.getString(Utils.USER_NAME,"")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user =  task.getResult().toObject(User.class);
                for (Item item:itemsArrayList) {
                    for (String id:user.getIds()) {
                        if(item.getId().equals(id)){
                            totalItems+=1;
                            totalSumOfItems+=item.getPrice();
                            cartItemsArrayList.add(item);
                            break;
                        }
                    }
                }

                 if (getArguments().get("type").equals(CART_TYPE)) {
                    initRecyclerView(cartItemsArrayList);
                }

            }
        });
    }

    private void loadItems() {
        db.collection(ITEMS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                itemsArrayList = (ArrayList<Item>) task.getResult().toObjects(Item.class);
                loadCartItems();
                if (getArguments().get("type").equals(CART_TYPE)) {
                    initRecyclerView(cartItemsArrayList);

                } else if (getArguments().get("type").equals(ITEMS_TYPE)) {
                    initRecyclerView(itemsArrayList);
                }

            }
        });
    }

    private void initRecyclerView(ArrayList<Item> cartItemsArrayList) {
        RecyclerView recyclerView = getView().findViewById(R.id.items_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(Utils.USER_DATA, Context.MODE_PRIVATE);


        if (getArguments().get("type").equals(CART_TYPE)) {
            initData();
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

            CartRecycler adapter=new CartRecycler(cartItemsArrayList,sharedPreferences.getString(Utils.USER_NAME,""));
            recyclerView.setAdapter(adapter);
        } else if (getArguments().get("type").equals(ITEMS_TYPE)) {
            ItemsRecycler adapter=new ItemsRecycler(cartItemsArrayList,sharedPreferences.getString(Utils.USER_NAME,""));
            recyclerView.setAdapter(adapter);

        }
    }
    private void initData() {
        LinearLayout cardView=getView().findViewById(R.id.linearLayout3);
        View divider4=getView().findViewById(R.id.divider4);

        if (getArguments().get("type").equals(CART_TYPE)) {
            cardView.setVisibility(View.VISIBLE);
            divider4.setVisibility(View.VISIBLE);
            TextView totalItems=getView().findViewById(R.id.total_items),totalSumOfItems=getView().findViewById(R.id.total_payment);
            totalItems.setText(" "+this.totalItems);
            totalSumOfItems.setText("₪"+this.totalSumOfItems);
        } else if (getArguments().get("type").equals(ITEMS_TYPE)) {
            cardView.setVisibility(View.GONE);
            divider4.setVisibility(View.GONE);
        }


    }

    private void searchItem() {
        EditText editText = getView().findViewById(R.id.search_item);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Item> textToCheckList = new ArrayList<>();
                String textToCheck = s.toString();
                if (textToCheck.length() != 0) {
                    //code for making First letter of string CAPITAL
                    textToCheck = textToCheck.substring(0, 1).toUpperCase() + textToCheck.substring(1);


                    //code for filling second l ist from backup list based on text to search here in this case, it is "textToCheck"
                    for (Item searchModel : getArguments().get("type").equals(ITEMS_TYPE)?itemsArrayList:cartItemsArrayList) {
                        if (searchModel.getName().contains(textToCheck)) {
                            textToCheckList.add(searchModel);
                        }
                    }
                } else {
                    textToCheckList.addAll(getArguments().get("type").equals(ITEMS_TYPE)?itemsArrayList:cartItemsArrayList);
                }

                // Setting new list to adapter and notifying it
                initRecyclerView(textToCheckList);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}