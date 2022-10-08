package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Model;

import android.graphics.Bitmap;

public class VideoRecordModel {
    private String date;
    private String fileName;
    private String filePath;
    private boolean isSelect = false;
    private Bitmap thumbnails;
    private String timeVideo;

    public VideoRecordModel(String str, Bitmap bitmap, String str2, String str3, String str4) {
        this.filePath = str;
        this.thumbnails = bitmap;
        this.timeVideo = str2;
        this.fileName = str3;
        this.date = str4;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public Bitmap getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(Bitmap bitmap) {
        this.thumbnails = bitmap;
    }

    public String getTimeVideo() {
        return this.timeVideo;
    }

    public void setTimeVideo(String str) {
        this.timeVideo = str;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }
}
