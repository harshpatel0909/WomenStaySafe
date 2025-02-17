package com.example.savehome.Model;

public class AudioVideoDataModel {

    String audioTitle , audioDuration , audioDayTime, audioDate;
    String videoTitle, videoDuration, videoDayTime, videoDate;

    public AudioVideoDataModel(String audioTitle, String audioDuration, String audioDayTime, String audioDate, String videoTitle, String videoDuration, String videoDayTime, String videoDate) {
        this.audioTitle = audioTitle;
        this.audioDuration = audioDuration;
        this.audioDayTime = audioDayTime;
        this.audioDate = audioDate;
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.videoDayTime = videoDayTime;
        this.videoDate = videoDate;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getAudioDayTime() {
        return audioDayTime;
    }

    public void setAudioDayTime(String audioDayTime) {
        this.audioDayTime = audioDayTime;
    }

    public String getAudioDate() {
        return audioDate;
    }

    public void setAudioDate(String audioDate) {
        this.audioDate = audioDate;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoDayTime() {
        return videoDayTime;
    }

    public void setVideoDayTime(String videoDayTime) {
        this.videoDayTime = videoDayTime;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }
}
