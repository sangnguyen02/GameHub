package com.example.gamehub.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamehub.Models.RatingItem;
import com.example.gamehub.R;

public class RatingViewHolder extends RecyclerView.ViewHolder {
    private TextView userNameTextView;
    private TextView ratingScoreTextView;

    public RatingViewHolder(@NonNull View itemView) {
        super(itemView);
        userNameTextView = itemView.findViewById(R.id.userNameTextView_rating);
        ratingScoreTextView = itemView.findViewById(R.id.ratingScoreTextView_rating);
    }

    public void bind(RatingItem item) {
        userNameTextView.setText(item.getFullname());
        ratingScoreTextView.setText(item.getRatingScore());
    }
}
