package com.example.cs_15_fyp.models;

import java.io.Serializable;
public class Reply implements Serializable{
    private String username;
    private String comment;

    public Reply(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }
}
