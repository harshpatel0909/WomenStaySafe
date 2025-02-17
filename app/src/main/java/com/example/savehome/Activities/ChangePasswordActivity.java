package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.savehome.Fragments.ContactsFragment;
import com.example.savehome.Fragments.ProfileFragment;
import com.example.savehome.Model.ChangePasswordModel;
import com.example.savehome.Model.LoginModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.webservice.RetroConfig;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackChangePassword)
    ImageView IvBackChangePassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etOldPasswordChange)
    EditText etOldPasswordChange;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etNewPass)
    EditText etNewPass;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etConfirmPassChangePassword)
    EditText etConfirmPassChangePassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnChange)
    Button btnChange;

    boolean passwordVisible;
    String oldPassword="",newPassword="",email="";
    LoginModel.Data cardUser;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

/*        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldPassword = bundle.getString("oldPassword");
            newPassword = bundle.getString("newPassword");
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.IvBackChangePassword, R.id.etOldPasswordChange, R.id.etNewPass, R.id.etConfirmPassChangePassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvBackChangePassword:
                onBackPressed();
                break;
            case R.id.etOldPasswordChange:
                etOldPasswordChange.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etOldPasswordChange.getRight() - etOldPasswordChange.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etOldPasswordChange.getSelectionEnd();
                            if (passwordVisible) {
                                etOldPasswordChange.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etOldPasswordChange.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etOldPasswordChange.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etOldPasswordChange.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etOldPasswordChange.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
            case R.id.etNewPass:
                etNewPass.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etNewPass.getRight() - etNewPass.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etNewPass.getSelectionEnd();
                            if (passwordVisible) {
                                etNewPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etNewPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etNewPass.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
            case R.id.etConfirmPassChangePassword:
                etConfirmPassChangePassword.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etConfirmPassChangePassword.getRight() - etConfirmPassChangePassword.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etConfirmPassChangePassword.getSelectionEnd();
                            if (passwordVisible) {
                                etConfirmPassChangePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etConfirmPassChangePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etConfirmPassChangePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etConfirmPassChangePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etConfirmPassChangePassword.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
        }
    }

    public void ChangePassword(View view) {
        boolean isValid = true;
        String oldPassword = Objects.requireNonNull(etOldPasswordChange.getText()).toString().trim();
        String newPassword = Objects.requireNonNull(etNewPass.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(etConfirmPassChangePassword.getText()).toString().trim();


        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            isValid = false;
            Toast.makeText(ChangePasswordActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "Password Would Not be matched", Toast.LENGTH_SHORT).show();
            return;

        }

        if (isValid) {
            changePasswordDetail(newPassword, oldPassword);
            return;
        }
    }

    private void changePasswordDetail(String newPassword, String oldPassword) {

        Call<ChangePasswordModel> call = RetroConfig.retrofit().changePassword(Objects.requireNonNull(PrefUtils.getLoginUser(this)).getEmail(),newPassword, oldPassword);
        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordModel> call, @NonNull Response<ChangePasswordModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        startActivity(new Intent(ChangePasswordActivity.this, HomeScreenActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(ChangePasswordActivity.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordModel> call, @NonNull Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }


}