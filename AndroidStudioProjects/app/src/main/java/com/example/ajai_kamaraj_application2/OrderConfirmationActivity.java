package com.example.ajai_kamaraj_application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Button backToHomepageButton = findViewById(R.id.backtohomepage);
        backToHomepageButton.setOnClickListener(v -> {
            // Navigate to MainActivity
            Intent intent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
            // Setting this flag will clear the back stack of activities ensuring MainActivity is the only activity running.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
