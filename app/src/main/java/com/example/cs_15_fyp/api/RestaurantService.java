package com.example.cs_15_fyp.api;

import com.example.cs_15_fyp.models.ApiResponse;
import com.example.cs_15_fyp.models.Restaurant;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("/restaurants/{id}")
    Call<Restaurant> getRestaurantById(@Path("id") String restaurantId);

    @GET("restaurants/business/{uid}")
    Call<ApiResponse<List<Restaurant>>> getRestaurantsByBusinessId(@Path("uid") String businessUid);

    @POST("restaurants")
    Call<ApiResponse<Restaurant>> createRestaurant(@Body Restaurant restaurant);

} 