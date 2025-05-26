package com.example.cs_15_fyp.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.firebase.auth.FirebaseUser;
/**
 * White-box unit tests for GiveReviewValidator.
 * Each test is annotated with the matching result-table ID (F1.2a–F1.2i).
 */
public class GiveReviewValidatorTest {
    // Rating validation
    /** F1.2a – Rating = 4.5 (normal valid) */
    @Test
    public void testRatingValidNormal() {
        assertTrue(GiveReviewValidator.isRatingValid(4.5f));
    }
    /** F1.2b – Rating = 0.0 (invalid – below range) */
    @Test
    public void testRatingInvalidZero() {
        assertFalse(GiveReviewValidator.isRatingValid(0.0f));
    }
    /** F1.2c – Rating = 0.5 (boundary valid lower limit) */
    @Test
    public void testRatingValidLowerBound() {
        assertTrue(GiveReviewValidator.isRatingValid(0.5f));
    }
    /** F1.2d – Rating = 5.5 (invalid – above max) */
    @Test
    public void testRatingInvalidTooHigh() {
        assertFalse(GiveReviewValidator.isRatingValid(5.5f));
    }
    /** Additional: Rating = 5.0 (upper boundary valid) */
    @Test
    public void testRatingValidUpperBound() {
        assertTrue(GiveReviewValidator.isRatingValid(5.0f));
    }
    // Restaurant-ID validation
    /** F1.2e – Restaurant-ID "123" (valid) */
    @Test
    public void testRestaurantIdValid() {
        assertTrue(GiveReviewValidator.isRestaurantIdValid("123"));
    }
    /** F1.2f – Restaurant-ID "" (invalid – empty) */
    @Test
    public void testRestaurantIdInvalidEmpty() {
        assertFalse(GiveReviewValidator.isRestaurantIdValid(""));
    }
    /** F1.2g – Restaurant-ID null (invalid) */
    @Test
    public void testRestaurantIdInvalidNull() {
        assertFalse(GiveReviewValidator.isRestaurantIdValid(null));
    }
    /** F1.2h – Restaurant-ID whitespace (invalid) */
    @Test
    public void testRestaurantIdInvalidWhitespace() {
        assertFalse(GiveReviewValidator.isRestaurantIdValid("   "));
    }
    // Login state validation
    /** F1.2i – FirebaseUser null (user not logged in) */
    @Test
    public void testUserNotLoggedIn() {
        assertFalse(GiveReviewValidator.isUserLoggedIn(null));
    }
}
