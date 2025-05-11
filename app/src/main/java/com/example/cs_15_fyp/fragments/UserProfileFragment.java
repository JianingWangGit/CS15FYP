package com.example.cs_15_fyp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs_15_fyp.R;

public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        View reviewsSection = view.findViewById(R.id.reviewsSection);
        View editEmailSection = view.findViewById(R.id.editEmailSection);
        TextView logoutText = view.findViewById(R.id.logoutTextView);

        reviewsSection.setOnClickListener(v -> {
            // Navigate to AllReviewsActivity
            Intent intent = new Intent(getActivity(), com.example.cs_15_fyp.activities.AllReviewsActivity.class);
            startActivity(intent);
        });

        editEmailSection.setOnClickListener(v -> {
            // Navigate to edit email/password screen
            Toast.makeText(getActivity(), "Edit email/password clicked", Toast.LENGTH_SHORT).show();
        });

        // Logout click listener
        logoutText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.cs_15_fyp.activities.LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}
