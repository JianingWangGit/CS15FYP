package com.example.cs_15_fyp.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class OpeningHoursValidatorTest {

    @Test
    public void testValidTimes() {
        assertTrue(OpeningHoursValidator.isTimeValid("09:00"));
        assertTrue(OpeningHoursValidator.isTimeValid("23:59"));
    }

    @Test
    public void testInvalidTimes() {
        assertFalse(OpeningHoursValidator.isTimeValid("9:00"));     // wrong format
        assertFalse(OpeningHoursValidator.isTimeValid("abcd"));     // non-numeric
        assertFalse(OpeningHoursValidator.isTimeValid("24:01"));    // invalid hour
        assertFalse(OpeningHoursValidator.isTimeValid("00:60"));    // invalid minute
        assertFalse(OpeningHoursValidator.isTimeValid(""));         // empty input
        assertFalse(OpeningHoursValidator.isTimeValid(null));       // null input
    }
}
