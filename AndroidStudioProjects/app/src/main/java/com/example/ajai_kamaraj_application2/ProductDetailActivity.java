package com.example.ajai_kamaraj_application2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private Product currentProduct;
    private DatabaseReference mDatabase;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView productImageView = findViewById(R.id.productDetailImage);
        TextView productNameTextView = findViewById(R.id.productDetailName);
        TextView productDescriptionTextView = findViewById(R.id.productDetailDescription);
        TextView additionalDetailsTextView = findViewById(R.id.additionalDetails);
        TextView productPriceTextView = findViewById(R.id.productPrice);
        Button buttonAddToCart = findViewById(R.id.addToCartButton);
        ImageButton cartButton = findViewById(R.id.cartButton);
        TextView quantityView = findViewById(R.id.quantityView);
        Button increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        Button decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("carts");

        // Increase quantity button click listener
        increaseQuantityButton.setOnClickListener(v -> {
            quantity++;
            quantityView.setText(String.valueOf(quantity));
        });

        // Decrease quantity button click listener
        decreaseQuantityButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityView.setText(String.valueOf(quantity));
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            String productName = intent.getStringExtra("productName");
            String productDescription = intent.getStringExtra("productDescription");
            String productImage = intent.getStringExtra("productDirectImageUrl");
            String additionalDetails = intent.getStringExtra("additionalDetails");
            double productPrice = getIntent().getDoubleExtra("productPrice", 0.0);

            productNameTextView.setText(productName);
            productDescriptionTextView.setText(productDescription);
            Glide.with(this).load(productImage).into(productImageView);
            additionalDetailsTextView.setText(additionalDetails);
            productPriceTextView.setText("$" + productPrice);

            currentProduct = new Product(productName, productDescription, productImage, additionalDetails, productPrice);

            buttonAddToCart.setOnClickListener(view -> {
                String key = mDatabase.push().getKey();
                if (key != null) {
                    currentProduct.setKey(key);
                    currentProduct.setQuantity(quantity);
                    currentProduct.setDirectImageUrl(productImage);
                    mDatabase.child(key).setValue(currentProduct);
                    Toast.makeText(ProductDetailActivity.this,
                            currentProduct.getName() + " added to cart!",
                            Toast.LENGTH_SHORT).show();

                    Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    cartIntent.putExtra("product", currentProduct); // Pass the product object to CartActivity
                    startActivity(cartIntent);
                }
            });
        } else {
            Toast.makeText(ProductDetailActivity.this, "No product data found", Toast.LENGTH_SHORT).show();
        }

        cartButton.setOnClickListener(v -> {
            Intent intentCart = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intentCart);
        });
    }
}
