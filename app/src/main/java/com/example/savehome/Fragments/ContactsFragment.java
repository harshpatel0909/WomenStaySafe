package com.example.savehome.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.savehome.Activities.AddNumberActivity;
import com.example.savehome.Adapter.OnActivityTouchListener;
import com.example.savehome.Adapter.RecyclerTouchListener;
import com.example.savehome.Adapter.SetContactAdapter;
import com.example.savehome.Model.ListContactModel;
import com.example.savehome.Model.ToastUtil;
import com.example.savehome.Model.UserDataModel;
import com.example.savehome.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsFragment extends Fragment implements RecyclerTouchListener.RecyclerTouchListenerHelper {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.FABtnAddContact)
    FloatingActionButton FABtnAddContact;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rowBG)
    LinearLayout rowBG;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rowFG)
    LinearLayout rowFG;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.delete)
    ImageView delete;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.userCardView)
    CardView userCardView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.UserCardIV)
    ImageView UserCardIV;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvUserPhone)
    TextView tvUserPhone;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edit_recycle_row)
    ImageView edit_recycle_row;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivAddContact)
    ImageView ivAddContact;


    List<ListContactModel.Data> data = new ArrayList<>();
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    SetContactAdapter mAdapter;

    RecyclerView mRecyclerView;
    String MyToken = "";

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);

/*        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new MyContactsActivity.MainAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvContactList);
      //  mAdapter = new SetContactAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        onTouchListener = new RecyclerTouchListener(requireActivity(), mRecyclerView);
        onTouchListener
                .setIndependentViews(R.id.edit_recycle_row)
                .setViewsToFade(R.id.edit_recycle_row)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        ToastUtil.makeToast(requireContext(), "Row " + (position + 1) + " clicked!");
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        ToastUtil.makeToast(requireContext(), "Button in row " + (position + 1) + " clicked!");
                    }
                })
                .setLongClickable(true, position -> ToastUtil.makeToast(requireContext(), "Row " + (position + 1) + " long clicked!"))
                .setSwipeOptionViews(R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, (viewID, position) -> {
                    String message = "";
                    if (viewID == R.id.delete) {
                        message += "Add";
                    }
                    message += " clicked for row " + (position + 1);
                    ToastUtil.makeToast(requireContext(), message);
                });
        return view;
    }

/*
    private List<ListContactModel.Data> getData() {
        List<ListContactModel.Data> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new ListContactModel.Data());
        }
        return list;
    }*/

    private List<UserDataModel> getData() {
        List<UserDataModel> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new UserDataModel("harsh " + (i + 1), "1234567890"));
        }
        return list;
    }



    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.FABtnAddContact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.FABtnAddContact:
                startActivity(new Intent(requireActivity(), AddNumberActivity.class));
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) touchListener.getTouchCoordinates(ev);
        return dispatchTouchEvent(ev);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;

    }
}
