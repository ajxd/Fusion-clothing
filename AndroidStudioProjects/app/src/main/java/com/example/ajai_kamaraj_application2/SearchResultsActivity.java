package com.example.ajai_kamaraj_application2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchResultsActivity extends Activity {
    RecyclerView productRecyclerView;
    FirestoreRecyclerAdapter<Product, ProductHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mens_accessories);

        String searchQuery = getIntent().getStringExtra("searchQuery");

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        setUpAdapter(searchQuery);
    }

    private void setUpAdapter(String searchQuery) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // build the query
        Query query = db.collection("products").document("MenAccessories").collection("Items").orderBy("name").startAt(searchQuery).endAt(searchQuery + "\uf8ff");

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Product, ProductHolder>(options) {
            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item_layout, parent, false);
                return new ProductHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Product model) {
                holder.bind(model);
            }
        };

        adapter.setHasStableIds(true); // Hint that IDs are stable
        productRecyclerView.setAdapter(adapter);
    }

    private class ProductHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        ImageView productImageView;
        Product currentProduct;


        ProductHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.productName);
            descriptionTextView = itemView.findViewById(R.id.productDescription);
            productImageView = itemView.findViewById(R.id.productImage);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(SearchResultsActivity.this, ProductDetailActivity.class);
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
            nameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());

            String imageUrl = product.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(itemView.getContext()).load(uri).into(productImageView);
                    currentProduct.setDirectImageUrl(uri.toString());
                }).addOnFailureListener(exception -> Log.e("SearchResultsActivity", "Failed to download image.", exception));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
            adapter = null;
            productRecyclerView.setAdapter(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter == null) {
            String searchQuery = getIntent().getStringExtra("searchQuery");
            setUpAdapter(searchQuery);
        }
    }
}
