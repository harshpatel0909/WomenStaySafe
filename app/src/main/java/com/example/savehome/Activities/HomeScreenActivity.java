package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.savehome.Fragments.ContactsFragment;
import com.example.savehome.Fragments.FilesFragment;
import com.example.savehome.Fragments.HomeFragment;
import com.example.savehome.Fragments.ProfileFragment;
import com.example.savehome.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavi)
    BottomNavigationView bottomNavi;

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);

        bottomNavi.setItemIconTintList(null);
        replaceFragment(new HomeFragment());
        bottomNavi.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navHome:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navFiles:
                    replaceFragment(new FilesFragment());
                    break;
                case R.id.navContacts:
                    replaceFragment(new ContactsFragment());
                    break;
                case R.id.navProfile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;

        });
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }
}
