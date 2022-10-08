package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
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

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.google.android.material.badge.BadgeDrawable;
import com.hbisoft.hbrecorder.Const;

public class FloatingControlBrushService extends Service implements View.OnClickListener {
    
    public final String TAG = FloatingControlBrushService.class.getName();
    private IBinder binder = new ServiceBinder();
    public LinearLayout floatingControls;
    public GestureDetector gestureDetector;
    public Handler handler = new Handler();
    public int height;
    public ImageView img;
    public boolean isOverRemoveView;
    public View mRemoveView;
    public int[] overlayViewLocation = {0, 0};
    public WindowManager.LayoutParams params;
    public SharedPreferences prefs;
    public BroadcastReceiver receiverCapture = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                int i = intent.getExtras().getInt("capture");
                if (i == 0) {
                    FloatingControlBrushService.this.floatingControls.setVisibility(4);
                } else if (i == 1) {
                    FloatingControlBrushService.this.floatingControls.setVisibility(0);
                }
            }
        }
    };
    public int[] removeViewLocation = {0, 0};
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
            layoutParams.height = Math.min(width / 10, height / 10);
            layoutParams.width = Math.min(width / 10, height / 10);
            img.setImageResource(R.drawable.ic_brush_service);
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
    public Vibrator vibrate;
    public int width;
    public WindowManager windowManager;

    public boolean isPointInArea(int loc1, int loc2, int loc3, int loc4, int width2) {
        return loc1 >= loc3 - width2 && loc1 <= loc3 + width2 && loc2 >= loc4 - width2 && loc2 <= loc4 + width2;
    }


    public void onCreate() {
        super.onCreate();
        this.vibrate = (Vibrator) getSystemService("vibrator");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.windowManager = (WindowManager) getApplicationContext().getSystemService("window");
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.screenrecorder_floatbutton_control_brush, (ViewGroup) null);
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
        this.params.y = this.height / 4;
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
                this.paramsF = FloatingControlBrushService.this.params;
            }

            public boolean onTouch(View view, MotionEvent motionEvent) {
                FloatingControlBrushService.this.handler.removeCallbacks(FloatingControlBrushService.this.runnable);
                if (FloatingControlBrushService.this.gestureDetector.onTouchEvent(motionEvent)) {
                    FloatingControlBrushService.this.mRemoveView.setVisibility(8);
                    FloatingControlBrushService.this.handler.removeCallbacks(FloatingControlBrushService.this.runnable);
                    FloatingControlBrushService.this.handler.postDelayed(FloatingControlBrushService.this.runnable,3000);
                    FloatingControlBrushService.this.openBrsuh();
                } else {
                    int action = motionEvent.getAction();
                    if (action == 0) {
                        ViewGroup.LayoutParams layoutParams = FloatingControlBrushService.this.img.getLayoutParams();
                        layoutParams.height = Math.min(FloatingControlBrushService.this.width / 8, FloatingControlBrushService.this.height / 8);
                        layoutParams.width = Math.min(FloatingControlBrushService.this.width / 8, FloatingControlBrushService.this.height / 8);
                        FloatingControlBrushService.this.img.setLayoutParams(layoutParams);
                        FloatingControlBrushService.this.floatingControls.setAlpha(1.0f);
                        this.initialX = this.paramsF.x;
                        this.initialY = this.paramsF.y;
                        this.initialTouchX = motionEvent.getRawX();
                        this.initialTouchY = motionEvent.getRawY();
                        this.flag = true;
                    } else if (action == 1) {
                        this.flag = false;
                        if (FloatingControlBrushService.this.params.x < FloatingControlBrushService.this.width - FloatingControlBrushService.this.params.x) {
                            FloatingControlBrushService.this.params.x = 0;
                        } else {
                            FloatingControlBrushService.this.params.x = FloatingControlBrushService.this.width - FloatingControlBrushService.this.floatingControls.getWidth();
                        }
                        if (FloatingControlBrushService.this.isOverRemoveView) {
                            Log.d(FloatingControlBrushService.this.TAG, "onTouch: AAA ");
                            FloatingControlBrushService.this.prefs.edit().putBoolean(Const.PREFS_TOOLS_BRUSH, false).apply();
                            FloatingControlBrushService.this.stopSelf();
                        } else {
                            FloatingControlBrushService.this.windowManager.updateViewLayout(FloatingControlBrushService.this.floatingControls, FloatingControlBrushService.this.params);
                            FloatingControlBrushService.this.handler.postDelayed(FloatingControlBrushService.this.runnable, 3000);
                        }
                        FloatingControlBrushService.this.mRemoveView.setVisibility(8);
                    } else if (action == 2) {
                        this.paramsF.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        this.paramsF.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        if (this.flag) {
                            FloatingControlBrushService.this.mRemoveView.setVisibility(0);
                        }
                        FloatingControlBrushService.this.windowManager.updateViewLayout(FloatingControlBrushService.this.floatingControls, this.paramsF);
                        FloatingControlBrushService.this.floatingControls.getLocationOnScreen(FloatingControlBrushService.this.overlayViewLocation);
                        FloatingControlBrushService.this.mRemoveView.getLocationOnScreen(FloatingControlBrushService.this.removeViewLocation);
                        FloatingControlBrushService floatingControlBrushService = FloatingControlBrushService.this;
                        floatingControlBrushService.isOverRemoveView = floatingControlBrushService.isPointInArea(floatingControlBrushService.overlayViewLocation[0], FloatingControlBrushService.this.overlayViewLocation[1], FloatingControlBrushService.this.removeViewLocation[0], FloatingControlBrushService.this.removeViewLocation[1], FloatingControlBrushService.this.mRemoveView.getWidth());
                        if (FloatingControlBrushService.this.isOverRemoveView) {
                            if (this.oneRun) {
                                if (Build.VERSION.SDK_INT < 26) {
                                    FloatingControlBrushService.this.vibrate.vibrate(200);
                                } else {
                                    FloatingControlBrushService.this.vibrate.vibrate(VibrationEffect.createOneShot(200, 255));
                                }
                            }
                            this.oneRun = false;
                        } else {
                            this.oneRun = true;
                        }
                    } else if (action == 3) {
                        FloatingControlBrushService.this.mRemoveView.setVisibility(8);
                    }
                }
                return false;
            }
        });
        addBubbleView();
        this.handler.postDelayed(this.runnable,3000);
        registerReceiver(this.receiverCapture, new IntentFilter(Const.ACTION_SCREEN_SHOT));
    }

    private void setupRemoveView(View view) {
        view.setVisibility(8);
        try {
            this.windowManager.addView(view, newWindowManagerLayoutParamsForRemoveView());
        } catch (Exception e) {
        }
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
            try {
                windowManager2.addView(linearLayout, this.params);
            } catch (Exception e) {
            }
        }
    }

    public void removeBubbleView() {
        LinearLayout linearLayout;
        WindowManager windowManager2 = this.windowManager;
        if (windowManager2 != null && (linearLayout = this.floatingControls) != null) {
            try {
                windowManager2.removeView(linearLayout);
            } catch (Exception e) {
            }
        }
    }

    public void onClick(View view) {
        view.getId();
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.preference_vibrate_key), true)) {
            ((Vibrator) getSystemService("vibrator")).vibrate(100);
        }
    }

    public void openBrsuh() {
        startService(new Intent(this, BrushService.class));
    }

    public void onDestroy() {
        View view;
        removeBubbleView();
        unregisterReceiver(this.receiverCapture);
        WindowManager windowManager2 = this.windowManager;
        if (!(windowManager2 == null || (view = this.mRemoveView) == null)) {
            try {
                windowManager2.removeView(view);
            } catch (Exception e) {
            }
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d(this.TAG, "Binding successful!");
        return this.binder;
    }

    public class ServiceBinder extends Binder {
        public ServiceBinder() {
        }

        public FloatingControlBrushService getService() {
            return FloatingControlBrushService.this;
        }
    }
}
