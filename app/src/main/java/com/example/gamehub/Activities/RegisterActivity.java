package com.example.gamehub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gamehub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    MaterialButton signUp;
    EditText email_input, password_input;
    View showSnackBarView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        initUI();
        initListener();
    }



    private void initUI() {
        signUp = findViewById(R.id.signup_btn_register);
        email_input = findViewById(R.id.edt_email_register);
        password_input = findViewById(R.id.edt_password_register);
        showSnackBarView = findViewById(android.R.id.content);
        progressBar = findViewById(R.id.progressBar_register);
    }

    private void initListener() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });

    }

    private void validateInput() {
        String mEmail = email_input.getText().toString().trim();
        String mPassword = password_input.getText().toString().trim();

        if (TextUtils.isEmpty(mEmail))
        {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Please enter your email", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if (!isValidEmail(mEmail)) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Please enter a valid email address", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if (TextUtils.isEmpty(mPassword))
        {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Please enter your password", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            signUp();
        }
    }

    private boolean isValidEmail(String mEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return mEmail.matches(emailPattern);
    }

    private void signUp() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = email_input.getText().toString().trim();
        String password = password_input.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            createCredentialToFirebaseDatabase(user.getEmail(), user.getUid());
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            Snackbar snackbar = Snackbar.make(showSnackBarView, "Your email has been already existed.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }

    private void createCredentialToFirebaseDatabase(final String email, final String idUser) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("Users").child(idUser).exists()) {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Fullname", "Not Update");
                    userdataMap.put("Phone", "Not Update");
                    userdataMap.put("Email", email);

                    RootRef.child("Users").child(idUser).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d("Firebase", "Data added to Realtime Database successfully");
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                    else {
                                        Log.e("Firebase", "Error adding data to Realtime Database: " + task.getException());
                                    }
                                }

                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}