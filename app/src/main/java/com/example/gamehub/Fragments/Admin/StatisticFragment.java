package com.example.gamehub.Fragments.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gamehub.Activities.Admin.ViewRatingActivity;
import com.example.gamehub.Activities.LoginActivity;
import com.example.gamehub.Adapters.RatingSpinnerAdapter;
import com.example.gamehub.Models.LeaderboardItem;
import com.example.gamehub.Models.RatingItem;
import com.example.gamehub.Models.RatingSpinner;
import com.example.gamehub.R;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticFragment extends Fragment {

    View rootView;
    PieChart pieChart;
    Spinner spinner;
    RatingSpinnerAdapter spinnerAdapter;
    String selectedGame;
    MaterialButton viewRating;
    TextView tv_totalUser;
    DatabaseReference ratingRef, totalUserRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_statistic, container, false);
        pieChart = rootView.findViewById(R.id.pieChart);
        configurePieChart();
        initUI();
        initListener();


        return rootView;
    }

    private void configurePieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

    }

    private void initUI() {
        tv_totalUser = rootView.findViewById(R.id.tv_total_user);
        viewRating = rootView.findViewById(R.id.view_rating_btn);
        spinner = rootView.findViewById(R.id.spn_rating_game_admin);
        spinnerAdapter = new RatingSpinnerAdapter(rootView.getContext(), R.layout.item_rating_selected, getListCategory());
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGame = spinnerAdapter.getItem(i).getName();
                loadPieChart();
                loadTotalUser();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadTotalUser() {
        totalUserRef = FirebaseDatabase.getInstance().getReference("Leaderboard").child(selectedGame);
        totalUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalUser = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        LeaderboardItem leaderboardItem = userSnapshot.getValue(LeaderboardItem.class);
                        if (leaderboardItem != null) {
                            totalUser++;
                        }
                    }
                }
                tv_totalUser.setText(String.valueOf(totalUser));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPieChart() {
        ratingRef = FirebaseDatabase.getInstance().getReference("Rating").child(selectedGame);
        ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int poorCount = 0, fairCount = 0, goodCount = 0, veryGoodCount = 0, excellentCount = 0;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    RatingItem ratingItem = userSnapshot.getValue(RatingItem.class);
                    if (ratingItem != null && ratingItem.getRatingScore() != null) {
                        switch (ratingItem.getRatingScore()) {
                            case "Poor":
                                poorCount++;
                                break;
                            case "Fair":
                                fairCount++;
                                break;
                            case "Good":
                                goodCount++;
                                break;
                            case "Very Good":
                                veryGoodCount++;
                                break;
                            case "Excellent":
                                excellentCount++;
                                break;
                        }
                    }
                }

                ArrayList<PieEntry> entries = new ArrayList<>();
                addEntryIfGreaterThanZero(entries, poorCount, "Poor");
                addEntryIfGreaterThanZero(entries, fairCount, "Fair");
                addEntryIfGreaterThanZero(entries, goodCount, "Good");
                addEntryIfGreaterThanZero(entries, veryGoodCount, "Very Good");
                addEntryIfGreaterThanZero(entries, excellentCount, "Excellent");

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueTextSize(12f);

                PieData pieData = new PieData(dataSet);

                pieChart.setData(pieData);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEntryIfGreaterThanZero(ArrayList<PieEntry> entries, int count, String label) {
        if (count > 0) {
            entries.add(new PieEntry(count, label));
        }
    }

    private void initListener() {
        viewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ViewRatingActivity.class);
                intent.putExtra("SelectedGame",selectedGame);
                startActivity(intent);
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


}