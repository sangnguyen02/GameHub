package com.example.gamehub.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.example.gamehub.Adapters.LeaderboardViewPagerAdapter;
import com.example.gamehub.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LeaderboardActivityUser extends AppCompatActivity {
    ImageView back_btn;
    TabLayout tableLayout;
    ViewPager2 viewPager2;
    LeaderboardViewPagerAdapter leaderboardViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_user);

        initUI();
        initListener();

        leaderboardViewPagerAdapter = new LeaderboardViewPagerAdapter(this);
        viewPager2.setAdapter(leaderboardViewPagerAdapter);
        new TabLayoutMediator(tableLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Space Ship");
                        break;
                    case 1:
                        tab.setText("2048");
                        break;
                }
            }
        }).attach();
    }

    private void initUI() {
        back_btn = findViewById(R.id.back_icon_leaderboard);
        tableLayout = findViewById(R.id.tab_layout_game_names);
        viewPager2 = findViewById(R.id.view_pager_leaderboard);


    }
    private void initListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}