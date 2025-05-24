package com.example.cs_15_fyp.utils;

import com.google.firebase.auth.FirebaseUser;

public class GiveReviewValidator {
    public static boolean isRatingValid(float rating) {
        return rating >= 0.5f && rating <= 5.0f;
    }

    public static boolean isRestaurantIdValid(String id) {
        return id != null && !id.trim().isEmpty();
    }

    public static boolean isUserLoggedIn(FirebaseUser user) {
        return user != null;
    }
}
