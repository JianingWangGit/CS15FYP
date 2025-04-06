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


import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class InfoRestaurantActivity extends AppCompatActivity {

    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        // Enable edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Views Creation
        Button btnGoToGiveReview = findViewById(R.id.btnGoToGiveReview);
        EditText searchBar = findViewById(R.id.searchBar);
        TextView restaurantName = findViewById(R.id.restaurantName);

        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(new ArrayList<>());
        reviewRecyclerView.setAdapter(adapter);


        btnGoToGiveReview.setOnClickListener(v -> {
            Intent intent = new Intent(InfoRestaurantActivity.this, GiveReviewActivity.class);
            startActivity(intent);
        });

        loadReviews();

        // Inside onCreate()
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Review> reviewList = new ArrayList<>();
        ReviewAdapter adapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setAdapter(adapter);

        // Load real reviews from backend
        Retrofit retrofit = ApiClient.getClient();
        ReviewApi reviewApi = retrofit.create(ReviewApi.class);

        reviewApi.getReviews().enqueue(new retrofit2.Callback<List<Review>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Review>> call, retrofit2.Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Review>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadReviews() {
        Retrofit retrofit = ApiClient.getClient();
        ReviewApi reviewApi = retrofit.create(ReviewApi.class);

        reviewApi.getReviews().enqueue(new retrofit2.Callback<List<Review>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Review>> call, retrofit2.Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
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
        loadReviews();  // reload reviews every time this screen becomes visible
    }


}
