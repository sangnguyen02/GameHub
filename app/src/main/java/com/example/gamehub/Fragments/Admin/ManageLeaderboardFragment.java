package com.example.gamehub.Fragments.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamehub.Activities.LoginActivity;
import com.example.gamehub.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ManageLeaderboardFragment extends Fragment {
    View rootView;

    MaterialButton signOut;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_manage_leaderboard, container, false);
        initUI();
        initListener();
        // Inflate the layout for this fragment
        return rootView;
    }



    private void initUI() {
        signOut=rootView.findViewById(R.id.signOutAdmin);
    }
    private void initListener() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
    }


}