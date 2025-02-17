package com.example.savehome.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savehome.Adapter.OnActivityTouchListener;
import com.example.savehome.Adapter.RecyclerTouchListener;
import com.example.savehome.Model.UserDataModel;
import com.example.savehome.R;

import java.util.ArrayList;
import java.util.List;

public class MyContactsActivity extends AppCompatActivity implements RecyclerTouchListener.RecyclerTouchListenerHelper {
    RecyclerView mRecyclerView;
    MainAdapter mAdapter;
    String[] dialogItems;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);

        dialogItems = new String[25];
        for (int i = 0; i < 25; i++) {
            dialogItems[i] = String.valueOf(i + 1);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new MainAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        onTouchListener = new RecyclerTouchListener(this, mRecyclerView);
        onTouchListener
                .setIndependentViews(R.id.edit_recycle_row)
                .setViewsToFade(R.id.edit_recycle_row)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(MyContactsActivity.this, "Row"+ (position + 1) +" clicked!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        Toast.makeText(MyContactsActivity.this,"Button in row " + (position + 1) + " clicked!" , Toast.LENGTH_SHORT).show();
                    }
                })
                .setLongClickable(true, position ->

                        Toast.makeText(this, "Row " + (position + 1) + " long clicked!", Toast.LENGTH_SHORT).show())
                .setSwipeOptionViews(R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, (viewID, position) -> {
                    String message = "";
                    if (viewID == R.id.delete) {
                        message += "Add";
                    }
                    message += " clicked for row " + (position + 1);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    private List<UserDataModel> getData() {
        List<UserDataModel> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new UserDataModel("harsh " + (i + 1), "1234567890"));
        }
        return list;
    }

/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) touchListener.getTouchCoordinates(ev);
        return super.dispatchTouchEvent(ev);
    }
*/

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    private static class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
        LayoutInflater inflater;
        List<UserDataModel> modelList;

        public MainAdapter(Context context, List<UserDataModel> list) {
            inflater = LayoutInflater.from(context);
            modelList = new ArrayList<>(list);
        }

        @NonNull
        @Override
        public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.user_data_layout, parent, false);
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

        static class MainViewHolder extends RecyclerView.ViewHolder {
            TextView userName, UserPhone;

            public MainViewHolder(View itemView) {
                super(itemView);
                userName = (TextView) itemView.findViewById(R.id.tvUserName);
                UserPhone = (TextView) itemView.findViewById(R.id.tvUserPhone);
            }

            public void bindData(UserDataModel userDataModel) {
                userName.setText(userDataModel.getUserName());
                UserPhone.setText(userDataModel.getUserPhone());
            }
        }
    }
}
