package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.GetIntentForImage;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import org.greenrobot.eventbus.EventBus;

public class ScreenshotRequestActivity extends AppCompatActivity {
    public static ScreenshotRequestActivity activity;
    GetIntentForImage image = new GetIntentForImage();
    MediaProjectionManager mediaProjectionManager;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        activity = this;
        MediaProjectionManager mediaProjectionManager2 = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        this.mediaProjectionManager = mediaProjectionManager2;
        if (mediaProjectionManager2 != null) {
            callScreenCapturePermission(mediaProjectionManager2);
        }
    }

    public void callScreenCapturePermission(MediaProjectionManager mediaProjectionManager2) {
        startActivityForResult(mediaProjectionManager2.createScreenCaptureIntent(), 105);
    }

    
    public void onPause() {
        super.onPause();
        EventBus.getDefault().post(this.image);
    }

    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 105 && intent != null) {
            if (-1 == i2) {
                this.image.setIntent(intent);
                Log.i("OLLLLLLLLL", "onActivityResult: " + intent);
            }
            EventBus.getDefault().post(this.image);
            finish();
        }
    }
}
