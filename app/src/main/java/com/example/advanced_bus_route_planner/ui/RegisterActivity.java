package com.example.advanced_bus_route_planner.ui;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced_bus_route_planner.R;
import com.example.advanced_bus_route_planner.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextEmail, editTextPassword;
    private RadioGroup radioGroupRole;
    private Button buttonRegister;
    private TextView textViewBackToLogin;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewBackToLogin = findViewById(R.id.textViewBackToLogin);
        progressBar = findViewById(R.id.progressBar);

        // Register button click
        buttonRegister.setOnClickListener(v -> registerUser());

        // Back to login click
        textViewBackToLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Get selected role
        int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRoleId);
        String role = selectedRoleId == R.id.radioCustomer ? "customer" : "admin";

        // Validate input
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        buttonRegister.setEnabled(false);

        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get user ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Create user object
                        User user = new User(userId, email, name, role);

                        // Save user data to Firestore
                        db.collection("users")
                                .document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    progressBar.setVisibility(View.GONE);
                                    buttonRegister.setEnabled(true);

                                    Toast.makeText(RegisterActivity.this,
                                            "Account created successfully!",
                                            Toast.LENGTH_SHORT).show();

                                    // Go to main activity
                                    goToMainActivity();
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    buttonRegister.setEnabled(true);

                                    Toast.makeText(RegisterActivity.this,
                                            "Failed to save user data: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        buttonRegister.setEnabled(true);

                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}