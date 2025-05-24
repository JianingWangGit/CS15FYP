package com.example.cs_15_fyp.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class AuthInputValidatorTest {

    @Test
    public void testValidEmail() {
        assertTrue(AuthInputValidator.isEmailValid("test@email.com"));
        assertFalse(AuthInputValidator.isEmailValid("bad-email"));
    }

    @Test
    public void testValidPassword() {
        assertTrue(AuthInputValidator.isPasswordValid("Pass@123"));
        assertFalse(AuthInputValidator.isPasswordValid("short"));
        assertFalse(AuthInputValidator.isPasswordValid("NoSpecial123"));
    }
}
