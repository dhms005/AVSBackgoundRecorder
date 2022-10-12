package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class DevSpy_BoostAlarm extends BroadcastReceiver {
    public static final String PREFERENCES_RES_BOOSTER = "akash";
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_RES_BOOSTER, 0);
        this.sharedpreferences = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        this.editor = edit;
        edit.putString("booster", "1");
        this.editor.commit();
    }
}
