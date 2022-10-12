package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class Video_Record_Background_Activity extends AppCompatActivity {


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.video_black_activity);
        Intent intent = new Intent(getApplicationContext(), Video_RecorderService.class);
        startService(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DevSpy_Conts.mOpenAppChecker = true;
    }
}
