package com.example.cs_15_fyp.utils;

import com.example.cs_15_fyp.models.Restaurant;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RestaurantFilterUtilsTest {

    private Restaurant createRestaurant(String name, String cuisine, double priceRange) {
        return new Restaurant(
                name,
                "desc",       // dummy description
                cuisine,
                4.0,          // dummy rating
                "123 Street", // dummy address
                "",           // image URL
                null,         // hours
                priceRange,
                "user123"     // dummy business user ID
        );
    }

    private List<Restaurant> getMockRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        list.add(createRestaurant("R1", "Italian", 2.0));
        list.add(createRestaurant("R2", "Japanese", 3.0));
        list.add(createRestaurant("R3", "Chinese", 2.0));
        return list;
    }

    @Test
    public void testNoFilters_appliesAll() {
        List<Restaurant> result = RestaurantFilterUtils.applyFilters(getMockRestaurants(), new HashSet<>(), new HashSet<>());
        assertEquals(3, result.size());
    }

    @Test
    public void testFilterByPrice_only2Dollar() {
        Set<Double> prices = new HashSet<>(Collections.singletonList(2.0));
        List<Restaurant> result = RestaurantFilterUtils.applyFilters(getMockRestaurants(), prices, new HashSet<>());
        assertEquals(2, result.size());
        for (Restaurant r : result) {
            assertEquals(2.0, r.getPriceRange(), 0.01);
        }
    }

    @Test
    public void testFilterByCuisine_onlyChinese() {
        Set<String> cuisines = new HashSet<>(Collections.singletonList("Chinese"));
        List<Restaurant> result = RestaurantFilterUtils.applyFilters(getMockRestaurants(), new HashSet<>(), cuisines);
        assertEquals(1, result.size());
        assertEquals("Chinese", result.get(0).getCuisine());
    }

    @Test
    public void testFilterByBothConditions() {
        Set<Double> prices = new HashSet<>(Collections.singletonList(3.0));
        Set<String> cuisines = new HashSet<>(Collections.singletonList("Japanese"));
        List<Restaurant> result = RestaurantFilterUtils.applyFilters(getMockRestaurants(), prices, cuisines);
        assertEquals(1, result.size());
        assertEquals("Japanese", result.get(0).getCuisine());
        assertEquals(3.0, result.get(0).getPriceRange(), 0.01);
    }

    @Test
    public void testNoMatch() {
        Set<Double> prices = new HashSet<>(Collections.singletonList(4.0));
        List<Restaurant> result = RestaurantFilterUtils.applyFilters(getMockRestaurants(), prices, new HashSet<>());
        assertTrue(result.isEmpty());
    }
}
