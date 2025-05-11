package com.example.cs_15_fyp.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.List;

public class Restaurant {
    @SerializedName("_id")
    private String id; // MongoDB ObjectId as String
    private String name;
    private String description;
    private String cuisine;
    private double rating;
    private String address;
    private String imageUrl;
    @SerializedName("menuImages")
    private List<String> menuImages;
    private Map<String, Map<String, String>> hours;
    private double priceRange; // 1-4 representing $, $$, $$$, $$$$

    public Restaurant(String id, String name, String description, String cuisine, 
                     double rating, String address, String imageUrl,
                     List<String> menuImages,
                     Map<String, Map<String, String>> hours, double priceRange) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cuisine = cuisine;
        this.rating = rating;
        this.address = address;
        this.imageUrl = imageUrl;
        this.menuImages = menuImages;
        this.hours = hours;
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
    public List<String> getMenuImages() { return menuImages; }
    public Map<String, Map<String, String>> getHours() { return hours; }
    public double getPriceRange() { return priceRange; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setRating(double rating) { this.rating = rating; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public void setAddress(String address) { this.address = address; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setMenuImages(List<String> menuImages) { this.menuImages = menuImages; }
    public void setHours(Map<String, Map<String, String>> hours) { this.hours = hours; }
    public void setPriceRange(double priceRange) { this.priceRange = priceRange; }

    public boolean isOpen() {
        if (hours == null) return false;

        // Set timezone to Melbourne
        TimeZone melbourneTimeZone = TimeZone.getTimeZone("Australia/Melbourne");
        Calendar calendar = Calendar.getInstance(melbourneTimeZone);
        String currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        
        Map<String, String> dayHours = hours.get(currentDay);
        if (dayHours == null) return false;

        String openTime = dayHours.get("open");
        String closeTime = dayHours.get("close");
        if (openTime == null || closeTime == null) return false;

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        timeFormat.setTimeZone(melbourneTimeZone);
        String currentTime = timeFormat.format(calendar.getTime());

        return currentTime.compareTo(openTime) >= 0 && currentTime.compareTo(closeTime) <= 0;
    }
} 