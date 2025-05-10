package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.fragments.NotificationsFragment;
import com.example.cs_15_fyp.fragments.RestaurantSearchFragment;
import com.example.cs_15_fyp.fragments.BusinessProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BusinessProfileActivity extends AppCompatActivity {

    private TextView businessName, logoutButton, yourReviews, editEmail, editMenu, help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        businessName = findViewById(R.id.business_name);
        logoutButton = findViewById(R.id.logout_button);
        yourReviews = findViewById(R.id.your_reviews);
        editEmail = findViewById(R.id.edit_email);
        editMenu = findViewById(R.id.edit_menu);
        help = findViewById(R.id.help);

        // Set business name dynamically if needed
        // businessName.setText(getBusinessNameFromIntentOrPrefs());

        logoutButton.setOnClickListener(v -> {
            // Add your logout logic here (e.g., FirebaseAuth.getInstance().signOut())
            startActivity(new Intent(BusinessProfileActivity.this, LoginActivity.class));
            finish();
        });

        yourReviews.setOnClickListener(v -> {
            // Open reviews activity
            Toast.makeText(this, "Your reviews clicked", Toast.LENGTH_SHORT).show();
        });

        editEmail.setOnClickListener(v -> {
            // Open edit email/username activity
            Toast.makeText(this, "Edit email/username clicked", Toast.LENGTH_SHORT).show();
        });

        editMenu.setOnClickListener(v -> {
            // Open edit menu/about us activity
            Toast.makeText(this, "Edit menu/about us clicked", Toast.LENGTH_SHORT).show();
        });

        help.setOnClickListener(v -> {
            // Open help activity
            Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show();
        });

        // Handle window insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set default fragment
        replaceFragment(new BusinessProfileFragment());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.notifications) {
                replaceFragment(new NotificationsFragment());
                return true;
            } else if (id == R.id.search) {
                replaceFragment(new RestaurantSearchFragment());
                return true;
            } else if (id == R.id.profile) {
                replaceFragment(new BusinessProfileFragment());
                return true;
            } else {
                return false;
            }
        });
        // Highlight profile tab by default
        bottomNav.setSelectedItemId(R.id.profile);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
