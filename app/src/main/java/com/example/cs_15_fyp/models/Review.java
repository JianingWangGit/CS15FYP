package com.example.cs_15_fyp.models;

import java.util.List;
import java.io.Serializable;

public class Review implements Serializable {

    private String restaurantId;
    private String username;
    private String comment;
    private float rating;

    private List<String> photos;

    public Review(String restaurantId, String username, String comment, float rating, List<String> photos) {
        this.restaurantId = restaurantId;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.photos = photos;
    }

    public String getRestaurantId() {
        return restaurantId;
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

