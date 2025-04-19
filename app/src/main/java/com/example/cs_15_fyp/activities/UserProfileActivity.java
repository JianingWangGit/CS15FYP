package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_15_fyp.R;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Setup click listeners for menu items
        View reviewsSection = findViewById(R.id.reviewsSection);
        View editEmailSection = findViewById(R.id.editEmailSection);

        reviewsSection.setOnClickListener(v -> {
            // TODO: Navigate to reviews screen
            Toast.makeText(this, "Your reviews clicked", Toast.LENGTH_SHORT).show();
        });

        editEmailSection.setOnClickListener(v -> {
            // TODO: Navigate to edit email/password screen
            Toast.makeText(this, "Edit email/password clicked", Toast.LENGTH_SHORT).show();
        });

        // For testing: Add long press listener to switch back to search
        findViewById(android.R.id.content).setOnLongClickListener(v -> {
            finish();
            return true;
        });
    }
} 