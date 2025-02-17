package com.example.savehome.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.savehome.Adapter.FragmentAdapter;
import com.example.savehome.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.ButterKnife;

public class FilesFragment extends Fragment implements View.OnTouchListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_files, container, false);
        ButterKnife.bind(this, view);
        TabLayout tableLayout = view.findViewById(R.id.tabMode);
        ViewPager viewPager = view.findViewById(R.id.viewLine);
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        return view;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
        /*
        viewPager.setUserInputEnabled(false);
       viewPager.requestDisallowInterceptTouchEvent(false);
        adapter = new Adapter(getActivity().getSupportFragmentManager(),getActivity().getLifecycle());
        adapter.addFragment(new AudioFragment(),"Audio");
        adapter.addFragment(new VideoFragment(),"Video");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setUserInputEnabled(false);
*/
    }

}