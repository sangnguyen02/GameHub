package com.example.gamehub.Activities.User;

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

import com.example.gamehub.Models.LeaderboardItem;
import com.example.gamehub.Models.RatingItem;
import com.example.gamehub.R;
import com.example.gamehub.ViewHolder.LeaderboardViewHolder;
import com.example.gamehub.ViewHolder.RatingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewAllRatingActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<RatingItem, RatingViewHolder> adapter;
    DatabaseReference ratingRef;
    RecyclerView rcv_all_rating;
    TextView titleAllRating;
    ImageView back_btn;
    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_rating);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("RatingGame")) {
            gameName = intent.getStringExtra("RatingGame");
            ratingRef = FirebaseDatabase.getInstance().getReference().child("Rating").child(gameName);
        }
        else if (intent != null && intent.hasExtra("GameListClickedName")) {
            gameName = intent.getStringExtra("GameListClickedName");
            ratingRef = FirebaseDatabase.getInstance().getReference().child("Rating").child(gameName);
        }
        initUI();
        initListener();
        loadAllRating();
    }



    private void initUI() {
        titleAllRating = findViewById(R.id.tv_titleRatingOf);
        titleAllRating.setText("Rating of " + gameName);
        back_btn = findViewById(R.id.back_icon_allrating);
        rcv_all_rating = findViewById(R.id.rcv_all_rating);
        rcv_all_rating.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcv_all_rating.setLayoutManager(linearLayoutManager);
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
        rcv_all_rating.setAdapter(adapter);
        adapter.startListening();
    }

}