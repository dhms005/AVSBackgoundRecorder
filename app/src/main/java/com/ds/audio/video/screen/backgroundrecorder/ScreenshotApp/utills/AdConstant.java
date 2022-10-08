package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import com.ds.audio.video.screen.backgroundrecorder.R;
//import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.FullShotDisclosure;
//import com.google.ads.consent.ConsentInformation;
//import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdConstant {
    public static String AD_AppOpen = "strADAppOpen()";
    public static String AD_Banner = "strADBANNER()";
    public static String AD_Full = "strADFULL()";
    public static String AD_Native = "strAdNative()";
    public static String AD_baseUrl = "StrbaseUrl()";
    public static int adCount = 0;
    public static int adLimit = 3;
    public static boolean npaflag = false;
    public static String[] publisherIds = {"strPUBLISHERID()"};

    private static native String StrbaseUrl();

    private static native String strADAppOpen();

    private static native String strADBANNER();

    private static native String strADFULL();

    private static native String strAdNative();

    private static native String strPUBLISHERID();

    static {
        System.loadLibrary("native-lib");
    }



}
