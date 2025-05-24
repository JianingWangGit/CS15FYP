package com.example.cs_15_fyp.utils;

import com.example.cs_15_fyp.models.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RestaurantFilterUtils {

    public static List<Restaurant> applyFilters(List<Restaurant> allRestaurants,
                                                Set<Double> selectedPriceRanges,
                                                Set<String> selectedCuisines) {
        if (allRestaurants == null) return new ArrayList<>();

        List<Restaurant> filtered = new ArrayList<>(allRestaurants);

        // Filter by price
        if (selectedPriceRanges != null && !selectedPriceRanges.isEmpty()) {
            filtered.removeIf(r -> !selectedPriceRanges.contains(r.getPriceRange()));
        }

        // Filter by cuisine
        if (selectedCuisines != null && !selectedCuisines.isEmpty()) {
            filtered.removeIf(r -> !selectedCuisines.contains(r.getCuisine()));
        }

        return filtered;
    }
}
