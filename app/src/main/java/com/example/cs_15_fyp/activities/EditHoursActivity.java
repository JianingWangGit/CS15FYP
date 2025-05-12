package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.RestaurantService;
import com.example.cs_15_fyp.models.Restaurant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditHoursActivity extends AppCompatActivity {
    private static final String TAG = "EditHoursActivity";
    
    private EditText mondayOpen, mondayClose;
    private EditText tuesdayOpen, tuesdayClose;
    private EditText wednesdayOpen, wednesdayClose;
    private EditText thursdayOpen, thursdayClose;
    private EditText fridayOpen, fridayClose;
    private EditText saturdayOpen, saturdayClose;
    private EditText sundayOpen, sundayClose;
    private Button btnSaveHours;
    
    private Restaurant currentRestaurant;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hours);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Edit Opening Hours");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize views
        initializeViews();
        
        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra("restaurantId");
        if (restaurantId == null) {
            Toast.makeText(this, "Restaurant ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Load restaurant data
        loadRestaurantData(restaurantId);
        
        // Save button click listener
        btnSaveHours.setOnClickListener(v -> saveOpeningHours());
    }
    
    private void initializeViews() {
        mondayOpen = findViewById(R.id.monday_open);
        mondayClose = findViewById(R.id.monday_close);
        tuesdayOpen = findViewById(R.id.tuesday_open);
        tuesdayClose = findViewById(R.id.tuesday_close);
        wednesdayOpen = findViewById(R.id.wednesday_open);
        wednesdayClose = findViewById(R.id.wednesday_close);
        thursdayOpen = findViewById(R.id.thursday_open);
        thursdayClose = findViewById(R.id.thursday_close);
        fridayOpen = findViewById(R.id.friday_open);
        fridayClose = findViewById(R.id.friday_close);
        saturdayOpen = findViewById(R.id.saturday_open);
        saturdayClose = findViewById(R.id.saturday_close);
        sundayOpen = findViewById(R.id.sunday_open);
        sundayClose = findViewById(R.id.sunday_close);
        btnSaveHours = findViewById(R.id.btn_save_hours);
    }
    
    private void loadRestaurantData(String restaurantId) {
        RestaurantService service = ApiClient.getClient().create(RestaurantService.class);
        service.getRestaurantById(restaurantId).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentRestaurant = response.body();
                    populateHoursFields(currentRestaurant.getHours());
                } else {
                    try {
                        Log.e(TAG, "Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(EditHoursActivity.this, "Failed to load restaurant data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.e(TAG, "Failed to load restaurant", t);
                Toast.makeText(EditHoursActivity.this, "Failed to load restaurant: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void populateHoursFields(Map<String, Map<String, String>> hours) {
        if (hours == null) {
            hours = new HashMap<>();
        }
        
        setTimeFields(mondayOpen, mondayClose, hours.get("Monday"));
        setTimeFields(tuesdayOpen, tuesdayClose, hours.get("Tuesday"));
        setTimeFields(wednesdayOpen, wednesdayClose, hours.get("Wednesday"));
        setTimeFields(thursdayOpen, thursdayClose, hours.get("Thursday"));
        setTimeFields(fridayOpen, fridayClose, hours.get("Friday"));
        setTimeFields(saturdayOpen, saturdayClose, hours.get("Saturday"));
        setTimeFields(sundayOpen, sundayClose, hours.get("Sunday"));
    }
    
    private void setTimeFields(EditText openField, EditText closeField, Map<String, String> dayHours) {
        if (dayHours != null) {
            String openTime = dayHours.get("open");
            String closeTime = dayHours.get("close");
            
            if (openTime != null && !openTime.isEmpty()) {
                openField.setText(openTime);
            }
            
            if (closeTime != null && !closeTime.isEmpty()) {
                closeField.setText(closeTime);
            }
        }
    }
    
    private void saveOpeningHours() {
        if (currentRestaurant == null) {
            Toast.makeText(this, "Restaurant data not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create a new hours map
        Map<String, Map<String, String>> hours = new HashMap<>();
        
        // Add each day's hours if both fields are filled
        addDayHours(hours, "Monday", mondayOpen, mondayClose);
        addDayHours(hours, "Tuesday", tuesdayOpen, tuesdayClose);
        addDayHours(hours, "Wednesday", wednesdayOpen, wednesdayClose);
        addDayHours(hours, "Thursday", thursdayOpen, thursdayClose);
        addDayHours(hours, "Friday", fridayOpen, fridayClose);
        addDayHours(hours, "Saturday", saturdayOpen, saturdayClose);
        addDayHours(hours, "Sunday", sundayOpen, sundayClose);
        
        // Update the restaurant's hours
        currentRestaurant.setHours(hours);
        
        // Save to server
        updateRestaurant();
    }
    
    private void addDayHours(Map<String, Map<String, String>> hours, String day, EditText openField, EditText closeField) {
        String openTime = openField.getText().toString().trim();
        String closeTime = closeField.getText().toString().trim();
        
        // Only add if both times are provided
        if (!openTime.isEmpty() && !closeTime.isEmpty()) {
            Map<String, String> dayHours = new HashMap<>();
            dayHours.put("open", openTime);
            dayHours.put("close", closeTime);
            hours.put(day, dayHours);
        }
    }
    
    private void updateRestaurant() {
        RestaurantService service = ApiClient.getClient().create(RestaurantService.class);
        service.updateRestaurant(restaurantId, currentRestaurant).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditHoursActivity.this, "Opening hours updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                    } catch (IOException e) {
                        errorBody = "Could not read error body: " + e.getMessage();
                    }
                    Log.e(TAG, "Failed to update restaurant. Code: " + response.code() + ", Error: " + errorBody);
                    Toast.makeText(EditHoursActivity.this, "Failed to update opening hours", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.e(TAG, "Error updating restaurant", t);
                Toast.makeText(EditHoursActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 