package com.example.cs_15_fyp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GiveReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView ratingDisplay;
    private EditText editTextReview;
    private Button btnSubmitReview;

    private ReviewApi reviewApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_review);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Connect views
        ratingBar = findViewById(R.id.ratingBar);
        ratingDisplay = findViewById(R.id.ratingDisplay);
        editTextReview = findViewById(R.id.editTextReview);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);

        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            ratingDisplay.setText("Your rating: " + rating + " ⭐");
        });

        // Initialize Retrofit
        Retrofit retrofit = ApiClient.getClient();
        reviewApi = retrofit.create(ReviewApi.class);

        // On submit review button click
        btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        String comment = editTextReview.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (comment.isEmpty() || rating == 0.0f) {
            Toast.makeText(this, "Please enter a comment and select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review("user123", comment, rating);

        reviewApi.submitReview(review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GiveReviewActivity.this, "Review submitted!", Toast.LENGTH_SHORT).show();
                    editTextReview.setText("");
                    ratingBar.setRating(0f);
                } else {
                    Toast.makeText(GiveReviewActivity.this, "Submission failed: " + response.code(), Toast.LENGTH_SHORT).show();

                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error body: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(GiveReviewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
