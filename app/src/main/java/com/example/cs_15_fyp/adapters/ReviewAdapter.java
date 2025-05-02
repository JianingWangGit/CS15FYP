package com.example.cs_15_fyp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Review review);
    }

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.usernameText.setText(review.getUsername());
        holder.commentText.setText(review.getComment());
        holder.ratingBar.setRating(review.getRating());
        
        List<String> photos = review.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            holder.photoCount.setText(photos.size() + " photos");
            
            if (photos.size() == 1) {
                // Display single image directly
                holder.singleImage.setVisibility(View.VISIBLE);
                holder.imagePager.setVisibility(View.GONE);
                
                Glide.with(context)
                    .load(photos.get(0))
                    .centerCrop()
                    .into(holder.singleImage);
            } else if (photos.size() > 1) {
                // Use ViewPager for multiple images
                holder.singleImage.setVisibility(View.GONE);
                holder.imagePager.setVisibility(View.VISIBLE);
                
                ReviewImageAdapter imageAdapter = new ReviewImageAdapter(context, photos);
                holder.imagePager.setAdapter(imageAdapter);
            } else {
                // No images
                holder.singleImage.setVisibility(View.GONE);
                holder.imagePager.setVisibility(View.GONE);
            }
        } else {
            holder.photoCount.setText("No photos");
            holder.singleImage.setVisibility(View.GONE);
            holder.imagePager.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(review);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void appendData(List<Review> newReviews) {
        int start = reviews.size();
        reviews.addAll(newReviews);
        notifyItemRangeInserted(start, newReviews.size());
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText, commentText, photoCount;
        RatingBar ratingBar;
        ImageView singleImage;
        ViewPager2 imagePager;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.reviewUsername);
            commentText = itemView.findViewById(R.id.reviewComment);
            photoCount = itemView.findViewById(R.id.reviewPhotoCount);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            singleImage = itemView.findViewById(R.id.reviewSingleImage);
            imagePager = itemView.findViewById(R.id.reviewImagePager);
        }
    }
}
