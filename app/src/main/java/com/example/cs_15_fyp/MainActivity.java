package com.example.cs_15_fyp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs_15_fyp.databinding.ActivityMainBinding;
import com.example.cs_15_fyp.temp_frag.NotificationsFragment;
import com.example.cs_15_fyp.temp_frag.ProfileFragment;
import com.example.cs_15_fyp.temp_frag.SearchFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        replaceFragment(new SearchFragment());
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
                replaceFragment(new SearchFragment());
                return true;
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
                return true;
            } else {
                return false;
            }

        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}