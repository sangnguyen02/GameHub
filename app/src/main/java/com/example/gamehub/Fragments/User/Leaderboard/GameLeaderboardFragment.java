package com.example.gamehub.Fragments.User.Leaderboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamehub.R;

public class GameLeaderboardFragment extends Fragment {
    String gameName;
    TextView titleGame;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameName = getArguments().getString("gameName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game_leaderboard, container, false);
        titleGame = rootView.findViewById(R.id.tv_title_game);
        titleGame.setText("Leaderboard for " + gameName);
        return rootView;
    }
}