package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.cs_15_fyp.adapters.ReviewImageAdapter;
import com.example.cs_15_fyp.models.Review;
import com.example.cs_15_fyp.R;

import java.util.List;

public class ReviewDetailActivity extends AppCompatActivity {

    private TextView usernameText, commentText, photoCountText;
    private RatingBar ratingBar;
    private ViewPager2 imagePager;
    private ImageView singleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        usernameText = findViewById(R.id.detailReviewUsername);
        commentText = findViewById(R.id.detailReviewComment);
        ratingBar = findViewById(R.id.detailReviewRatingBar);
        photoCountText = findViewById(R.id.detailPhotoCount);
        imagePager = findViewById(R.id.detailImagePager);
        singleImage = findViewById(R.id.detailSingleImage);

        // Get the passed Review object
        Review review = (Review) getIntent().getSerializableExtra("review");

        if (review != null) {
            usernameText.setText(review.getUsername());
            commentText.setText(review.getComment());
            ratingBar.setRating(review.getRating());
            
            List<String> photos = review.getPhotos();
            if (photos != null && !photos.isEmpty()) {
                photoCountText.setText(photos.size() + " photos");
                
                if (photos.size() == 1) {
                    // Show single image
                    singleImage.setVisibility(View.VISIBLE);
                    imagePager.setVisibility(View.GONE);
                    
                    Glide.with(this)
                        .load(photos.get(0))
                        .fitCenter()
                        .into(singleImage);
                } else if (photos.size() > 1) {
                    // Show multiple images in pager
                    singleImage.setVisibility(View.GONE);
                    imagePager.setVisibility(View.VISIBLE);
                    
                    ReviewImageAdapter imageAdapter = new ReviewImageAdapter(this, photos);
                    imagePager.setAdapter(imageAdapter);
                } else {
                    singleImage.setVisibility(View.GONE);
                    imagePager.setVisibility(View.GONE);
                }
            } else {
                photoCountText.setText("No photos");
                singleImage.setVisibility(View.GONE);
                imagePager.setVisibility(View.GONE);
            }
        }
    }
}
