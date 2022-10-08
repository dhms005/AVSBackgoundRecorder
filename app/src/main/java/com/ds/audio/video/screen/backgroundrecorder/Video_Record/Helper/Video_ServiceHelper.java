package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;

public class Video_ServiceHelper {
    public static boolean isServiceRunning(Class<?> cls, Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startRecordService(Context context) {
        Intent intent = new Intent(context, Video_RecorderService.class);
        Log.e("aaa", "Service running : " + isServiceRunning(Video_RecorderService.class, context));
        if (isServiceRunning(Video_RecorderService.class, context)) {
            context.stopService(intent);
        } else {
            context.startService(intent);
        }
    }
}
