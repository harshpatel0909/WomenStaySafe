package com.example.savehome.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savehome.Fragments.ContactsFragment;
import com.example.savehome.Model.UserDataModel;
import com.example.savehome.R;
import java.util.ArrayList;
import java.util.List;


public class SetContactAdapter extends RecyclerView.Adapter<SetContactAdapter.MainHolder> {

/*    List<ListContactModel.Data> data;
    LayoutInflater inflater;*/

    LayoutInflater inflater;
    List<UserDataModel> modelList;

    public SetContactAdapter(Context context, List<UserDataModel> list) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }
/*
    public SetContactAdapter(Context context, List<ListContactModel.Data> list) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>(list);
    }
*/

    @NonNull
    @Override
    public SetContactAdapter.MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_data_layout, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetContactAdapter.MainHolder holder, int position) {
        /*        holder.bindData(data.get(position));*/
        holder.bindData(modelList.get(position));

    }

    @Override
    public int getItemCount() {
        /*    return data.size();*/
        return modelList.size();
    }

    public static class MainHolder extends RecyclerView.ViewHolder {
        TextView userName, UserPhone;


        public MainHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.tvUserName);
            UserPhone = (TextView) itemView.findViewById(R.id.tvUserPhone);
        }


/*        public void bindData(ListContactModel.Data listContactModel) {
            userName.setText(listContactModel.getName());
            UserPhone.setText(listContactModel.getNumber());

        }*/

        public void bindData(UserDataModel userDataModel) {
            userName.setText(userDataModel.getUserName());
            UserPhone.setText(userDataModel.getUserPhone());
        }
    }
}
