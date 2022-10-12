package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Activities.Video_Record_Background_Activity;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class Video_AlarmReceiver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
//        Log.e("#TESTSCHEDULE", "1-->   " + SharePrefUtils.getString(CY_M_Conts.CURRENT_TIME, ""));
//        mRepository = new WordRepository(getApplication());
        Intent intent2 = new Intent(context, Video_RecorderService.class);
//        intent2.putExtra(CY_M_Conts.CAMERA_USE, intent.getStringExtra(CY_M_Conts.CAMERA_USE));
//        intent2.putExtra(CY_M_Conts.CAMERA_DURATION, intent.getStringExtra(CY_M_Conts.CAMERA_DURATION));
        if (!Video_ServiceHelper.isServiceRunning(Video_RecorderService.class, context)) {
            Log.e("#TESTSCHEDULE", "isServiceRunning");


            if (CY_M_Conts.Video_Backgorund_Forgroundchecker) {

                SharePrefUtils.putString(CY_M_Conts.VIDEO_FROM_SCHEDULE, "1");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent2);
                } else {
                    context.startService(intent2);
                }

            } else {
                CY_M_Conts.mOpenAppChecker = false;
                Intent intent1 = new Intent(context, Video_Record_Background_Activity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }


        } else {
            Log.e("#TESTSCHEDULE", "isServiceRunningNot");
        }

    }


}
