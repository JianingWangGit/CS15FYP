package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.RestaurantAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.models.Restaurant;
import com.example.cs_15_fyp.models.ApiResponse;
import com.example.cs_15_fyp.api.RestaurantService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantSearchActivity extends AppCompatActivity implements RestaurantAdapter.OnRestaurantClickListener {
    private RestaurantAdapter adapter;
    private List<Restaurant> allRestaurants;
    private TextInputEditText searchInput;
    private ChipGroup priceFilterChipGroup;
    private ChipGroup cuisineChipGroup;
    private final Set<String> selectedCuisines = new HashSet<>();
    private final Set<Double> selectedPriceRanges = new HashSet<>();
    private RestaurantService restaurantService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Restaurants");
        }

        // Initialize views
        searchInput = findViewById(R.id.searchInput);
        priceFilterChipGroup = findViewById(R.id.filterChipGroup);
        cuisineChipGroup = findViewById(R.id.cuisineChipGroup);
        RecyclerView recyclerView = findViewById(R.id.restaurantRecyclerView);

        // Initialize data structures
        allRestaurants = new ArrayList<>();
        adapter = new RestaurantAdapter(this);
        restaurantService = ApiClient.getRestaurantApi();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup search functionality
        setupSearch();

        // Setup filter chips
        setupFilterChips();

        // Load initial data
        loadRestaurants();
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    searchRestaurants(s.toString());
                } else if (s.length() == 0) {
                    loadRestaurants();
                }
            }
        });
    }

    private void setupFilterChips() {
        // Price range filter chips
        for (int i = 0; i < priceFilterChipGroup.getChildCount(); i++) {
            final int priceIndex = i;
            Chip chip = (Chip) priceFilterChipGroup.getChildAt(i);
            chip.setCheckable(true);
            chip.setOnClickListener(v -> {
                if (chip.isChecked()) {
                    selectedPriceRanges.add((double) (priceIndex + 1));
                } else {
                    selectedPriceRanges.remove((double) (priceIndex + 1));
                }
                filterRestaurants();
            });
        }

        // Cuisine filter chips
        for (int i = 0; i < cuisineChipGroup.getChildCount(); i++) {
            final Chip cuisineChip = (Chip) cuisineChipGroup.getChildAt(i);
            cuisineChip.setCheckable(true);
            cuisineChip.setOnClickListener(v -> {
                if (cuisineChip.isChecked()) {
                    selectedCuisines.add(cuisineChip.getText().toString());
                } else {
                    selectedCuisines.remove(cuisineChip.getText().toString());
                }
                filterRestaurants();
            });
        }
    }

    private void loadRestaurants() {
        Log.d("RestaurantSearch", "Attempting to load restaurants");
        Call<ApiResponse<List<Restaurant>>> call = restaurantService.getAllRestaurants();
        
        call.enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call, Response<ApiResponse<List<Restaurant>>> response) {
                Log.d("RestaurantSearch", "Response received. Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<Restaurant> restaurants = response.body().getData();
                    Log.d("RestaurantSearch", "Successfully loaded " + restaurants.size() + " restaurants");
                    allRestaurants = restaurants;
                    filterRestaurants();
                } else {
                    Log.e("RestaurantSearch", "Failed to load restaurants. Response code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("RestaurantSearch", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(RestaurantSearchActivity.this, "Failed to load restaurants", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                Log.e("RestaurantSearch", "Network error: " + t.getMessage(), t);
                Toast.makeText(RestaurantSearchActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchRestaurants(String query) {
        restaurantService.searchRestaurants(query).enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call, Response<ApiResponse<List<Restaurant>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRestaurants = response.body().getData();
                    filterRestaurants();
                } else {
                    Toast.makeText(RestaurantSearchActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                Toast.makeText(RestaurantSearchActivity.this, "Search failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterRestaurants() {
        List<Restaurant> filteredList = new ArrayList<>(allRestaurants);

        // Apply price range filter
        if (!selectedPriceRanges.isEmpty()) {
            filteredList.removeIf(restaurant -> !selectedPriceRanges.contains(restaurant.getPriceRange()));
        }

        // Apply cuisine filter
        if (!selectedCuisines.isEmpty()) {
            filteredList.removeIf(restaurant -> !selectedCuisines.contains(restaurant.getCuisine()));
        }

        adapter.filterRestaurants(filteredList);
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        Toast.makeText(this, "Clicked: " + restaurant.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 