package com.example.cs_15_fyp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.RestaurantService;
import com.example.cs_15_fyp.models.Restaurant;
import com.example.cs_15_fyp.utils.FirebaseStorageHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRestaurantActivity extends AppCompatActivity {
    private static final String TAG = "EditRestaurantActivity";
    private EditText nameInput, descInput, cuisineInput, addressInput;
    private ImageView restaurantImage;
    private Button btnUpdate, btnUploadImage, btnEditHours;
    private Restaurant currentRestaurant;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        // Initialize Firebase Storage
        FirebaseStorageHelper.initialize(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Edit Restaurant");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize views
        nameInput = findViewById(R.id.input_name);
        descInput = findViewById(R.id.input_description);
        cuisineInput = findViewById(R.id.input_cuisine);
        addressInput = findViewById(R.id.input_address);
        restaurantImage = findViewById(R.id.restaurant_image);
        btnUpdate = findViewById(R.id.btn_update);
        btnUploadImage = findViewById(R.id.btn_upload_image);
        btnEditHours = findViewById(R.id.btn_edit_hours);

        // Get restaurant ID from intent
        String restaurantId = getIntent().getStringExtra("restaurantId");
        if (restaurantId == null) {
            Toast.makeText(this, "Restaurant ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load restaurant data
        loadRestaurantData(restaurantId);

        // Setup image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        restaurantImage.setImageURI(selectedImageUri);
                    }
                }
        );

        // Setup click listeners
        btnUploadImage.setOnClickListener(v -> openImagePicker());
        btnUpdate.setOnClickListener(v -> updateRestaurant());
        btnEditHours.setOnClickListener(v -> {
            Intent intent = new Intent(EditRestaurantActivity.this, EditHoursActivity.class);
            intent.putExtra("restaurantId", currentRestaurant.getId());
            startActivityForResult(intent, 2); // Use a different request code than the image picker
        });
    }

    private void loadRestaurantData(String restaurantId) {
        RestaurantService service = ApiClient.getClient().create(RestaurantService.class);
        service.getRestaurantById(restaurantId).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentRestaurant = response.body();
                    populateFields();
                } else {
                    Toast.makeText(EditRestaurantActivity.this, 
                            "Failed to load restaurant data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Toast.makeText(EditRestaurantActivity.this, 
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateFields() {
        nameInput.setText(currentRestaurant.getName());
        descInput.setText(currentRestaurant.getDescription());
        cuisineInput.setText(currentRestaurant.getCuisine());
        addressInput.setText(currentRestaurant.getAddress());

        // Load current image
        if (currentRestaurant.getImageUrl() != null && !currentRestaurant.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentRestaurant.getImageUrl())
                    .centerCrop()
                    .into(restaurantImage);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Restaurant Image"));
    }

    private void updateRestaurant() {
        String name = nameInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();
        String cuisine = cuisineInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (name.isEmpty() || desc.isEmpty() || cuisine.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        btnUpdate.setEnabled(false);
        Toast.makeText(this, "Updating restaurant...", Toast.LENGTH_SHORT).show();

        if (selectedImageUri != null) {
            // Upload new image first
            uploadImageAndUpdateRestaurant(name, desc, cuisine, address);
        } else {
            // Update without changing image
            updateRestaurantData(name, desc, cuisine, address, currentRestaurant.getImageUrl());
        }
    }

    private void uploadImageAndUpdateRestaurant(String name, String desc, String cuisine, String address) {
        String fileName = "restaurant_" + UUID.randomUUID().toString() + ".jpg";
        List<Uri> imageUris = new ArrayList<>();
        imageUris.add(selectedImageUri);

        FirebaseStorageHelper.uploadReviewImages(imageUris)
                .thenAccept(downloadUrls -> {
                    if (!downloadUrls.isEmpty()) {
                        String newImageUrl = downloadUrls.get(0);
                        updateRestaurantData(name, desc, cuisine, address, newImageUrl);
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            btnUpdate.setEnabled(true);
                        });
                    }
                })
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error uploading image: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        btnUpdate.setEnabled(true);
                    });
                    return null;
                });
    }

    private void updateRestaurantData(String name, String desc, String cuisine, 
                                    String address, String imageUrl) {
        currentRestaurant.setName(name);
        currentRestaurant.setDescription(desc);
        currentRestaurant.setCuisine(cuisine);
        currentRestaurant.setAddress(address);
        currentRestaurant.setImageUrl(imageUrl);

        RestaurantService service = ApiClient.getClient().create(RestaurantService.class);
        service.updateRestaurant(currentRestaurant.getId(), currentRestaurant)
                .enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditRestaurantActivity.this, 
                                    "Restaurant updated successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            String errorBody = "";
                            try {
                                errorBody = response.errorBody() != null ? 
                                    response.errorBody().string() : "No error body";
                            } catch (IOException e) {
                                errorBody = "Could not read error body: " + e.getMessage();
                            }
                            Log.e(TAG, "Failed to update restaurant. Code: " + response.code() + 
                                    ", Error: " + errorBody);
                            Toast.makeText(EditRestaurantActivity.this, 
                                    "Failed to update restaurant: " + errorBody, Toast.LENGTH_LONG).show();
                            btnUpdate.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {
                        Log.e(TAG, "Error updating restaurant", t);
                        Toast.makeText(EditRestaurantActivity.this, 
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        btnUpdate.setEnabled(true);
                    }
                });
    }
} 