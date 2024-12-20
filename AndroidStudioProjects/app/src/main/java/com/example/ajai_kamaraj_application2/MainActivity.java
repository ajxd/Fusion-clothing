package com.example.ajai_kamaraj_application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the Firebase Analytics instance.
        FirebaseAnalytics.getInstance(this);

        Button browseCollection = findViewById(R.id.browse_collection);
        browseCollection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MensAccessoriesActivity.class);
            startActivity(intent);
        });

        Button browseCollections = findViewById(R.id.browse_collections);
        browseCollections.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, womenAccessoriesActivity.class);
            startActivity(intent);
        });
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform the search
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("searchQuery", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        // Adding cart button click listener
        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Adding logout button click listener
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Call the method
        writeMessageToFirebase();
    }

    public void writeMessageToFirebase() {
        // Get an instance of the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the "message" location
        DatabaseReference myRef;
        myRef = database.getReference("message");

        // Write a message
        myRef.setValue("Hello, World!");

        // Create a Cloud Storage reference from the app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference();
    }
}
