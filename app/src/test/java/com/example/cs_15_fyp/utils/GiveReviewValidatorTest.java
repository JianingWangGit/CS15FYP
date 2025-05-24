package com.example.cs_15_fyp.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class GiveReviewValidatorTest {

    @Test
    public void testValidRating() {
        assertTrue(GiveReviewValidator.isRatingValid(4.5f));
        assertFalse(GiveReviewValidator.isRatingValid(0.0f));
    }

    @Test
    public void testValidRestaurantId() {
        assertTrue(GiveReviewValidator.isRestaurantIdValid("123"));
        assertFalse(GiveReviewValidator.isRestaurantIdValid(""));
    }

    @Test
    public void testUserLoggedIn() {
        assertFalse(GiveReviewValidator.isUserLoggedIn(null));
    }
}
