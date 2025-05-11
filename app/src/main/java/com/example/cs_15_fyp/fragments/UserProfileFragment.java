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
import com.example.cs_15_fyp.activities.LoginActivity;
import com.example.cs_15_fyp.activities.MyReviewsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        View reviewsSection = view.findViewById(R.id.reviewsSection);
        View editEmailSection = view.findViewById(R.id.editEmailSection);
        TextView logoutText = view.findViewById(R.id.logoutTextView);

        reviewsSection.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(getActivity(), MyReviewsActivity.class);
                intent.putExtra("userId", user.getUid());
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "You must be logged in to view your reviews", Toast.LENGTH_SHORT).show();
            }
        });

        editEmailSection.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Edit email/password clicked", Toast.LENGTH_SHORT).show()
        );

        logoutText.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        return view;
    }
}
