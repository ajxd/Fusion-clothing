package com.example.ajai_kamaraj_application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddressFillingActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etStreet;
    private EditText etCity;
    private EditText etState;
    private EditText etZipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_filling);

        etName = findViewById(R.id.et_name);
        etStreet = findViewById(R.id.et_street);
        etCity = findViewById(R.id.et_city);
        etState = findViewById(R.id.et_state);
        etZipcode = findViewById(R.id.et_zipcode);
        Button btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String street = etStreet.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String state = etState.getText().toString().trim();
            String zipcode = etZipcode.getText().toString().trim();

            if (name.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty()) {
                Toast.makeText(AddressFillingActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validation
            if (name.length() < 3) {
                Toast.makeText(AddressFillingActivity.this, "Name should be at least 3 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (street.length() < 5) {
                Toast.makeText(AddressFillingActivity.this, "Street should be at least 5 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (city.length() < 3) {
                Toast.makeText(AddressFillingActivity.this, "City should be at least 3 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (state.length() < 2) {
                Toast.makeText(AddressFillingActivity.this, "State should be at least 2 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (zipcode.length() != 5 || !zipcode.matches("\\d{6}")) {
                Toast.makeText(AddressFillingActivity.this, "Zipcode should be 6 digits long!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(AddressFillingActivity.this, OrderConfirmationActivity.class);
            startActivity(intent);
        });
    }
}
