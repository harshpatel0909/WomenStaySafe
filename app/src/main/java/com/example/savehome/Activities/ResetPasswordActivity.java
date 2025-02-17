package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.ResetPasswordModel;
import com.example.savehome.R;
import com.example.savehome.webservice.RetroConfig;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBackResetPassword)
    ImageView ivBackResetPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etNewPasswordReset)
    EditText etNewPasswordReset;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etConfirmPass)
    EditText etConfirmPass;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnReset)
    Button btnReset;

    boolean passwordVisible;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.ivBackResetPassword, R.id.etNewPasswordReset, R.id.etConfirmPass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackResetPassword:
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.etNewPasswordReset:
                etNewPasswordReset.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etNewPasswordReset.getRight() - etNewPasswordReset.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etNewPasswordReset.getSelectionEnd();
                            if (passwordVisible) {
                                etNewPasswordReset.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etNewPasswordReset.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etNewPasswordReset.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etNewPasswordReset.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etNewPasswordReset.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
            case R.id.etConfirmPass:
                etConfirmPass.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etConfirmPass.getRight() - etConfirmPass.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etNewPasswordReset.getSelectionEnd();
                            if (passwordVisible) {
                                etConfirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etConfirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etConfirmPass.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
        }
    }


    public void ResetPassword(View view) {
        validatePassword();
    }

    public void validatePassword() {
        String passwordInput = Objects.requireNonNull(etNewPasswordReset.getText()).toString().trim();
        String confirmPasswordInput = Objects.requireNonNull(etConfirmPass.getText()).toString().trim();

        if (passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
            Toast.makeText(ResetPasswordActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;


        }
        if (!passwordInput.equals(confirmPasswordInput)) {
            Toast.makeText(ResetPasswordActivity.this, "Password Would Not be matched", Toast.LENGTH_SHORT).show();
            return;


        }
        if (passwordInput.length() <= 8) {
            Toast.makeText(ResetPasswordActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        ResetDetail(passwordInput, confirmPasswordInput);
    }

    private void ResetDetail(String passwordInput, String confirmPasswordInput) {
        Call<ResetPasswordModel> call = RetroConfig.retrofit().resetPassword(passwordInput, confirmPasswordInput);
        call.enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ResetPasswordModel> call, @NonNull Response<ResetPasswordModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    //           if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginOptionActivity.class);
                    startActivity(intent);
                    //         Log.e("message", response.body().getMessage());
                    Toast.makeText(ResetPasswordActivity.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            //    }

            @Override
            public void onFailure(@NonNull Call<ResetPasswordModel> call, @NonNull Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
