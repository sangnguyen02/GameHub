package com.example.gamehub.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamehub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends AppCompatActivity {

    EditText uName, uGender, uEmail, uPhone, uLocation;
    CircleImageView imgProfile;
    String uID;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initUI();
        initListener();
        Intent intent = getIntent();
        uID = intent.getStringExtra("userId");
        showUserProfile(imgProfile, uName, uGender, uEmail, uPhone, uLocation);


    }




    private void initUI() {
        imgProfile = findViewById(R.id.img_userprofiledetail);
        uName = findViewById(R.id.edt_username);
        uGender = findViewById(R.id.edt_usergender);
        uEmail = findViewById(R.id.edt_useremail);
        uPhone = findViewById(R.id.edt_userphone);
        uLocation = findViewById(R.id.edt_userlocation);
        back_btn = findViewById(R.id.back_icon_detailuser);

    }
    private void initListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showUserProfile(CircleImageView imgProfile, EditText uName, EditText uGender, EditText uEmail, EditText uPhone, EditText uLocation) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uID);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("Fullname").getValue().toString();
                        String gender = snapshot.child("Gender").getValue().toString();
                        String email = snapshot.child("Email").getValue().toString();
                        String phone = snapshot.child("Phone").getValue().toString();
                        String address = snapshot.child("Location").getValue().toString();
                        if(image != null) {
                            Picasso.get().load(image).into(imgProfile);
                        }
                        uName.setText(name);
                        uGender.setText(gender);
                        uEmail.setText(email);
                        uPhone.setText(phone);
                        uLocation.setText(address);
                    }
                    else {
                        imgProfile.setImageResource(R.drawable.usericon);
                        String name = snapshot.child("Fullname").getValue().toString();
                        String gender = snapshot.child("Gender").getValue().toString();
                        String email = snapshot.child("Email").getValue().toString();
                        String phone = snapshot.child("Phone").getValue().toString();
                        String address = snapshot.child("Location").getValue().toString();
                        uName.setText(name);
                        uGender.setText(gender);
                        uEmail.setText(email);
                        uPhone.setText(phone);
                        uLocation.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}