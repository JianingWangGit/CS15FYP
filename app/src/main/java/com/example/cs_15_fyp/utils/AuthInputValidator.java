package com.example.cs_15_fyp.utils;

import android.util.Patterns;

public class AuthInputValidator {
    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8 && password.length() <= 12
                && password.matches(".*\\d.*") && password.matches(".*[^a-zA-Z0-9].*");
    }
}
