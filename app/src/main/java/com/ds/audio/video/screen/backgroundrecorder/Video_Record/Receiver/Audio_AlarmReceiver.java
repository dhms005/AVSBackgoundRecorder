package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.services.Audio_Recorder_Service;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class Audio_AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.e("#TESTSCHEDULE", "1-->   " + SharePrefUtils.getString(DevSpy_Conts.AUDIO_CURRENT_TIME, ""));
        Intent intent2 = new Intent(context, Audio_Recorder_Service.class);
//        intent2.putExtra(CY_M_Conts.CAMERA_USE, intent.getStringExtra(CY_M_Conts.CAMERA_USE));
//        intent2.putExtra(CY_M_Conts.CAMERA_DURATION, intent.getStringExtra(CY_M_Conts.CAMERA_DURATION));
        if (!Video_ServiceHelper.isServiceRunning(Audio_Recorder_Service.class, context)) {
            SharePrefUtils.putString(DevSpy_Conts.AUDIO_FROM_SCHEDULE, "1");
//            Log.e("#TESTSCHEDULE", "isServiceRunning");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent2);
            } else {
                context.startService(intent2);
            }
//            context.startService(intent2);
        } else {
//            Log.e("#TESTSCHEDULE", "isServiceRunningNot");
        }

    }

   /*public static int getRandomNumber() {
        return (new Random()).nextInt((1000) + 1);
    }*/
}
