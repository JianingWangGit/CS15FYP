package com.example.cs_15_fyp.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class RestaurantInputValidatorTest {

    @Test
    public void testValidInput() {
        assertTrue(RestaurantInputValidator.isValidRestaurantInput("R1", "desc", "Italian", "address"));
    }

    @Test
    public void testInvalidInput() {
        assertFalse(RestaurantInputValidator.isValidRestaurantInput("", "desc", "cuisine", "addr"));
        assertFalse(RestaurantInputValidator.isValidRestaurantInput("name", "", "", ""));
    }
}
