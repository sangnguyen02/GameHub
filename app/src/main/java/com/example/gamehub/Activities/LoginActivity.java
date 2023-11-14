package com.example.gamehub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.gamehub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    MaterialButton signUp, signIn;
    EditText email_input, password_input;
    ProgressBar progressBar;
    View showSnackBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        initListener();
    }

    private void initUI() {
        signIn = findViewById(R.id.sign_btn);
        signUp = findViewById(R.id.signup_btn);
        email_input = findViewById(R.id.edt_email);
        password_input = findViewById(R.id.edt_password);
        progressBar = findViewById(R.id.progressBar_login);
        showSnackBarView = findViewById(android.R.id.content);
    }
    private void initListener() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
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
            signIn();
        }
    }

    private boolean isValidEmail(String mEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return mEmail.matches(emailPattern);
    }

    private void signIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = email_input.getText().toString().trim();
        String password = password_input.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            Snackbar snackbar = Snackbar.make(showSnackBarView, "Authentication failed.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }
}