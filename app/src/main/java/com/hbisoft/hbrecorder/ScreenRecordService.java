package com.hbisoft.hbrecorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.util.Log;


import com.ds.audio.video.screen.backgroundrecorder.R;

import java.io.IOException;
import java.util.Objects;

public class ScreenRecordService extends Service {
    public static final String BUNDLED_LISTENER = "listener";
    private static final String TAG = "ScreenRecordService";
    private static String fileName;
    private static String filePath;
    private boolean alreadyStarted = false;
    private int audioBitrate;
    private int audioSamplingRate;
    private int audioSourceAsInt;
    private boolean isAudioEnabled;
    private boolean isCustomSettingsEnabled;
    private boolean isVideoHD;
    private Intent mIntent;
    private MediaProjection mMediaProjection;
    private MediaRecorder mMediaRecorder;
    private int mResultCode;
    private Intent mResultData;
    private int mScreenDensity;
    private int mScreenHeight;
    private int mScreenWidth;
    private VirtualDisplay mVirtualDisplay;
    private String name;
    private int orientationHint;
    private int outputFormatAsInt;
    private String path;
    private Uri returnedUri = null;
    private boolean showCameraOverlay;
    private int videoBitrate;
    private int videoEncoderAsInt;
    private int videoFrameRate;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String notificationTitle;
        Notification notification;
        Intent intent2 = intent;
        String pauseResumeAction = intent.getAction();
        if (pauseResumeAction == null || !pauseResumeAction.equals("pause")) {
            if (pauseResumeAction == null || !pauseResumeAction.equals("resume")) {
                this.mIntent = intent2;
                byte[] notificationSmallIcon = intent2.getByteArrayExtra("notificationSmallBitmap");
                String notificationTitle2 = intent2.getStringExtra("notificationTitle");
                String notificationDescription = intent2.getStringExtra("notificationDescription");
                String notificationButtonText = intent2.getStringExtra("notificationButtonText");
                this.orientationHint = intent2.getIntExtra("orientation", 400);
                this.mResultCode = intent2.getIntExtra("code", -1);
                this.mResultData = (Intent) intent2.getParcelableExtra("data");
                this.mScreenWidth = intent2.getIntExtra("width", 0);
                this.mScreenHeight = intent2.getIntExtra("height", 0);
                if (intent2.getStringExtra("mUri") != null) {
                    this.returnedUri = Uri.parse(intent2.getStringExtra("mUri"));
                }
                if (this.mScreenHeight == 0 || this.mScreenWidth == 0) {
                    HBRecorderCodecInfo hBRecorderCodecInfo = new HBRecorderCodecInfo();
                    hBRecorderCodecInfo.setContext(this);
                    this.mScreenHeight = hBRecorderCodecInfo.getMaxSupportedHeight();
                    this.mScreenWidth = hBRecorderCodecInfo.getMaxSupportedWidth();
                }
                this.mScreenDensity = intent2.getIntExtra("density", 1);
                this.isVideoHD = intent2.getBooleanExtra("quality", true);
                this.isAudioEnabled = intent2.getBooleanExtra("audio", true);
                this.path = intent2.getStringExtra("path");
                this.name = intent2.getStringExtra("fileName");
                String audioSource = intent2.getStringExtra("audioSource");
                String videoEncoder = intent2.getStringExtra("videoEncoder");
                this.videoFrameRate = intent2.getIntExtra("videoFrameRate", 30);
                this.videoBitrate = intent2.getIntExtra("videoBitrate", 40000000);
                if (audioSource != null) {
                    setAudioSourceAsInt(audioSource);
                }
                if (videoEncoder != null) {
                    setvideoEncoderAsInt(videoEncoder);
                }
                filePath = this.name;
                this.audioBitrate = intent2.getIntExtra("audioBitrate", 128000);
                this.audioSamplingRate = intent2.getIntExtra("audioSamplingRate", 44100);
                String outputFormat = intent2.getStringExtra("outputFormat");
                if (outputFormat != null) {
                    setOutputFormatAsInt(outputFormat);
                }
                this.isCustomSettingsEnabled = intent2.getBooleanExtra("enableCustomSettings", false);
                if (notificationButtonText == null) {
                    notificationButtonText = "STOP RECORDING";
                }
                if (this.audioBitrate == 0) {
                    this.audioBitrate = 128000;
                }
                if (this.audioSamplingRate == 0) {
                    this.audioSamplingRate = 44100;
                }
                if (notificationTitle2 == null || notificationTitle2.equals("")) {
                    notificationTitle = "Recording your screen";
                } else {
                    notificationTitle = notificationTitle2;
                }
                if (notificationDescription == null || notificationDescription.equals("")) {
                    notificationDescription = "Drag down to stop the recording";
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel notificationChannel = new NotificationChannel("001", "RecordChannel", 0);
                    notificationChannel.setLightColor(-16776961);
                    notificationChannel.setLockscreenVisibility(0);
                    NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel);
                        String str = pauseResumeAction;
                        Object obj = "RecordChannel";
                        String str2 = audioSource;
                        NotificationManager notificationManager2 = notificationManager;
                        String str3 = videoEncoder;
                        Notification.Action build = new Notification.Action.Builder(Icon.createWithResource(this, 17301678), notificationButtonText, PendingIntent.getBroadcast(this, 0, new Intent(this, NotificationReceiver.class), 67108864)).build();
                        if (notificationSmallIcon != null) {
                            notification = new Notification.Builder(getApplicationContext(), "001").setOngoing(true).setSmallIcon(Icon.createWithBitmap(BitmapFactory.decodeByteArray(notificationSmallIcon, 0, notificationSmallIcon.length))).setContentTitle(notificationTitle).setContentText(notificationDescription).addAction(build).build();
                        } else {
                            notification = new Notification.Builder(getApplicationContext(), "001").setOngoing(true).setSmallIcon(R.drawable.app_logo).setContentTitle(notificationTitle).setContentText(notificationDescription).addAction(build).build();
                        }
                        startForeground(102, notification);
                    } else {
                        Object obj2 = "RecordChannel";
                        String str4 = audioSource;
                        NotificationManager notificationManager3 = notificationManager;
                        String str5 = videoEncoder;
                    }
                } else {
                    String str6 = audioSource;
                    String str7 = videoEncoder;
                    startForeground(102, new Notification());
                }
                if (this.returnedUri == null && this.path == null) {
                    this.path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                }
                try {
                    initRecorder();
                } catch (Exception e) {
                    Exception e2 = e;
                    e2.printStackTrace();
                    Log.e("EEEEEEE ", e2.getMessage());
                    ResultReceiver resultReceiver = (ResultReceiver) intent2.getParcelableExtra("listener");
                    Bundle bundle = new Bundle();
                    bundle.putString("errorReason", Log.getStackTraceString(e2));
                    if (resultReceiver != null) {
                        resultReceiver.send(-1, bundle);
                    }
                }
                try {
                    initMediaProjection();
                } catch (Exception e3) {
                    ResultReceiver resultReceiver2 = (ResultReceiver) intent2.getParcelableExtra("listener");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("errorReason", Log.getStackTraceString(e3));
                    if (resultReceiver2 != null) {
                        resultReceiver2.send(-1, bundle2);
                    }
                }
                try {
                    initVirtualDisplay();
                } catch (Exception e4) {
                    ResultReceiver resultReceiver3 = (ResultReceiver) intent2.getParcelableExtra("listener");
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("errorReason", Log.getStackTraceString(e4));
                    if (resultReceiver3 != null) {
                        resultReceiver3.send(-1, bundle3);
                    }
                }
//                this.mMediaRecorder.setOnErrorListener(new ScreenRecordService$$ExternalSyntheticLambda0(intent2));

                this.mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                    @Override
                    public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                        ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("listener");
                        Bundle bundle = new Bundle();
                        bundle.putString("error", "38");
                        bundle.putString("errorReason", String.valueOf(i));
                        if (resultReceiver != null) {
                            resultReceiver.send(-1, bundle);
                        }
                    }
                });
                try {
                    this.mMediaRecorder.start();
                    ResultReceiver resultReceiver4 = (ResultReceiver) intent2.getParcelableExtra("listener");
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("onStart", "111");
                    if (resultReceiver4 == null) {
                        return 1;
                    }
                    resultReceiver4.send(-1, bundle4);
                    return 1;
                } catch (Exception e42) {
                    ResultReceiver resultReceiver5 = (ResultReceiver) intent2.getParcelableExtra("listener");
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("error", "38");
                    bundle5.putString("errorReason", Log.getStackTraceString(e42));
                    if (resultReceiver5 == null) {
                        return 1;
                    }
                    resultReceiver5.send(-1, bundle5);
                    return 1;
                }
            } else if (Build.VERSION.SDK_INT >= 24) {
                resumeRecording();
                String str8 = pauseResumeAction;
                return 1;
            } else {
                String str9 = pauseResumeAction;
                return 1;
            }
        } else if (Build.VERSION.SDK_INT >= 24) {
            pauseRecording();
            String str10 = pauseResumeAction;
            return 1;
        } else {
            String str11 = pauseResumeAction;
            return 1;
        }
    }

    private void pauseRecording() {
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder != null) {
            mediaRecorder.pause();
        }
    }

    private void resumeRecording() {
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder != null) {
            mediaRecorder.resume();
        }
    }


    private void setOutputFormatAsInt(String outputFormat) {
        switch (outputFormat) {
            case "DEFAULT":
                outputFormatAsInt = 0;
                break;
            case "THREE_GPP":
                outputFormatAsInt = 1;
                break;
            case "AMR_NB":
                outputFormatAsInt = 3;
                break;
            case "AMR_WB":
                outputFormatAsInt = 4;
                break;
            case "AAC_ADTS":
                outputFormatAsInt = 6;
                break;
            case "MPEG_2_TS":
                outputFormatAsInt = 8;
                break;
            case "WEBM":
                outputFormatAsInt = 9;
                break;
            case "OGG":
                outputFormatAsInt = 11;
                break;
            case "MPEG_4":
            default:
                outputFormatAsInt = 2;
        }
    }

    //Set video encoder as int based on what developer has provided
    //It is important to provide one of the following and nothing else.
    private void setvideoEncoderAsInt(String encoder) {
        switch (encoder) {
            case "DEFAULT":
                videoEncoderAsInt = 0;
                break;
            case "H263":
                videoEncoderAsInt = 1;
                break;
            case "H264":
                videoEncoderAsInt = 2;
                break;
            case "MPEG_4_SP":
                videoEncoderAsInt = 3;
                break;
            case "VP8":
                videoEncoderAsInt = 4;
                break;
            case "HEVC":
                videoEncoderAsInt = 5;
                break;
        }
    }

    //Set audio source as int based on what developer has provided
    //It is important to provide one of the following and nothing else.
    private void setAudioSourceAsInt(String audioSource) {
        switch (audioSource) {
            case "DEFAULT":
                audioSourceAsInt = 0;
                break;
            case "MIC":
                audioSourceAsInt = 1;
                break;
            case "VOICE_UPLINK":
                audioSourceAsInt = 2;
                break;
            case "VOICE_DOWNLINK":
                audioSourceAsInt = 3;
                break;
            case "VOICE_CALL":
                audioSourceAsInt = 4;
                break;
            case "CAMCODER":
                audioSourceAsInt = 5;
                break;
            case "VOICE_RECOGNITION":
                audioSourceAsInt = 6;
                break;
            case "VOICE_COMMUNICATION":
                audioSourceAsInt = 7;
                break;
            case "REMOTE_SUBMIX":
                audioSourceAsInt = 8;
                break;
            case "UNPROCESSED":
                audioSourceAsInt = 9;
                break;
            case "VOICE_PERFORMANCE":
                audioSourceAsInt = 10;
                break;
        }
    }

    private void initMediaProjection() {
        Object systemService = getSystemService("media_projection");
        Objects.requireNonNull(systemService);
        this.mMediaProjection = ((MediaProjectionManager) systemService).getMediaProjection(this.mResultCode, this.mResultData);
    }

    public static String getFilePath() {
        return filePath;
    }

    public static String getFileName() {
        return fileName;
    }

    private long getUnixTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private void initRecorder() throws Exception {
        String videoQuality = !this.isVideoHD ? "SD-" : "HD-";
        if (this.name == null) {
            this.name = videoQuality + getUnixTimeStamp();
        }
        filePath = this.path + "/" + this.name + ".mp4";
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(".mp4");
        fileName = sb.toString();
        this.mMediaRecorder = new MediaRecorder();
        if (this.isAudioEnabled) {
            Log.e("audioSourceAsInt ", String.valueOf(this.audioSourceAsInt));
            this.mMediaRecorder.setAudioSource(this.audioSourceAsInt);
            this.mMediaRecorder.setAudioChannels(1);
        }
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(this.outputFormatAsInt);
        int i = this.orientationHint;
        if (i != 400) {
            this.mMediaRecorder.setOrientationHint(i);
        }
        if (this.isAudioEnabled) {
            this.mMediaRecorder.setAudioEncoder(3);
            this.mMediaRecorder.setAudioEncodingBitRate(256000);
            this.mMediaRecorder.setAudioSamplingRate(88200);
        }
        this.mMediaRecorder.setVideoEncoder(2);
        if (this.returnedUri != null) {
            try {
                ParcelFileDescriptor openFileDescriptor = getContentResolver().openFileDescriptor(this.returnedUri, "rw");
                Objects.requireNonNull(openFileDescriptor);
                this.mMediaRecorder.setOutputFile(openFileDescriptor.getFileDescriptor());
            } catch (Exception e) {
                ResultReceiver resultReceiver = (ResultReceiver) this.mIntent.getParcelableExtra("listener");
                Bundle bundle = new Bundle();
                bundle.putString("errorReason", Log.getStackTraceString(e));
                if (resultReceiver != null) {
                    resultReceiver.send(-1, bundle);
                }
            }
        } else {
            this.mMediaRecorder.setOutputFile(filePath);
        }
        this.mMediaRecorder.setVideoSize(this.mScreenWidth, this.mScreenHeight);
        if (this.isCustomSettingsEnabled) {
            this.mMediaRecorder.setVideoEncodingBitRate(this.videoBitrate);
            this.mMediaRecorder.setVideoFrameRate(this.videoFrameRate);
        } else if (!this.isVideoHD) {
            this.mMediaRecorder.setVideoEncodingBitRate(12000000);
            this.mMediaRecorder.setVideoFrameRate(30);
        } else {
            this.mMediaRecorder.setVideoEncodingBitRate(this.mScreenWidth * 5 * this.mScreenHeight);
            this.mMediaRecorder.setVideoFrameRate(60);
        }
        try {
            this.mMediaRecorder.prepare();
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.d("Audio", "unable to prepare");
        }
    }

    private void initVirtualDisplay() {
        this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay(TAG, this.mScreenWidth, this.mScreenHeight, this.mScreenDensity, 16, this.mMediaRecorder.getSurface(), (VirtualDisplay.Callback) null, (Handler) null);
    }

    public void onDestroy() {
        super.onDestroy();
        resetAll();
        callOnComplete();
    }

    private void callOnComplete() {
        ResultReceiver resultReceiver = (ResultReceiver) this.mIntent.getParcelableExtra("listener");
        Bundle bundle = new Bundle();
        bundle.putString("onComplete", "Uri was passed");
        if (resultReceiver != null) {
            resultReceiver.send(-1, bundle);
        }
    }

    private void resetAll() {
        stopForeground(true);
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            virtualDisplay.release();
            this.mVirtualDisplay = null;
        }
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener((MediaRecorder.OnErrorListener) null);
            this.mMediaRecorder.reset();
        }
        MediaProjection mediaProjection = this.mMediaProjection;
        if (mediaProjection != null) {
            mediaProjection.stop();
            this.mMediaProjection = null;
        }
    }
}
