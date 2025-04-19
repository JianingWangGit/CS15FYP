package com.example.cs_15_fyp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);

        View reviewsSection = view.findViewById(R.id.reviewsSection);
        View editEmailSection = view.findViewById(R.id.editEmailSection);

        reviewsSection.setOnClickListener(v -> {
            // Navigate to reviews screen
            Toast.makeText(getActivity(), "Your reviews clicked", Toast.LENGTH_SHORT).show();
        });

        editEmailSection.setOnClickListener(v -> {
            // Navigate to edit email/password screen
            Toast.makeText(getActivity(), "Edit email/password clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
