package com.example.cs_15_fyp.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.cs_15_fyp.adapters.ReviewImageAdapter;
import com.example.cs_15_fyp.models.Review;
import com.example.cs_15_fyp.R;

import java.util.List;

public class ReviewDetailActivity extends AppCompatActivity {

    private TextView usernameText, commentText, photoCountText;
    private RatingBar ratingBar;
    private ViewPager2 imagePager;
    private ImageView singleImage;

    private LinearLayout repliesContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        repliesContainer = findViewById(R.id.detailReplies);

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

        Button btnreply = findViewById(R.id.btnReply);
        btnreply.setOnClickListener(v -> showreplyDialog());
    }

    private void showreplyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("reply to Review");

        final EditText input = new EditText(this);
        input.setHint("Type your reply here...");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String replyText = input.getText().toString().trim();
            if (!replyText.isEmpty()) {
                // Create and display the reply
                TextView replyView = new TextView(this);
                replyView.setText("You: " + replyText);
                replyView.setTextSize(14);
                replyView.setTextColor(Color.BLACK);
                replyView.setPadding(8, 4, 8, 4);

                repliesContainer.addView(replyView);
                repliesContainer.setVisibility(View.VISIBLE);

                Toast.makeText(this, "reply submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "reply cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
