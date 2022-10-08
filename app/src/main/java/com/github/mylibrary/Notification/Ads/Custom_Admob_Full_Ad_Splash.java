package com.github.mylibrary.Notification.Ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Custom_Admob_Full_Ad_Splash {

    private MyAdViewAdListener mMyAdViewAdListener;

    private InterstitialAd admob_mInterstitialAd;

    public static boolean admob_mInterstitialAd_splash;

    public interface MyAdViewAdListener {
        void onAdClosed();

        void onAdFailedToLoad();

        void onAdLeftApplication();

        void onAdLoaded();

        void onAdOpened();
    }


    public void reload_admob_full_Ad(Context context) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, SharePrefUtils.getString(Constant_ad.GOOGLE_FULL_AD_SPLASH, Custom_Ad_Key.KEY_ADMOB_SPLASH), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                admob_mInterstitialAd = interstitialAd;
                admob_mInterstitialAd_splash = false;
                if (Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener != null) {
                    Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener.onAdLoaded();
                }
                full_admethod();
//                Log.e("Ad", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
//                Log.e("Ad", loadAdError.getMessage());
                admob_mInterstitialAd = null;
                admob_mInterstitialAd_splash = false;
                if (Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener != null) {
                    Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

        });




//        admob_mInterstitialAd = new InterstitialAd(context);
//        admob_mInterstitialAd.setAdUnitId(SharePrefUtils.getString(Constant_ad.GOOGLE_FULL_AD, Custom_Ad_Key.KEY_ADMOB_FULL));
//
//
//        admob_mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                if (Custom_Admob_Full_Ad.this.mMyAdViewAdListener != null) {
//                    Custom_Admob_Full_Ad.this.mMyAdViewAdListener.onAdLoaded();
//                }
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                if (Custom_Admob_Full_Ad.this.mMyAdViewAdListener != null) {
//                    Custom_Admob_Full_Ad.this.mMyAdViewAdListener.onAdFailedToLoad();
//                }
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//                if (Custom_Admob_Full_Ad.this.mMyAdViewAdListener != null) {
//                    Custom_Admob_Full_Ad.this.mMyAdViewAdListener.onAdOpened();
//                }
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the interstitial ad is closed.
//                if (Custom_Admob_Full_Ad.this.mMyAdViewAdListener != null) {
//                    Custom_Admob_Full_Ad.this.mMyAdViewAdListener.onAdClosed();
//                }
//            }
//        });
//
//        // For auto play video ads, it's recommended to load the ad
//        // at least 30 seconds before it is shown
//        admob_mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void full_admethod(){
        admob_mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
//                Log.e("Ad", "The ad was dismissed.");

                if (Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener != null) {
                    Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener.onAdClosed();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
//                Log.e("Ad", "The ad failed to show.");
                if (Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener != null) {
                    Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                admob_mInterstitialAd = null;
                admob_mInterstitialAd_splash = true;
//                Log.e("Ad", "The ad was shown.");
                if (Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener != null) {
                    Custom_Admob_Full_Ad_Splash.this.mMyAdViewAdListener.onAdOpened();
                }
            }
        });
    }

    public void show_admob_full_Ad(Activity context) {
        if (this.admob_mInterstitialAd == null) {
            // reload_admob_full_Ad(context);
            return;
        }

        this.admob_mInterstitialAd.show(context);
    }
    public void setMyNativeAdListener(MyAdViewAdListener myNativeAdListener) {
        this.mMyAdViewAdListener = myNativeAdListener;
    }
    public boolean isLoaded() {
        if (this.admob_mInterstitialAd != null) {
            // reload_admob_full_Ad(context);
            return true;
        }
        return false;
    }
}
