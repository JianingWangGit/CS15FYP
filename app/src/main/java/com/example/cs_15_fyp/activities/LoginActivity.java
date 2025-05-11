package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_15_fyp.R;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private ImageView loginGoogle;
    private TextView forgotPassword, signupPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.forgot_pass);
        loginGoogle = findViewById(R.id.login_google);
        signupPrompt = findViewById(R.id.signup_prompt);

        // Email/Password login
        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (!validateEmail(email) || !validatePassword(password)) return;

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            routeUserBasedOnType(user.getUid());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        });

        // Forgot password
        forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class)));

        // "Don't have an account? Sign up"
        signupPrompt.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        // Google Sign-In config
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Add this in strings.xml
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        loginGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void routeUserBasedOnType(String uid) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String userType = snapshot.getString("userType");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userType", userType);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch user type", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Google login successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            routeUserBasedOnType(user.getUid());
                        }
                    } else {
                        Toast.makeText(this, "Google login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
