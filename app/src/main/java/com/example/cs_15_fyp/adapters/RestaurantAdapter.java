package com.example.cs_15_fyp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private List<Restaurant> restaurants;
    private OnRestaurantClickListener listener;

    public interface OnRestaurantClickListener {
        void onRestaurantClick(Restaurant restaurant);
    }

    public RestaurantAdapter(OnRestaurantClickListener listener) {
        this.restaurants = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public void filterRestaurants(List<Restaurant> filteredRestaurants) {
        this.restaurants = filteredRestaurants;
        notifyDataSetChanged();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private ImageView restaurantImage;
        private TextView restaurantName;
        private RatingBar ratingBar;
        private TextView priceRange;
        private TextView cuisineType;
        private TextView address;
        private TextView openStatus;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            priceRange = itemView.findViewById(R.id.priceRange);
            cuisineType = itemView.findViewById(R.id.cuisineType);
            address = itemView.findViewById(R.id.address);
            openStatus = itemView.findViewById(R.id.openStatus);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRestaurantClick(restaurants.get(position));
                }
            });
        }

        public void bind(Restaurant restaurant) {
            restaurantName.setText(restaurant.getName());
            ratingBar.setRating((float) restaurant.getRating());
            priceRange.setText(getPriceRangeText(restaurant.getPriceRange()));
            cuisineType.setText(restaurant.getCuisine());
            address.setText(restaurant.getAddress());
            openStatus.setText(restaurant.isOpen() ? "Open" : "Closed");
            openStatus.setTextColor(itemView.getContext().getResources().getColor(
                    restaurant.isOpen() ? android.R.color.holo_green_dark : android.R.color.holo_red_dark
            ));

            // Set placeholder image
            restaurantImage.setImageResource(R.drawable.placeholder_restaurant);
        }

        private String getPriceRangeText(double priceRange) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < priceRange; i++) {
                sb.append("$");
            }
            return sb.toString();
        }
    }
} 