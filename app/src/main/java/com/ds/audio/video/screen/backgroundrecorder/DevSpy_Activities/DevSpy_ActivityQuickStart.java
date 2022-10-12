package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class DevSpy_ActivityQuickStart extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        if (!Video_ServiceHelper.isServiceRunning(Video_RecorderService.class, this)) {
            startService(new Intent(this, Video_RecorderService.class));
        }
        finish();
    }
}
