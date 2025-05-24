package com.example.cs_15_fyp.utils;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewInputValidatorTest {

    @Test
    public void testValidRating() {
        assertTrue(ReviewInputValidator.isValidRating(5.0f));
        assertTrue(ReviewInputValidator.isValidRating(1.5f));
        assertTrue(ReviewInputValidator.isValidRating(0.5f));
    }

    @Test
    public void testInvalidRating() {
        assertFalse(ReviewInputValidator.isValidRating(0.0f));
        assertFalse(ReviewInputValidator.isValidRating(5.5f));
        assertFalse(ReviewInputValidator.isValidRating(-1.0f));
    }

    @Test
    public void testValidComment() {
        assertTrue(ReviewInputValidator.isValidComment(""));
        assertTrue(ReviewInputValidator.isValidComment(null));
        assertTrue(ReviewInputValidator.isValidComment("Nice place."));
        assertTrue(ReviewInputValidator.isValidComment("a".repeat(500)));
    }

    @Test
    public void testInvalidComment() {
        assertFalse(ReviewInputValidator.isValidComment("a".repeat(501)));
    }

    @Test
    public void testValidRestaurantId() {
        assertTrue(ReviewInputValidator.hasValidRestaurantId("abc123"));
    }

    @Test
    public void testInvalidRestaurantId() {
        assertFalse(ReviewInputValidator.hasValidRestaurantId(""));
        assertFalse(ReviewInputValidator.hasValidRestaurantId("   "));
        assertFalse(ReviewInputValidator.hasValidRestaurantId(null));
    }
}
