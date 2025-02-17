package com.example.savehome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savehome.Model.AddContactModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
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

public class AddNumberActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackAddNumber)
    ImageView IvBackAddNumber;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivProfileAddManually)
    ImageView ivProfileAddManually;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etName)
    EditText etName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPhoneAddManually)
    EditText etPhoneAddManually;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnSaveAddManually)
    Button btnSaveAddManually;

    String imagePath = "";

    private static final String TAG = AddNumberActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        ButterKnife.bind(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.IvBackAddNumber, R.id.btnSaveAddManually, R.id.ivProfileAddManually})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvBackAddNumber:
                onBackPressed();
                break;
            case R.id.btnSaveAddManually:
                boolean isValid = true;
                String userName = Objects.requireNonNull(etName.getText()).toString().trim();
                String phoneNumber = Objects.requireNonNull(etPhoneAddManually.getText()).toString().trim();
                String img_url = imagePath;

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(img_url)) {
                    isValid = false;
                    Toast.makeText(AddNumberActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isValid) {
                    AddContactDetail(userName, phoneNumber, img_url);
                }
                break;

//            case R.id.ivProfileAddManually:
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(this);
//                break;


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
                ivProfileAddManually.setImageURI(resultUri);
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

    private void AddContactDetail(String userName, String phoneNumber, String img_url) {

        RequestBody user_name = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody phone_number = RequestBody.create(MediaType.parse("text/plain"), phoneNumber);

        MultipartBody.Part imageMB = null;
        if (!TextUtils.isEmpty(img_url)) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(img_url));
            imageMB = MultipartBody.Part.createFormData("image", new File(img_url).getName(), imageBody);
        }

        Call<AddContactModel> call = RetroConfig.retrofit().AddContactDetail((Objects.requireNonNull(PrefUtils.getLoginUser(this)).getCustomToken()),imageMB, Objects.requireNonNull(PrefUtils.getLoginUser(this)).getId(), user_name, phone_number);
        call.enqueue(new Callback<AddContactModel>() {
            @Override
            public void onResponse(@NonNull Call<AddContactModel> call, @NonNull Response<AddContactModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        startActivity(new Intent(AddNumberActivity.this, HomeScreenActivity.class));
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(AddNumberActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddContactModel> call, @NonNull Throwable t) {
                Toast.makeText(AddNumberActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

/*            Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();*/
/*    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNumberActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(AddNumberActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(AddNumberActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }*/