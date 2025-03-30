package com.example.cs_15_fyp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReviewApi {
    @GET("/reviews")
    Call<List<Review>> getReviews();

    @POST("/reviews")
    Call<Review> submitReview(@Body Review review);
}

