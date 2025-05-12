package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ReviewAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private String userId;
    private boolean isLoading = false;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = currentUser.getUid(); // Automatically get UID from FirebaseAuth

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Your Reviews");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.myReviewsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(reviewAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                if (!isLoading && lm != null && lm.findLastVisibleItemPosition() >= reviewAdapter.getItemCount() - 2) {
                    loadUserReviews();
                }
            }
        });

        loadUserReviews();
    }


    private void loadUserReviews() {
        isLoading = true;
        int skip = currentPage * PAGE_SIZE;

        ReviewApi reviewApi = ApiClient.getReviewApi();
        reviewApi.getReviewsByUser(userId, PAGE_SIZE, skip).enqueue(new Callback<List<Review>>() {
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
                    Toast.makeText(MyReviewsActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
                Toast.makeText(MyReviewsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
