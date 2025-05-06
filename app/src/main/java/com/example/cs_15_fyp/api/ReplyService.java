package com.example.cs_15_fyp.api;

import com.example.cs_15_fyp.models.ApiResponse;
import com.example.cs_15_fyp.models.Reply;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReplyService {
    @GET("responses")
    Call<ApiResponse<List<Reply>>> getAllReplies();

    @POST("response")
    Call<Reply> submitResponse(String ID, @Body Reply response);

}
