package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.savehome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackSearchActivity)
    ImageView IvBackSearchActivity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvLocationTracking)
    TextView tvLocationTracking;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cardviewcurrentlocation)
    CardView cardviewcurrentlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.IvBackSearchActivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackSignIn:
                startActivity(new Intent(SearchActivity.this, LocationTracking.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

}