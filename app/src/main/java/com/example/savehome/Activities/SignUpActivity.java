package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.RegisterModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.webservice.RetroConfig;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBackSignUp)
    ImageView ivBackSignUp;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivProfileSignUp)
    ImageView ivProfileSignUp;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etUsernameSignup)
    EditText etUsernameSignup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etEmail_SignUp)
    EditText etEmail_SignUp;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPasswordSignUp)
    EditText etPasswordSignUp;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etConfirmPasswordSignUp)
    EditText etConfirmPasswordSignUp;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TermsConditions)
    TextView TermsConditions;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    boolean passwordVisible;
    String fcmToken = "";
    String token = "";
    String imagePath = "";

    public static final int REQUEST_IMAGE = 100;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        ImagePickerActivity.clearCache(this);
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @OnClick({R.id.ivProfileSignUp, R.id.TermsConditions, R.id.ivBackSignUp, R.id.etPasswordSignUp, R.id.etConfirmPasswordSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivProfileSignUp:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;
            case R.id.TermsConditions:
                startActivity(new Intent(SignUpActivity.this, TermsAndConditions.class));
                break;
            case R.id.ivBackSignUp:
                startActivity(new Intent(SignUpActivity.this, LoginOptionActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.etPasswordSignUp:
                etPasswordSignUp.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etPasswordSignUp.getRight() - etPasswordSignUp.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etPasswordSignUp.getSelectionEnd();
                            if (passwordVisible) {
                                etPasswordSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etPasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etPasswordSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etPasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etPasswordSignUp.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
            case R.id.etConfirmPasswordSignUp:
                etConfirmPasswordSignUp.setOnTouchListener((view1, motionEvent) -> {
                    final int Right = 2;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= etConfirmPasswordSignUp.getRight() - etConfirmPasswordSignUp.getCompoundDrawables()[Right].getBounds().width()) {
                            int selection = etConfirmPasswordSignUp.getSelectionEnd();
                            if (passwordVisible) {
                                etConfirmPasswordSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
                                etConfirmPasswordSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible = false;
                            } else {
                                etConfirmPasswordSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visible, 0);
                                etConfirmPasswordSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible = true;
                            }
                            etConfirmPasswordSignUp.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri resultUri = result.getUri();
                ivProfileSignUp.setImageURI(resultUri);
                imagePath = getPath(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
            }
        }
    }

    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);

/*        Glide.with(this).load(uri)
                .into(ivProfileSignUp);
        ivProfileSignUp.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));*/
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void Signup(View view) {
        boolean isValid = true;
        String username = Objects.requireNonNull(etUsernameSignup.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmail_SignUp.getText()).toString().trim();
        String gender = autoCompleteTextView.getText().toString().trim();
        String password = Objects.requireNonNull(etPasswordSignUp.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(etConfirmPasswordSignUp.getText()).toString().trim();
        String img_url = imagePath;

        if (TextUtils.isEmpty(username) || (TextUtils.isEmpty(email) || (TextUtils.isEmpty(gender) || (TextUtils.isEmpty(img_url) || (TextUtils.isEmpty(password) || (TextUtils.isEmpty(confirmPassword))))))) {
            isValid = false;
            Toast.makeText(SignUpActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Password Would Not be matched", Toast.LENGTH_SHORT).show();
            return;

        }
        if (password.length() <= 8 || confirmPassword.length() <= 8) {
            Toast.makeText(SignUpActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValid) {
            SignUpDetail(username, email, gender, password, confirmPassword, img_url);
            return;
        }
    }

    private void SignUpDetail(String username, String email, String gender, String password, String confirmPassword, String img_url) {

        RequestBody user_name = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody email_id = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody selection_gender = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody password_Up = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody confirm_password_up = RequestBody.create(MediaType.parse("text/plain"), confirmPassword);
        RequestBody fcm_tkn_id = RequestBody.create(MediaType.parse("text/plain"), fcmToken);
        RequestBody tkn_id = RequestBody.create(MediaType.parse("text/plain"), token);


        MultipartBody.Part imageMB = null;
        if (!TextUtils.isEmpty(img_url)) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(img_url));
            imageMB = MultipartBody.Part.createFormData("image", new File(img_url).getName(), imageBody);
        }

        Call<RegisterModel> call = RetroConfig.retrofit().register(user_name, email_id, selection_gender, password_Up, fcm_tkn_id, tkn_id, imageMB);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(@NonNull Call<RegisterModel> call, @NonNull Response<RegisterModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {

                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class)
                                .putExtra("username", username)
                                .putExtra("email", email)
                                .putExtra("gender", gender)
                                .putExtra("password", password)
                                .putExtra("bImg", img_url));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Log.e("message", response.body().getMessage());
                        PrefUtils.saveSignUpUser(SignUpActivity.this, Constants.PREF_USER, response.body().getData());
                        Toast.makeText(SignUpActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterModel> call, @NonNull Throwable t) {
                Toast.makeText(SignUpActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String[] gender = getResources().getStringArray(R.array.Gender);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(SignUpActivity.this, R.layout.gender_dropdown_item, gender);
        autoCompleteTextView.setAdapter(itemAdapter);
    }
}