package com.example.savehome.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.savehome.Activities.AddNumberActivity;
import com.example.savehome.Activities.AudioActivity;
import com.example.savehome.Activities.LocationTracking;
import com.example.savehome.Activities.NotificationsActivity;
import com.example.savehome.Model.AddAudioModel;
import com.example.savehome.Model.AddVideoModel;
import com.example.savehome.R;
import com.example.savehome.Util.Constants;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.webservice.RetroConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class HomeFragment extends Fragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvNotificationTb)
    ImageView IvNotificationTb;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cvUnsure)
    CardView cvUnsure;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cvBitScared)
    CardView cvBitScared;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cvScared)
    CardView cvScared;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cvVeryScared)
    CardView cvVeryScared;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;//Any random number
    static final int REQUEST_TAKE_VIDEO = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    String currentVideoPath;
    AddAudioModel.Data data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.IvNotificationTb, R.id.cvUnsure, R.id.cvBitScared, R.id.cvScared, R.id.cvVeryScared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvNotificationTb:
                startActivity(new Intent(requireActivity(), NotificationsActivity.class));
                break;
            case R.id.cvUnsure:
                startActivity(new Intent(requireActivity(), LocationTracking.class));
                break;
            case R.id.cvBitScared:
                startActivity(new Intent(requireActivity(), AudioActivity.class));
                break;
            case R.id.cvVeryScared:
                showDialog();
                break;
            case R.id.cvScared:
                checkStoragePermission();
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                //change permission to WRITE_STORAGE
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(requireActivity())
                        .setTitle("Permission Required")
                        .setMessage("Storage permission is required to save video")
                        .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //on click Allow we need request again
                                dialogInterface.cancel();
                                ActivityCompat.requestPermissions(requireActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        }).setNegativeButton("DENIED", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    //cancel the dialog interface
                }).show();
                //forget to call show()
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        } else {

            Toast.makeText(getActivity(), "permission Already grated", Toast.LENGTH_SHORT).show();
            //capture video when permission is granted
            openCameraToCaptureVideo();

        }
    }

    private void openCameraToCaptureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File VideoFile = null;
            try {
                VideoFile = createVideoFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error while saving Videos.", Toast.LENGTH_LONG).show();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (VideoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.savehome",
                        VideoFile);
                //    String authorities = requireActivity().getPackageName() + ".fileprovider";
                //   Uri imageUri = FileProvider.getUriForFile(requireActivity(),authorities,photoFile);
                //  intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_VIDEO);
            }
        }
    }

    private File createVideoFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "mp4" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir("Videos");
        File video = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",        /*  suffix */
                storageDir      /* directory */
        );

   //     RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), imageFileName);
    /*    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", data.getAudio(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), data.getAudio());
        Call<AddVideoModel> call = RetroConfig.retrofit().
                AddVideoDetail(Objects.requireNonNull(PrefUtils.getLoginUser(requireContext())).getCustomToken(), fileToUpload, Objects.requireNonNull(PrefUtils.getLoginUser(requireContext())).getId(), timeStamp);
        call.enqueue(new Callback<AddVideoModel>() {
            @Override
            public void onResponse(@NonNull Call<AddVideoModel> call, @NonNull Response<AddVideoModel> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS)) {
                        *//*  startActivity(new Intent(AddNumberActivity.this, HomeScreenActivity.class));*//*
                        Log.e("message", response.body().getMessage());
                        Toast.makeText(requireActivity(), "Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddVideoModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });*/


        // Save a file: path for use with ACTION_VIEW intents
        currentVideoPath = video.getAbsolutePath();

        return video;
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_calling_police);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission is granted
                openCameraToCaptureVideo();

            } else {
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            //remove bundle in has null value
            //get file uri to upload to firestore Storage
            File f = new File(currentVideoPath);
            Uri contentUri = Uri.fromFile(f);
            Toast.makeText(getContext(), "uri " + contentUri, Toast.LENGTH_SHORT).show();
            //showing image to image view

        }

    }
}