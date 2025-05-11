package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_15_fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText signupEmail, signupPassword;
    private Spinner userTypeSpinner;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        userTypeSpinner = findViewById(R.id.usertype_spinner);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.user_type,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        );
        userTypeSpinner.setAdapter(adapter);
        userTypeSpinner.setSelection(0); // Set prompt/default

        signupButton.setOnClickListener(v -> handleSignup());

        loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });
    }

    private void handleSignup() {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String selectedUserType = userTypeSpinner.getSelectedItem().toString().trim();

        if (userTypeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateEmail(email) || !validatePassword(password)) {
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fuser = auth.getCurrentUser();
                        if (fuser != null) {
                            saveUserToFirestore(fuser.getUid(), email, selectedUserType);
                        }
                    } else {
                        Exception e = task.getException();
                        if (e != null && e.getMessage() != null && e.getMessage().contains("already in use")) {
                            Toast.makeText(this, "Email already registered. Please log in.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (e != null) e.printStackTrace();
                    }
                });
    }

    private void saveUserToFirestore(String uid, String email, String userType) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("username", generateRandomUsername());
        userInfo.put("userType", userType);
        userInfo.put("createdAt", FieldValue.serverTimestamp());

        db.collection("Users").document(uid)
                .set(userInfo)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save user info", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String pass) {
        if (pass.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return false;
        }
        if (pass.length() < 8 || pass.length() > 12) {
            signupPassword.setError("Password must be 8–12 characters long");
            return false;
        }
        if (!pass.matches(".*\\d.*")) {
            signupPassword.setError("Include at least one number");
            return false;
        }
        if (!pass.matches(".*[^a-zA-Z0-9].*")) {
            signupPassword.setError("Include a special character");
            return false;
        }
        return true;
    }

    private String generateRandomUsername() {
        int length = 8;
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }
}
