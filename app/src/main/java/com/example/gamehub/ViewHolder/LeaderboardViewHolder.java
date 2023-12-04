package com.example.gamehub.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamehub.Models.LeaderboardItem;
import com.example.gamehub.R;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    private ImageView rankImageView;
    private TextView userNameTextView;
    private TextView userScoreTextView;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);
        //rankImageView = itemView.findViewById(R.id.rankImageView);
        userNameTextView = itemView.findViewById(R.id.userNameTextView);
        userScoreTextView = itemView.findViewById(R.id.userScoreTextView);
    }

    public void bind(LeaderboardItem item) {
        // Gán dữ liệu từ LeaderboardItem vào View
        userNameTextView.setText(item.getFullname());
        userScoreTextView.setText(String.valueOf(item.getUserScore()));

//        if (position == 0) {
//            rankImageView.setImageResource(R.drawable.first_rank);
//            rankImageView.setVisibility(View.VISIBLE);
//        } else if (position == 1) {
//            rankImageView.setImageResource(R.drawable.second_rank);
//            rankImageView.setVisibility(View.VISIBLE);
//        } else if (position == 2) {
//            rankImageView.setImageResource(R.drawable.third_rank);
//            rankImageView.setVisibility(View.VISIBLE);
//        } else {
//            // For positions 3 and beyond, hide the rank image
//            rankImageView.setVisibility(View.GONE);
//        }
    }
}
