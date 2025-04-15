package com.example.cs_15_fyp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    private RecyclerView allReviewsRecyclerView;
    private ReviewAdapter reviewAdapter;

    private static final int PAGE_SIZE = 10;  // Number of reviews to fetch at a time
    private int currentPage = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

        // Setup RecyclerView and Adapter
        allReviewsRecyclerView = findViewById(R.id.allReviewsRecyclerView);
        allReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(new ArrayList<>());
        allReviewsRecyclerView.setAdapter(reviewAdapter);

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
        isLoading = true;
        int skip = currentPage * PAGE_SIZE;

        Retrofit retrofit = ApiClient.getClient();
        ReviewApi reviewApi = retrofit.create(ReviewApi.class);

        reviewApi.getReviews(PAGE_SIZE, skip).enqueue(new Callback<List<Review>>() {
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
