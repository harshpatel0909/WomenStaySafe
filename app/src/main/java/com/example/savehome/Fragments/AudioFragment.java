package com.example.savehome.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class AudioFragment extends Fragment implements RecyclerTouchListener.RecyclerTouchListenerHelper {
    RecyclerView mRecyclerView;
    MainAdapter mAdapter;
    String[] dialogItems;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;

    public AudioFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        ButterKnife.bind(this, view);

        dialogItems = new String[25];
        for (int i = 0; i < 25; i++) {
            dialogItems[i] = String.valueOf(i + 1);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.AudioRecycleView);
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
                        Toast.makeText(requireActivity(), "Row " + (position + 1) + " clicked!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        Toast.makeText(requireActivity(), "Button in row " + (position + 1) + " clicked!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(requireActivity(), "Row " + (position + 1) + " clicked!", Toast.LENGTH_SHORT).show();

                });
        return view;
    }

/*    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }*/

    private List<AudioVideoDataModel> getData() {
        List<AudioVideoDataModel> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new AudioVideoDataModel("Audio " + (i + 1), "(20:05)", "4:58 PM", "01/03/2022",
                    null, null, null, null));

        }
        return list;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) touchListener.getTouchCoordinates(ev);
        //  return super.dispatchTouchEvent(ev);
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
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_data_layout, parent, false);
            //  View view = inflater.inflate(R.layout.audio_data_layout, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            holder.bindData(modelList.get(position));
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        public static class MainViewHolder extends RecyclerView.ViewHolder {
            TextView audioTitle, audioTime, audioClockTime, audioDate;

            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
                audioTitle = (TextView) itemView.findViewById(R.id.tvAudio);
                audioTime = (TextView) itemView.findViewById(R.id.tvAudioTime);
                audioClockTime = (TextView) itemView.findViewById(R.id.tvClockTime);
                audioDate = (TextView) itemView.findViewById(R.id.tvAudioDate);
            }

            public void bindData(AudioVideoDataModel userDataModel) {
                audioTitle.setText(userDataModel.getAudioTitle());
                audioTime.setText(userDataModel.getAudioDuration());
                audioClockTime.setText(userDataModel.getAudioDayTime());
                audioDate.setText(userDataModel.getAudioDate());
            }
        }
    }
}


