package com.example.cs_15_fyp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.api.ApiClient;
import com.example.cs_15_fyp.api.RestaurantService;
import com.example.cs_15_fyp.models.ApiResponse;
import com.example.cs_15_fyp.models.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRestaurantActivity extends AppCompatActivity {

    private EditText nameInput, descInput, cuisineInput, addressInput;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        nameInput = findViewById(R.id.input_name);
        descInput = findViewById(R.id.input_description);
        cuisineInput = findViewById(R.id.input_cuisine);
        addressInput = findViewById(R.id.input_address);
        createBtn = findViewById(R.id.button_create);

        createBtn.setOnClickListener(v -> submitRestaurant());
    }

    private void submitRestaurant() {
        String name = nameInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();
        String cuisine = cuisineInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(cuisine) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Default hours to empty or null
        Map<String, Map<String, String>> hours = null;

        Restaurant restaurant = new Restaurant(
                name,
                desc,
                cuisine,
                2.0,                   // default rating
                address,
                "https://via.placeholder.com/400", // dummy image
                new HashMap<>(),       // empty hours
                1,                     // dummy price range
                user.getUid()
        );

        RestaurantService service = ApiClient.getClient().create(RestaurantService.class);
        service.createRestaurant(restaurant).enqueue(new Callback<ApiResponse<Restaurant>>() {
            @Override
            public void onResponse(Call<ApiResponse<Restaurant>> call, Response<ApiResponse<Restaurant>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateRestaurantActivity.this, "Restaurant created!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateRestaurantActivity.this, "Creation failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Restaurant>> call, Throwable t) {
                Toast.makeText(CreateRestaurantActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}