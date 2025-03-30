package com.example.cs_15_fyp;
public class Review {
    private String username;
    private String comment;
    private float rating;

    public Review(String username, String comment, float rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }
}

