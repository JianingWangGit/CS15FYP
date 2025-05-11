package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.adapters.ImagePagerAdapter;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.ReviewApi;
import com.example.cs_15_fyp.models.Review;
import com.example.cs_15_fyp.utils.FirebaseStorageHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Write a Review");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        FirebaseStorageHelper.initialize(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ratingBar = findViewById(R.id.ratingBar);
        ratingDisplay = findViewById(R.id.ratingDisplay);
        editTextReview = findViewById(R.id.editTextReview);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        viewPagerPhotos = findViewById(R.id.viewPagerPhotos);
        photoCount = findViewById(R.id.photoCount);
        btnUploadPhotos = findViewById(R.id.btnUploadPhotos);

        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            ratingDisplay.setText("Your rating: " + rating + " ⭐");
        });

        Retrofit retrofit = ApiClient.getClient();
        reviewApi = retrofit.create(ReviewApi.class);

        btnSubmitReview.setOnClickListener(v -> submitReview());
        btnUploadPhotos.setOnClickListener(v -> openImagePicker());

        imagePagerAdapter = new ImagePagerAdapter(this, imageUris, () -> {
            photoCount.setText(imageUris.size() + " photos");
        });
        viewPagerPhotos.setAdapter(imagePagerAdapter);

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
                        imagePagerAdapter.notifyDataSetChanged();
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

    private void submitReview() {
        String comment = editTextReview.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (rating == 0.0f) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        String restaurantId = getIntent().getStringExtra("restaurantId");
        if (restaurantId == null || restaurantId.isEmpty()) {
            Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You must be logged in to submit a review", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        if (imageUris.isEmpty()) {
            fetchUsernameAndSubmit(restaurantId, userId, comment, rating, new ArrayList<>());
        } else {
            btnSubmitReview.setEnabled(false);
            Toast.makeText(this, "Uploading your photos, please wait...", Toast.LENGTH_SHORT).show();

            FirebaseStorageHelper.uploadReviewImages(imageUris)
                    .thenAccept(downloadUrls -> runOnUiThread(() ->
                            fetchUsernameAndSubmit(restaurantId, userId, comment, rating, downloadUrls)))
                    .exceptionally(e -> {
                        runOnUiThread(() -> {
                            Toast.makeText(this,
                                    "Upload failed. Please check your internet connection and try again.",
                                    Toast.LENGTH_LONG).show();
                            btnSubmitReview.setEnabled(true);
                        });
                        return null;
                    });
        }
    }

    private void fetchUsernameAndSubmit(String restaurantId, String userId, String comment, float rating, List<String> photoUrls) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    String username = snapshot.getString("username");
                    if (username == null || username.isEmpty()) {
                        username = "anonymous";
                    }

                    Review review = new Review(restaurantId, userId, username, comment, rating, photoUrls);
                    submitReviewObject(review);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                    btnSubmitReview.setEnabled(true);
                });
    }

    private void submitReviewObject(Review review) {
        reviewApi.submitReview(review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GiveReviewActivity.this, "Thank you! Your review has been submitted.", Toast.LENGTH_SHORT).show();
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
