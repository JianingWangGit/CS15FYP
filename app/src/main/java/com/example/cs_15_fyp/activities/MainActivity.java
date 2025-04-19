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


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new UserProfileFragment());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.bottomNavigationView.setOnItemSelectedListener( item -> {

            int id = item.getItemId();

            if (id == R.id.notifications) {
                replaceFragment(new NotificationsFragment());
                return true;
            } else if (id == R.id.search) {
                replaceFragment(new RestaurantSearchFragment());
                return true;
            } else if (id == R.id.profile) {
                replaceFragment(new UserProfileFragment());
                return true;
            } else {
                return false;
            }

        });

        if (savedInstanceState == null) {
            replaceFragment(new RestaurantSearchFragment()); // ← change this to your default fragment
            binding.bottomNavigationView.setSelectedItemId(R.id.search); // ← this highlights the correct tab
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}