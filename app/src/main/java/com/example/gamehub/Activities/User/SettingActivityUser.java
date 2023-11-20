package com.example.gamehub.Activities.User;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.gamehub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivityUser extends AppCompatActivity {

    CardView layoutBottomSheetChangePassword;
    BottomSheetBehavior bottomSheetBehaviorChangePassword;
    MaterialButton confirm_btn, open_bts_btn;
    ImageView back_btn;
    EditText newPassword, confirm_newPassword;
    View showSnackBarView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        initUI();
        initListener();
    }



    private void initUI() {
        progressBar = findViewById(R.id.progressBar_setting);
        showSnackBarView = findViewById(android.R.id.content);
        layoutBottomSheetChangePassword = findViewById(R.id.bottom_sheet_input_password);
        bottomSheetBehaviorChangePassword = BottomSheetBehavior.from(layoutBottomSheetChangePassword);
        confirm_btn = findViewById(R.id.confirm_change_password_btn);
        back_btn = findViewById(R.id.back_icon_setting);
        open_bts_btn = findViewById(R.id.btn_open_change_password_bts);
        newPassword = findViewById(R.id.inputText_newpassword);
        confirm_newPassword = findViewById(R.id.inputText_confirmpassword);
    }

    private void initListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        open_bts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehaviorChangePassword.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickConfirmChangePassword();
            }
        });


    }

    private void onClickConfirmChangePassword() {
        progressBar.setVisibility(View.VISIBLE);
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirm_newPassword.getText().toString().trim();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Please fill in both fields", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Passwords do not match", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar snackbar = Snackbar.make(showSnackBarView, "Password changes successfully", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            progressBar.setVisibility(View.INVISIBLE);
                            bottomSheetBehaviorChangePassword.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }
                });
    }
}