package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_15_fyp.models.Review;
import com.example.cs_15_fyp.R;

public class ReviewDetailActivity extends AppCompatActivity {

    private TextView usernameText, commentText, photoCountText;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        usernameText = findViewById(R.id.detailReviewUsername);
        commentText = findViewById(R.id.detailReviewComment);
        ratingBar = findViewById(R.id.detailReviewRatingBar);
        photoCountText = findViewById(R.id.detailPhotoCount);

        // Get the passed Review object
        Review review = (Review) getIntent().getSerializableExtra("review");

        if (review != null) {
            usernameText.setText(review.getUsername());
            commentText.setText(review.getComment());
            ratingBar.setRating(review.getRating());
            if (review.getPhotos() != null) {
                photoCountText.setText(review.getPhotos().size() + " photos");
            } else {
                photoCountText.setText("No photos");
            }
        }
    }
}
