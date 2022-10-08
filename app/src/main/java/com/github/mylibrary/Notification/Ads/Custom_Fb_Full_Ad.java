package com.github.mylibrary.Notification.Ads;

import android.content.Context;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class Custom_Fb_Full_Ad {

    private MyAdViewAdListener mMyAdViewAdListener;
    InterstitialAd interstitialAd;


    public interface MyAdViewAdListener {
        void onAdClosed();

        void onAdFailedToLoad();

        void onAdLeftApplication();

        void onAdLoaded();

        void onAdOpened();
    }


    public void reload_fb_full_Ad(Context context) {

        interstitialAd = new InterstitialAd(context, SharePrefUtils.getString(Constant_ad.FACEBOOK_FULL, Custom_Ad_Key.KEY_FACEBOOK_FULL));
        // Set listeners for the Interstitial Ad

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                // Log.e("TAG", "Interstitial ad displayed.");
                if (Custom_Fb_Full_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Full_Ad.this.mMyAdViewAdListener.onAdOpened();
                }
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                // Log.e("TAG", "Interstitial ad dismissed.");
                if (Custom_Fb_Full_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Full_Ad.this.mMyAdViewAdListener.onAdClosed();
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                // Log.e("TAG", "Interstitial ad failed to load: " + adError.getErrorMessage());
                if (Custom_Fb_Full_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Full_Ad.this.mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Log.d("TAG", "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                if (Custom_Fb_Full_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Full_Ad.this.mMyAdViewAdListener.onAdLoaded();
                }
                // interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                // Log.d("TAG", "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                // Log.d("TAG", "Interstitial ad impression logged!");
            }
        };


        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    public void show_fb_full_Ad(Context context) {
        if (this.interstitialAd == null || !this.interstitialAd.isAdLoaded()) {
            // reload_fb_full_Ad(context);
            return;
        }

        this.interstitialAd.show();
    }

    public void setMyNativeAdListener(MyAdViewAdListener myNativeAdListener) {
        this.mMyAdViewAdListener = myNativeAdListener;
    }

    public boolean isLoaded() {
        if (this.interstitialAd != null && this.interstitialAd.isAdLoaded()) {
            // reload_admob_full_Ad(context);
            return true;
        }
        return false;
    }
}
