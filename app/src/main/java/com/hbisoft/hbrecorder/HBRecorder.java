package com.hbisoft.hbrecorder;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class HBRecorder implements MyListener {
    private int audioBitrate = 0;
    private int audioSamplingRate = 0;
    private String audioSource = "MIC";
    private byte[] byteArray;
    public final Context context;
    private boolean enableCustomSettings = false;
    private String fileName;
    public final HBRecorderListener hbRecorderListener;
    private boolean isAudioEnabled = true;
    boolean isPaused = false;
    private boolean isVideoHDEnabled = true;
    private int mScreenDensity;
    private int mScreenHeight;
    private int mScreenWidth;
    Uri mUri;
    boolean mWasUriSet = false;
    private String notificationButtonText;
    private String notificationDescription;
    private String notificationTitle;
    private int orientation;
    private String outputFormat = "DEFAULT";
    private String outputPath;
    private int resultCode;
    Intent service;
    private int videoBitrate = 40000000;
    private String videoEncoder = "DEFAULT";
    private int videoFrameRate = 30;
    boolean wasOnErrorCalled = false;

    public void initScreenshotCapture() {
    }

    public HBRecorder(Context context2, HBRecorderListener hBRecorderListener) {
        this.context = context2.getApplicationContext();
        this.hbRecorderListener = hBRecorderListener;
        setScreenDensity();
    }

    public void setOrientationHint(int orientationHint) {
        this.orientation = orientationHint;
    }

    public void setOutputPath(String outputPath2) {
        this.outputPath = outputPath2;
    }

    public void setOutputUri(Uri uri) {
        this.mWasUriSet = true;
        this.mUri = uri;
    }

    public boolean wasUriSet() {
        return this.mWasUriSet;
    }

    public void setFileName(String fileName2) {
        this.fileName = fileName2;
    }

    public void setAudioBitrate(int audioBitrate2) {
        this.audioBitrate = audioBitrate2;
    }

    public void setAudioSamplingRate(int audioSamplingRate2) {
        this.audioSamplingRate = audioSamplingRate2;
    }

    public void isAudioEnabled(boolean isAudioEnable) {
        this.isAudioEnabled = isAudioEnable;
    }

    public void setAudioSource(String audioSource2) {
        this.audioSource = audioSource2;
    }

    public void recordHDVideo(boolean isHdEnabled) {
        this.isVideoHDEnabled = isHdEnabled;
    }

    public void setVideoEncoder(String videoEncoder2) {
        this.videoEncoder = videoEncoder2;
    }

    public void enableCustomSettings() {
        this.enableCustomSettings = true;
    }

    public void setVideoFrameRate(int videoFrameRate2) {
        this.videoFrameRate = videoFrameRate2;
    }

    public void setVideoBitrate(int videoBitrate2) {
        this.videoBitrate = videoBitrate2;
    }

    public void setOutputFormat(String outputFormat2) {
        this.outputFormat = outputFormat2;
    }

    private void setScreenDensity() {
        this.mScreenDensity = Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public int getDefaultWidth() {
        HBRecorderCodecInfo hBRecorderCodecInfo = new HBRecorderCodecInfo();
        hBRecorderCodecInfo.setContext(this.context);
        return hBRecorderCodecInfo.getMaxSupportedWidth();
    }

    public int getDefaultHeight() {
        HBRecorderCodecInfo hBRecorderCodecInfo = new HBRecorderCodecInfo();
        hBRecorderCodecInfo.setContext(this.context);
        return hBRecorderCodecInfo.getMaxSupportedHeight();
    }

    public void setScreenDimensions(int height, int width) {
        this.mScreenHeight = height;
        this.mScreenWidth = width;
    }

    public String getFilePath() {
        return ScreenRecordService.getFilePath();
    }

    public String getFileName() {
        return ScreenRecordService.getFileName();
    }

    public void startScreenRecording(Intent intent, int code) {
        this.resultCode = code;
        startService(intent);
    }

    public void stopScreenRecording() {
        this.context.stopService(new Intent(this.context, ScreenRecordService.class));
    }

    public void pauseScreenRecording() {
        Intent intent = this.service;
        if (intent != null) {
            this.isPaused = true;
            intent.setAction("pause");
            this.context.startService(this.service);
        }
    }

    public void resumeScreenRecording() {
        Intent intent = this.service;
        if (intent != null) {
            this.isPaused = false;
            intent.setAction("resume");
            this.context.startService(this.service);
        }
    }

    public boolean isRecordingPaused() {
        return this.isPaused;
    }

    public boolean isBusyRecording() {
        ActivityManager activityManager = (ActivityManager) this.context.getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (ScreenRecordService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void takeScreenshot(Intent intent, int i) {
        Toast.makeText(this.context, "takeScreenshot()-HBRecorder", 1).show();
    }

    public void setNotificationSmallIcon(int i) {
        Bitmap decodeResource = BitmapFactory.decodeResource(this.context.getResources(), i);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeResource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        this.byteArray = byteArrayOutputStream.toByteArray();
    }

    public void setNotificationSmallIcon(byte[] bArr) {
        this.byteArray = bArr;
    }

    public void setNotificationTitle(String notificationTitle2) {
        this.notificationTitle = notificationTitle2;
    }

    public void setNotificationDescription(String notificationDescription2) {
        this.notificationDescription = notificationDescription2;
    }

    public void setNotificationButtonText(String notificationButtonText2) {
        this.notificationButtonText = notificationButtonText2;
    }

    private void startService(Intent intent) {
        try {
//            if (!this.mWasUriSet) {
//                if (this.outputPath != null) {
//                    this.observer = new FileObserver(new File(this.outputPath).getParent(), this);
//                } else {
//                    this.observer = new FileObserver(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)), this);
//                }
//                this.observer.startWatching();
//            }
            Intent intent2 = new Intent(this.context, ScreenRecordService.class);
            this.service = intent2;
            if (this.mWasUriSet) {
                intent2.putExtra("mUri", this.mUri.toString());
            }
            this.service.putExtra("code", this.resultCode);
            this.service.putExtra("data", intent);
            this.service.putExtra("audio", this.isAudioEnabled);
            this.service.putExtra("width", this.mScreenWidth);
            this.service.putExtra("height", this.mScreenHeight);
            this.service.putExtra("density", this.mScreenDensity);
            this.service.putExtra("quality", this.isVideoHDEnabled);
            this.service.putExtra("path", this.outputPath);
            this.service.putExtra("fileName", this.fileName);
            this.service.putExtra("orientation", this.orientation);
            this.service.putExtra("audioBitrate", this.audioBitrate);
            this.service.putExtra("audioSamplingRate", this.audioSamplingRate);
            this.service.putExtra("notificationSmallBitmap", this.byteArray);
            this.service.putExtra("notificationTitle", this.notificationTitle);
            this.service.putExtra("notificationDescription", this.notificationDescription);
            this.service.putExtra("notificationButtonText", this.notificationButtonText);
            this.service.putExtra("enableCustomSettings", this.enableCustomSettings);
            this.service.putExtra("audioSource", this.audioSource);
            this.service.putExtra("videoEncoder", this.videoEncoder);
            this.service.putExtra("videoFrameRate", this.videoFrameRate);
            this.service.putExtra("videoBitrate", this.videoBitrate);
            this.service.putExtra("outputFormat", this.outputFormat);
            this.service.putExtra("listener", new ResultReceiver(new Handler()) {
                public void onReceiveResult(int resultCode, Bundle bundle) {
                    super.onReceiveResult(resultCode, bundle);
                    if (resultCode == -1) {
                        String errorReason = bundle.getString("errorReason");
                        String complete = bundle.getString("onComplete");
                        String start = bundle.getString("onStart");
                        if (errorReason != null) {
                            if (!HBRecorder.this.mWasUriSet) {
//                                HBRecorder.this.observer.stopWatching();
                            }
                            HBRecorder.this.wasOnErrorCalled = true;
                            HBRecorder.this.hbRecorderListener.HBRecorderOnError(100, errorReason);
                            try {
                                HBRecorder.this.context.stopService(new Intent(HBRecorder.this.context, ScreenRecordService.class));
                            } catch (Exception e) {
                            }
                        } else if (complete != null) {
                            if (!HBRecorder.this.wasOnErrorCalled) {
                                HBRecorder.this.hbRecorderListener.HBRecorderOnComplete();
                            }
                            HBRecorder.this.wasOnErrorCalled = false;
                        } else if (start != null) {
                            HBRecorder.this.hbRecorderListener.HBRecorderOnStart();
                        }
                    }
                }
            });
            this.context.startService(this.service);
        } catch (Exception e) {
            this.hbRecorderListener.HBRecorderOnError(0, Log.getStackTraceString(e));
        }
    }

    public void callback() {
//        this.observer.stopWatching();
        this.hbRecorderListener.HBRecorderOnComplete();
    }
}
