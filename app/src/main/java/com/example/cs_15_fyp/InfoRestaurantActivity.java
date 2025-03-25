package com.example.cs_15_fyp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_restaurant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Step 1: Connect views with findViewById
        EditText searchBar = findViewById(R.id.searchBar);
        TextView restaurantName = findViewById(R.id.restaurantName);
        RatingBar restaurantRating = findViewById(R.id.restaurantRating);
        ImageView restaurantLogo = findViewById(R.id.restaurantLogo);
        Button btnFavorites = findViewById(R.id.btnFavorites);
        Button btnFilter = findViewById(R.id.btnFilter);

        // Example usage
        restaurantName.setText("Delicious Dumplings");
        restaurantRating.setRating(4.5f);
    }

}