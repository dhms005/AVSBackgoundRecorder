package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.HBService;


public class RecordingServiceStopReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d("Stop ", "Stopping Service Now ...");
        context.stopService(new Intent(context, HBService.class));
    }
}
