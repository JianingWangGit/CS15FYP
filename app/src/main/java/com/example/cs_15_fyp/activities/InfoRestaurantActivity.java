package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ReviewAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.RestaurantService;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.ApiResponse;
import com.example.cs_15_fyp.models.Restaurant;
import com.example.cs_15_fyp.models.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoRestaurantActivity extends AppCompatActivity {

    private ReviewAdapter adapter;
    private String restaurantId;
    private String restaurantNameText;
    private TextView ratingNumberText;

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
        TextView restaurantName = findViewById(R.id.restaurantName);
        ratingNumberText = findViewById(R.id.ratingNumber);
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
        getSupportActionBar().setTitle(restaurantNameText);

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

        loadRestaurantDetails();
        loadPreviewReviews();
    }

    private void loadRestaurantDetails() {
        RestaurantService restaurantService = ApiClient.getRestaurantApi();
        restaurantService.getAllRestaurants().enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call, Response<ApiResponse<List<Restaurant>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Restaurant r : response.body().getData()) {
                        if (r.getId().equals(restaurantId)) {
                            ratingNumberText.setText(String.format("%.1f", r.getRating()));

                            ImageView restaurantImage = findViewById(R.id.restaurantImage);
                            if (r.getImageUrl() != null && !r.getImageUrl().isEmpty()) {
                                Glide.with(InfoRestaurantActivity.this)
                                        .load(r.getImageUrl())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .placeholder(R.drawable.placeholder_restaurant)
                                        .error(R.drawable.placeholder_restaurant)
                                        .into(restaurantImage);
                            } else {
                                restaurantImage.setImageResource(R.drawable.placeholder_restaurant);
                            }

                            // Load menu images
                            LinearLayout menuImagesContainer = findViewById(R.id.menuImagesContainer);
                            View menuSection = findViewById(R.id.menuSection); // Add this ID to the parent LinearLayout in XML
                            menuImagesContainer.removeAllViews(); // Clear existing views
                            
                            if (r.getMenuImages() != null && !r.getMenuImages().isEmpty()) {
                                menuSection.setVisibility(View.VISIBLE);
                                for (String imageUrl : r.getMenuImages()) {
                                    ImageView imageView = new ImageView(InfoRestaurantActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            (int) (240 * getResources().getDisplayMetrics().density), // convert dp to pixels
                                            (int) (180 * getResources().getDisplayMetrics().density)  // convert dp to pixels
                                    );
                                    params.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density)); // convert dp to pixels
                                    imageView.setLayoutParams(params);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    imageView.setBackgroundResource(R.drawable.rounded_image_background);
                                    
                                    Glide.with(InfoRestaurantActivity.this)
                                        .load(imageUrl)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .placeholder(R.drawable.placeholder_restaurant)
                                        .error(R.drawable.placeholder_restaurant)
                                        .into(imageView);
                                    
                                    menuImagesContainer.addView(imageView);
                                }
                            } else {
                                menuSection.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                Log.e("InfoRestaurant", "Failed to load restaurant details", t);
            }
        });
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
        loadRestaurantDetails();
        loadPreviewReviews();
    }
}
