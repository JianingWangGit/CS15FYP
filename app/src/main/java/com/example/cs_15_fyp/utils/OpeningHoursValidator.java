package com.example.cs_15_fyp.utils;

public class OpeningHoursValidator {
    public static boolean isTimeValid(String time) {
        if (time == null || !time.matches("^\\d{2}:\\d{2}$")) return false;

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
    }
}
