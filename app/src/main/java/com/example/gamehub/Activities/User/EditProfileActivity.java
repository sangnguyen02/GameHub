package com.example.gamehub.Activities.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gamehub.Adapters.GenderSpinnerAdapter;
import com.example.gamehub.Fragments.User.ProfileFragment;
import com.example.gamehub.Models.GenderSpinner;
import com.example.gamehub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    String userID, selectedGender;
    View showSnackBarView;
    CircleImageView profileImageView;
    Uri imageUri;
    ImageView saveIcon, backIcon;
    Spinner spinnerGender;
    EditText userFullname, userEmail, userPhone, userLocation;
    GenderSpinnerAdapter genderSpinnerAdapter;
    StorageReference storageProfilePrictureRef;
    StorageTask uploadTask;
    String myUrl = "";
    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        else {

        }

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        userID = user.getUid();
        initUI();
        userEmail.setText(user.getEmail());
        initListener();
        showUserProfile(profileImageView, userFullname, spinnerGender, userPhone, userLocation);

    }



    private void initUI() {
        showSnackBarView = findViewById(android.R.id.content);
        profileImageView = findViewById(R.id.img_profile_edit);
        saveIcon = findViewById(R.id.save_icon);
        backIcon = findViewById(R.id.back_icon);
        userFullname = findViewById(R.id.edt_fullname);
        spinnerGender = findViewById(R.id.spn_category);
        userPhone = findViewById(R.id.edt_phone);
        userEmail = findViewById(R.id.edt_email);
        userLocation = findViewById(R.id.edt_location);
        genderSpinnerAdapter = new GenderSpinnerAdapter(this, R.layout.item_gender_selected,getListCategory());
        spinnerGender.setAdapter(genderSpinnerAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGender = genderSpinnerAdapter.getItem(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initListener() {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfoSaved();
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private List<GenderSpinner> getListCategory() {
        List<GenderSpinner> list = new ArrayList<>();
        list.add(new GenderSpinner("Male", "male_icon"));
        list.add(new GenderSpinner("Female", "female_icon"));
        return list;
    }

    private void showUserProfile(final CircleImageView profileImageView
            , final EditText fullNameEditText
            , final Spinner genderSpinner
            , final EditText userPhoneEditText
            , final EditText locationEditText) {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("Fullname").getValue().toString();
                    String gender = snapshot.child("Gender").getValue().toString();
                    String phone = snapshot.child("Phone").getValue().toString();
                    String address = snapshot.child("Location").getValue().toString();

                    Picasso.get().load(image).into(profileImageView);
                    fullNameEditText.setText(name);
                    int genderPosition = findGenderPosition(gender);
                    genderSpinner.setSelection(genderPosition);
                    userPhoneEditText.setText(phone);
                    locationEditText.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private int findGenderPosition(String gender) {
        List<GenderSpinner> genderList = getListCategory();
        for (int i = 0; i < genderList.size(); i++) {
            if (genderList.get(i).getName().equalsIgnoreCase(gender)) {
                return i;
            }
        }
        return 0;
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {

            imageUri = data.getData();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(EditProfileActivity.this, EditProfileActivity.class));
            finish();
        }
    }

    private void userInfoSaved()
    {
        if (!isValidPhoneNumber(userPhone.getText().toString()) || TextUtils.isEmpty(userPhone.getText().toString())) {
            Toast.makeText(EditProfileActivity.this, "Your phone number is not valid.", Toast.LENGTH_SHORT).show();
        }
        else {
            uploadInfor();
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{10}$"; // 10 chữ số
        return phoneNumber.matches(phoneRegex);
    }

    private void uploadInfor()
    {
        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(userID + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            return fileRef.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("Fullname",userFullname.getText().toString());
                                userMap. put("Gender", selectedGender);
                                userMap. put("Phone", userPhone.getText().toString());
                                userMap.put("Location", userLocation.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(userID).updateChildren(userMap);

                                Toast.makeText(EditProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(EditProfileActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("Fullname", userFullname.getText().toString());
            userMap.put("Gender", selectedGender);
            userMap.put("Phone", userPhone.getText().toString());
            userMap.put("Location", userLocation.getText().toString());

            // Do not update the "image" field in this case

            ref.child(userID).updateChildren(userMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}