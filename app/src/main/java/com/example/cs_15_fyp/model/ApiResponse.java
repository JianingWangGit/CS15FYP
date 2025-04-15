package com.example.cs_15_fyp.model;

import java.util.List;

public class ApiResponse<T> {
    private boolean success;
    private List<T> data;

    public boolean isSuccess() {
        return success;
    }

    public List<T> getData() {
        return data;
    }
} 