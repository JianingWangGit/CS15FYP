package com.example.cs_15_fyp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Reply implements Serializable{
    @SerializedName("_id")
    private String id;
    private String reviewId;
    private String email;
    private String username;
    private String comment;

    public Reply(String reviewId, String email, String username, String comment) {
        this.reviewId = reviewId;
        this.email = email;
        this.username = username;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getEmail() {
        return email;
    }
}
