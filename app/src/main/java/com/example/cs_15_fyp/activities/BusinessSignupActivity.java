package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;

import com.example.cs_15_fyp.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class BusinessSignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);
        auth = FirebaseAuth.getInstance();

        signupEmail = findViewById(R.id.businesssignup_email);
        signupButton = findViewById(R.id.businesssignup_button);

        signupButton.setOnClickListener(v -> {
            String email = signupEmail.getText().toString();

            ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                    //.setUrl("https://yourapp.page.link/emailSignIn") // Your domain
                    .setUrl("https://cs-15-fyyp.firebaseapp.com/__/auth/action?mode=action&oobCode=code")
                    .setHandleCodeInApp(true)
                    .setAndroidPackageName(
                            "com.example.cs_15_fyp",
                            true, // Install if not available
                            null) // Minimum app version
                    .build();

            if (!email.isEmpty()) {
                    auth.sendSignInLinkToEmail(email, actionCodeSettings)
                                    .addOnSuccessListener(authResult -> {
                                        Toast.makeText(BusinessSignupActivity.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(BusinessSignupActivity.this, MainActivity.class)); //to business profile?
                                        finish();
                                    }).addOnFailureListener(e ->
                                    Toast.makeText(BusinessSignupActivity.this, "Sign in Failed", Toast.LENGTH_SHORT).show());

            } else if (email.isEmpty()) {
                signupEmail.setError("Email cannot be empty");
            } else {
                signupEmail.setError("Please enter valid email");
            }
        });
    }

}