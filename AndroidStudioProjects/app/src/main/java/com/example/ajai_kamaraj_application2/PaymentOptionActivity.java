package com.example.ajai_kamaraj_application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        Button cashOnDeliveryButton = findViewById(R.id.cashOnDeliveryButton);
        Button payOnlineButton = findViewById(R.id.payOnlineButton);

        cashOnDeliveryButton.setOnClickListener(v -> {
            // Navigate to Address Filling Page. Replace with your address activity.
            Intent intent = new Intent(PaymentOptionActivity.this, AddressFillingActivity.class);
            startActivity(intent);
        });

        payOnlineButton.setOnClickListener(v -> {
            // Navigate to Stripe Payment Page
            Intent intent = new Intent(PaymentOptionActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }
}
