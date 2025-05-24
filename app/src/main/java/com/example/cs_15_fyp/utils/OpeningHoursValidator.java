package com.example.cs_15_fyp.utils;

public class OpeningHoursValidator {
    public static boolean isTimeValid(String time) {
        return time != null && time.matches("^\\d{2}:\\d{2}$");
    }
}

