package com.example.cs_15_fyp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.activities.LoginActivity;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.RestaurantService;
import com.example.cs_15_fyp.models.ApiResponse;
import com.example.cs_15_fyp.models.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessProfileFragment extends Fragment {

    private LinearLayout businessContent, emptyRestaurantSection;
    private TextView businessNameView;
    private Button createRestaurantButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business_profile, container, false);

        // Views
        TextView logoutButton = view.findViewById(R.id.logout_button);
        businessContent = view.findViewById(R.id.business_content);
        emptyRestaurantSection = view.findViewById(R.id.empty_restaurant_section);
        createRestaurantButton = view.findViewById(R.id.create_restaurant_button);
        businessNameView = view.findViewById(R.id.business_name);

        // Logout
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });

        // Create button
        createRestaurantButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "TODO: Launch CreateRestaurantActivity", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(getActivity(), CreateRestaurantActivity.class));
        });

        fetchBusinessRestaurant();

        return view;
    }

    private void fetchBusinessRestaurant() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        RestaurantService service = ApiClient.getClient().create(RestaurantService.class);

        service.getRestaurantsByBusinessId(uid).enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call,
                                   Response<ApiResponse<List<Restaurant>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Restaurant> restaurants = response.body().getData();
                    if (restaurants.isEmpty()) {
                        showNoRestaurantUI();
                    } else {
                        Restaurant myRestaurant = restaurants.get(0); // Assume single for now
                        businessNameView.setText(myRestaurant.getName());
                        showBusinessUI();
                    }
                } else {
                    showNoRestaurantUI();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load restaurant info", Toast.LENGTH_SHORT).show();
                showNoRestaurantUI();
            }
        });
    }

    private void showNoRestaurantUI() {
        businessContent.setVisibility(View.GONE);
        emptyRestaurantSection.setVisibility(View.VISIBLE);
    }

    private void showBusinessUI() {
        emptyRestaurantSection.setVisibility(View.GONE);
        businessContent.setVisibility(View.VISIBLE);
    }
}
