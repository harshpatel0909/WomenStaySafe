package com.example.savehome.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.savehome.Fragments.AudioFragment;
import com.example.savehome.Fragments.VideoFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final String[] tabTitles = new String[]{"Audio", "Video"};

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AudioFragment();
            case 1:
                return new VideoFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
