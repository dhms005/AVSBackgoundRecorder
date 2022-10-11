package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver;

import static com.unity3d.services.core.properties.ClientProperties.getApplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_Save_Schedule_Activity;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.services.Audio_Recorder_Service;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordRepository;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class Audio_AlarmReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        Log.e("#TESTSCHEDULE", "1-->   " + SharePrefUtils.getString(CY_M_Conts.AUDIO_CURRENT_TIME, ""));
        Intent intent2 = new Intent(context, Audio_Recorder_Service.class);
//        intent2.putExtra(CY_M_Conts.CAMERA_USE, intent.getStringExtra(CY_M_Conts.CAMERA_USE));
//        intent2.putExtra(CY_M_Conts.CAMERA_DURATION, intent.getStringExtra(CY_M_Conts.CAMERA_DURATION));
        if (!Video_ServiceHelper.isServiceRunning(Audio_Recorder_Service.class, context)) {
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
