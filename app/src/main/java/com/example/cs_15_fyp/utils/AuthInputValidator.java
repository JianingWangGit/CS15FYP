package com.example.cs_15_fyp.utils;

import android.util.Patterns;

public class AuthInputValidator {

    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        if (password == null) return false;
        if (password.length() < 8 || password.length() > 12) return false;
        if (!password.matches(".*\\d.*")) return false; // Must include a digit
        if (!password.matches(".*[^a-zA-Z0-9].*")) return false; // Must include special char
        return true;
    }
}
