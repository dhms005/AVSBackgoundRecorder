package com.ds.audio.video.screen.backgroundrecorder.ads;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.github.mylibrary.Notification.Ads.Constant_ad.GOOGLE_FULL_AD;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Key;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

/**
 * Prefetches App Open Ads.
 */
public class DevSpy_Custom_Full_Ad_Timer implements androidx.lifecycle.LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";

    private final DevSpy_MyApplication ESMyApplication;
    private InterstitialAd admob_mInterstitialAd;
    public static boolean admob_mInterstitialAd_temp = false;
    private MyAdViewAdListener mMyAdViewAdListener;
    Activity context;

    public interface MyAdViewAdListener {

        void onAdClosed();

        void onAdFailedToLoad();

        void onAdLeftApplication();

        void onAdLoaded();

        void onAdOpened();

        void onAdShowing();
    }

    /**
     * Constructor
     */
    public DevSpy_Custom_Full_Ad_Timer(DevSpy_MyApplication ESMyApplication) {
        this.ESMyApplication = ESMyApplication;
//        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        if (SharePrefUtils.getInt(Constant_ad.MINUTE, 0) != 0 && !SharePrefUtils.getString(GOOGLE_FULL_AD, "").equals("")) {
//            reload_admob_full_Ad(ESMyApplication);
//        }
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void reload_admob_full_Ad(Context context) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, SharePrefUtils.getString(GOOGLE_FULL_AD, Custom_Ad_Key.KEY_ADMOB_FULL), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@androidx.annotation.NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                admob_mInterstitialAd = interstitialAd;
                Constant_ad.IsAdShowing = false;
//                admob_mInterstitialAd_temp = interstitialAd;

                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdLoaded();
                }
                full_admethod();
                //Log.e("Ad123", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@androidx.annotation.NonNull LoadAdError loadAdError) {
                // Handle the error
                // Log.e("Ad123", loadAdError.getMessage());
                Constant_ad.IsAdShowing = false;
                admob_mInterstitialAd = null;
                admob_mInterstitialAd_temp = false;
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

        });
    }


    public void full_admethod() {
        admob_mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
//                Log.e("Ad", "The ad was dismissed.");

                admob_mInterstitialAd_temp = false;
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdClosed();
                }
                Constant_ad.IsAdShowing = false;

                reload_admob_full_Ad(ESMyApplication);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
//                Log.e("Ad", "The ad failed to show.");
                Constant_ad.IsAdShowing = false;
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                admob_mInterstitialAd = null;
                admob_mInterstitialAd_temp = true;
                Constant_ad.IsAdShowing = true;
//                Log.e("Ad", "The ad was shown.");
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdOpened();
                }
            }
        });
    }


    public void show_admob_full_Ad(Activity context) {
        if (this.admob_mInterstitialAd == null) {
            return;
        }
        this.context = context;
        this.admob_mInterstitialAd.show(context);
    }


    public void setMyNativeAdListener(MyAdViewAdListener myNativeAdListener) {
        this.mMyAdViewAdListener = myNativeAdListener;
    }

    public boolean isLoaded() {
        if (admob_mInterstitialAd != null) {
            // reload_admob_full_Ad(context);
            return true;
        }
        reload_admob_full_Ad(ESMyApplication);
        return false;
    }
}
