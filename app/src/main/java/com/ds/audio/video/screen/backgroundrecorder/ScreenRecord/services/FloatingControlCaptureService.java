package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import static com.unity3d.services.core.properties.ClientProperties.getActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Screen_Shot_Tab;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Activities.ScreenShotActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.ScreenShot_Video_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.google.android.material.badge.BadgeDrawable;
import com.hbisoft.hbrecorder.Const;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FloatingControlCaptureService extends Service implements View.OnClickListener, HBRecorderListener {
    private static int IMAGES_PRODUCED;
    private IBinder binder = new ServiceBinder();
    ContentValues contentValues;
    public LinearLayout floatingControls;
    public GestureDetector gestureDetector;
    public Handler handler = new Handler();
    public HBRecorder hbRecorder;
    public int height;
    public ImageView img;
    public boolean isOverRemoveView;
    public View mRemoveView;
    private Intent mScreenCaptureIntent = null;
    private int mScreenCaptureResultCode;
    Uri mUri;
    public int[] overlayViewLocation = {0, 0};
    public WindowManager.LayoutParams params;
    public SharedPreferences prefs;
    public BroadcastReceiver receiverCapture = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                int i = intent.getExtras().getInt("capture");
                if (i == 0) {
                    FloatingControlCaptureService.this.floatingControls.setVisibility(4);
                } else if (i == 1) {
                    FloatingControlCaptureService.this.floatingControls.setVisibility(0);
                }
            }
        }
    };
    public int[] removeViewLocation = {0, 0};
    ContentResolver resolver;
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
            layoutParams.height = Math.min(width / 10, height / 10);
            layoutParams.width = Math.min(width / 10, height / 10);
            img.setImageResource(R.drawable.ic_camera_service);
            floatingControls.setAlpha(0.5f);
            img.setLayoutParams(layoutParams);
            if (params.x < width - params.x) {
                params.x = 0;
            } else {
                params.x = width;
            }
            try {
                windowManager.updateViewLayout(floatingControls, params);
            } catch (Exception e) {
            }
        }
    };
    String screenshotOutput;
    private int ssDensity;
    public Handler ssHandler;
    private int ssHeight;
    public ImageReader ssImageReader;
    public MediaProjection ssMediaProjection;
    public VirtualDisplay ssVirtualDisplay;
    private int ssWidth;
    public Vibrator vibrate;
    public int width;
    public WindowManager windowManager;

    private static int getVirtualDisplayFlags() {
        return 16;
    }

    public boolean isPointInArea(int i, int i2, int i3, int i4, int i5) {
        return i >= i3 - i5 && i <= i3 + i5 && i2 >= i4 - i5 && i2 <= i4 + i5;
    }


    public void onCreate() {
        super.onCreate();
        new Thread() {
            public void run() {
                Looper.prepare();
                FloatingControlCaptureService.this.ssHandler = new Handler(Looper.getMainLooper());
                Looper.loop();
            }
        }.start();
        this.vibrate = (Vibrator) getSystemService("vibrator");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.windowManager = (WindowManager) getApplicationContext().getSystemService("window");
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.screenrecorder_floatbutton_control_capture, (ViewGroup) null);
        this.floatingControls = linearLayout;
        this.img = (ImageView) linearLayout.findViewById(R.id.imgIcon);
        View onGetRemoveView = onGetRemoveView();
        this.mRemoveView = onGetRemoveView;
        setupRemoveView(onGetRemoveView);
        this.params = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT < 26 ? 2002 : 2038, 8, -3);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.height = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.params.gravity = BadgeDrawable.TOP_START;
        this.params.x = this.width;
        this.params.y = this.height / 2;
        this.gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }
        });
        this.floatingControls.setOnTouchListener(new View.OnTouchListener() {
            private boolean flag = false;
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;
            private boolean oneRun = false;
            private WindowManager.LayoutParams paramsF;

            {
                this.paramsF = FloatingControlCaptureService.this.params;
            }

            public boolean onTouch(View view, MotionEvent motionEvent) {
                FloatingControlCaptureService.this.handler.removeCallbacks(FloatingControlCaptureService.this.runnable);
                if (FloatingControlCaptureService.this.gestureDetector.onTouchEvent(motionEvent)) {
                    FloatingControlCaptureService.this.mRemoveView.setVisibility(8);
                    FloatingControlCaptureService.this.handler.removeCallbacks(FloatingControlCaptureService.this.runnable);
                    FloatingControlCaptureService.this.handler.postDelayed(FloatingControlCaptureService.this.runnable, 3000);
                    FloatingControlCaptureService.this.openCapture();
                } else {
                    int action = motionEvent.getAction();
                    if (action == 0) {
                        ViewGroup.LayoutParams layoutParams = FloatingControlCaptureService.this.img.getLayoutParams();
                        layoutParams.height = Math.min(FloatingControlCaptureService.this.width / 8, FloatingControlCaptureService.this.height / 8);
                        layoutParams.width = Math.min(FloatingControlCaptureService.this.width / 8, FloatingControlCaptureService.this.height / 8);
                        FloatingControlCaptureService.this.img.setLayoutParams(layoutParams);
                        FloatingControlCaptureService.this.floatingControls.setAlpha(1.0f);
                        this.initialX = this.paramsF.x;
                        this.initialY = this.paramsF.y;
                        this.initialTouchX = motionEvent.getRawX();
                        this.initialTouchY = motionEvent.getRawY();
                        this.flag = true;
                    } else if (action == 1) {
                        this.flag = false;
                        if (FloatingControlCaptureService.this.params.x < FloatingControlCaptureService.this.width - FloatingControlCaptureService.this.params.x) {
                            FloatingControlCaptureService.this.params.x = 0;
                        } else {
                            FloatingControlCaptureService.this.params.x = FloatingControlCaptureService.this.width - FloatingControlCaptureService.this.floatingControls.getWidth();
                        }
                        if (FloatingControlCaptureService.this.isOverRemoveView) {
                            FloatingControlCaptureService.this.prefs.edit().putBoolean(Const.PREFS_TOOLS_CAPTURE, false).apply();
                            FloatingControlCaptureService.this.stopSelf();
                        } else {
                            FloatingControlCaptureService.this.windowManager.updateViewLayout(FloatingControlCaptureService.this.floatingControls, FloatingControlCaptureService.this.params);
                            FloatingControlCaptureService.this.handler.postDelayed(FloatingControlCaptureService.this.runnable, 3000);
                        }
                        FloatingControlCaptureService.this.mRemoveView.setVisibility(8);
                    } else if (action == 2) {
                        this.paramsF.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        this.paramsF.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        if (this.flag) {
                            FloatingControlCaptureService.this.mRemoveView.setVisibility(0);
                        }
                        FloatingControlCaptureService.this.windowManager.updateViewLayout(FloatingControlCaptureService.this.floatingControls, this.paramsF);
                        FloatingControlCaptureService.this.floatingControls.getLocationOnScreen(FloatingControlCaptureService.this.overlayViewLocation);
                        FloatingControlCaptureService.this.mRemoveView.getLocationOnScreen(FloatingControlCaptureService.this.removeViewLocation);
                        FloatingControlCaptureService floatingControlCaptureService = FloatingControlCaptureService.this;
                        floatingControlCaptureService.isOverRemoveView = floatingControlCaptureService.isPointInArea(floatingControlCaptureService.overlayViewLocation[0], FloatingControlCaptureService.this.overlayViewLocation[1], FloatingControlCaptureService.this.removeViewLocation[0], FloatingControlCaptureService.this.removeViewLocation[1], FloatingControlCaptureService.this.mRemoveView.getWidth());
                        if (FloatingControlCaptureService.this.isOverRemoveView) {
                            if (this.oneRun) {
                                if (Build.VERSION.SDK_INT < 26) {
                                    FloatingControlCaptureService.this.vibrate.vibrate(200);
                                } else {
                                    FloatingControlCaptureService.this.vibrate.vibrate(VibrationEffect.createOneShot(200, 255));
                                }
                            }
                            this.oneRun = false;
                        } else {
                            this.oneRun = true;
                        }
                    } else if (action == 3) {
                        FloatingControlCaptureService.this.mRemoveView.setVisibility(8);
                    }
                }
                return false;
            }
        });

        Log.e("#DEBUG", "onCreate: " + AppPref.getShowButton(this));
        if (AppPref.getShowButton(this)) {
            addBubbleView();
        }

        this.handler.postDelayed(this.runnable, 3000);
        registerReceiver(this.receiverCapture, new IntentFilter(Const.ACTION_SCREEN_SHOT));
    }

    private void setupRemoveView(View view) {
        view.setVisibility(8);
        this.windowManager.addView(view, newWindowManagerLayoutParamsForRemoveView());
    }

    private static WindowManager.LayoutParams newWindowManagerLayoutParamsForRemoveView() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT < 26 ? 2002 : 2038, 262664, -3);
        layoutParams.gravity = 81;
        layoutParams.y = 56;
        return layoutParams;
    }

    public View onGetRemoveView() {
        return LayoutInflater.from(this).inflate(R.layout.screenrecorder_overlay_remove_view, (ViewGroup) null);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.hbRecorder = new HBRecorder(this, this);
            setOutputPath();
            this.mScreenCaptureIntent = (Intent) intent.getParcelableExtra("android.intent.extra.INTENT");
        }
        if (Screen_Shot_Tab.getInstance() != null) {
            Screen_Shot_Tab.getInstance().setStartStopButton();
        }

        ViewGroup.LayoutParams layoutParams = this.img.getLayoutParams();
        layoutParams.height = Math.min(this.width / 8, this.height / 8);
        layoutParams.width = Math.min(this.width / 8, this.height / 8);
        this.img.setLayoutParams(layoutParams);
        this.floatingControls.setAlpha(1.0f);
        return super.onStartCommand(intent, flags, startId);
    }

    public void addBubbleView() {
        LinearLayout linearLayout;
        WindowManager windowManager2 = this.windowManager;
        if (windowManager2 != null && (linearLayout = this.floatingControls) != null) {
            windowManager2.addView(linearLayout, this.params);
        }
    }

    public void removeBubbleView() {
        LinearLayout linearLayout;
        WindowManager windowManager2 = this.windowManager;
        if (windowManager2 != null && (linearLayout = this.floatingControls) != null) {
            windowManager2.removeView(linearLayout);
        }
    }

    public void onClick(View view) {
    }

    public void openCapture() {
        if (!this.hbRecorder.isBusyRecording()) {


            DevSpy_Conts.mOpenAppChecker = false;

            Intent intent = new Intent(this, ScreenShotActivity.class);
            intent.putExtra("android.intent.extra.INTENT", this.mScreenCaptureIntent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    public void onDestroy() {
        if (AppPref.getShowButton(this)) {
            removeBubbleView();
        }
        unregisterReceiver(this.receiverCapture);
        WindowManager windowManager2 = this.windowManager;
        if (windowManager2 != null) {
            View view = this.mRemoveView;
            View view2 = view;
            if (view != null) {
                windowManager2.removeView(view2);
            }
        }

//        if (Screen_Shot_Tab.)
        if (Screen_Shot_Tab.getInstance() != null) {

            Log.e("@@@@", "Null getting");
            Screen_Shot_Tab.getInstance().setStartStopButton();
        }

        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d("TAG", "Binding successful!");
        return this.binder;
    }

    public void HBRecorderOnStart() {
    }

    public void HBRecorderOnComplete() {
    }

    public void HBRecorderOnError(int errorCode, String reason) {
    }

    public class ServiceBinder extends Binder {
        public ServiceBinder() {
        }

        public FloatingControlCaptureService getService() {
            return FloatingControlCaptureService.this;
        }
    }


    private void setOutputPath() {
        String generateFileName = generateFileName();
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

        String output_path = ScreenShot_Video_FileHelper.videoPath(FloatingControlCaptureService.this);
        this.hbRecorder.setOutputPath(output_path);
    }

    private String generateFileName() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())).replace(" ", "");
    }
}
