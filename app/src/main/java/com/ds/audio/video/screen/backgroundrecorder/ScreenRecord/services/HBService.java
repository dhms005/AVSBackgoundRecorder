package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.services.Audio_Recorder_Service;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Activities.Video_Record_Background_Activity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.NotificationHelper;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.ScreenRecorder_SharedPreHelper;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.ScreenShot_Video_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.managers.SharedPreferencesManager;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.hbisoft.hbrecorder.Const;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class HBService extends Service implements HBRecorderListener {
    private static int IMAGES_PRODUCED;
    ContentValues contentValues;
    public HBRecorder hbRecorder;
    private Boolean isMenuVisible = false;
    boolean isScreenshotTaken = false;
    private int mCameraHeight = 120;
    private int mCameraWidth = 160;
    public View mCountdownLayout;
    private ImageView mImgClose;
    private ImageView mImgPause;
    private ImageView mImgRec;
    private ImageView mImgResume;
    private ImageView mImgScreenshot;
    private ImageView mImgSetting;
    private ImageView mImgStart;
    private ImageView mImgStop;
    private ImageView mImgToolbox;
    private int mMode;
    public Boolean mRecordingPaused = false;
    public Boolean mRecordingStarted = false;
    private Intent mScreenCaptureIntent = null;
    private int mScreenCaptureResultCode;
    private int mScreenHeight;
    public int mScreenWidth;
    public TextView mTvCountdown;
    Uri mUri;
    public View mViewRoot;
    public View mWarermarkLayout;
    public WindowManager mWindowManager;
    WindowManager.LayoutParams paramCountdown;
    WindowManager.LayoutParams paramViewRoot;
    WindowManager.LayoutParams paramWatermark;
    SharedPreferences prefs;
    ContentResolver resolver;
    public Handler ssHandler;
    private ScreenRecorder_SharedPreHelper sharedPreHelper;

    private ConstraintLayout mLayout;
    private NotificationManager mNotificationManager;
    private WindowManager.LayoutParams mParams;
    private WindowManager windowManager;

    TextView tv_video_record;
    TextView tv_audio_record;
    TextView tv_screen_record;
    ImageView imv_close;
    public boolean isTimerRunning = false;

    private LocalBroadcastManager broadcastManagerStart;
    private String mCheckServiesStart;

    public void HBRecorderOnStart() {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return 2;
        }

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.hbRecorder = new HBRecorder(this, this);
        setOutputPath();
        this.mScreenCaptureIntent = (Intent) intent.getParcelableExtra("android.intent.extra.INTENT");
        this.mCheckServiesStart = DevSpy_Conts.ACTION_START_Service_Checker;
        if (this.hbRecorder.isBusyRecording()) {
            Utils.toast(getApplicationContext(), "Recording in progress", 1);
            return 2;
        }
        if (mCheckServiesStart.equals("cctv")) {
            start_video_service();
        } else if (mCheckServiesStart.equals("audio")) {
            start_audio_service();
        } else if (mCheckServiesStart.equals("screenRecord")) {
            start_screenRecorder_service();
        } else if (mCheckServiesStart.equals("screenshot")) {
//            openCaptureControlService();
        } else {

        }

        if (prefs.getBoolean(Const.PREFS_TOOLS_BRUSH, false)) {
            openBrushControlService();
        }
        if (prefs.getBoolean(Const.PREFS_TOOLS_CAPTURE, true)) {
            openCaptureControlService();
        }

//        showCountDown();
        return 2;
    }

    public void onCreate() {
        super.onCreate();

        Utils.createScreenshotFolder();
        Video_FileHelper.CreateFolder(this);
        Audio_FileHelper.CreateFolder(this);
        ScreenShot_Video_FileHelper.CreateFolder(this);

        updateScreenSize();

        this.broadcastManagerStart = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(DevSpy_Conts.ACTION_START_Video_Service);
        intentFilter2.addAction(DevSpy_Conts.ACTION_START_Audio_Service);
        intentFilter2.addAction(DevSpy_Conts.ACTION_START_ScreenRecorder_Service);
        intentFilter2.addAction(DevSpy_Conts.ACTION_START_ScreenShot_Service);
        intentFilter2.addAction(DevSpy_Conts.ACTION_STOP_ScreenShot_Service);
        this.broadcastManagerStart.registerReceiver(this.receiverStart, intentFilter2);

        this.sharedPreHelper = new ScreenRecorder_SharedPreHelper(this);
        if (this.paramViewRoot == null) {
            initParam();
            initServiceNotification();
        }
        if (this.mViewRoot == null) {
            initializeViews();
        }
        new Thread() {
            public void run() {
                Looper.prepare();
                HBService.this.ssHandler = new Handler(Looper.getMainLooper());
                Looper.loop();
            }
        }.start();
    }

    public void onDestroy() {
        super.onDestroy();
        this.broadcastManagerStart.unregisterReceiver(this.receiverStart);
        View view = this.mViewRoot;
        if (view != null) {
            this.mWindowManager.removeViewImmediate(view);
        }
        try {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mLayout);
//            windowManager.removeView(mViewRoot);
            // invalidate the view
//            surfaceView.invalidate();
            mLayout.invalidate();
            // remove all views
//            ((ViewGroup) surfaceView.getParent()).removeAllViews();
            ((ViewGroup) mLayout.getParent()).removeAllViews();
            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.d("CCTVRecorderFragment12", e.toString());
        }
    }


    private void initServiceNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(NotificationHelper.CHANNEL_ID_SERVICE, NotificationHelper.CHANNEL_NAME_SERVICE, 0);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setLockscreenVisibility(0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
                startForeground(101, new Notification.Builder(this, NotificationHelper.CHANNEL_ID_SERVICE).setOngoing(true).setSmallIcon(R.drawable.app_logo).setContentTitle(getString(R.string.running)).setContentText(getString(R.string.recordingNotificationMsg)).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
                return;
            }
            return;
        }
        startForeground(101, new Notification());
    }

    private void initParam() {
        int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        this.paramViewRoot = new WindowManager.LayoutParams(-2, -2, i, 8, -3);
        this.paramCountdown = new WindowManager.LayoutParams(-2, -2, i, 8, -3);
        this.paramWatermark = new WindowManager.LayoutParams(-2, -2, i, 8, -3);
    }

    private void initializeViews() {
        this.mViewRoot = LayoutInflater.from(this).inflate(R.layout.screenrecorder_recording, (ViewGroup) null);
        View countDownView = LayoutInflater.from(this).inflate(R.layout.screenrecorder_countdown, (ViewGroup) null);
        View watermarkView = LayoutInflater.from(this).inflate(R.layout.screenrecorder_watermark, (ViewGroup) null);
        this.paramViewRoot.gravity = 8388627;
        this.paramViewRoot.x = 0;
        this.paramViewRoot.y = 0;
        this.paramWatermark.gravity = BadgeDrawable.BOTTOM_END;
        WindowManager windowManager = (WindowManager) getSystemService("window");
        this.mWindowManager = windowManager;
        windowManager.addView(countDownView, this.paramCountdown);
        this.mWindowManager.addView(this.mViewRoot, this.paramViewRoot);
        this.mWindowManager.addView(watermarkView, this.paramWatermark);
        this.mCountdownLayout = countDownView.findViewById(R.id.countdown_container);
        this.mWarermarkLayout = watermarkView.findViewById(R.id.watermark_container);
        this.mTvCountdown = (TextView) countDownView.findViewById(R.id.tvCountDown);
        toggleView(this.mCountdownLayout, 8);
        toggleView(this.mWarermarkLayout, 8);
        this.mImgRec = (ImageView) this.mViewRoot.findViewById(R.id.imgRec);
        this.mImgToolbox = (ImageView) this.mViewRoot.findViewById(R.id.imgToolbox);
        this.mImgClose = (ImageView) this.mViewRoot.findViewById(R.id.imgClose);
        this.mImgScreenshot = (ImageView) this.mViewRoot.findViewById(R.id.imgScreenshot);
        this.mImgPause = (ImageView) this.mViewRoot.findViewById(R.id.imgPause);
        this.mImgStart = (ImageView) this.mViewRoot.findViewById(R.id.imgStart);
        this.mImgSetting = (ImageView) this.mViewRoot.findViewById(R.id.imgSetting);
        this.mImgStop = (ImageView) this.mViewRoot.findViewById(R.id.imgStop);
        ImageView imageView = (ImageView) this.mViewRoot.findViewById(R.id.imgResume);
        this.mImgResume = imageView;
        toggleView(imageView, 8);
        toggleView(this.mImgStop, 8);
        toggleNavigationButton(8);
        this.mImgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 24) {
                    mRecordingPaused = true;
                    toggleNavigationButton(8);
                    hbRecorder.pauseScreenRecording();
                    Utils.toast(getApplicationContext(), "Paused", 1);
                    return;
                }
                Utils.toast(getApplicationContext(), "This feature is not available in your device", 1);
            }
        });
        this.mImgResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 24) {
                    mRecordingPaused = false;
                    toggleNavigationButton(8);
                    hbRecorder.resumeScreenRecording();
                    Utils.toast(getApplicationContext(), "Resumed", 1);
                    return;
                }
                Utils.toast(getApplicationContext(), "This feature is not available in your device", 1);
            }
        });
        this.mImgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openServiceOption();
            }
        });
        this.mImgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecordingStarted) {
                    stopRecording();
                }

                if (DevSpy_Conts.mRecordingStarted_Other) {
                    stop_audio_service();
                    stop_video_service();
                }

            }
        });
        this.mImgScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hbRecorder.isBusyRecording()) {
                    toggleNavigationButton(8);
                    Intent intent = new Intent(HBService.this, FloatingControlCaptureService.class);
                    intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
                    startService(intent);
                }
            }
        });
        this.mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isServiceRunning(FloatingControlCaptureService.class.getName(), getApplicationContext())) {
                    stopService(new Intent(HBService.this, FloatingControlCaptureService.class));
                }
                if (Utils.isServiceRunning(FloatingControlBrushService.class.getName(), getApplicationContext())) {
                    stopService(new Intent(HBService.this, FloatingControlBrushService.class));
                }
                if (hbRecorder.isBusyRecording()) {
//                    if (SharedPreferencesManager.getInstance().getBoolean(Utils.IS_REWARD_VIDEO, false)) {
//                        SharedPreferencesManager.getInstance().setBoolean(Utils.IS_REWARD_VIDEO, false);
//                    }
                    toggleView(mWarermarkLayout, 8);
                    mRecordingStarted = false;
                    toggleNavigationButton(8);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hbRecorder.stopScreenRecording();
                            refreshRecordings();
                            stopService();
                        }
                    }, 100);
                    return;
                }
                stopService();
            }
        });
        this.mImgToolbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTools();
            }
        });
        this.mViewRoot.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.initialX = HBService.this.paramViewRoot.x;
                    this.initialY = HBService.this.paramViewRoot.y;
                    this.initialTouchX = motionEvent.getRawX();
                    this.initialTouchY = motionEvent.getRawY();
                    return true;
                } else if (action == 1) {
                    if (motionEvent.getRawX() < ((float) (HBService.this.mScreenWidth / 2))) {
                        HBService.this.paramViewRoot.x = 0;
                    } else {
                        HBService.this.paramViewRoot.x = HBService.this.mScreenWidth;
                    }
                    HBService.this.paramViewRoot.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                    HBService.this.mWindowManager.updateViewLayout(HBService.this.mViewRoot, HBService.this.paramViewRoot);
                    int rawX = (int) (motionEvent.getRawX() - this.initialTouchX);
                    int rawY = (int) (motionEvent.getRawY() - this.initialTouchY);
                    if (rawX < 20 && rawY < 20) {
                        if (HBService.this.isViewCollapsed()) {
                            HBService.this.toggleNavigationButton(0);
                        } else {
                            HBService.this.toggleNavigationButton(8);
                        }
                    }
                    return true;
                } else if (action != 2) {
                    return false;
                } else {
                    HBService.this.paramViewRoot.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                    HBService.this.paramViewRoot.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                    HBService.this.mWindowManager.updateViewLayout(HBService.this.mViewRoot, HBService.this.paramViewRoot);
                    return true;
                }
            }
        });
    }


    public void openCaptureControlService() {

//        if (Utils.isServiceRunning(FloatingControlCaptureService.class.getName(), getApplicationContext())) {
        Intent intent = new Intent(this, FloatingControlCaptureService.class);
        intent.putExtra("android.intent.extra.INTENT", this.mScreenCaptureIntent);
        startService(intent);
//        }

    }


    public void openBrushControlService() {
        startService(new Intent(this, FloatingControlBrushService.class));
    }


    private void openServiceOption() {
        initView();
    }


    public void openTools() {
        toggleNavigationButton(8);
        Intent intent = new Intent(this, ToolsService.class);
        intent.putExtra("android.intent.extra.INTENT", this.mScreenCaptureIntent);
        startService(intent);
    }

    public void toggleView(View view, int visibility) {
        view.setVisibility(visibility);
    }

    private void initView() {
        this.windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        this.mLayout = (ConstraintLayout) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.screenrecorder_option, null);

        tv_video_record = mLayout.findViewById(R.id.tv_video_record);
        tv_audio_record = mLayout.findViewById(R.id.tv_audio_record);
        tv_screen_record = mLayout.findViewById(R.id.tv_screen_record);
        imv_close = mLayout.findViewById(R.id.imv_close);

        tv_video_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("#DEBUG", "tv_video_record");
                windowManager.removeView(mLayout);

                start_video_service();
            }
        });
        tv_audio_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("#DEBUG", "tv_audio_record");
                windowManager.removeView(mLayout);

                start_audio_service();
            }
        });
        tv_screen_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("#DEBUG", "tv_screen_record");
//                showCountDown();

                start_screenRecorder_service();

                toggleNavigationButton(8);
                windowManager.removeView(mLayout);
            }
        });
        imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleNavigationButton(8);
                windowManager.removeView(mLayout);
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, Build.VERSION.SDK_INT < 26 ? 2002 : 2038, 8, -3);
        this.mParams = layoutParams;
        this.windowManager.addView(this.mLayout, layoutParams);
    }

    public void showCountDown() {
        toggleView(this.mCountdownLayout, 0);

        new CountDownTimer(1000 * ((long) (Integer.parseInt(this.prefs.getString(getResources().getString(R.string.key_common_countdown), ExifInterface.GPS_MEASUREMENT_3D)) + 1)), 1000) {
            public void onTick(long l) {
                HBService hBService = HBService.this;
                hBService.toggleView(hBService.mViewRoot, 8);
                TextView textView = HBService.this.mTvCountdown;
                textView.setText("" + (l / 1000));
            }

            public void onFinish() {
                HBService hBService = HBService.this;
                hBService.toggleView(hBService.mCountdownLayout, 8);
                HBService hBService2 = HBService.this;
                hBService2.toggleView(hBService2.mViewRoot, 0);
                HBService.this.mRecordingStarted = true;
                HBService.this.toggleNavigationButton(8);
                Log.e("#DEBUG", "" + SharedPreferencesManager.getInstance().getBoolean(Utils.IS_REWARD_VIDEO, false));
                if (SharedPreferencesManager.getInstance().getBoolean(Utils.IS_REWARD_VIDEO, false)) {
                    HBService hBService3 = HBService.this;
                    hBService3.toggleView(hBService3.mWarermarkLayout, View.GONE);
                } else {
                    HBService hBService4 = HBService.this;
                    hBService4.toggleView(hBService4.mWarermarkLayout, View.VISIBLE);
                }

                HBService.this.initRecording();
            }
        }.start();
    }

    private void updateScreenSize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
    }

    public void onConfigurationChanged(Configuration configuration) {
        updateScreenSize();
        WindowManager.LayoutParams layoutParams = this.paramViewRoot;
        if (layoutParams != null) {
            layoutParams.gravity = 8388627;
            this.paramViewRoot.x = 0;
            this.paramViewRoot.y = 0;
        }
    }

    public void toggleNavigationButton(int visibility) {
        this.mImgStart.setVisibility(visibility);
        this.mImgPause.setVisibility(visibility);
        this.mImgClose.setVisibility(visibility);
        this.mImgToolbox.setVisibility(visibility);
        this.mImgStop.setVisibility(visibility);
        this.mImgResume.setVisibility(visibility);
        if (visibility == 8) {
            this.mViewRoot.setPadding(25, 25, 25, 25);
            this.isMenuVisible = false;
            this.mImgRec.setImageResource(R.drawable.ic_recording_off_lite);
            return;
        }
        this.isMenuVisible = true;
        this.mImgRec.setImageResource(R.drawable.ic_close_bubble);
        if (this.mRecordingStarted) {
            this.mImgStart.setVisibility(8);
            this.mImgStop.setVisibility(0);
            this.mImgPause.setVisibility(0);
        } else {
            this.mImgStop.setVisibility(8);
            this.mImgPause.setVisibility(8);
            this.mImgStart.setVisibility(0);
        }

        if (DevSpy_Conts.mRecordingStarted_Other) {
            this.mImgStart.setVisibility(8);
            this.mImgStop.setVisibility(0);
        }

        if (this.mRecordingPaused.booleanValue()) {
            this.mImgPause.setVisibility(8);
            this.mImgResume.setVisibility(0);
        } else {
            this.mImgResume.setVisibility(8);
        }
        this.mViewRoot.setPadding(20, 20, 20, 20);
    }

    public boolean isViewCollapsed() {
        View view = this.mViewRoot;
        return view == null || view.findViewById(R.id.imgToolbox).getVisibility() == 8;
    }

    public void stopService() {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                stopForeground(true);
            }
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOutputPath() {
        String generateFileName = "SR_" + generateFileName();
//        if (Build.VERSION.SDK_INT >= 29) {
//            this.resolver = getContentResolver();
//            ContentValues contentValues2 = new ContentValues();
//            this.contentValues = contentValues2;
//            contentValues2.put("relative_path", "DCIM/ScreenRecorder");
//            this.contentValues.put("title", generateFileName);
//            this.contentValues.put("_display_name", generateFileName);
//            this.contentValues.put("mime_type", "video/mp4");
//            this.mUri = this.resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, this.contentValues);
//            this.hbRecorder.setFileName(generateFileName);
//            this.hbRecorder.setOutputUri(this.mUri);
//            return;
//        }

        String output_path = ScreenShot_Video_FileHelper.videoPath(HBService.this);
        this.hbRecorder.setFileName(generateFileName);
        this.hbRecorder.setOutputPath(output_path);

    }

    private void updateGalleryUri() {
        this.contentValues.clear();
        this.contentValues.put("is_pending", 0);
        getContentResolver().update(this.mUri, this.contentValues, (String) null, (String[]) null);
    }

    private void refreshGalleryFile() {
        MediaScannerConnection.scanFile(this, new String[]{this.hbRecorder.getFilePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + str + ":");
                StringBuilder sb = new StringBuilder();
                sb.append("-> uri=");
                sb.append(uri);
                Log.i("ExternalStorage", sb.toString());
            }
        });
    }


    private String generateFileName() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())).replace(" ", "");
    }

    public void initRecording() {

        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(HBService.this);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 23) {
            intent.setAction(DevSpy_Conts.ACTION_START_SCREEN_RECORDER_SERVICE);
        }
        instance.sendBroadcast(intent);

        this.sharedPreHelper.saveTimeToPre();
        this.mScreenCaptureResultCode = this.mScreenCaptureIntent.getIntExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, Utils.RESULT_CODE_FAILED);
        this.hbRecorder.enableCustomSettings();
        customSettings();
        this.hbRecorder.startScreenRecording(this.mScreenCaptureIntent, this.mScreenCaptureResultCode);
    }

    public void HBRecorderOnComplete() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                if (SharedPreferencesManager.getInstance().getBoolean(Utils.IS_REWARD_VIDEO, false)) {
//                    SharedPreferencesManager.getInstance().setBoolean(Utils.IS_REWARD_VIDEO, false);
//                }

                Log.e("@@@", "recorded");

                toggleView(mWarermarkLayout, 8);
                mRecordingStarted = false;
                toggleNavigationButton(8);

                sharedPreHelper.remove();

                LocalBroadcastManager instance = LocalBroadcastManager.getInstance(HBService.this);
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 23) {
                    intent.setAction(DevSpy_Conts.ACTION_STOP_SCREEN_RECORDER_EXTRA);
                }
                instance.sendBroadcast(intent);

//                Utils.toast(getApplicationContext(), "Recording Saved", 1);


//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.setFlags(268468224);
//                intent.setAction(Utils.ACTION_VIDEO_RECORDED);
//                startActivity(intent);
            }
        });
        if (DevSpy_Conts.isTimerRunning_ScreenRecorder) {
            DevSpy_Conts.isTimerRunning_ScreenRecorder = false;
//
        }
        this.sharedPreHelper.remove();
        refreshRecordings();
    }


    public void HBRecorderOnError(int errorCode, String reason) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (errorCode == 38) {
                    Utils.toast(getApplicationContext(), getString(R.string.settingsNotSupported), 1);
                    return;
                }
                Utils.toast(getApplicationContext(), getString(R.string.unableToRecord), 1);
                Log.e("HBRecorderOnError", reason);
                sharedPreHelper.remove();
            }
        });
    }


    public void refreshRecordings() {
        if (this.hbRecorder.wasUriSet()) {
            updateGalleryUri();
        } else {
            refreshGalleryFile();
        }
    }


    private void customSettings() {
        SharedPreferences var1_1 = PreferenceManager.getDefaultSharedPreferences(this);

        boolean var2_2 = var1_1.getBoolean(getResources().getString(R.string.key_record_audio), true);
        this.hbRecorder.isAudioEnabled(var2_2);


        String var3_3 = var1_1.getString(getResources().getString(R.string.key_audio_source), null);
        if (var3_3 == null) {
            this.hbRecorder.setAudioSource("DEFAULT");
        } else {
            if (var3_3.equals("0")) {
                this.hbRecorder.setAudioSource("DEFAULT");
            } else if (var3_3.equals("1")) {
                this.hbRecorder.setAudioSource("CAMCODER");
            } else if (var3_3.equals("2")) {
                this.hbRecorder.setAudioSource("MIC");
            } else {
                this.hbRecorder.setAudioSource("DEFAULT");
            }
        }

        String var4_5 = var1_1.getString(getResources().getString(R.string.key_video_encoder), null);

        if (var4_5 == null) {
            this.hbRecorder.setVideoEncoder("DEFAULT");
        } else {
            if (var4_5.equals("0")) {
                this.hbRecorder.setVideoEncoder("DEFAULT");
            } else if (var4_5.equals("1")) {
                this.hbRecorder.setVideoEncoder("H264");
            } else if (var4_5.equals("2")) {
                this.hbRecorder.setVideoEncoder("H263");
            } else if (var4_5.equals("3")) {
                this.hbRecorder.setVideoEncoder("HEVC");
            } else if (var4_5.equals("4")) {
                this.hbRecorder.setVideoEncoder("MPEG_4_SP");
            } else if (var4_5.equals("5")) {
                this.hbRecorder.setVideoEncoder("VP8");
            } else {
                this.hbRecorder.setVideoEncoder("DEFAULT");
            }
        }

        String var5_7 = var1_1.getString(getResources().getString(R.string.key_video_resolution), null);
        if (var5_7 == null) {
            this.hbRecorder.setScreenDimensions(426, 240);
        } else {
            if (var5_7.equals("0")) {
                this.hbRecorder.setScreenDimensions(426, 240);
            } else if (var5_7.equals("1")) {
                this.hbRecorder.setScreenDimensions(640, 360);
            } else if (var5_7.equals("2")) {
                this.hbRecorder.setScreenDimensions(854, 480);
            } else if (var5_7.equals("3")) {
                this.hbRecorder.setScreenDimensions(1280, 720);
            } else if (var5_7.equals("4")) {
                this.hbRecorder.setScreenDimensions(1920, 1080);
            } else {
                this.hbRecorder.setScreenDimensions(426, 240);
            }
        }

        String var6_9 = var1_1.getString(getResources().getString(R.string.key_video_fps), null);
        if (var6_9 == null) {
            this.hbRecorder.setVideoFrameRate(60);
        } else {
            if (var6_9.equals("0")) {
                this.hbRecorder.setVideoFrameRate(60);
            } else if (var6_9.equals("1")) {
                this.hbRecorder.setVideoFrameRate(50);
            } else if (var6_9.equals("2")) {
                this.hbRecorder.setVideoFrameRate(48);
            } else if (var6_9.equals("3")) {
                this.hbRecorder.setVideoFrameRate(30);
            } else if (var6_9.equals("4")) {
                this.hbRecorder.setVideoFrameRate(25);
            } else if (var6_9.equals("5")) {
                this.hbRecorder.setVideoFrameRate(24);
            } else {
                this.hbRecorder.setVideoFrameRate(60);
            }
        }

        String var7_11 = var1_1.getString(getResources().getString(R.string.key_video_bitrate), null);
        if (var7_11 == null) {
            this.hbRecorder.setVideoBitrate(12000000);
        } else {
            if (var7_11.equals("0")) {
                this.hbRecorder.setVideoBitrate(12000000);
            } else if (var7_11.equals("1")) {
                this.hbRecorder.setVideoBitrate(8000000);
            } else if (var7_11.equals("2")) {
                this.hbRecorder.setVideoBitrate(7500000);
            } else if (var7_11.equals("3")) {
                this.hbRecorder.setVideoBitrate(5000000);
            } else if (var7_11.equals("4")) {
                this.hbRecorder.setVideoBitrate(4000000);
            } else if (var7_11.equals("5")) {
                this.hbRecorder.setVideoBitrate(2500000);
            } else if (var7_11.equals("6")) {
                this.hbRecorder.setVideoBitrate(1500000);
            } else if (var7_11.equals("7")) {
                this.hbRecorder.setVideoBitrate(1000000);
            } else {
                this.hbRecorder.setVideoBitrate(12000000);
            }
        }


        String var8_13 = var1_1.getString(getResources().getString(R.string.key_output_format), null);

        if (var8_13 == null) {
            this.hbRecorder.setOutputFormat("DEFAULT");
        } else {
            if (var8_13.equals("0")) {
                this.hbRecorder.setOutputFormat("DEFAULT");
            } else if (var8_13.equals("1")) {
                this.hbRecorder.setOutputFormat("MPEG_4");
            } else if (var8_13.equals("2")) {
                this.hbRecorder.setOutputFormat("THREE_GPP");
            } else if (var8_13.equals("3")) {
                this.hbRecorder.setOutputFormat("WEBM");
            } else {
                this.hbRecorder.setOutputFormat("DEFAULT");
            }
        }


    }

    private BroadcastReceiver receiverStart = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DevSpy_Conts.ACTION_START_Video_Service)) {
                start_video_service();
            } else if (intent.getAction().equals(DevSpy_Conts.ACTION_START_Audio_Service)) {
                start_audio_service();
            } else if (intent.getAction().equals(DevSpy_Conts.ACTION_START_ScreenRecorder_Service)) {
                start_screenRecorder_service();
            } else if (intent.getAction().equals(DevSpy_Conts.ACTION_START_ScreenShot_Service)) {
                openCaptureControlService();
            } else if (intent.getAction().equals(DevSpy_Conts.ACTION_STOP_ScreenShot_Service)) {
                if (Utils.isServiceRunning(FloatingControlCaptureService.class.getName(), getApplicationContext())) {
                    stopService(new Intent(HBService.this, FloatingControlCaptureService.class));
                }
            }
        }
    };


    private void start_video_service() {

        Intent intent = new Intent(getApplicationContext(), Video_RecorderService.class);
        intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setAction("Recorder");
        Log.e("#TESTSCHEDULE", "start_video_service-->   " + DevSpy_Conts.isTimerRunning_Video);
        if (DevSpy_Conts.isTimerRunning_Video) {
            DevSpy_Conts.isTimerRunning_Video = false;
            stopService(intent);
//                    stopTimer();
            return;
        }

        if (DevSpy_Conts.Video_Backgorund_Forgroundchecker) {
            startService(intent);
        } else {
            DevSpy_Conts.mOpenAppChecker = false;
            Intent intent1 = new Intent(this, Video_Record_Background_Activity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
        SharePrefUtils.putString(DevSpy_Conts.VIDEO_FROM_SCHEDULE, "0");
        DevSpy_Conts.isTimerRunning_Video = true;
        DevSpy_Conts.mRecordingStarted_Other = true;

        toggleNavigationButton(8);

        //  Toasty.success(HBService.this, getString(R.string.startVideoService), Toasty.LENGTH_LONG).show();
    }

    private void stop_video_service() {

        Intent intent = new Intent(getApplicationContext(), Video_RecorderService.class);
        intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setAction("Recorder");
        if (DevSpy_Conts.isTimerRunning_Video) {
            DevSpy_Conts.isTimerRunning_Video = false;
            stopService(intent);
            toggleNavigationButton(8);
//                    stopTimer();
            return;
        }
        //   Toasty.success(HBService.this, getString(R.string.stopVideoService), Toasty.LENGTH_LONG).show();

    }

    private void start_audio_service() {

        Intent intent = new Intent(getApplicationContext(), Audio_Recorder_Service.class);
        intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setAction("Recorder");
        if (DevSpy_Conts.isTimerRunning_Audio) {
            DevSpy_Conts.isTimerRunning_Audio = false;
            stopService(intent);

//                    stopTimer();
            return;
        }
        SharePrefUtils.putString(DevSpy_Conts.AUDIO_FROM_SCHEDULE, "0");
        DevSpy_Conts.isTimerRunning_Audio = true;
        startService(intent);
        DevSpy_Conts.mRecordingStarted_Other = true;
        toggleNavigationButton(8);
        // Toasty.success(HBService.this, getString(R.string.startAudioService), Toasty.LENGTH_LONG).show();
    }

    private void stop_audio_service() {

        Intent intent = new Intent(getApplicationContext(), Audio_Recorder_Service.class);
        intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setAction("Recorder");
        if (DevSpy_Conts.isTimerRunning_Audio) {
            DevSpy_Conts.isTimerRunning_Audio = false;
            stopService(intent);
            toggleNavigationButton(8);
//                    stopTimer();
            return;
        }
        // Toasty.success(HBService.this, getString(R.string.stopAudioService), Toasty.LENGTH_LONG).show();
    }

    private void start_screenRecorder_service() {


        if (DevSpy_Conts.isTimerRunning_ScreenRecorder) {
            DevSpy_Conts.isTimerRunning_ScreenRecorder = false;
//            stopService(intent);
//                    stopTimer();
            stopRecording();

            return;
        }
        DevSpy_Conts.isTimerRunning_ScreenRecorder = true;
//        startService(intent);
        showCountDown();
        toggleNavigationButton(8);
        //Toasty.success(HBService.this, getString(R.string.startScreenRecorderService), Toasty.LENGTH_LONG).show();
    }

    private void stop_screenRecorder_service() {

        if (DevSpy_Conts.isTimerRunning_ScreenRecorder) {
            DevSpy_Conts.isTimerRunning_ScreenRecorder = false;
//            stopService(intent);
//                    stopTimer();
            stopRecording();
            return;
        }
        // Toasty.success(HBService.this, getString(R.string.stopScreenRecorderService), Toasty.LENGTH_LONG).show();
    }

    private void stopRecording() {
        // Toasty.success(HBService.this, getString(R.string.stopRecording), Toasty.LENGTH_LONG).show();
        toggleView(mWarermarkLayout, 8);
        mRecordingStarted = false;
        toggleNavigationButton(8);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hbRecorder.stopScreenRecording();

                refreshRecordings();
            }
        }, 100);
        Toasty.success(HBService.this, getString(R.string.stopRecording), Toasty.LENGTH_LONG).show();
    }
}