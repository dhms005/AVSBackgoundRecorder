package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver;

import static com.unity3d.services.core.properties.ClientProperties.getApplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_Save_Schedule_Activity;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordRepository;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;


public class Video_AlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.e("#TESTSCHEDULE", "1-->   " + SharePrefUtils.getString(CY_M_Conts.CURRENT_TIME, ""));
        Intent intent2 = new Intent(context, Video_RecorderService.class);
        intent2.putExtra(CY_M_Conts.CAMERA_USE, intent.getStringExtra(CY_M_Conts.CAMERA_USE));
//        intent2.putExtra(CY_M_Conts.CAMERA_DURATION, intent.getStringExtra(CY_M_Conts.CAMERA_DURATION));
        if (!Video_ServiceHelper.isServiceRunning(Video_RecorderService.class, context)) {
            Log.e("#TESTSCHEDULE", "isServiceRunning");
            context.startService(intent2);
        } else {
            Log.e("#TESTSCHEDULE", "isServiceRunningNot");
        }
    }
}
