package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_MyApplication;

public class SharedPreferencesManager {
    private static SharedPreferencesManager appPreferences;
    private SharedPreferences sharedPreferences;

    public static SharedPreferencesManager getInstance() {
        if (appPreferences == null) {
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager();
            appPreferences = sharedPreferencesManager;
            if (sharedPreferencesManager.sharedPreferences == null) {
                Context context = DevSpy_MyApplication.getAppContext();
                appPreferences.sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), 0);
            }
        }
        return appPreferences;
    }

    public boolean contains(String name) {
        return this.sharedPreferences.contains(name);
    }

    public boolean getBoolean(String name, boolean b) {
        return this.sharedPreferences.getBoolean(name, b);
    }

    public void setBoolean(String name, boolean b) {
        this.sharedPreferences.edit().putBoolean(name, b).apply();
    }

    public boolean getBoolean(String name) {
        return this.sharedPreferences.getBoolean(name, false);
    }
}
