package com.example.gamehub.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamehub.Models.LeaderboardItem;
import com.example.gamehub.R;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    private TextView userNameTextView;
    private TextView userScoreTextView;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);

        userNameTextView = itemView.findViewById(R.id.userNameTextView);
        userScoreTextView = itemView.findViewById(R.id.userScoreTextView);
    }

    public void bind(LeaderboardItem item) {
        // Gán dữ liệu từ LeaderboardItem vào View
        userNameTextView.setText(item.getFullname());
        userScoreTextView.setText(String.valueOf(item.getUserScore()));
    }
}
