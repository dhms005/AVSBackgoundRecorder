package com.hbisoft.hbrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.stopService(new Intent(context, ScreenRecordService.class));
    }
}
