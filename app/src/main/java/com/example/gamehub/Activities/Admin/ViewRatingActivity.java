package com.example.gamehub.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamehub.Models.RatingItem;
import com.example.gamehub.R;
import com.example.gamehub.ViewHolder.RatingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewRatingActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter<RatingItem, RatingViewHolder> adapter;
    String selectedGame;
    DatabaseReference ratingRef;
    RecyclerView rcv_all_rating_admin;
    TextView titleAllRating_admin;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rating);
        Intent intent = getIntent();
        if(intent != null) {
            selectedGame = intent.getStringExtra("SelectedGame");
            ratingRef = FirebaseDatabase.getInstance().getReference().child("Rating").child(selectedGame);
        }
        initUI();
        initListener();
        loadAllRating();
    }

    private void initUI() {
        titleAllRating_admin = findViewById(R.id.tv_titleRatingOf_admin);
        titleAllRating_admin.setText("Rating of " + selectedGame);
        back_btn = findViewById(R.id.back_icon_allrating_admin);
        rcv_all_rating_admin = findViewById(R.id.rcv_all_rating_admin);
        rcv_all_rating_admin.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcv_all_rating_admin.setLayoutManager(linearLayoutManager);
    }

    private void initListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadAllRating() {
        FirebaseRecyclerOptions<RatingItem> options =
                new FirebaseRecyclerOptions.Builder<RatingItem>()
                        .setQuery(ratingRef, RatingItem.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<RatingItem, RatingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RatingViewHolder holder, int position, @NonNull RatingItem model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_rating, parent, false);
                return new RatingViewHolder(view);
            }
        };
        rcv_all_rating_admin.setAdapter(adapter);
        adapter.startListening();
    }
}