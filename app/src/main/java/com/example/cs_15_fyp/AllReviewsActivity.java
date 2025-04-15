package com.example.cs_15_fyp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.adapters.ReviewAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllReviewsActivity extends AppCompatActivity {

    private ReviewAdapter reviewAdapter;
    private RecyclerView allReviewsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

        allReviewsRecyclerView = findViewById(R.id.allReviewsRecyclerView);
        allReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(new ArrayList<>());
        allReviewsRecyclerView.setAdapter(reviewAdapter);

        loadAllReviews();
    }

    private void loadAllReviews() {
        Retrofit retrofit = ApiClient.getClient();
        ReviewApi reviewApi = retrofit.create(ReviewApi.class);

        reviewApi.getReviews().enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewAdapter.updateData(response.body());
                } else {
                    Toast.makeText(AllReviewsActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AllReviewsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
