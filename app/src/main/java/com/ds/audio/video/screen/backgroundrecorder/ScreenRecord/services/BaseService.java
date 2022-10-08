package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class BaseService extends Service {
    public abstract void startPerformService();

    public abstract void stopPerformService();

    public IBinder onBind(Intent intent) {
        return null;
    }
}
