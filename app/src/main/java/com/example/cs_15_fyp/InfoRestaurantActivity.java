package com.example.cs_15_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InfoRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        // Enable edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Views Creation
        Button btnGoToGiveReview = findViewById(R.id.btnGoToGiveReview);
        EditText searchBar = findViewById(R.id.searchBar);
        TextView restaurantName = findViewById(R.id.restaurantName);
        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecyclerView);


        btnGoToGiveReview.setOnClickListener(v -> {
            Intent intent = new Intent(InfoRestaurantActivity.this, GiveReviewActivity.class);
            startActivity(intent);
        });


        //  Dummy review data
        restaurantName.setText("Delicious Dumplings");


        // Set up RecyclerView
//        ReviewAdapter adapter = new ReviewAdapter(reviews);
//        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        reviewRecyclerView.setAdapter(adapter);
    }

}
