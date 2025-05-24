package com.example.cs_15_fyp.utils;

public class ReviewInputValidator {

    /**
     * Validates the rating input.
     * @param rating the rating value provided by the user
     * @return true if the rating is between 0.5 and 5.0 (inclusive), false otherwise
     */
    public static boolean isValidRating(float rating) {
        return rating >= 0.5f && rating <= 5.0f;
    }

    /**
     * Validates the comment length (optional).
     * @param comment the text input from the user
     * @return true if it's either empty or not exceeding 500 characters
     */
    public static boolean isValidComment(String comment) {
        return comment == null || comment.length() <= 500;
    }

    /**
     * Checks if the restaurant ID is not null or empty.
     */
    public static boolean hasValidRestaurantId(String restaurantId) {
        return restaurantId != null && !restaurantId.trim().isEmpty();
    }
}
