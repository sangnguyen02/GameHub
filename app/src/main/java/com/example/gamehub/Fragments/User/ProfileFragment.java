package com.example.gamehub.Fragments.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamehub.Activities.LoginActivity;
import com.example.gamehub.Activities.MainActivity;
import com.example.gamehub.Activities.User.EditProfileActivity;
import com.example.gamehub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CardView editProfileCard, viewLeaderboardCard, settingCard, signOutCard;
    CircleImageView profile_img;
    TextView profile_fullname;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI();
        initListener();
        showUserProfile();

        return rootView;
    }



    private void initUI() {
        profile_img = rootView.findViewById(R.id.img_profile);
        profile_fullname = rootView.findViewById(R.id.tv_fullname);
        editProfileCard = rootView.findViewById(R.id.edit_card);
        viewLeaderboardCard = rootView.findViewById(R.id.leaderboard_card);
        settingCard = rootView.findViewById(R.id.setting_card);
        signOutCard = rootView.findViewById(R.id.logout_card);
    }

    private void initListener() {

        editProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        signOutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
    }



    private void showUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String imageUrl = snapshot.child("image").getValue(String.class);
                        String fullName = snapshot.child("Fullname").getValue(String.class);
                        updateUI(imageUrl, fullName);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void updateUI(String imageUrl, String fullName) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(profile_img);
        }
        if (fullName != null && !fullName.isEmpty()) {
            profile_fullname.setText(fullName);
        }
    }
}