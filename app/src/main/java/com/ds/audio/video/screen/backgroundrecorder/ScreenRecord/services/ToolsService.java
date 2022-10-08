package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.hbisoft.hbrecorder.Const;

public class ToolsService extends Service implements View.OnTouchListener, View.OnClickListener {
    private ConstraintLayout mLayout;
    private NotificationManager mNotificationManager;
    private WindowManager.LayoutParams mParams;
    private Intent mScreenCaptureIntent = null;
    public SharedPreferences prefs;
    private WindowManager windowManager;
    Switch sw_capture, sw_camera, sw_brush;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    public void onCreate() {
        super.onCreate();
    }

    private void initView() {
        this.windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        this.mLayout = (ConstraintLayout) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.screenrecorder_tools, null);

        sw_capture = mLayout.findViewById(R.id.sw_capture);
        sw_camera = this.mLayout.findViewById(R.id.sw_camera);
        sw_brush = this.mLayout.findViewById(R.id.sw_brush);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ((ImageView) this.mLayout.findViewById(R.id.imv_close)).setOnClickListener(view -> stopSelf());
        sw_capture.setChecked(this.prefs.getBoolean(Const.PREFS_TOOLS_CAPTURE, false));
        sw_camera.setChecked(this.prefs.getBoolean(Const.PREFS_TOOLS_CAMERA, false));
        sw_brush.setChecked(this.prefs.getBoolean(Const.PREFS_TOOLS_BRUSH, false));
        sw_capture.setOnCheckedChangeListener((compoundButton, b) -> {
            prefs.edit().putBoolean(Const.PREFS_TOOLS_CAPTURE, b).apply();
            Intent intent = new Intent(ToolsService.this, FloatingControlCaptureService.class);
            intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
            if (!b) {
                stopService(intent);
            } else {
                startService(intent);
            }
        });
        sw_brush.setOnCheckedChangeListener((compoundButton, b) -> {
            prefs.edit().putBoolean(Const.PREFS_TOOLS_BRUSH, b).apply();
            Intent intent = new Intent(ToolsService.this, FloatingControlBrushService.class);
            if (!b) {
                stopService(intent);
            } else {
                startService(intent);
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, Build.VERSION.SDK_INT < 26 ? 2002 : 2038, 8, -3);
        this.mParams = layoutParams;
        this.windowManager.addView(this.mLayout, layoutParams);
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.mScreenCaptureIntent = intent.getParcelableExtra("android.intent.extra.INTENT");
        }
        initView();
        return START_NOT_STICKY;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imv_close) {
            stopSelf();
        }
    }

    public void onDestroy() {
        ConstraintLayout constraintLayout;
        WindowManager windowManager2 = this.windowManager;
        if (!(windowManager2 == null || (constraintLayout = this.mLayout) == null)) {
            windowManager2.removeView(constraintLayout);
        }
        super.onDestroy();
    }
}
