package com.example.cs_15_fyp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.databinding.ActivityMainBinding;
import com.example.cs_15_fyp.fragments.NotificationsFragment;
import com.example.cs_15_fyp.fragments.RestaurantSearchFragment;
import com.example.cs_15_fyp.fragments.UserProfileFragment;
import com.example.cs_15_fyp.fragments.BusinessProfileFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String userType;  // store globally to avoid repeating getIntent().getStringExtra()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Grab user type from login redirection
        userType = getIntent().getStringExtra("userType");

        // Default fragment on launch = Search
        if (savedInstanceState == null) {
            replaceFragment(new RestaurantSearchFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.search);
        }

        // Apply edge insets for modern UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bottom nav bar logic
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.notifications) {
                replaceFragment(new NotificationsFragment());
                return true;
            } else if (id == R.id.search) {
                replaceFragment(new RestaurantSearchFragment());
                return true;
            } else if (id == R.id.profile) {
                // Load correct profile
                Fragment frag = isBusiness() ? new BusinessProfileFragment() : new UserProfileFragment();
                replaceFragment(frag);
                return true;
            } else {
                return false;
            }
        });
    }

    private boolean isBusiness() {
        return userType != null && userType.equalsIgnoreCase("business");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
