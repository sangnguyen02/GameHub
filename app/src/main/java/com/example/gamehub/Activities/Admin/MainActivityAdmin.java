package com.example.gamehub.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.gamehub.Fragments.Admin.ManageLeaderboardFragment;
import com.example.gamehub.Fragments.Admin.ManageUsersFragment;
import com.example.gamehub.Fragments.User.GameFragment;
import com.example.gamehub.Fragments.User.ProfileFragment;
import com.example.gamehub.R;
import com.example.gamehub.databinding.ActivityMainAdminBinding;
import com.example.gamehub.databinding.ActivityMainUserBinding;

public class MainActivityAdmin extends AppCompatActivity {

    ActivityMainAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ManageUsersFragment());

        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.manage_users:
                    replaceFragment(new ManageUsersFragment());
                    break;


                case R.id.manage_leaderboard:
                    replaceFragment(new ManageLeaderboardFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}