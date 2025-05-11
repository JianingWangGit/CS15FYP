package com.example.cs_15_fyp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Review implements Serializable {

    @SerializedName("_id")
    private String id;
    private String restaurantId;
    private String username;
    private String comment;
    private float rating;

    private String userId;

    private List<String> photos;

    private List<Reply> replies = new ArrayList<>();

    public Review(String restaurantId, String userId, String username, String comment, float rating, List<String> photos) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.photos = photos;
    }

    // Getters and Setters
    public String getId() {
        return id;
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void addReply(Reply reply) {
        this.replies.add(reply);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

