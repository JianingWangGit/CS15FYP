package com.example.cs_15_fyp.models;

import com.google.gson.annotations.SerializedName;

public class Restaurant {
    @SerializedName("_id")
    private String id; // MongoDB ObjectId as String
    private String name;
    private String description;
    private String cuisine;
    private double rating;
    private String address;
    private String imageUrl;
    private boolean isOpen;
    private double priceRange; // 1-4 representing $, $$, $$$, $$$$

    public Restaurant(String id, String name, String description, String cuisine, 
                     double rating, String address, String imageUrl, 
                     boolean isOpen, double priceRange) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cuisine = cuisine;
        this.rating = rating;
        this.address = address;
        this.imageUrl = imageUrl;
        this.isOpen = isOpen;
        this.priceRange = priceRange;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCuisine() { return cuisine; }
    public double getRating() { return rating; }
    public String getAddress() { return address; }
    public String getImageUrl() { return imageUrl; }
    public boolean isOpen() { return isOpen; }
    public double getPriceRange() { return priceRange; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public void setRating(double rating) { this.rating = rating; }
    public void setAddress(String address) { this.address = address; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setOpen(boolean open) { isOpen = open; }
    public void setPriceRange(double priceRange) { this.priceRange = priceRange; }
} 