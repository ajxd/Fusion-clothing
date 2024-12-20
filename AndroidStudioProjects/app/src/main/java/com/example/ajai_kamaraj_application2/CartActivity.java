package com.example.ajai_kamaraj_application2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Cart cart;
    private RecyclerView cartRecyclerView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mDatabase = FirebaseDatabase.getInstance().getReference("carts");

        cart = ((MyApplication) getApplication()).getCart();

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null) {
            Product product = (Product) intent.getSerializableExtra("product");
            if (product != null) {
                cart.addToCart(product);
            }
        }

        Button proceedToCheckoutButton = findViewById(R.id.proceedToCheckoutButton);
        proceedToCheckoutButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(CartActivity.this,  PaymentOptionActivity.class);
            startActivity(intent1);
        });

        updateUI();
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        List<Product> products = cart.getProducts();
        CartAdapter adapter = new CartAdapter(products);
        cartRecyclerView.setAdapter(adapter);

        double totalPrice = 0;
        for (Product product : products) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText("Total Price: $" + totalPrice);
    }

    private class CartHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDescription;
        TextView quantityView;
        ImageView productImage;
        TextView productPrice;
        Button removeButton;
        Button increaseQuantityButton;
        Button decreaseQuantityButton;
        Product currentProduct;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            quantityView = itemView.findViewById(R.id.quantityView);
            removeButton = itemView.findViewById(R.id.removeButton);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);

            increaseQuantityButton.setOnClickListener(v -> {
                if (currentProduct != null && currentProduct.getKey() != null) {
                    currentProduct.setQuantity(currentProduct.getQuantity() + 1);
                    mDatabase.child(currentProduct.getKey()).setValue(currentProduct);
                    quantityView.setText(String.valueOf(currentProduct.getQuantity()));
                } else {
                    Log.d("IncreaseQuantity", "currentProduct or currentProduct key is null");
                }
            });

            decreaseQuantityButton.setOnClickListener(v -> {
                if (currentProduct != null && currentProduct.getKey() != null) {
                    if (currentProduct.getQuantity() > 1) {
                        currentProduct.setQuantity(currentProduct.getQuantity() - 1);
                        mDatabase.child(currentProduct.getKey()).setValue(currentProduct);
                        quantityView.setText(String.valueOf(currentProduct.getQuantity()));
                    }
                } else {
                    Log.d("DecreaseQuantity", "currentProduct or currentProduct key is null");
                }
            });

            removeButton.setOnClickListener(v -> {
                if (currentProduct != null && currentProduct.getKey() != null) {
                    mDatabase.child(currentProduct.getKey()).removeValue();
                    cart.getProducts().remove(currentProduct);
                    updateUI();
                } else {
                    Log.d("RemoveItem", "currentProduct or currentProduct key is null");
                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(Product product) {
            currentProduct = product;
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText("$" + product.getPrice());
            quantityView.setText(String.valueOf(product.getQuantity()));
            if (product.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(product.getImageUrl())
                        .into(productImage);
            } else {
                Log.d("GlideError", "ImageUrl is null for product: " + product.getName());
            }
        }
    }

    private class CartAdapter extends RecyclerView.Adapter<CartHolder> {
        private final List<Product> products;

        public CartAdapter(List<Product> products) {
            this.products = products;
        }

        @NonNull
        @Override
        public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CartActivity.this);
            View view = layoutInflater.inflate(R.layout.cart_item_layout, parent, false);
            return new CartHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartHolder holder, int position) {
            Product product = products.get(position);
            holder.bind(product);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }
}