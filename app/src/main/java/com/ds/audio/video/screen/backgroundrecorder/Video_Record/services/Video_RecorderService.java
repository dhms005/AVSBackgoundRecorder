package com.ds.audio.video.screen.backgroundrecorder.Video_Record.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_ActivityPager;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_CameraHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_SharedPreHelper;
import com.ds.audio.video.screen.backgroundrecorder.R;


import java.io.File;
import java.io.IOException;

public class Video_RecorderService extends Service implements SurfaceHolder.Callback {
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String ACTION_STOP_SERVICE = "stop_service";
    private static File outputmp4;
    private int NOTIFCATION_ID = 1;
    private String audioSource;
    private Camera camera = null;
    private boolean checkNotify;
    private boolean chkFreeStore;
    private boolean chkSlientRecord;
    private boolean chkFlashLight;
    private boolean chkVibration;
    private MediaRecorder mediaRecorder = null;
    private String orientation;
    private String recordDuration;
    private Video_SharedPreHelper sharedPreHelper;
    private SurfaceView surfaceView;
    private String useCam;
    private String videoQuality;
    private WindowManager windowManager;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void onCreate() {
        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        } else {
            startForeground(this.NOTIFCATION_ID, new Notification());
        }
        readData(this);
        this.sharedPreHelper = new Video_SharedPreHelper(this);
        int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        this.windowManager = (WindowManager) getSystemService("window");
        this.surfaceView = new SurfaceView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(1, 1, -2, -2, i, 280, -1);
        layoutParams.gravity = 51;
        this.windowManager.addView(this.surfaceView, layoutParams);
        this.surfaceView.getHolder().addCallback(this);
    }

    private void startMyOwnForeground() {
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(BuildConfig.APPLICATION_ID, "Secret Camera", 0);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setLockscreenVisibility(0);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
        }

        startForeground(this.NOTIFCATION_ID, new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID).setOngoing(true).setSmallIcon(R.drawable.app_logo).setContentTitle("App is running in background").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        NotificationCompat.Builder builder;
        super.onStartCommand(intent, i, i2);

//        this.recordDuration = intent.getStringExtra(CY_M_Conts.CAMERA_DURATION);
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
            return START_STICKY;
//            checkNotify = false;
        } else {
//            checkNotify = true;
        }
        if (this.checkNotify) {


            if (chkVibration) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(500);
                }
            }


            if (Build.VERSION.SDK_INT >= 26) {
                builder = new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID);
                ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel(BuildConfig.APPLICATION_ID, "Secret Camera", 3));
            } else {
                builder = new NotificationCompat.Builder(this);
            }
            builder.setContentTitle(getString(R.string.camera_running));
            builder.setContentText(getString(R.string.tap_to_open));
            builder.setPriority(1);
            builder.setSmallIcon(R.drawable.notify_icon);
            builder.setWhen(0);
            builder.setPriority(2);
            Intent intent2 = new Intent(getApplicationContext(), Video_ActivityPager.class);
            TaskStackBuilder create = TaskStackBuilder.create(getApplicationContext());
            create.addNextIntent(intent2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.setContentIntent(create.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
            } else {
                builder.setContentIntent(create.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
            }

            Intent intent3 = new Intent(this, Video_RecorderService.class);
            intent3.setAction(ACTION_STOP_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.addAction(R.drawable.notify_stop, getString(R.string.stop), PendingIntent.getService(this, 1, intent3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
            } else {
                builder.addAction(R.drawable.notify_stop, getString(R.string.stop), PendingIntent.getService(this, 1, intent3, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(this.NOTIFCATION_ID, builder.build());

            startForeground(this.NOTIFCATION_ID, builder.build());
        }
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        this.sharedPreHelper.remove();
        stopForeground(true);
        CY_M_Conts.mRecordingStarted_Other = false;
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 23) {
            intent.setAction(CY_M_Conts.ACTION_STOP_VIDEO_EXTRA);
        }
        instance.sendBroadcast(intent);
        releaseMediaRecorder();
        releaseCamera();
        if (outputmp4 != null) {
            Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent2.setData(Uri.fromFile(outputmp4));
            sendBroadcast(intent2);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (prepareVideoRecorder(surfaceHolder)) {
                this.mediaRecorder.start();
                this.sharedPreHelper.saveTimeToPre();
                LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 23) {
                    intent.setAction(CY_M_Conts.ACTION_START_VIDEO_SERVICE);
                }
                instance.sendBroadcast(intent);
                return;
            }
            stopSelf();
        } catch (Exception unused) {
            stopSelf();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.camera.stopPreview();
        this.camera.release();
        this.camera = null;
    }


    private boolean prepareVideoRecorder(SurfaceHolder surfaceHolder) {
        this.camera = Video_MediaRecorderFactory.setVideoCamera(this.useCam);
        CamcorderProfile camcorderProfile = Video_MediaRecorderFactory.setCamcorderProfile(this.videoQuality);
        Camera.Parameters parameters = this.camera.getParameters();
        Camera.Size optimalVideoSize = Video_CameraHelper.getOptimalVideoSize(parameters.getSupportedVideoSizes(), parameters.getSupportedPreviewSizes(), camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        camcorderProfile.videoFrameWidth = optimalVideoSize.width;
        camcorderProfile.videoFrameHeight = optimalVideoSize.height;
        if (chkFlashLight) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }

        parameters.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        this.camera.setParameters(parameters);
        this.mediaRecorder = new MediaRecorder();
        this.camera.unlock();
        this.mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        this.mediaRecorder.setCamera(this.camera);
//        camera = Camera.open(0);
//        cameraPreview = new Pul_CameraPreview(this, camera,surfaceHolder);
        this.mediaRecorder = Video_MediaRecorderFactory.setOrientation(this.mediaRecorder, this.orientation, this.useCam);
        this.mediaRecorder = Video_MediaRecorderFactory.setSource(this.mediaRecorder, this.chkSlientRecord, this.audioSource, camcorderProfile);
        outputmp4 = Video_FileHelper.getOutputMediaFile(2, Video_RecorderService.this);
        File file = outputmp4;
        if (file == null) {
            return false;
        }
        this.mediaRecorder.setOutputFile(file.getPath());
        this.mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {


            public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
                if (i == 800 || i == 801) {
                    Video_RecorderService.this.stopSelf();
                }
            }
        });
        int parseInt = Integer.parseInt(this.recordDuration);
        if (parseInt < 100000) {
            this.mediaRecorder.setMaxDuration(parseInt * 10000);
        }
        if (this.chkFreeStore) {
            this.mediaRecorder.setMaxFileSize((Video_FileHelper.getAvailableExternalMemory() - 10) * 100000000);
        }
        try {
            this.mediaRecorder.prepare();
            return true;
        } catch (IllegalStateException unused) {
            releaseMediaRecorder();
            return false;
        } catch (IOException unused2) {
            releaseMediaRecorder();
            return false;
        }
    }

    private void releaseMediaRecorder() {
        MediaRecorder mediaRecorder2 = this.mediaRecorder;
        if (mediaRecorder2 != null) {
            mediaRecorder2.reset();
            this.mediaRecorder.release();
            this.mediaRecorder = null;
            this.camera.lock();
//            CY_M_CCTV_RecorderFragment.cPreview.setVisibility(View.GONE);
        }
    }

    private void releaseCamera() {
        Camera camera2 = this.camera;
        if (camera2 != null) {
            camera2.release();
            this.camera = null;
//            CY_M_CCTV_RecorderFragment.cPreview.setVisibility(View.GONE);
        }
    }

    private void readData(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.checkNotify = defaultSharedPreferences.getBoolean("prefShowNotifi", true);
        this.useCam = defaultSharedPreferences.getString("prefVideoCamera", "0");
        this.videoQuality = defaultSharedPreferences.getString("prefVideoQuality", "0");
        this.audioSource = defaultSharedPreferences.getString("prefAudioSource", "1");
        this.chkFreeStore = defaultSharedPreferences.getBoolean("prefChkFreeSto", true);
        this.chkSlientRecord = defaultSharedPreferences.getBoolean("prefChkSlientRecord", false);
        this.orientation = defaultSharedPreferences.getString("prefOrientation", "1");
        this.recordDuration = defaultSharedPreferences.getString("prefDuration", "300");

        this.chkFlashLight = defaultSharedPreferences.getBoolean("prefFlashLight", false);
        this.chkVibration = defaultSharedPreferences.getBoolean("prefVibration", false);
    }
}
