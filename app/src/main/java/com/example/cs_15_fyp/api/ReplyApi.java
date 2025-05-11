package com.example.cs_15_fyp.api;

import com.example.cs_15_fyp.models.Reply;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReplyApi {

    @GET("/responses/{reviewId}/replies")
    Call<List<Reply>> getReplies(@Path("reviewId") String reviewId);

    @POST("/responses/{reviewId}/reply")
    Call<Reply> postReply(@Path("reviewId") String reviewId, @Body Reply reply);
}
