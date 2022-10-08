package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Screen_Shot_Tab;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Service.FloatingService;

public class ScreenshotNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        action.hashCode();
        if (action.equals("exit")) {
            context.stopService(new Intent(context, FloatingService.class));
//            closeStatus();
            if (Screen_Shot_Tab.getInstance() != null) {
                Screen_Shot_Tab.getInstance().setStartStopButton();
            }
        } else if (action.equals(Constants.FOLDER_NAME)) {
//            closeStatus();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    FloatingService.getmInstance().startProjection();
                }
            }, 500);
        }
    }

    private void closeStatus() {
        CY_M_MyApplication.getAppContext().sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }
}
