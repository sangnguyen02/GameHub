package com.example.gamehub.Activities.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.gamehub.Fragments.User.GameFragment;
import com.example.gamehub.Fragments.User.ProfileFragment;
import com.example.gamehub.R;
import com.example.gamehub.databinding.ActivityMainUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityUser extends AppCompatActivity {
    ActivityMainUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnlineStatus();
        replaceFragment(new GameFragment());
        initUI();
        initListener();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.games:
                    replaceFragment(new GameFragment());
                    break;


                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }



    private void initUI() {

    }
    private void initListener() {

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void setOnlineStatus() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference("Users");
        if(currentUser != null) {
            String userId = currentUser.getUid();
            UsersRef.child(userId).child("Status").setValue("1");
        }
    }
}