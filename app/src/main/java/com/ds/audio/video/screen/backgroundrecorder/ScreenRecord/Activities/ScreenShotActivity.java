package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Activities;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.FloatingSSCapService;
import com.hbisoft.hbrecorder.Const;

public class ScreenShotActivity extends AppCompatActivity {
    private static final int PERMISSION_RECORD_DISPLAY = 3006;
    private Intent mScreenCaptureIntent = null;
    private int type = 0;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startScreenCapturing();
    }

    public void startScreenCapturing() {
        if (Settings.canDrawOverlays(this)) {
            startActivityForResult(((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent(), PERMISSION_RECORD_DISPLAY);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode != PERMISSION_RECORD_DISPLAY) {
            return;
        }
        if (resultCode != -1) {
            this.mScreenCaptureIntent = null;
            finish();
            return;
        }
        this.mScreenCaptureIntent = intent;
        intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, resultCode);
        new Handler().postDelayed(() -> {
            try {
                Intent intent2 = new Intent(this, FloatingSSCapService.class);
                intent2.setAction(Const.SCREEN_SHORT_START);
                intent2.putExtra("screenshort_resultcode", resultCode);
                intent2.putExtra("android.intent.extra.INTENT", this.mScreenCaptureIntent);
                intent2.putExtra("screenshort_type", this.type);
                startService(intent2);
                finish();
            } catch (Exception e) {
                Log.d("TAG", "startCaptureScreen: BB " + e.getLocalizedMessage());
            }
        }, 250);
    }

}
