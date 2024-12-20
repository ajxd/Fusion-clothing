package com.example.ajai_kamaraj_application2;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    private Cart cart;

    @Override
    public void onCreate() {
        super.onCreate();
        cart = new Cart();

        // Initialize Firebase Database reference
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("carts");

        // Retrieve cart data from Firebase and add them to cart
        mDatabase.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting cart data", task.getException());
            } else {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        cart.addToCart(product);
                    }
                }
            }
        });
    }

    public Cart getCart() {
        return cart;
    }
}
