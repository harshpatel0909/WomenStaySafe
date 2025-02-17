package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.savehome.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class TermsAndConditions extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvTermsAndConditions)
    ImageView IvTermsAndConditions;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.JustifyTextView)
    JustifyTextView JustifyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.IvTermsAndConditions)
    public void onViewClicked() {
        onBackPressed();
    }
}