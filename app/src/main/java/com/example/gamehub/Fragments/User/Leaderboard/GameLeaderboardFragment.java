package com.example.gamehub.Fragments.User.Leaderboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamehub.Models.LeaderboardItem;
import com.example.gamehub.Models.User;
import com.example.gamehub.R;
import com.example.gamehub.ViewHolder.LeaderboardViewHolder;
import com.example.gamehub.ViewHolder.ManageUsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLeaderboardFragment extends Fragment {

    private FirebaseRecyclerAdapter<LeaderboardItem, LeaderboardViewHolder> adapter;
    DatabaseReference LeaderboardRef;
    RecyclerView rcv_leaderboard;
    String gameName;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game_leaderboard, container, false);
        if (getArguments() != null) {
            gameName = getArguments().getString("gameName");
            Log.d("Game Name", gameName);
            LeaderboardRef = FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(gameName);
        }
        Log.d("Game Name", gameName);
        initUI();
        loadLeaderboard();
        return rootView;
    }

    private void initUI() {
//        rcv_leaderboard = rootView.findViewById(R.id.rcv_leaderboard);
//        rcv_leaderboard.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(),RecyclerView.VERTICAL,false);
//        rcv_leaderboard.setLayoutManager(linearLayoutManager);

        rcv_leaderboard = rootView.findViewById(R.id.rcv_leaderboard);
        rcv_leaderboard.setHasFixedSize(true);

        // Set reverseLayout directly here
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rcv_leaderboard.setLayoutManager(linearLayoutManager);

    }

    private void loadLeaderboard() {
        FirebaseRecyclerOptions<LeaderboardItem> options =
                new FirebaseRecyclerOptions.Builder<LeaderboardItem>()
                        .setQuery(LeaderboardRef.limitToLast(10).orderByChild("UserScore"), LeaderboardItem.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<LeaderboardItem, LeaderboardViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position, @NonNull LeaderboardItem model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
                return new LeaderboardViewHolder(view);
            }
        };

        rcv_leaderboard.setAdapter(adapter);
        adapter.startListening();

    }


}