package com.example.gamehub.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gamehub.Adapters.RatingSpinnerAdapter;
import com.example.gamehub.Models.GenderSpinner;
import com.example.gamehub.Models.RatingSpinner;
import com.example.gamehub.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingGameActivity extends AppCompatActivity {
    MaterialButton confirm_btn, view_all_btn;
    Spinner ratingSpinner;
    RatingBar ratingBar;
    TextView ratingScore;
    String selectedRatingGame;
    ImageView back_btn;
    RatingSpinnerAdapter ratingSpinnerAdapter;

    DatabaseReference ratingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_game);
        initUI();
        initListener();
    }

    private void initUI() {
        ratingBar = findViewById(R.id.ratingBar);
        ratingScore = findViewById(R.id.tv_ratingScore);
        ratingScore.setText("Poor");
        back_btn = findViewById(R.id.back_icon_rating);
        confirm_btn = findViewById(R.id.confirm_rating_btn);
        view_all_btn = findViewById(R.id.view_all_rating_btn);
        ratingSpinner = findViewById(R.id.spn_rating_game);
        ratingSpinnerAdapter = new RatingSpinnerAdapter(this, R.layout.item_rating_selected, getListCategory());
        ratingSpinner.setAdapter(ratingSpinnerAdapter);
        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRatingGame = ratingSpinnerAdapter.getItem(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int roundedRating = Math.round(v); // Round the float rating to an integer
                updateRatingScore(roundedRating);
            }
        });
    }

    private void initListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickConfirmRating();
            }
        });

        view_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewAllRatingActivity.class);
                intent.putExtra("RatingGame",selectedRatingGame);
                startActivity(intent);
            }
        });
    }

    private void onClickConfirmRating() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Fullname");
        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullname = snapshot.getValue(String.class);
                if(fullname != null) {
                    ratingRef = FirebaseDatabase.getInstance().getReference("Rating").child(selectedRatingGame).child(user.getUid());
                    Map<String, Object> ratingData = new HashMap<>();
                    ratingData.put("UserID", user.getUid());
                    ratingData.put("Fullname", fullname);
                    ratingData.put("RatingScore", ratingScore.getText());

                    ratingRef.setValue(ratingData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private List<RatingSpinner> getListCategory() {
        List<RatingSpinner> list = new ArrayList<>();
        list.add(new RatingSpinner("SpaceShip"));
        list.add(new RatingSpinner("2048"));
        list.add(new RatingSpinner("ColorBird"));
        list.add(new RatingSpinner("DotRescue"));
        list.add(new RatingSpinner("Orbit"));
        list.add(new RatingSpinner("PixelAdventure"));
        return list;
    }

    private void updateRatingScore(int rating) {
        String ratingText;
        switch (rating) {
            case 1:
                ratingText = "Poor";
                break;
            case 2:
                ratingText = "Fair";
                break;
            case 3:
                ratingText = "Good";
                break;
            case 4:
                ratingText = "Very Good";
                break;
            case 5:
                ratingText = "Excellent";
                break;
            default:
                ratingText = ""; // Handle other cases if needed
                break;
        }

        // Update the TextView with the calculated rating text
        ratingScore.setText(ratingText);
    }
}