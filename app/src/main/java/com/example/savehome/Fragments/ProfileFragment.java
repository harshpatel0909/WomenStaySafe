package com.example.savehome.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.savehome.Activities.ChangePasswordActivity;
import com.example.savehome.Activities.EditProfileActivity;
import com.example.savehome.Activities.HomeScreenActivity;
import com.example.savehome.Activities.LoginOptionActivity;
import com.example.savehome.Activities.TermsAndConditions;
import com.example.savehome.Model.EditProfileModel;
import com.example.savehome.Model.LogOutModel;
import com.example.savehome.Model.LoginModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.webservice.RetroConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    Context context;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.userProfileFG)
    ImageView userProfileFG;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvEditProfile)
    ImageView IvEditProfile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.userNameFG)
    TextView userNameFG;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.userNumberFG)
    TextView userNumberFG;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.userEmailFG)
    TextView userEmailFG;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cvTermsAndCondition)
    CardView cvTermsAndCondition;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cvChangePassword)
    CardView cvChangePassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnLogoutProfile)
    Button btnLogoutProfile;

    String busername = "", bemail = "", bgender = "", bpassword = "", bImg = "";
    String gender = "", email = "", password = "", name = "", fcm_token = "", fcm_id = "", device_type = "", photo = "";
    String MyToken = "";
    List<LoginModel.Data> data = new ArrayList<>();

    private File imagePath;
    LoginModel.Data userData;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle != null) {
            busername = bundle.getString("username");
            bemail = bundle.getString("email");
            bImg = bundle.getString("bImg");
        }

        userNameFG.setText(busername);
        userEmailFG.setText(bemail);

        /*Glide.with(requireActivity()).load(bImg).into(userProfileFG);*/
        // userData = PrefUtils.getLoginUser(requireActivity());

        if (PrefUtils.getLoginUser(requireActivity()) != null) {
      /*      name = userData.getName();
            email = userData.getEmail();
            photo = userData.getPhoto();
            fcm_token = userData.getCustomToken();*/

            /*        Glide.with(requireActivity()).load(Constants.basePath + photo).into(userProfileFG);*/
            userNameFG.setText(Objects.requireNonNull(PrefUtils.getLoginUser(requireActivity())).getName());
            userEmailFG.setText(Objects.requireNonNull(PrefUtils.getLoginUser(requireActivity())).getEmail());
            MyToken = fcm_token;

        } else {
            EditProfileDetail(name, email);
        }
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.btnLogoutProfile, R.id.cvTermsAndCondition, R.id.cvChangePassword, R.id.IvEditProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cvTermsAndCondition:
                startActivity(new Intent(requireActivity(), TermsAndConditions.class));
                break;
            case R.id.cvChangePassword:
                startActivity(new Intent(requireActivity(), ChangePasswordActivity.class));
                break;
            case R.id.IvEditProfile:
                startActivity(new Intent(requireActivity(), EditProfileActivity.class));
                break;
            case R.id.btnLogoutProfile:
                Call<LogOutModel> call = RetroConfig.retrofit().LogOutDetail(Objects.requireNonNull(PrefUtils.getLoginUser(requireActivity())).getCustomToken());
                call.enqueue(new Callback<LogOutModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LogOutModel> call, @NonNull Response<LogOutModel> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                                startActivity(new Intent(requireActivity(), LoginOptionActivity.class));
                                Log.e("message", response.body().getMessage());
                                Toast.makeText(requireActivity(), "Logout Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LogOutModel> call, @NonNull Throwable t) {
                        Toast.makeText(requireActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private void EditProfileDetail(String name, String email) {
        Call<EditProfileModel> call = RetroConfig.retrofit().EditProfileDetail(bImg, name, email);
        call.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<EditProfileModel> call, @NonNull Response<EditProfileModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getData() != null) {
                        userNameFG.setText(response.body().getData().getName());
                        userEmailFG.setText(response.body().getData().getEmail());
                        startActivity(new Intent(requireActivity(), HomeScreenActivity.class));
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(requireActivity(), "Save Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}