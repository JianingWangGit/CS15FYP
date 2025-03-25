package com.example.cs_15_fyp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.adapter.RestaurantAdapter;
import com.example.cs_15_fyp.model.Restaurant;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestaurantSearchActivity extends AppCompatActivity implements RestaurantAdapter.OnRestaurantClickListener {
    private RestaurantAdapter adapter;
    private List<Restaurant> allRestaurants;
    private TextInputEditText searchInput;
    private ChipGroup priceFilterChipGroup;
    private ChipGroup cuisineChipGroup;
    private final Set<String> selectedCuisines = new HashSet<>();
    private final Set<Double> selectedPriceRanges = new HashSet<>();

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
                filterRestaurants();
            }
        });
    }

    private void setupFilterChips() {
        // Price range filter chips
        for (int i = 0; i < priceFilterChipGroup.getChildCount(); i++) {
            final int priceIndex = i;
            Chip chip = (Chip) priceFilterChipGroup.getChildAt(i);
            chip.setCheckable(true); // Make chips checkable
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
            cuisineChip.setCheckable(true); // Make chips checkable
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
        // TODO: Replace with actual data loading from your backend
        // This is just sample data
        allRestaurants.add(new Restaurant("1", "Pizza Place", "Best pizza in town", "Italian",
                4.5, "123 Main St", "https://example.com/pizza.jpg", true, 2));
        allRestaurants.add(new Restaurant("2", "Sushi Bar", "Fresh sushi daily", "Japanese",
                4.8, "456 Oak Ave", "https://example.com/sushi.jpg", true, 3));
        allRestaurants.add(new Restaurant("3", "Taco Fiesta", "Authentic Mexican", "Mexican",
                4.2, "789 Pine Rd", "https://example.com/tacos.jpg", false, 1));
        allRestaurants.add(new Restaurant("4", "Curry House", "Spicy Indian cuisine", "Indian",
                4.6, "321 Maple Dr", "https://example.com/curry.jpg", true, 2));

        filterRestaurants();
    }

    private void filterRestaurants() {
        String searchQuery = searchInput.getText().toString().toLowerCase();
        List<Restaurant> filteredList = new ArrayList<>();

        for (Restaurant restaurant : allRestaurants) {
            boolean matchesSearch = restaurant.getName().toLowerCase().contains(searchQuery) ||
                    restaurant.getDescription().toLowerCase().contains(searchQuery) ||
                    restaurant.getCuisine().toLowerCase().contains(searchQuery);

            boolean matchesPrice = selectedPriceRanges.isEmpty() ||
                    selectedPriceRanges.contains(restaurant.getPriceRange());

            boolean matchesCuisine = selectedCuisines.isEmpty() ||
                    selectedCuisines.contains(restaurant.getCuisine());

            if (matchesSearch && matchesPrice && matchesCuisine) {
                filteredList.add(restaurant);
            }
        }

        adapter.filterRestaurants(filteredList);
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        // TODO: Handle restaurant click - navigate to detail view
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