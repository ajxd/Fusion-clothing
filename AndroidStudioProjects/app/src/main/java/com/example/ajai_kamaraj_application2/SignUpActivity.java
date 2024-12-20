package com.example.ajai_kamaraj_application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupName;
    private EditText signupEmail;
    private EditText signupPassword;
    private EditText signupPasswordConfirmation;
    private EditText signupMobileNumber;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signupName = findViewById(R.id.signupName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupPasswordConfirmation = findViewById(R.id.signupPasswordConfirmation);
        signupMobileNumber = findViewById(R.id.signupMobileNumber);

        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> {
            String name = signupName.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String passwordConfirmation = signupPasswordConfirmation.getText().toString().trim();
            String mobileNumber = signupMobileNumber.getText().toString().trim();

            if (!validateInputs(name, email, password, passwordConfirmation, mobileNumber)) {
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                                    if (verificationTask.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Verification email sent. Please verify.", Toast.LENGTH_SHORT).show();

                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("name", name);
                                        userData.put("email", email);
                                        userData.put("mobileNumber", mobileNumber);
                                        userData.put("isVerified", false);

                                        firestore.collection("customers").document(user.getUid())
                                                .set(userData)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(SignUpActivity.this, "User data stored successfully", Toast.LENGTH_SHORT).show();

                                                    // Redirect to LoginActivity
                                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Failed to store user data.", Toast.LENGTH_SHORT).show());
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Account creation failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private boolean validateInputs(String name, String email, String password, String passwordConfirmation, String mobileNumber) {
        // Name validation
        if (name.isEmpty() || name.length() < 3 || name.length() > 50 || !name.matches("^[a-zA-Z\\s]+$")) {
            Toast.makeText(SignUpActivity.this, "Name should be 3 to 50 characters long and contain only alphabets and spaces.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Email validation
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Password validation
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (password.isEmpty() || !password.matches(passwordPattern)) {
            Toast.makeText(SignUpActivity.this, "Password should be at least 8 characters long and contain an uppercase letter, a lowercase letter, a number, and a special character.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(passwordConfirmation)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Mobile Number validation
        if (mobileNumber.length() != 10 || !mobileNumber.matches("^[0-9]+$")) {
            Toast.makeText(SignUpActivity.this, "Enter a valid 10 digit mobile number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
