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

    @GET("reviews/{id}/replies")
    Call<List<Reply>> getReplies(@Path("id") String reviewId);
    @POST("reviews/{id}/reply")
    Call<Review> postReply(
        @Path("id") String reviewId,
        @Body Reply reply
    );
    @PUT("/reviews/{id}/replies/{replyId}")
    Call<Review> updateReply(
            @Path("id") String reviewId,
            @Path("replyId") String replyId,
            @Body Reply reply
    );

    @HTTP(method = "DELETE", path = "/reviews/{id}/replies/{replyId}", hasBody = true)
    Call<Review> deleteReply(
            @Path("id") String reviewId,
            @Path("replyId") String replyId,
            @Body String email
    );
}

