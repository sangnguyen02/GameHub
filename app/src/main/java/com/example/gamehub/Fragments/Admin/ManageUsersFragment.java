package com.example.gamehub.Fragments.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamehub.Activities.Admin.UserDetailActivity;
import com.example.gamehub.Activities.LoginActivity;
import com.example.gamehub.Models.User;
import com.example.gamehub.R;
import com.example.gamehub.ViewHolder.ManageUsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ManageUsersFragment extends Fragment {
    View rootView;
    DatabaseReference UsersRef;
    RecyclerView rcv_manageUsers;
    FloatingActionButton signOut;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_manage_users, container, false);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        initUI();
        initListener();
        loadUsers();
        return rootView;
    }
    private void initUI() {
        signOut = rootView.findViewById(R.id.signOutAdmin);
        rcv_manageUsers = rootView.findViewById(R.id.rcv_manageUsers);
        rcv_manageUsers.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(),RecyclerView.VERTICAL,false);
        rcv_manageUsers.setLayoutManager(linearLayoutManager);
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


    private void loadUsers() {

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(UsersRef, User.class)
                        .build();

        FirebaseRecyclerAdapter<User, ManageUsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, ManageUsersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ManageUsersViewHolder holder, int position, @NonNull final User model) {
                        if (model.getImage() != null) {
                            Picasso.get().load(model.getImage()).into(holder.imgUser);
                        } else {
                            holder.imgUser.setImageResource(R.drawable.usericon);
                            holder.imgUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        if (model.getFullname() != null) {
                            holder.tvName.setText(model.getFullname());
                        } else {
                            holder.tvName.setText("Not update");
                        }

                        if (model.getStatus().equals("1")) {
                            holder.tvStatus.setText("Active");
                            holder.tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.active));
                        } else if (model.getStatus().equals("0")) {
                            holder.tvStatus.setText("Inactive");
                            holder.tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactive));
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(rootView.getContext(), UserDetailActivity.class);
                                intent.putExtra("userId", model.getUserId());
                                if (model.getUserId() == null) {
                                    Log.d("ID",model.getUserId());
                                }
                                else {
                                    startActivity(intent);
                                }

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ManageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_users, parent, false);
                        ManageUsersViewHolder holder = new ManageUsersViewHolder(view);
                        return holder;
                    }
                };
        rcv_manageUsers.setAdapter(adapter);
        adapter.startListening();
    }
}