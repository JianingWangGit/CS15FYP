//package com.example.cs_15_fyp;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
//
//    private final List<Review> reviewList;
//
//    public ReviewAdapter(List<Review> reviews) {
//        this.reviewList = reviews;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView reviewUser, reviewText;
//        RatingBar reviewRating;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            reviewUser = itemView.findViewById(R.id.reviewUser);
//            reviewText = itemView.findViewById(R.id.reviewText);
//            reviewRating = itemView.findViewById(R.id.reviewRating);
//        }
//    }
//
//    @NonNull
//    @Override
//    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_review, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
//        Review review = reviewList.get(position);
//        holder.reviewUser.setText(review.username);
//        holder.reviewText.setText(review.comment);
//        holder.reviewRating.setRating(review.rating);
//    }
//
//    @Override
//    public int getItemCount() {
//        return reviewList.size();
//    }
//}
//
