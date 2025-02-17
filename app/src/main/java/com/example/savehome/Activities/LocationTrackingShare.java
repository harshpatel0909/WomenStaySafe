package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationTrackingShare extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackLocationShare)
    ImageView IvBackLocationShare;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etCurrentLocation)
    TextView etCurrentLocation;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etSelectDestination)
    TextView etSelectDestination;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivShare)
    ImageView ivShare;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnEndJourney)
    Button btnEndJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracking_share);
        ButterKnife.bind(this);

    }
    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.ivShare, R.id.IvBackLocationShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivShare:
                startActivity(new Intent(LocationTrackingShare.this, SendLocationActivity.class));
                break;
            case R.id.IvBackLocationShare:
                startActivity(new Intent(LocationTrackingShare.this, SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }


}