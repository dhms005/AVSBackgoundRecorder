package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_ActivityPager;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_ScheduleListActivity;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_SharedPreHelper;
import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver.Audio_AlarmReceiver;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.UserModel;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.Video_Database_Helper;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordRepository;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Audio_Recorder_Service extends Service implements SurfaceHolder.Callback {
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String ACTION_STOP_SERVICE = "stop_service";
    private static File outputmp4;
    private int NOTIFCATION_ID = 3;
    private String audioSource;
    private boolean checkNotify;
    private boolean chkFreeStore;
    private boolean chkVibration;
    private MediaRecorder mediaRecorder = null;
    private String recordDuration;
    private Audio_SharedPreHelper sharedPreHelper;
    private SurfaceView surfaceView;
    private WindowManager windowManager;
    private WordRepository mRepository;



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
        this.sharedPreHelper = new Audio_SharedPreHelper(this);
        int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        this.windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.surfaceView = new SurfaceView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(1, 1, -2, -2, i, 280, -1);
        layoutParams.gravity = 51;
        this.windowManager.addView(this.surfaceView, layoutParams);
        this.surfaceView.getHolder().addCallback(this);
    }

    private void startMyOwnForeground() {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(BuildConfig.APPLICATION_ID, "Secret Camera", NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setLockscreenVisibility(0);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        }

        startForeground(this.NOTIFCATION_ID, new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID).setOngoing(true).setSmallIcon(R.drawable.app_logo).setContentTitle("App is running in background").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        NotificationCompat.Builder builder;
        super.onStartCommand(intent, i, i2);



//        mRepository.deleteTimer(Double.parseDouble(SharePrefUtils.getString(CY_M_Conts.AUDIO_CURRENT_TIME, "")));


        if (Build.VERSION.SDK_INT >= 23 && intent.getStringExtra(DevSpy_Conts.CAMERA_USE) != null) {
//            this.useCam = intent.getStringExtra(CY_M_Conts.CAMERA_USE);
//            this.recordDuration = intent.getStringExtra(CY_M_Conts.CAMERA_DURATION);
        }
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
            return START_STICKY;
//            checkNotify = false;
        } else {
//            checkNotify = true;

            if (SharePrefUtils.getString(DevSpy_Conts.AUDIO_FROM_SCHEDULE, "0").equals("1")){
                if (!SharePrefUtils.getString(DevSpy_Conts.AUDIO_CURRENT_TIME, "").equals("")) {

                    Video_Database_Helper.Audio_deleteEntry(SharePrefUtils.getString(DevSpy_Conts.AUDIO_CURRENT_TIME, ""));
                    if (Audio_ScheduleListActivity.adapter != null){
                        Audio_ScheduleListActivity.adapter.notifyDataSetChanged();
                    }
                    ArrayList<UserModel> users = Video_Database_Helper.Audio_getRows();

                    new Handler().postDelayed(() -> {
                        //TODO Perform your action here
                        if (users.size() > 0) {
//                    Log.e("#TESTSCHEDULE", "2-->   " + Video_Save_Schedule_Activity.schedeluLisst.get(0).getTime());
                            Intent intent1 = new Intent(this, Audio_AlarmReceiver.class);
//            intent1.putExtra(CY_M_Conts.CAMERA_USE, Video_Save_Schedule_Activity.schedeluLisst.get(0).getCamera());
//          intent1.putExtra(CY_M_Conts.CAMERA_DURATION, String.valueOf(Video_Save_Schedule_Activity.schedeluLisst.get(0).getDuration() * 60));
                            SharePrefUtils.putString(DevSpy_Conts.AUDIO_CURRENT_TIME, users.get(0).getV_time());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(AlarmManager.RTC_WAKEUP, Long.parseLong(users.get(0).getV_time()), PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
                            } else {
                                ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(AlarmManager.RTC_WAKEUP, Long.parseLong(users.get(0).getV_time()), PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT));
                            }
                        }else {
                            SharePrefUtils.putString(DevSpy_Conts.AUDIO_CURRENT_TIME, "");
                        }

                    }, 3000);
                }
            }
        }


        if (this.checkNotify) {
            if (chkVibration) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }

            }


            if (Build.VERSION.SDK_INT >= 26) {
                builder = new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID);
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(new NotificationChannel(BuildConfig.APPLICATION_ID, "Secret Camera", NotificationManager.IMPORTANCE_DEFAULT));
            } else {
                builder = new NotificationCompat.Builder(this);
            }
            builder.setContentTitle(getString(R.string.audio_running));
            builder.setContentText(getString(R.string.tap_to_open));
            builder.setPriority(1);
            builder.setSmallIcon(R.drawable.app_logo);
            builder.setWhen(0);
            builder.setPriority(2);
            Intent intent2 = new Intent(getApplicationContext(), Audio_ActivityPager.class);
            TaskStackBuilder create = TaskStackBuilder.create(getApplicationContext());
            create.addNextIntent(intent2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.setContentIntent(create.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
            } else {
                builder.setContentIntent(create.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
            }

            Intent intent3 = new Intent(this, Audio_Recorder_Service.class);
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
        DevSpy_Conts.mRecordingStarted_Other = false;
        DevSpy_Conts.isTimerRunning_Audio = false;
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 23) {
            intent.setAction(DevSpy_Conts.ACTION_STOP_AUDIO_EXTRA);
        }
        instance.sendBroadcast(intent);
        releaseMediaRecorder();
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
                    intent.setAction(DevSpy_Conts.ACTION_START_AUDIO_SERVICE);
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
    }


    private boolean prepareVideoRecorder(SurfaceHolder surfaceHolder) {

        this.mediaRecorder = new MediaRecorder();

//

        // below method is used to set the audio
        // source which we are using a mic.

        if (audioSource.equals("0")) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        } else if (audioSource.equals("1")) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        } else if (audioSource.equals("2")) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        } else {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }


        // below method is used to set
        // the output format of the audio.
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // below method is used to set the
        // audio encoder for our recorded audio.
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        outputmp4 = Audio_FileHelper.getOutputMediaFile(2, Audio_Recorder_Service.this);
        File file = outputmp4;
        if (file == null) {
            return false;
        }
        this.mediaRecorder.setOutputFile(file.getPath());
        this.mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {


            public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
                if (i == 800 || i == 801) {
                    Audio_Recorder_Service.this.stopSelf();
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

        }
    }


    private void readData(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.checkNotify = defaultSharedPreferences.getBoolean("pref_Aud_ShowNotifi", true);
        this.audioSource = defaultSharedPreferences.getString("pref_Aud_AudioSource", "1");
        this.chkFreeStore = defaultSharedPreferences.getBoolean("prefChkFreeSto", true);
        this.recordDuration = defaultSharedPreferences.getString("pref_Aud_Duration", "300");
        this.chkVibration = defaultSharedPreferences.getBoolean("prefVibration", false);
    }
}
