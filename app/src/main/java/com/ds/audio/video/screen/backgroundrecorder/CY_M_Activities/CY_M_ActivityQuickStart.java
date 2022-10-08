package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_ServiceHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.services.Video_RecorderService;

public class CY_M_ActivityQuickStart extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!Video_ServiceHelper.isServiceRunning(Video_RecorderService.class, this)) {
            startService(new Intent(this, Video_RecorderService.class));
        }
        finish();
    }
}
