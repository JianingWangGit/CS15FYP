package com.example.cs_15_fyp.api;

import com.example.cs_15_fyp.models.Reply;
import com.example.cs_15_fyp.models.Review;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApi {
    @GET("/reviews")
    Call<List<Review>> getReviewsForRestaurant(
            @Query("restaurantId") String restaurantId,   // ✅ change to String
            @Query("limit") int limit,
            @Query("skip") int skip
    );



    @POST("/reviews")
    Call<Review> submitReview(@Body Review review);

    @GET("/reviews/{id}")
    Call<Review> getReviewById(@Path("id") String reviewId);
}

