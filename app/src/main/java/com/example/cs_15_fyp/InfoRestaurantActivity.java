package com.example.cs_15_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cs_15_fyp.adapters.ReviewAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class InfoRestaurantActivity extends AppCompatActivity {

    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI elements
        Button btnGoToGiveReview = findViewById(R.id.btnGoToGiveReview);
        Button btnSeeAllReviews = findViewById(R.id.btnSeeAllReviews);
        EditText searchBar = findViewById(R.id.searchBar);
        TextView restaurantName = findViewById(R.id.restaurantName);

        // RecyclerView setup
        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(new ArrayList<>());
        reviewRecyclerView.setAdapter(adapter);

        // Write review button
        btnGoToGiveReview.setOnClickListener(v -> {
            Intent intent = new Intent(InfoRestaurantActivity.this, GiveReviewActivity.class);
            startActivity(intent);
        });

        // See all reviews button
        btnSeeAllReviews.setOnClickListener(v -> {
            Intent intent = new Intent(InfoRestaurantActivity.this, AllReviewsActivity.class);
            startActivity(intent);
        });

        // Load reviews preview
        loadPreviewReviews();
    }

    private void loadPreviewReviews() {
        Retrofit retrofit = ApiClient.getClient();
        ReviewApi reviewApi = retrofit.create(ReviewApi.class);

        reviewApi.getReviews().enqueue(new retrofit2.Callback<List<Review>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Review>> call, retrofit2.Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> allReviews = response.body();
                    List<Review> preview = allReviews.size() > 2 ? allReviews.subList(0, 2) : allReviews;
                    adapter.updateData(preview);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Review>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreviewReviews(); // refresh preview on resume
    }

}
