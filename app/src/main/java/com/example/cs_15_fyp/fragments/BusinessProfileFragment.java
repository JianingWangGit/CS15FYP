package com.example.cs_15_fyp.fragments;

import android.app.Activity;
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
import com.example.cs_15_fyp.activities.AllReviewsActivity;
import com.example.cs_15_fyp.activities.CreateRestaurantActivity;
import com.example.cs_15_fyp.activities.EditRestaurantActivity;
import com.example.cs_15_fyp.activities.EditHoursActivity;
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

    private static final int REQUEST_CREATE_RESTAURANT = 101;
    private static final int REQUEST_EDIT_RESTAURANT = 102;
    private static final int REQUEST_EDIT_HOURS = 103;

    private LinearLayout businessContent, emptyRestaurantSection;
    private TextView businessNameView;
    private Button createRestaurantButton;
    private Restaurant myRestaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business_profile, container, false);

        // View bindings
        TextView logoutButton = view.findViewById(R.id.logout_button);
        businessContent = view.findViewById(R.id.business_content);
        emptyRestaurantSection = view.findViewById(R.id.empty_restaurant_section);
        createRestaurantButton = view.findViewById(R.id.create_restaurant_button);
        businessNameView = view.findViewById(R.id.business_name);

        // Logout handler
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });

        // Create Restaurant
        createRestaurantButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateRestaurantActivity.class);
            startActivityForResult(intent, REQUEST_CREATE_RESTAURANT);
        });

        // Edit Restaurant Info
        LinearLayout editRestaurantSection = view.findViewById(R.id.editRestaurantSection);
        editRestaurantSection.setOnClickListener(v -> {
            if (myRestaurant != null) {
                Intent intent = new Intent(getActivity(), EditRestaurantActivity.class);
                intent.putExtra("restaurantId", myRestaurant.getId());
                startActivityForResult(intent, REQUEST_EDIT_RESTAURANT);
            } else {
                Toast.makeText(getActivity(), "Restaurant not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit Restaurant Hours
        LinearLayout editHoursSection = view.findViewById(R.id.editHoursSection);
        editHoursSection.setOnClickListener(v -> {
            if (myRestaurant != null) {
                Intent intent = new Intent(getActivity(), EditHoursActivity.class);
                intent.putExtra("restaurantId", myRestaurant.getId());
                startActivityForResult(intent, REQUEST_EDIT_HOURS);
            } else {
                Toast.makeText(getActivity(), "Restaurant not found", Toast.LENGTH_SHORT).show();
            }
        });

        // View Restaurant Reviews
        Button viewReviewsButton = view.findViewById(R.id.view_restaurant_reviews_button);
        viewReviewsButton.setOnClickListener(v -> {
            if (myRestaurant != null) {
                Intent intent = new Intent(getActivity(), AllReviewsActivity.class);
                intent.putExtra("restaurantId", myRestaurant.getId());
                intent.putExtra("restaurantName", myRestaurant.getName());
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Restaurant not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Load associated restaurant
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
                        myRestaurant = restaurants.get(0);  // Assuming one restaurant per business
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CREATE_RESTAURANT || 
             requestCode == REQUEST_EDIT_RESTAURANT || 
             requestCode == REQUEST_EDIT_HOURS) 
                && resultCode == Activity.RESULT_OK) {
            fetchBusinessRestaurant(); // Refresh data after creating/editing a restaurant
        }
    }
}
