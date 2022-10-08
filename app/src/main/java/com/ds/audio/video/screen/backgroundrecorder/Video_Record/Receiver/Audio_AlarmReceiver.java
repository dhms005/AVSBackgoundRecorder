package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver;

import static com.unity3d.services.core.properties.ClientProperties.getApplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_Save_Schedule_Activity;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.services.Audio_Recorder_Service;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordRepository;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class Audio_AlarmReceiver extends BroadcastReceiver {

    private WordRepository mRepository;

    public void onReceive(Context context, Intent intent) {
//        Log.e("#TESTSCHEDULE", "1-->   " + SharePrefUtils.getString(CY_M_Conts.CURRENT_TIME, ""));
        mRepository = new WordRepository(getApplication());
        Intent intent2 = new Intent(context, Audio_Recorder_Service.class);
        intent2.putExtra(CY_M_Conts.CAMERA_USE, intent.getStringExtra(CY_M_Conts.CAMERA_USE));
//        intent2.putExtra(CY_M_Conts.CAMERA_DURATION, intent.getStringExtra(CY_M_Conts.CAMERA_DURATION));
        if (!Video_ServiceHelper.isServiceRunning(Audio_Recorder_Service.class, context)) {
//            Log.e("#TESTSCHEDULE", "isServiceRunning");
            context.startService(intent2);
        } else {
//            Log.e("#TESTSCHEDULE", "isServiceRunningNot");
        }
        mRepository.deleteTimer(Double.parseDouble(SharePrefUtils.getString(CY_M_Conts.AUDIO_CURRENT_TIME, "")));
        if (Audio_Save_Schedule_Activity.schedeluLisst.size() > 0) {
//            Log.e("#TESTSCHEDULE", "2-->   " + Audio_Save_Schedule_Activity.schedeluLisst.get(0).getTime());
            try {
                Thread.sleep(3000);
//                Log.e("#TESTSCHEDULE", "3-->   " + Audio_Save_Schedule_Activity.schedeluLisst.get(0).getTime());
                Intent intent1 = new Intent(context, Audio_AlarmReceiver.class);
                intent1.putExtra(CY_M_Conts.CAMERA_USE, Audio_Save_Schedule_Activity.schedeluLisst.get(0).getCamera());
//                intent1.putExtra(CY_M_Conts.CAMERA_DURATION, String.valueOf(Audio_Save_Schedule_Activity.schedeluLisst.get(0).getDuration() * 60));
                SharePrefUtils.putString(CY_M_Conts.AUDIO_CURRENT_TIME, String.valueOf(Audio_Save_Schedule_Activity.schedeluLisst.get(0).getTime()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(AlarmManager.RTC_WAKEUP, Audio_Save_Schedule_Activity.schedeluLisst.get(0).getTime(), PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
                } else {
                    ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(AlarmManager.RTC_WAKEUP, Audio_Save_Schedule_Activity.schedeluLisst.get(0).getTime(), PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
//                Log.e("#TESTSCHEDULE", "InterruptedException-->   " + Audio_Save_Schedule_Activity.schedeluLisst.get(0).getTime());
            }
        }
    }

   /*public static int getRandomNumber() {
        return (new Random()).nextInt((1000) + 1);
    }*/
}
