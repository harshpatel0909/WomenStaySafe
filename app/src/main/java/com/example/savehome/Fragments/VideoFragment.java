package com.example.savehome.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savehome.Adapter.OnActivityTouchListener;
import com.example.savehome.Adapter.RecyclerTouchListener;
import com.example.savehome.Model.AudioVideoDataModel;
import com.example.savehome.Model.ToastUtil;
import com.example.savehome.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class VideoFragment extends Fragment implements RecyclerTouchListener.RecyclerTouchListenerHelper {
    RecyclerView mRecyclerView;
    MainAdapter mAdapter;
    String[] dialogItems;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);

/*        FragmentVideoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, null, false);
        View view = binding.getRoot();*/

        dialogItems = new String[25];
        for (int i = 0; i < 25; i++) {
            dialogItems[i] = String.valueOf(i + 1);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.VideoRecycleView);
            mAdapter = new MainAdapter(getActivity(), getData());

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);
            onTouchListener
                    .setIndependentViews(R.id.edit_recycle_row)
                    .setViewsToFade(R.id.edit_recycle_row)
                    .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                        @Override
                        public void onRowClicked(int position) {
                            ToastUtil.makeToast(getActivity(), "Row " + (position + 1) + " clicked!");
                        }

                        @Override
                        public void onIndependentViewClicked(int independentViewID, int position) {
                            ToastUtil.makeToast(getActivity(), "Button in row " + (position + 1) + " clicked!");
                        }
                    })
                    .setLongClickable(true, position -> ToastUtil.makeToast(getActivity(), "Row " + (position + 1) + " long clicked!"))
                    .setSwipeOptionViews(R.id.delete)
                    .setSwipeable(R.id.rowFG, R.id.rowBG, (viewID, position) -> {
                        String message = "";
                        if (viewID == R.id.delete) {
                            message += "Add";
                        }
                        message += " clicked for row " + (position + 1);
                        ToastUtil.makeToast(getActivity(), message);
                    });
        }
        return view;
    }

/*
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
*/

    private List<AudioVideoDataModel> getData() {
        List<AudioVideoDataModel> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new AudioVideoDataModel(null, null, null, null, "Video" + (i + 1), "(10:00)", "12:00 PM", "07/07/2022"));
        }
        return list;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) touchListener.getTouchCoordinates(ev);
        //return super.dispatchTouchEvent(ev);
        return dispatchTouchEvent(ev);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = (OnActivityTouchListener) listener;

    }


    private static class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
        FragmentActivity inflater;
        List<AudioVideoDataModel> modelList;

        public MainAdapter(FragmentActivity inflater, List<AudioVideoDataModel> modelList) {
            this.inflater = inflater;
            this.modelList = modelList;
        }

        @NonNull
        @Override
        public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_data_layout, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {
            holder.bindData(modelList.get(position));

        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        public static class MainViewHolder extends RecyclerView.ViewHolder {
            TextView videoTitle, videoTime, videoClockTime, videoDate;

            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
                videoTitle = (TextView) itemView.findViewById(R.id.tvVideo);
                videoTime = (TextView) itemView.findViewById(R.id.tvVideoTime);
                videoClockTime = (TextView) itemView.findViewById(R.id.tvVideoClockTime);
                videoDate = (TextView) itemView.findViewById(R.id.tvVideoDate);
            }

            public void bindData(AudioVideoDataModel audioVideoDataModel) {
                videoTitle.setText(audioVideoDataModel.getVideoTitle());
                videoTime.setText(audioVideoDataModel.getVideoDuration());
                videoClockTime.setText(audioVideoDataModel.getVideoDayTime());
                videoDate.setText(audioVideoDataModel.getVideoDate());

            }
        }
    }
}