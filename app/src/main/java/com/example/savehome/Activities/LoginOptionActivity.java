package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.savehome.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginOptionActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnLogin_SignUp)
    Button btnLogin_SignUp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);
        ButterKnife.bind(this);
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.btnLogin_SignIn, R.id.btnLogin_SignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin_SignIn:
                startActivity(new Intent(LoginOptionActivity.this, LoginActivity.class));
                break;
            case R.id.btnLogin_SignUp:
                startActivity(new Intent(LoginOptionActivity.this, SignUpActivity.class));
                break;

        }
    }
}