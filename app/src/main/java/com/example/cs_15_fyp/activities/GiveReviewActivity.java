package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Base64;
import android.util.Log;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ImagePagerAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;
import com.example.cs_15_fyp.utils.FirebaseStorageHelper;

import java.io.InputStream;


public class GiveReviewActivity extends AppCompatActivity {
    private static final String TAG = "GiveReviewActivity";
    private RatingBar ratingBar;
    private TextView ratingDisplay;
    private EditText editTextReview;
    private Button btnSubmitReview;
    private ReviewApi reviewApi;

    private ArrayList<Uri> imageUris = new ArrayList<>();
    private ViewPager2 viewPagerPhotos;
    private ImagePagerAdapter imagePagerAdapter;
    private TextView photoCount;
    private Button btnUploadPhotos;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_review);

        // Initialize Firebase Storage
        FirebaseStorageHelper.initialize(this);

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
        viewPagerPhotos = findViewById(R.id.viewPagerPhotos);
        photoCount = findViewById(R.id.photoCount);
        btnUploadPhotos = findViewById(R.id.btnUploadPhotos);

        // Rating change listener
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            ratingDisplay.setText("Your rating: " + rating + " ⭐");
        });

        // Retrofit init
        Retrofit retrofit = ApiClient.getClient();
        reviewApi = retrofit.create(ReviewApi.class);

        // Submit button
        btnSubmitReview.setOnClickListener(v -> submitReview());

        // Upload button
        btnUploadPhotos.setOnClickListener(v -> openImagePicker());

        imagePagerAdapter = new ImagePagerAdapter(this, imageUris, () -> {
            photoCount.setText(imageUris.size() + " photos");
        });
        viewPagerPhotos.setAdapter(imagePagerAdapter);

        // Modern activity result launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                if (!imageUris.contains(imageUri)) {
                                    imageUris.add(imageUri);
                                }
                            }
                        } else if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            if (!imageUris.contains(imageUri)) {
                                imageUris.add(imageUri);
                            }
                        }
                        photoCount.setText(imageUris.size() + " photos");
                        imagePagerAdapter.notifyDataSetChanged(); // 👈 refresh ViewPager
                    }
                }
        );
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Pictures"));
    }

    private String convertImageUriToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            return Base64.encodeToString(bytes, Base64.NO_WRAP); // NO_WRAP is cleaner
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void submitReview() {
        String comment = editTextReview.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (rating == 0.0f) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get restaurantId from intent
        String restaurantId = getIntent().getStringExtra("restaurantId");
        if (restaurantId == null || restaurantId.isEmpty()) {
            Toast.makeText(this, "Restaurant ID is missing in Intent!", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(this, "Received restaurant ID: " + restaurantId, Toast.LENGTH_SHORT).show();
        }

        if (imageUris.isEmpty()) {
            // No images to upload, submit review directly
            submitReviewToApi(restaurantId, comment, rating, new ArrayList<>());
        } else {
            // Show upload in progress
            btnSubmitReview.setEnabled(false);
            Toast.makeText(this, "Uploading images...", Toast.LENGTH_SHORT).show();
            
            // Upload images to Firebase Storage
            FirebaseStorageHelper.uploadReviewImages(imageUris)
                .thenAccept(downloadUrls -> {
                    // Images uploaded successfully, submit review with image URLs
                    runOnUiThread(() -> {
                        submitReviewToApi(restaurantId, comment, rating, downloadUrls);
                    });
                })
                .exceptionally(e -> {
                    // Handle upload failure
                    runOnUiThread(() -> {
                        Log.e(TAG, "Failed to upload images", e);
                        Toast.makeText(GiveReviewActivity.this, 
                            "Failed to upload images: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnSubmitReview.setEnabled(true);
                    });
                    return null;
                });
        }
    }

    private void submitReviewToApi(String restaurantId, String comment, float rating, List<String> photoUrls) {
        // Submit Review with restaurantId
        Review review = new Review(restaurantId, "user123", comment, rating, photoUrls);

        reviewApi.submitReview(review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GiveReviewActivity.this, "Review submitted!", Toast.LENGTH_SHORT).show();
                    editTextReview.setText("");
                    ratingBar.setRating(0f);
                    imageUris.clear();
                    imagePagerAdapter.notifyDataSetChanged();
                    photoCount.setText("0 photos");

                    Intent intent = new Intent(GiveReviewActivity.this, InfoRestaurantActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(GiveReviewActivity.this, "Submission failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    btnSubmitReview.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(GiveReviewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                btnSubmitReview.setEnabled(true);
            }
        });
    }
}
