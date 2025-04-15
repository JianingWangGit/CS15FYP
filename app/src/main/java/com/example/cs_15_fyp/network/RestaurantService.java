package com.example.cs_15_fyp.network;

import com.example.cs_15_fyp.model.Restaurant;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantService {
    @GET("restaurants")
    Call<ApiResponse<List<Restaurant>>> getAllRestaurants();

    @GET("restaurants/search")
    Call<ApiResponse<List<Restaurant>>> searchRestaurants(@Query("query") String query);

    @GET("restaurants/cuisine")
    Call<ApiResponse<List<Restaurant>>> getRestaurantsByCuisine(@Query("cuisine") String cuisine);

    @GET("restaurants/price")
    Call<ApiResponse<List<Restaurant>>> getRestaurantsByPrice(@Query("priceRange") int priceRange);
} 