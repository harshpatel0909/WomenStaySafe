package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.EditProfileModel;
import com.example.savehome.Model.LoginModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.webservice.RetroConfig;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackEditProfile)
    ImageView IvBackEditProfile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivProfileEditProfile)
    ImageView ivProfileEditProfile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etUsernameEditProfile)
    EditText etUsernameEditProfile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etEmailEditProfile)
    EditText etEmailEditProfile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPhoneEditProfile)
    EditText etPhoneEditProfile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnSaveEditProfile)
    Button btnSaveEditProfile;

    String username = "", email = "", gender = "", password = "", bImg = "";
    //    private File imagePath;
    String imagePath = "";
    LoginModel.Data data;
    LoginModel.Data userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

/*        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("bName");
            email = bundle.getString("bMobile");

        }

        etUsernameEditProfile.setText(username);
        etEmailEditProfile.setText(email);


        userData = PrefUtils.getLoginUser(EditProfileActivity.this, Constants.PREF_USER);
        if (userData != null) {
            username = userData.getName();
            email = userData.getEmail();

            etUsernameEditProfile.setText(username);
            etEmailEditProfile.setText(email);


        }*//*    etUsernameEditProfile.setText(response.body().getData().getName());
             etEmailEditProfile.setText(response.body().getData().getEmail());*/
/*        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            email = bundle.getString("email");
            gender = bundle.getString("gender");
            password = bundle.getString("password");
            bImg = bundle.getString("bImg");
        }

        Glide.with(EditProfileActivity.this).load(bImg).into(ivProfileEditProfile);*/

        EditProfileDetail(username, email, bImg);
    }

    private void EditProfileDetail(String username, String email, String bImg) {

        Call<EditProfileModel> call = RetroConfig.retrofit().EditProfileDetail(bImg, username, email);
        call.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<EditProfileModel> call, @NonNull Response<EditProfileModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        etUsernameEditProfile.setText(response.body().getData().getName());
                        etEmailEditProfile.setText(response.body().getData().getEmail());
                        startActivity(new Intent(EditProfileActivity.this, HomeScreenActivity.class));
                 /*       Intent intent = new Intent(EditProfileActivity.this, HomeScreenActivity.class);
                        startActivity(intent);*/
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(EditProfileActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.IvBackEditProfile})
    public void IvBackEditProfile(View view) {
        onBackPressed();
    }


    @OnClick({R.id.btnSaveEditProfile})
    public void ProfileSave(View view) {
        boolean isValid = true;
        String username = Objects.requireNonNull(etUsernameEditProfile.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmailEditProfile.getText()).toString().trim();
        String phone = Objects.requireNonNull(etPhoneEditProfile.getText()).toString().trim();

        if (TextUtils.isEmpty(username) || (TextUtils.isEmpty(email) || (TextUtils.isEmpty(phone)))) {
            isValid = false;
            Toast.makeText(EditProfileActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValid) {
            UpdateProfile(username, email, bImg);

            return;
        }
    }

    private void UpdateProfile(String username, String email, String bImg) {
      /*  String user_name = String.create(MediaType.parse("text/plain"), username);
        RequestBody email_id = RequestBody.create(MediaType.parse("text/plain"), email);
       // RequestBody user_phone = RequestBody.create(MediaType.parse("text/plain"), phone);*/

        String user_name = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), username));
        String email_id = String.valueOf(RequestBody.create(MediaType.parse("text/plain"), email));

        RequestBody gender_user = null;
        MultipartBody.Part image_user = null;
        MultipartBody.Part imageMB = null;

        if (!TextUtils.isEmpty(imagePath)) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(imagePath));
            imageMB = MultipartBody.Part.createFormData("image", new File(imagePath).getName(), imageBody);
        }

        Call<EditProfileModel> call = RetroConfig.retrofit().EditProfileDetail(imagePath, user_name, email_id);
        call.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<EditProfileModel> call, @NonNull Response<EditProfileModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {/*
                        etUsernameEditProfile.setText(response.body().getData().getName());
                        etEmailEditProfile.setText(response.body().getData().getEmail());*/
                        Intent intent = new Intent(EditProfileActivity.this, HomeScreenActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());

                        Toast.makeText(EditProfileActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



/*    public void ProfileSave(View view){
        boolean isValid = true;
        String username = Objects.requireNonNull(etUsernameEditProfile.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmailEditProfile.getText()).toString().trim();
        String phone = Objects.requireNonNull(etPhoneEditProfile.getText()).toString().trim();

        String gender = null;
        String image = null;


        if (TextUtils.isEmpty(username) || (TextUtils.isEmpty(email)|| (TextUtils.isEmpty(phone) ))) {
            isValid = false;
            Toast.makeText(EditProfileActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValid) {
            EditProfileDetail(username, email, phone,gender,image);
            return;
        }

    }*/

/*    private void EditProfileDetail(String username, String email, String phone,String gender,String image) {

        RequestBody user_name = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody email_id = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody user_phone = RequestBody.create(MediaType.parse("text/plain"), phone);

        RequestBody gender_user =null;
        MultipartBody.Part image_user = null;


        Call<EditProfileModel> call = RetroConfig.retrofit().EditProfileDetail(null,user_phone, null,user_name,email_id);
        call.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<EditProfileModel> call, @NonNull Response<EditProfileModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        etUsernameEditProfile.setText(response.body().getData().getName());
                        etEmailEditProfile.setText(response.body().getData().getEmail());
                        Intent intent = new Intent(EditProfileActivity.this, HomeScreenActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());

                        Toast.makeText(EditProfileActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

            }
        });



    }*/


}