package com.example.gamehub.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.gamehub.Fragments.User.Leaderboard.GameLeaderboardFragment;
import com.example.gamehub.R;
import com.google.android.material.navigation.NavigationView;

public class GameLeaderboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView back_btn, open_drawer_btn;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout_game_leaderboard;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_leaderboard);

        initUI();
        initListener();

        navigationView = findViewById(R.id.navigation_view_games);
        navigationView.setNavigationItemSelectedListener(this);
        setupDrawerContent(navigationView);
        if (savedInstanceState == null) {
            selectGame(R.id.nav_space_ship); // Default game
        }
    }

    private void initUI() {
        back_btn = findViewById(R.id.back_icon_leaderboard);
        open_drawer_btn = findViewById(R.id.open_drawer_icon_leaderboard);
        drawerLayout_game_leaderboard = findViewById(R.id.drawer_game_leaderboard);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout_game_leaderboard
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout_game_leaderboard.addDrawerListener(actionBarDrawerToggle);

    }

    private void initListener() {
        open_drawer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout_game_leaderboard.openDrawer(Gravity.LEFT);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectGame(menuItem.getItemId());
                        return true;
                    }
                });
    }

    private void selectGame(int itemId) {
        Fragment fragment = new GameLeaderboardFragment();
        Bundle args = new Bundle();
        switch (itemId) {
            case R.id.nav_space_ship:
                args.putString("gameName", "Space Ship");
                break;
            case R.id.nav_2048:
                args.putString("gameName", "2048");
                break;
        }
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();

        setTitle(args.getString("gameName"));
        drawerLayout_game_leaderboard.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectGame(item.getItemId());
        return true;
    }
}