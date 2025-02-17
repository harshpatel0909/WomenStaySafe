package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.VerifyOTPModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.webservice.RetroConfig;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConfirmAccountActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBackConfirmAccount)
    ImageView ivBackConfirmAccount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvMail)
    TextView tvMail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.otp_view)
    OtpTextView otp_view;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Resend)
    TextView Resend;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);
        otp_view = findViewById(R.id.otp_view);
        ButterKnife.bind(this);
    }

    public void Confirm(View view) {
        boolean isValid = true;
        String code = Objects.requireNonNull(otp_view.getOTP());

        if (TextUtils.isEmpty(code)) {
            isValid = false;
            Toast.makeText(ConfirmAccountActivity.this, "Please Enter 4-Digit Code", Toast.LENGTH_SHORT).show();
        }
        if (isValid) {
            VerifyCode(code);
        }

    }

    private void VerifyCode(String code) {
        Call<VerifyOTPModel> call = RetroConfig.retrofit().verifyOTP(code);
        call.enqueue(new Callback<VerifyOTPModel>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOTPModel> call, @NonNull Response<VerifyOTPModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        startActivity(new Intent(ConfirmAccountActivity.this, ResetPasswordActivity.class));
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(ConfirmAccountActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOTPModel> call, @NonNull Throwable t) {
                Toast.makeText(ConfirmAccountActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @OnClick({R.id.ivBackConfirmAccount})
    public void ivBackConfirmAccount(View view) {
        startActivity(new Intent(ConfirmAccountActivity.this, ForgotPasswordActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}