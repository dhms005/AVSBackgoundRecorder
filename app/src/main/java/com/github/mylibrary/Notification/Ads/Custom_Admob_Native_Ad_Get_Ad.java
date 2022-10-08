package com.github.mylibrary.Notification.Ads;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;


public class Custom_Admob_Native_Ad_Get_Ad {

    private MyAdViewAdListener mMyAdViewAdListener;

    NativeAd adViewCache;
    // private NativeAdLayout nativeAdLayout;

    public interface MyAdViewAdListener {
        void onAdClosed();

        void onAdFailedToLoad();

        void onAdLeftApplication();

        void onAdLoaded();

        NativeAd onAdLoaded(NativeAd adViewCache);

        void onAdOpened();
    }


    public void setMyNativeAdListener(MyAdViewAdListener myNativeAdListener) {
        this.mMyAdViewAdListener = myNativeAdListener;
    }

    public void loadNativeAd(final Activity context) {

//        AdLoader.Builder builder = new AdLoader.Builder(context, SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE));

        AdLoader adLoader = new AdLoader.Builder(context, SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        // Show the ad.

                        if (Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener.onAdLoaded();
                        }


                        if (context.isDestroyed()) {
                            NativeAd.destroy();
                            return;
                        }

                        adViewCache = NativeAd;

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        if (Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener.onAdFailedToLoad();
                        }
                    }

                    @Override
                    public void onAdOpened() {
                        if (Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener.onAdOpened();
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                        if (Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener.onAdLoaded();
                        }
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        if (Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Ad_Get_Ad.this.mMyAdViewAdListener.onAdClosed();
                        }
                    }


                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());



    }

    public NativeAd CheckAdCache() {
        return adViewCache;
    }

}
