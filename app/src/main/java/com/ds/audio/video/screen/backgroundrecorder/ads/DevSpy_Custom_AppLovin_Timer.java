package com.ds.audio.video.screen.backgroundrecorder.ads;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.OnLifecycleEvent;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Key;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

/**
 * Prefetches App Open Ads.
 */
public class DevSpy_Custom_AppLovin_Timer implements androidx.lifecycle.LifecycleObserver {
    private static final String LOG_TAG = "FSO_Custom_AppLovin_Timer";

    private final DevSpy_MyApplication myApplication;
    private MyAdViewAdListener mMyAdViewAdListener;
    Activity context;
    MaxInterstitialAd interstitialAppLovinAd;

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
    public DevSpy_Custom_AppLovin_Timer(DevSpy_MyApplication myApplication) {
        this.myApplication = myApplication;
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
    public void reload_admob_full_Ad(Activity context) {

        if (!SharePrefUtils.getString(Constant_ad.FACEBOOK_FULL, Custom_Ad_Key.KEY_FACEBOOK_FULL).equals("")) {
            interstitialAppLovinAd = new MaxInterstitialAd(SharePrefUtils.getString(Constant_ad.FACEBOOK_FULL, Custom_Ad_Key.KEY_FACEBOOK_FULL), context);
            interstitialAppLovinAd.loadAd();
            full_admethod();
        }

    }


    public void full_admethod() {

        interstitialAppLovinAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
//                Log.e("LOG_TAG", "onAdLoaded: ");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.e("LOG_TAG", "onAdDisplayed: ");
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdOpened();
                }
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.e("LOG_TAG", "onAdHidden: ");
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdClosed();
                }
                reload_admob_full_Ad(context);
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.e("LOG_TAG", "onAdClicked: ");

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.e("LOG_TAG", "onAdLoadFailed: ");
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.e("LOG_TAG", "onAdDisplayFailed: ");
                if (mMyAdViewAdListener != null) {
                    mMyAdViewAdListener.onAdFailedToLoad();
                }
                reload_admob_full_Ad(context);
            }
        });

    }


    public void show_admob_full_Ad(Activity context) {
        if (this.interstitialAppLovinAd == null) {
            return;
        }
        this.context = context;
        if (interstitialAppLovinAd.isReady()) {
            interstitialAppLovinAd.showAd();
        }
    }


    public void setMyNativeAdListener(MyAdViewAdListener myNativeAdListener) {
        this.mMyAdViewAdListener = myNativeAdListener;
    }

    public boolean isLoaded() {
        if (interstitialAppLovinAd != null) {
            // reload_admob_full_Ad(context);
            return true;
        }
//        reload_admob_full_Ad(myApplication);
        return false;
    }
}
