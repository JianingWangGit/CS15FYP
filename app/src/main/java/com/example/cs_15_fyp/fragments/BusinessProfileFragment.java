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
import com.example.cs_15_fyp.activities.AllReviewsActivity;
import com.example.cs_15_fyp.activities.LoginActivity;

public class BusinessProfileFragment extends Fragment {

    private String restaurantId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_profile, container, false);

        TextView businessName = view.findViewById(R.id.business_name);
        TextView logoutButton = view.findViewById(R.id.logout_button);
        TextView yourReviews = view.findViewById(R.id.your_reviews);
        TextView editEmail = view.findViewById(R.id.edit_email);
        TextView editMenu = view.findViewById(R.id.edit_menu);
        TextView help = view.findViewById(R.id.help);

        // Get restaurantId from arguments or set it here
        if (getArguments() != null && getArguments().containsKey("restaurantId")) {
            restaurantId = getArguments().getString("restaurantId");
        } else {
            // TODO: Set restaurantId from a persistent source if needed
            restaurantId = null;
        }

        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            if (getActivity() != null) getActivity().finish();
        });

        yourReviews.setOnClickListener(v -> {
            if (restaurantId != null) {
                Intent intent = new Intent(getActivity(), AllReviewsActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Business ID not set", Toast.LENGTH_SHORT).show();
            }
        });
        editEmail.setOnClickListener(v -> Toast.makeText(getActivity(), "Edit email/username clicked", Toast.LENGTH_SHORT).show());
        editMenu.setOnClickListener(v -> Toast.makeText(getActivity(), "Edit menu/about us clicked", Toast.LENGTH_SHORT).show());
        help.setOnClickListener(v -> Toast.makeText(getActivity(), "Help clicked", Toast.LENGTH_SHORT).show());

        return view;
    }
}
