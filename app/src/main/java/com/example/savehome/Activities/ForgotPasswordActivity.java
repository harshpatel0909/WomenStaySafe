package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.ForgotPasswordModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.webservice.RetroConfig;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBackForgotPassword)
    ImageView ivBackForgotPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etEmail_forgot)
    EditText etEmail_forgot;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnSend)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBackForgotPassword})
    public void ivBackForgotPassword(View view) {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void ForgotPassword(View view) {
        boolean isValid = true;
        String EmailForgot = Objects.requireNonNull(etEmail_forgot.getText()).toString().trim();

        if (TextUtils.isEmpty(EmailForgot)) {
            isValid = false;
            Toast.makeText(ForgotPasswordActivity.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
        }

        if (isValid) {
            ForgotDetail(EmailForgot);
        }
    }

    private void ForgotDetail(String emailForgot) {
        Call<ForgotPasswordModel> call = RetroConfig.retrofit().forgotPassword(emailForgot);
        call.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotPasswordModel> call, @NonNull Response<ForgotPasswordModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, ConfirmAccountActivity.class);
                        startActivity(intent);
                    //    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(ForgotPasswordActivity.this, "Code sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotPasswordModel> call, @NonNull Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}