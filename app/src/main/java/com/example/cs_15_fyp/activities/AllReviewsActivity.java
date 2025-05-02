package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ReviewAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllReviewsActivity extends AppCompatActivity {

    private RecyclerView allReviewsRecyclerView;
    private ReviewAdapter reviewAdapter;

    private static final int PAGE_SIZE = 10;  // Number of reviews to fetch at a time
    private int currentPage = 0;
    private boolean isLoading = false;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

        // Receive restaurantId
        Intent intent = getIntent();
        if (intent != null) {
            restaurantId = getIntent().getStringExtra("restaurantId");
        }

        // Setup RecyclerView and Adapter
        allReviewsRecyclerView = findViewById(R.id.allReviewsRecyclerView);
        allReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(this, new ArrayList<>());
        allReviewsRecyclerView.setAdapter(reviewAdapter);

        reviewAdapter.setOnItemClickListener(review -> {
            Intent detailIntent = new Intent(AllReviewsActivity.this, ReviewDetailActivity.class);
            detailIntent.putExtra("review", review);
            startActivity(detailIntent);
        });

        // Setup infinite scroll listener
        allReviewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null &&
                        layoutManager.findLastVisibleItemPosition() >= reviewAdapter.getItemCount() - 2) {
                    loadAllReviews();
                }
            }
        });

        // Initial load
        loadAllReviews();
    }

    private void loadAllReviews() {
        if (restaurantId == null || restaurantId.isEmpty()) return; // Invalid

        isLoading = true;
        int skip = currentPage * PAGE_SIZE;
        ReviewApi reviewApi = ApiClient.getReviewApi();

        reviewApi.getReviewsForRestaurant(restaurantId, PAGE_SIZE, skip).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> newReviews = response.body();
                    if (!newReviews.isEmpty()) {
                        reviewAdapter.appendData(newReviews);
                        currentPage++;
                    }
                } else {
                    Toast.makeText(AllReviewsActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(AllReviewsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
