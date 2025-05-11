package com.example.cs_15_fyp.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ReviewImageAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReplyApi;
import com.example.cs_15_fyp.api.RestaurantService;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Reply;
import com.example.cs_15_fyp.models.Restaurant;
import com.example.cs_15_fyp.models.Review;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReviewDetailActivity extends AppCompatActivity {

    private TextView usernameText, commentText, photoCountText;
    private RatingBar ratingBar;
    private ViewPager2 imagePager;
    private ImageView singleImage;
    private LinearLayout repliesContainer;

    private Review review;
    private ReviewApi reviewApi;
    private ReplyApi replyApi;
    private RestaurantService restaurantService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Review Detail");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize API
        reviewApi = ApiClient.getReviewApi();
        replyApi = ApiClient.getReplyApi();
        restaurantService = ApiClient.getRestaurantApi();

        // Bind views
        repliesContainer = findViewById(R.id.detailReplies);
        usernameText = findViewById(R.id.detailReviewUsername);
        commentText = findViewById(R.id.detailReviewComment);
        ratingBar = findViewById(R.id.detailReviewRatingBar);
        photoCountText = findViewById(R.id.detailPhotoCount);
        imagePager = findViewById(R.id.detailImagePager);
        singleImage = findViewById(R.id.detailSingleImage);

        // Retrieve Review
        review = (Review) getIntent().getSerializableExtra("review");
        if (review != null) {
            // Populate review details
            usernameText.setText(review.getUsername());
            commentText.setText(review.getComment());
            ratingBar.setRating(review.getRating());

            // Photos
            List<String> photos = review.getPhotos();
            if (photos != null && !photos.isEmpty()) {
                photoCountText.setText(photos.size() + " photos");
                if (photos.size() == 1) {
                    singleImage.setVisibility(View.VISIBLE);
                    imagePager.setVisibility(View.GONE);
                    Glide.with(this).load(photos.get(0)).fitCenter().into(singleImage);
                } else {
                    singleImage.setVisibility(View.GONE);
                    imagePager.setVisibility(View.VISIBLE);
                    ReviewImageAdapter imageAdapter = new ReviewImageAdapter(this, photos);
                    imagePager.setAdapter(imageAdapter);
                }
            } else {
                photoCountText.setText("No photos");
                singleImage.setVisibility(View.GONE);
                imagePager.setVisibility(View.GONE);
            }

            // Display existing replies
            loadReplies(review.getId());
        }

        Button btnReply = findViewById(R.id.btnReply);
        btnReply.setOnClickListener(v -> showReplyDialog());
    }

    private void loadReplies(String reviewId) {
        replyApi.getReplies(reviewId).enqueue(new Callback<List<Reply>>() {
            @Override
            public void onResponse(Call<List<Reply>> call, Response<List<Reply>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Reply> replies = response.body();
                    repliesContainer.removeAllViews();

                    // First: Fetch the Review to get the restaurantId
                    reviewApi.getReviewById(reviewId).enqueue(new Callback<Review>() {
                        @Override
                        public void onResponse(Call<Review> call, Response<Review> reviewResponse) {
                            if (reviewResponse.isSuccessful() && reviewResponse.body() != null) {
                                String restaurantId = reviewResponse.body().getRestaurantId();

                                // Then: Fetch the Restaurant name using the restaurantId
                                restaurantService.getRestaurantById(restaurantId).enqueue(new Callback<Restaurant>() {
                                    @Override
                                    public void onResponse(Call<Restaurant> call, Response<Restaurant> restaurantResponse) {
                                        if (restaurantResponse.isSuccessful() && restaurantResponse.body() != null) {
                                            String restaurantName = restaurantResponse.body().getName();

                                            for (Reply r : replies) {
                                                TextView tv = new TextView(ReviewDetailActivity.this);
                                                tv.setText(restaurantName + ": " + r.getComment());
                                                tv.setTextSize(14);
                                                tv.setTextColor(Color.DKGRAY);
                                                tv.setPadding(16, 8, 16, 8);

                                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                );
                                                lp.setMargins(16, 8, 16, 8);
                                                tv.setLayoutParams(lp);

                                                repliesContainer.addView(tv);
                                            }
                                        } else {
                                            Toast.makeText(ReviewDetailActivity.this, "Failed to get restaurant name", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Restaurant> call, Throwable t) {
                                        Toast.makeText(ReviewDetailActivity.this, "Error fetching restaurant: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(ReviewDetailActivity.this, "Failed to fetch review", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Review> call, Throwable t) {
                            Toast.makeText(ReviewDetailActivity.this, "Error fetching review: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(ReviewDetailActivity.this, "Failed to load replies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reply>> call, Throwable t) {
                Toast.makeText(ReviewDetailActivity.this, "Error loading replies: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showReplyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reply to Review");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 8);


        final EditText inputComment = new EditText(this);
        inputComment.setHint("Type your reply here...");
        inputComment.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        layout.addView(inputComment);

        builder.setView(layout);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String comment = inputComment.getText().toString().trim();
            if (comment.isEmpty()) {
                Toast.makeText(this, "Please fill in the field", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            // take first 3 letters (or entire string if shorter)
            String username = email.length() >= 3 ? email.substring(0,3) : email;

            // Create Reply object
            Reply newReply = new Reply(review.getId(), email, username, comment);

            // Call backend
            replyApi.postReply(review.getId(), newReply).enqueue(new Callback<Reply>() {
                @Override
                public void onResponse(Call<Reply> call, Response<Reply> response) {
                    if (response.isSuccessful()) {
                        loadReplies(review.getId());    // reload replies using the original review id
                        Toast.makeText(ReviewDetailActivity.this, "Reply added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReviewDetailActivity.this, "Failed to add reply", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Reply> call, Throwable t) {
                    Toast.makeText(ReviewDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
