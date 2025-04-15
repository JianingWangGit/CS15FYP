package com.example.cs_15_fyp.models;

import java.util.List;

public class Review {
    private String username;
    private String comment;
    private float rating;

    private List<String> photos;

    public Review(String username, String comment, float rating, List<String> photos) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.photos = photos;
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

    public List<String> getPhotos() {
        return photos;
    }
}

