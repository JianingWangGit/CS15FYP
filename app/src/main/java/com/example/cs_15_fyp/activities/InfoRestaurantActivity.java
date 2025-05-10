package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ReviewAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class InfoRestaurantActivity extends AppCompatActivity {

    private ReviewAdapter adapter;
    private String restaurantId;
    private String restaurantNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI Elements
        Button btnGoToGiveReview = findViewById(R.id.btnGoToGiveReview);
        Button btnSeeAllReviews = findViewById(R.id.btnSeeAllReviews);
        EditText searchBar = findViewById(R.id.searchBar);
        TextView restaurantName = findViewById(R.id.restaurantName);
        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(this, new ArrayList<>());
        reviewRecyclerView.setAdapter(adapter);

        // Get data from intent
        Intent received = getIntent();
        if (received != null) {
            restaurantNameText = received.getStringExtra("restaurantName");
            restaurantId = received.getStringExtra("restaurantId");

            if (restaurantId == null || restaurantId.isEmpty()) {
                Toast.makeText(this, "restaurantId missing in InfoRestaurantActivity", Toast.LENGTH_LONG).show();
            }
        }

        if (restaurantNameText == null) restaurantNameText = "Restaurant Info";
        restaurantName.setText(restaurantNameText);
        getSupportActionBar().setTitle(restaurantNameText); // dynamic title

        btnGoToGiveReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, GiveReviewActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            intent.putExtra("restaurantName", restaurantNameText);
            startActivity(intent);
        });

        btnSeeAllReviews.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllReviewsActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            startActivity(intent);
        });

        loadPreviewReviews();
    }

    private void loadPreviewReviews() {
        if (restaurantId == null || restaurantId.isEmpty()) return;

        Retrofit retrofit = ApiClient.getClient();
        ReviewApi reviewApi = retrofit.create(ReviewApi.class);

        reviewApi.getReviewsForRestaurant(restaurantId, 2, 0).enqueue(new retrofit2.Callback<List<Review>>() {
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
        loadPreviewReviews();
    }
}
