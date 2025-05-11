package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PersonalSignupActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    //private FirebaseFirestore fstore;
    private EditText signupEmail, signupPassword;
    private Button personalsignupButton;

    //private ImageView signupGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_personal_signup);

        auth = FirebaseAuth.getInstance();
        //fstore = FirebaseFirestore.getInstance();
        signupEmail = findViewById(R.id.personalsignup_email);
        signupPassword = findViewById(R.id.personalsignup_password);
        personalsignupButton = findViewById(R.id.personalsignup_button);


        personalsignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                //account creation
                // === Input validation ===
                if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                    return;
                }

                if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                    return;
                }

                if (pass.length() < 8 || pass.length() > 12) {
                    signupPassword.setError("Password must be 8–12 characters long");
                    return;
                }

                if (!pass.matches(".*\\d.*")) {
                    signupPassword.setError("Password must include at least one number");
                    return;
                }

                if (!pass.matches(".*[^a-zA-Z0-9].*")) {
                    signupPassword.setError("Password must include a special character (e.g. @, #, !)");
                    return;
                } else {

                    auth.createUserWithEmailAndPassword(user,pass)
                            .addOnCompleteListener(task ->{
                                if (task.isSuccessful()){
                                    FirebaseUser fuser = auth.getCurrentUser();

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("Email", signupEmail.getText().toString().trim());
                                    userData.put("UserType", "Personal");
                                    userData.put("createdAt", FieldValue.serverTimestamp());
                                    userData.put("Username", generaterandomUser());
                                    userData.put("userId", fuser.getUid());

                                    db.collection("Users")
                                            .document(fuser.getUid())
                                            .set(userData)
                                            .addOnSuccessListener(aVoid -> {
                                                 startActivity(new Intent(PersonalSignupActivity.this, MainActivity.class)); //TODO :remove after merge
                                            });
                                    Toast.makeText(PersonalSignupActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();

                            } else {
                                        Exception e = task.getException();
                                        if (e != null && e.getMessage() != null && e.getMessage().contains("The email address is already in use")) {
                                            Toast.makeText(PersonalSignupActivity.this, "This email is already registered. Please log in.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(PersonalSignupActivity.this, "Sign up Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        e.printStackTrace();  // Print full exception in Logcat
                                    }

                    });
                }
            }
        });
}
    private String generaterandomUser() {
        int length = 8;
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0;i<length;i++){
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }
}
