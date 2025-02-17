package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import com.example.savehome.Model.RegisterModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.databinding.ActivitySplashScreenBinding;

import butterknife.ButterKnife;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    RegisterModel.Data userDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(() -> {
            userDetail = PrefUtils.getSignUpUser(SplashScreenActivity.this, Constants.PREF_USER);
            if (userDetail != null){
                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
            }   else {
                startActivity(new Intent(getApplicationContext(), LoginOptionActivity.class));
                finish();
            }

          /*  Intent intent = new Intent(SplashScreenActivity.this, LoginOptionActivity.class);
            SplashScreenActivity.this.startActivity(intent);
            SplashScreenActivity.this.finish();*/
        }, 2000);
    }
}