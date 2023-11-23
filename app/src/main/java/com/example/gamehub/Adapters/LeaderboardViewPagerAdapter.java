package com.example.gamehub.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gamehub.Fragments.User.Leaderboard.SpaceShipFragment;
import com.example.gamehub.Fragments.User.Leaderboard.TwoZeroFourEightFragment;

public class LeaderboardViewPagerAdapter extends FragmentStateAdapter {
    public LeaderboardViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SpaceShipFragment();
            case 1:
                return new TwoZeroFourEightFragment();
            default:
                return new SpaceShipFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
