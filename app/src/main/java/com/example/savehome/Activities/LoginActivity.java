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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.LoginModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.webservice.RetroConfig;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBackSignIn)
    ImageView ivBackSignIn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etEmailUser_Login)
    EditText etEmailUser_Login;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPassword)
    EditText etPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvForgotLogin)
    TextView tvForgotLogin;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnLogin_SignIn)
    Button btnLogin_SignIn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnGoogle)
    Button btnGoogle;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnFacebook)
    Button btnFacebook;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnApple)
    Button btnApple;

    boolean passwordVisible;
    String fcmToken = "";
    String token = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.ivBackSignIn, R.id.tvForgotLogin, R.id.etPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackSignIn:
                startActivity(new Intent(LoginActivity.this, LoginOptionActivity.class));
                break;
            case R.id.tvForgotLogin:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.etPassword:
                etPassword.setOnTouchListener((@SuppressLint("ClickableViewAccessibility") View view1, MotionEvent motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etPassword.getRight() - etPassword.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etPassword.getSelectionEnd();
                            if (passwordVisible) {
                                etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etPassword.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;

    /*            Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();*/
        }
    }

    public void Login(View view) {
        boolean isValid = true;
        String usernameEmail = Objects.requireNonNull(etEmailUser_Login.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(usernameEmail) || TextUtils.isEmpty(password)) {
            isValid = false;
            Toast.makeText(LoginActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValid) {
            LoginDetail(usernameEmail, password, fcmToken, token);

        }
    }

    private void LoginDetail(String usernameEmail, String password, String fcmToken, String token) {

  /*      String username_email = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), usernameEmail));
        String password_Login = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), password));
        String fcm_tkn_id = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), fcmToken));
        String tkn_id = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), token));*/


        Call<LoginModel> call = RetroConfig.retrofit().login(usernameEmail, password, fcmToken, token);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class)
                                        .putExtra("email",usernameEmail));
                      /*  Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                        startActivity(intent);*/
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());
                        PrefUtils.saveLoginUser(LoginActivity.this, Constants.PREF_USER, response.body().getData());
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

}