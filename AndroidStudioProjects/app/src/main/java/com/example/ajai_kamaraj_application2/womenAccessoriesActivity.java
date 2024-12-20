package com.example.ajai_kamaraj_application2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class womenAccessoriesActivity extends AppCompatActivity {
    RecyclerView productRecyclerView;
    FirestoreRecyclerAdapter<Product, ProductHolder> adapter;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_womens_accessories);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("products").document("WomenAccessories").collection("Items");


        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new ProductAdapter(options);
        productRecyclerView.setAdapter(adapter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Save RecyclerView state
        recyclerViewState = Objects.requireNonNull(productRecyclerView.getLayoutManager()).onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerViewState != null) {
            // Restore RecyclerView state
            Objects.requireNonNull(productRecyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDescription;
        ImageView productImage;
        Product currentProduct;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(womenAccessoriesActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", currentProduct.getName());
                intent.putExtra("productDescription", currentProduct.getDescription());
                intent.putExtra("productDirectImageUrl", currentProduct.getDirectImageUrl());
                intent.putExtra("additionalDetails", currentProduct.getAdditionalDetails());
                intent.putExtra("productPrice", currentProduct.getPrice());
                startActivity(intent);
            });
        }

        public void bind(Product product) {
            currentProduct = product;
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());

            String imageUrl = product.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);

                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(getApplicationContext()).load(uri).into(productImage);
                    currentProduct.setDirectImageUrl(uri.toString());
                }).addOnFailureListener(exception -> Log.e("ProductHolder", "Failed to download image.", exception));
            } else {
                Toast.makeText(getApplicationContext(), "Image URL is empty or null for " + product.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductHolder> {

        public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
            super(options);
        }

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
            return new ProductHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Product model) {
            holder.bind(model);
        }
    }
}
