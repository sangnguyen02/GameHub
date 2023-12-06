package com.example.gamehub.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.gamehub.Fragments.User.Leaderboard.GameLeaderboardFragment;
import com.example.gamehub.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class GameLeaderboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView back_btn, open_drawer_btn;

    MaterialButton open_rating;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout_game_leaderboard;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_leaderboard);

        navigationView = findViewById(R.id.navigation_view_games);
        navigationView.setNavigationItemSelectedListener(this);

        initUI();
        initListener();


        setupDrawerContent(navigationView);
        if (savedInstanceState == null) {
            selectGame(R.id.nav_space_ship); // Default game
        }
    }

    private void initUI() {
        View headerView = navigationView.getHeaderView(0);
        back_btn = findViewById(R.id.back_icon_leaderboard);
        open_drawer_btn = findViewById(R.id.open_drawer_icon_leaderboard);
        open_rating = headerView.findViewById(R.id.open_rating_btn);
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

        open_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingGameActivity.class);
                startActivity(intent);
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
                args.putString("gameName", "SpaceShip");
                break;
            case R.id.nav_2048:
                args.putString("gameName", "2048");
                break;
            case R.id.nav_color_bird:
                args.putString("gameName", "ColorBird");
                break;

            case R.id.nav_dot_rescue:
                args.putString("gameName", "DotRescue");
                break;

            case R.id.nav_orbit:
                args.putString("gameName", "Orbit");
                break;

            case R.id.nav_pixel_adventure:
                args.putString("gameName", "PixelAdventure");
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