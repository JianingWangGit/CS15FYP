package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
     private EditText signupEmail, signupPassword;

    private Spinner usertypeSpinner;
    public Boolean usertype;

    private Button signupButton;
    private TextView loginRedirectText;
    //private ImageView signupGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        //using spinner
        usertypeSpinner = findViewById(R.id.usertype_spinner);

        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        usertypeSpinner.setAdapter(adapter);
        // Set a default selection that acts as a prompt
       usertypeSpinner.setSelection(0);

        //TODO: google signin
        //signupGoogle = findViewById(R.id.signup_google);


        /*signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                //String usertype = usertypeSpinner.getSelectedItem().toString().trim();

                //account creation
                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else {
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(SignUpActivity.this, RestaurantSearchActivity.class));
                                startActivity(new Intent(String.valueOf(SignUpActivity.this))); //TODO :remove after merge
                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });*/

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String usertype_spinner = usertypeSpinner.getSelectedItem().toString().trim();

                if (usertypeSpinner.getSelectedItemPosition() == 0) {
                    // First item is the prompt, so selection isn't valid
                    Toast.makeText(SignUpActivity.this, "Please make a user type selection", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    /*if (usertypeSpinner.getSelectedItemPosition() == 1){
                        usertype = true; //personal account
                    } else if (usertypeSpinner.getSelectedItemPosition() == 2) {
                        usertype = false; //business account
                    }*/

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
                        auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();

                                    FirebaseUser fuser = auth.getCurrentUser();
                                    DocumentReference df   = fstore.collection("Users").document(fuser.getUid());
                                    Map<String,Object> userInfo = new HashMap<>();
                                    userInfo.put("Email", signupEmail.getText().toString().trim());
                                    userInfo.put("Personal", usertypeSpinner.getSelectedItemPosition());
                                    df.set(userInfo);

                                    startActivity(new Intent(String.valueOf(SignUpActivity.this))); //TODO :remove after merge
                                    //startActivity(new Intent(SignUpActivity.this, RestaurantSearchActivity.class));
                                    finish();
                                } else {
                                    Exception e = task.getException();
                                    if (e != null && e.getMessage() != null && e.getMessage().contains("The email address is already in use")) {
                                        Toast.makeText(SignUpActivity.this, "This email is already registered. Please log in.", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(SignUpActivity.this, "Sign up Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    e.printStackTrace();  // Print full exception in Logcat
                                }
                            }
                        });
                    }
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}