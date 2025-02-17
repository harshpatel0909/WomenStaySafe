package com.example.savehome.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.savehome.Model.AddAudioModel;
import com.example.savehome.R;
import com.example.savehome.Util.PrefUtils;
import com.example.savehome.webservice.RetroConfig;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class AudioActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackAudio)
    ImageView IvBackAudio;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ib_record)
    ImageView ib_record;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ib_play)
    ImageView ib_play;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.AudioBg)
    ImageView AudioBg;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_time)
    TextView tv_time;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lav_playing)
    LottieAnimationView lav_playing;


    /*    private long PauseOffSet = 0;
        private boolean isPlaying = false;*/
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 101;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    boolean isRecording = false;
    boolean isPlaying = false;
    int seconds = 0;
    String path = null;
    LottieAnimationView lavPlaying;
    int dummySeconds = 0;
    int playableSeconds = 0;
    Handler handler;

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);
        mediaPlayer = new MediaPlayer();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.IvBackAudio, R.id.ib_record, R.id.ib_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvBackAudio:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
            case R.id.ib_record:
                if (checkRecordingPermission()) {
                    if (!isRecording) {
                        isRecording = true;
                        executorService.execute(() -> {
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setOutputFile(getRecordingFilePath());
                            path = getRecordingFilePath();

                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            try {
                                mediaRecorder.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaRecorder.start();
                            runOnUiThread(() -> {
                                AudioBg.setVisibility(View.VISIBLE);
                                lav_playing.setVisibility(View.GONE);
                                playableSeconds = 0;
                                seconds = 0;
                                dummySeconds = 0;
                                ib_record.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.recording_active));
                                runTimer();
                            });
                        });

                    } else {
                        executorService.execute(() -> {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                            playableSeconds = seconds;
                            dummySeconds = seconds;
                            seconds = 0;
                            isRecording = false;

                            runOnUiThread(() -> {
                                AudioBg.setVisibility(View.VISIBLE);
                                lav_playing.setVisibility(View.GONE);
                                handler.removeCallbacksAndMessages(null);
                                ib_record.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.recording_in_active));

                            });
                        });
                    }


                } else {
                    requestRecordingPermission();

                }
                break;
            case R.id.ib_play:
                if (!isPlaying) {
                    if (path != null) {
                        try {
                            mediaPlayer.setDataSource(getRecordingFilePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No Recording Present", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    isPlaying = true;
                    ib_play.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.recording_pause));
                    AudioBg.setVisibility(View.GONE);
                    lav_playing.setVisibility(View.VISIBLE);
                    runTimer();

                } else {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    isPlaying = false;
                    seconds = 0;
                    handler.removeCallbacksAndMessages(null);
                    AudioBg.setVisibility(View.VISIBLE);
                    lav_playing.setVisibility(View.GONE);
                    ib_play.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.recording_play));
                }
                break;
        }
    }

/*
    private void ApiCall() {

        Call<AddAudioModel>call = RetroConfig.retrofit().AddAudioDetail(PrefUtils.getLoginUser(this).getCustomToken(),,PrefUtils.getLoginUser(this).getId(),);
    }
*/

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "testFile" + ".mp3");
        return file.getPath();
    }

    public boolean checkRecordingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestRecordingPermission();
            return false;
        }
        return true;
    }

    private void runTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int sec = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, sec);
                tv_time.setText(time);
                if (isRecording || (isPlaying && playableSeconds != -1)) {
                    seconds++;
                    playableSeconds--;
                    if (playableSeconds == -1 && isPlaying) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        isPlaying = false;
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        AudioBg.setVisibility(View.VISIBLE);
                        lav_playing.setVisibility(View.GONE);
                        ib_play.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.recording_play));
                        return;
                    }
                }
                handler.postDelayed(this, 1000);

            }
        });
    }

    private void requestRecordingPermission() {
        ActivityCompat.requestPermissions(AudioActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord) {
                    Toast.makeText(this, "Permission Given", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    /*   private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }*//*    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.ivBtnReset, R.id.IvBackAudio,R.id.btnTogglePausePlay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBtnReset:
                showDialog();
                break;
            case R.id.IvBackAudio:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
            case R.id.btnTogglePausePlay:
                btnTogglePausePlay.setText(null);
                btnTogglePausePlay.setTextOn(null);
                btnTogglePausePlay.setTextOff(null);

                //the event Listener will be automatically generated by android studio
                btnTogglePausePlay.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        chronometer.setBase(SystemClock.elapsedRealtime() - PauseOffSet);
                        chronometer.start();
                        isPlaying = true;

                    } else {
                        chronometer.stop();
                        PauseOffSet = SystemClock.elapsedRealtime() - chronometer.getBase();
                        isPlaying = false;
                    }
                });
                break;

        }
    }*/
    /*        ivBtnReset.setOnClickListener(view -> {
            if (isPlaying) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                PauseOffSet = 0;
                chronometer.start();
                isPlaying = true;
            }
        });*/
    /*    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chronometer)
    Chronometer chronometer;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnAudioCancel)
    ImageView BtnAudioCancel;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBtnReset)
    ImageView ivBtnReset;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnTogglePausePlay)
    ToggleButton btnTogglePausePlay;*/
}
