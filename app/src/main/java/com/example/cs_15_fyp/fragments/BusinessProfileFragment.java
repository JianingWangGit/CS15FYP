package com.example.cs_15_fyp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs_15_fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.example.cs_15_fyp.activities.LoginActivity;

public class BusinessProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_profile, container, false);

        TextView logoutButton = view.findViewById(R.id.logout_button);
        // ... find other views as needed ...

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });

        // ... other click listeners ...

        return view;
    }
} 