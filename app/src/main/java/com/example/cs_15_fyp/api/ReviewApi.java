package com.example.cs_15_fyp.api;

import com.example.cs_15_fyp.models.Review;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReviewApi {
    @GET("/reviews")
    Call<List<Review>> getReviews(@Query("limit") int limit, @Query("skip") int skip);

    @POST("/reviews")
    Call<Review> submitReview(@Body Review review);
}

