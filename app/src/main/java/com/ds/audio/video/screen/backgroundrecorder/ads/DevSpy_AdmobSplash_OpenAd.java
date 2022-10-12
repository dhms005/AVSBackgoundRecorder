package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Key;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class DevSpy_AdmobSplash_OpenAd implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    DevSpy_MyApplication myApplication;
    private static final String LOG_TAG = "AppOpenAdManager_2";
    private final String AD_UNIT_ID;
    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;

    /**
     * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
     */
    private long loadTime = 0;

    /**
     * Constructor.
     */
    public DevSpy_AdmobSplash_OpenAd(Context activity) {
        AD_UNIT_ID = SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            loadAd(activity);
        }
    }

    public DevSpy_AdmobSplash_OpenAd(DevSpy_MyApplication new_my_application, Context activity) {
        AD_UNIT_ID = SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD);
        this.myApplication = new_my_application;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        loadAd(activity);
    }


    public void loadAd(Context context) {
        if (isLoadingAd || isAdAvailable() ) {
            return;
        }
        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();

                        //Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;

                        //Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    /**
     * Check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Check if ad exists and can be shown.
     */
    private boolean isAdAvailable() {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    private void showAdIfAvailable(@NonNull final Activity activity) {
        showAdIfAvailable(
                activity,
                new DevSpy_MyApplication.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                });
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull final Activity activity,
            @NonNull DevSpy_MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            return;
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable() ) {
            onShowAdCompleteListener.onShowAdComplete();
//            loadAd(activity);
            return;
        }


        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null;
                        isShowingAd = false;
                        onShowAdCompleteListener.onShowAdComplete();
//                        loadAd(activity);
                    }

                    /** Called when fullscreen content failed to show. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        appOpenAd = null;
                        isShowingAd = false;

                        onShowAdCompleteListener.onShowAdComplete();
//                        loadAd(activity);
                    }

                    /** Called when fullscreen content is shown. */
                    @Override
                    public void onAdShowedFullScreenContent() {

                        //Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                    }
                });

        isShowingAd = true;
        appOpenAd.show(activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
