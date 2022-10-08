package com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.utils;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MyApp extends Application {
    private static Context context;
    private static MyApp mInstance;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public static Context getAppContext() {
        return context;
    }
}