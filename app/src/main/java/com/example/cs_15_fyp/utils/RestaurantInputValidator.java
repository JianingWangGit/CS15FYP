package com.example.cs_15_fyp.utils;

public class RestaurantInputValidator {
    public static boolean isValidRestaurantInput(String name, String desc, String cuisine, String address) {
        return !(name == null || name.isEmpty() || desc == null || desc.isEmpty()
                || cuisine == null || cuisine.isEmpty() || address == null || address.isEmpty());
    }
}
