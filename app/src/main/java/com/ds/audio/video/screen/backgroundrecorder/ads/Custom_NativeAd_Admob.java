package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Key;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Random;


public class Custom_NativeAd_Admob {

    MaxNativeAdLoader nativeAdLoader;
    MaxNativeAdLoader nativeAdLoaderSmall;
    MaxNativeAdLoader nativeAdLoaderNativeBanner;

    public void showBigNativeAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdBig = nativeAd;
                            inFlat_admobNativeAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    showBigNativeAds_FailedCall(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showBigNativeAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

    public void showBigNativeAds_FailedCall(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE2, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdBig = nativeAd;
                            inFlat_admobNativeAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    setQurekaNativeBigAds(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showBigNativeAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

    public void showNativeSmallAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdSmall = nativeAd;
                            inFlat_admobNativeSmallAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    showNativeSmallAds_FailedCall(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNativeSmallAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public void showNativeSmallAds_FailedCall(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE2, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdSmall = nativeAd;
                            inFlat_admobNativeSmallAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    setQurekaNativeSmallAds(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNativeSmallAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public void showNativeBannerAd(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE_BANNER, Custom_Ad_Key.KEY_ADMOB_NATIVE_BANNER);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeBanner = nativeAd;
                            inFlat_admobNativeBannerAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    showNativeBannerAd_FailedCall(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNativeBannerAd(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

    public void showNativeBannerAd_FailedCall(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE2, Custom_Ad_Key.KEY_ADMOB_NATIVE_BANNER);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeBanner = nativeAd;
                            inFlat_admobNativeBannerAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    setQurekaNativeBannerAds(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNativeBannerAd(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }
    }

    public void showNative100DpAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdBig = nativeAd;
                            inFlat_admobNative100DPAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

//                                if (!activity.isDestroyed()) {
//                                    setQurekaNativeBannerAds(activity, viewGroup);
//                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNative100DpAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        }
    }

    public void showNative400DpAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdBig400 = nativeAd;
                            inFlat_admobNative400DPAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    showNative400DpAds_FailedCall(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNative400DpAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        }
    }

    public void showNative400DpAds_FailedCall(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String admobnative = SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE2, Custom_Ad_Key.KEY_ADMOB_NATIVE);

            AdLoader adLoader = new AdLoader.Builder(activity, admobnative)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
//                        Log.e("call ads", "call loaded");
//                        Log.e("call ads","call click "+nativeAd.getHeadline());
                            DevSpy_Utility._nativeAdBig400 = nativeAd;
                            inFlat_admobNative400DPAds(nativeAd, viewGroup, activity);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {

                                if (!activity.isDestroyed()) {
                                    setQurekaNativeBig400DPAds(activity, viewGroup);
                                }

                            }
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
//                        Log.e("call ads", "call click");

                            if (!activity.isDestroyed()) {
                                showNative400DpAds(activity, viewGroup);

                            }
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        }
    }


    public void inFlat_admobNativeAds(NativeAd unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        viewGroup.setVisibility(View.VISIBLE);


        NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.z_admob_native_new, null);


        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
//        mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        TextView button;
        button = adView.findViewById(R.id.ad_call_to_action);
        if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
            (button.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
        }

        NativeAdView nativeAdView;
        nativeAdView = adView.findViewById(R.id.ad_view);
        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
        } else {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
        }

        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            button.setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }

        if (unifiedNativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        }

//        if (unifiedNativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(unifiedNativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(unifiedNativeAd);

        viewGroup.removeAllViews();
        viewGroup.addView(adView);

    }

    public void inFlat_admobNativeSmallAds(NativeAd unifiedNativeAd, ViewGroup viewGroup, Activity context) {
        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        viewGroup.setVisibility(View.VISIBLE);

        NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.z_new_native_small_new, null);

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
//        mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        TextView button;
        button = adView.findViewById(R.id.ad_call_to_action);
        if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
            (button.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
        }
        NativeAdView nativeAdView;
        nativeAdView = adView.findViewById(R.id.ad_view);
        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
        } else {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
        }

        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            button.setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }

        if (unifiedNativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        }

        if (unifiedNativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(unifiedNativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(unifiedNativeAd);
        viewGroup.removeAllViews();
        viewGroup.addView(adView);
    }

    public void inFlat_admobNativeBannerAds(NativeAd unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        viewGroup.setVisibility(View.VISIBLE);

        NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.z_admob_native_banner_layout_1, null);

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
//        mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        TextView button;
        button = adView.findViewById(R.id.ad_call_to_action);
        if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
            (button.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
        }
        NativeAdView nativeAdView;
        nativeAdView = adView.findViewById(R.id.ad_view);
        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
        } else {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
        }

        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            button.setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }

        if (unifiedNativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        }

        if (unifiedNativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(unifiedNativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(unifiedNativeAd);
        viewGroup.removeAllViews();
        viewGroup.addView(adView);
    }

    public void inFlat_admobNative100DPAds(NativeAd unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        viewGroup.setVisibility(View.VISIBLE);


        NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.z_admob_native_new_2, null);


        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
//        mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        TextView button;
        button = adView.findViewById(R.id.ad_call_to_action);
        if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
            (button.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
        }

        NativeAdView nativeAdView;
        nativeAdView = adView.findViewById(R.id.ad_view);
        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
        } else {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
        }

        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            button.setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }

        if (unifiedNativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        }

//        if (unifiedNativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(unifiedNativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(unifiedNativeAd);

        viewGroup.removeAllViews();
        viewGroup.addView(adView);

    }

    public void inFlat_admobNative400DPAds(NativeAd unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        viewGroup.setVisibility(View.VISIBLE);


        NativeAdView adView = (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.native_ad_main, null);


        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
//        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

//        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
//            @Override
//            public void onChildViewAdded(View parent, View child) {
//                if (child instanceof ImageView) {
//                    ImageView imageView = (ImageView) child;
//                    imageView.setAdjustViewBounds(true);
//                }
//            }
//
//            @Override
//            public void onChildViewRemoved(View parent, View child) {}
//        });


        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        TextView button;
        button = adView.findViewById(R.id.ad_call_to_action);

        if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
            (button.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
        }
        NativeAdView nativeAdView;
        nativeAdView = adView.findViewById(R.id.ad_view);
        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
        } else {
            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
        }

        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            button.setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }

        if (unifiedNativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        }

//        if (unifiedNativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(unifiedNativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(unifiedNativeAd);

        viewGroup.removeAllViews();
        viewGroup.addView(adView);

    }


    public void _showCacheBigNativeAd(Activity activity, final ViewGroup viewGroup) {
        inFlat_admobNativeAds(DevSpy_Utility._nativeAdBig, viewGroup, activity);
    }

    public void _showCacheSmallNativeAd(Activity activity, final ViewGroup viewGroup) {
        inFlat_admobNativeSmallAds(DevSpy_Utility._nativeAdSmall, viewGroup, activity);
    }

    public void _showCacheNative400DpAd(Activity activity, final ViewGroup viewGroup) {
        inFlat_admobNative400DPAds(DevSpy_Utility._nativeAdBig400, viewGroup, activity);
    }


    public NativeAd CacheNativeBig() {
        if (SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            return null;
        } else {
            return DevSpy_Utility._nativeAdBig;
        }
    }

    public NativeAd CacheNativeSmall() {
        if (SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            return null;
        } else {
            return DevSpy_Utility._nativeAdSmall;
        }
    }

    public NativeAd CacheNative400Dp() {
        if (SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            return null;
        } else {
            if (DevSpy_Utility._nativeAdBig400 != null) {
                return DevSpy_Utility._nativeAdBig400;
            } else {
                return null;
            }
        }
    }


    /**
     * AppLovin Native Ads
     *
     * @param activity
     * @param viewGroup
     */
    public void showAppLovinNativeAd(Activity activity, final ViewGroup viewGroup) {
        Log.e("AAAAAAAA", "onNativeAdLoaded");
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL);
            if (!adID.equals("")) {
                nativeAdLoader = new MaxNativeAdLoader(SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL), activity);
//        nativeAdLoader = new MaxNativeAdLoader("10507cc3ddc3a04d", activity);
                nativeAdLoader.setRevenueListener(new MaxAdRevenueListener() {
                    @Override
                    public void onAdRevenuePaid(MaxAd ad) {

                    }
                });
                nativeAdLoader.loadAd();
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);
                        Log.e("AAAAAAAA", "onNativeAdLoaded");
                        DevSpy_Utility.maxNativeAdView = maxNativeAdView;
                        inFlat_AppLovinbNativeAds(maxNativeAdView, viewGroup, activity);
                        nativeAdLoader.destroy();
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);
                        Log.e("AAAAAAAA", "onNativeAdLoadFailed");
                        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                            setQurekaNativeBigAds(activity, viewGroup);
                        }
                    }
                });
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                    setQurekaNativeBigAds(activity, viewGroup);
                }
            }
        }
    }

    public void showAppLovinNativeAd100DP(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL);
            if (!adID.equals("")) {
                nativeAdLoader = new MaxNativeAdLoader(SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL), activity);
//        nativeAdLoader = new MaxNativeAdLoader("10507cc3ddc3a04d", activity);
                nativeAdLoader.setRevenueListener(new MaxAdRevenueListener() {
                    @Override
                    public void onAdRevenuePaid(MaxAd ad) {

                    }
                });
                nativeAdLoader.loadAd();
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);
                        DevSpy_Utility.maxNativeAdView = maxNativeAdView;
                        inFlat_AppLovinbNativeAds100DP(maxNativeAdView, viewGroup, activity);
                        nativeAdLoader.destroy();
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);

                    }
                });
            }
        }
    }

    public void showAppLovinNativeAd400DP(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL);
            if (!adID.equals("")) {
                nativeAdLoader = new MaxNativeAdLoader(SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL), activity);
//        nativeAdLoader = new MaxNativeAdLoader("10507cc3ddc3a04d", activity);
                nativeAdLoader.setRevenueListener(new MaxAdRevenueListener() {
                    @Override
                    public void onAdRevenuePaid(MaxAd ad) {

                    }
                });
                nativeAdLoader.loadAd();
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);
                        DevSpy_Utility.maxNativeAdView = maxNativeAdView;
                        inFlat_AppLovinbNativeAds400DP(maxNativeAdView, viewGroup, activity);
                        nativeAdLoader.destroy();
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);
                        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                            setQurekaNativeBig400DPAds(activity, viewGroup);
                        }
                    }
                });
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                    setQurekaNativeBig400DPAds(activity, viewGroup);
                }
            }
        }
    }

    public void showAppLovinSmallNativeAd(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL);
            if (!adID.equals("")) {
                nativeAdLoaderSmall = new MaxNativeAdLoader(SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL), activity);
                nativeAdLoaderSmall.setRevenueListener(new MaxAdRevenueListener() {
                    @Override
                    public void onAdRevenuePaid(MaxAd ad) {

                    }
                });
                nativeAdLoaderSmall.loadAd();
                nativeAdLoaderSmall.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);
                        DevSpy_Utility.maxNativeAdVieSmall = maxNativeAdView;
                        inFlat_AppLovinbSmallNativeAds(maxNativeAdView, viewGroup, activity);
                        nativeAdLoaderSmall.destroy();
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);
                        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                            setQurekaNativeSmallAds(activity, viewGroup);
                        }
                    }
                });
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                    setQurekaNativeSmallAds(activity, viewGroup);
                }
            }
        }
    }

    public void showAppLovinNativeBannerAd(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL);
            if (!adID.equals("")) {
                nativeAdLoaderNativeBanner = new MaxNativeAdLoader(SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL), activity);
                nativeAdLoaderNativeBanner.setRevenueListener(new MaxAdRevenueListener() {
                    @Override
                    public void onAdRevenuePaid(MaxAd ad) {

                    }
                });
                nativeAdLoaderNativeBanner.loadAd();
                nativeAdLoaderNativeBanner.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);
                        DevSpy_Utility.maxNativeAdVieNativeBanner = maxNativeAdView;
                        inFlat_AppLovinbNativeBannerAds(maxNativeAdView, viewGroup, activity);
                        nativeAdLoaderNativeBanner.destroy();
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);
                        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                            setQurekaNativeBannerAds(activity, viewGroup);
                        }
                    }
                });
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("1")) {
                    setQurekaNativeBannerAds(activity, viewGroup);
                }
            }
        }
    }


    public void inFlat_AppLovinbNativeAds(MaxNativeAdView unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.z_admob_native_new)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();


        MaxNativeAdView maxNativeAdView = new MaxNativeAdView(binder, context);

        nativeAdLoader.loadAd(maxNativeAdView);


//        NativeAdView nativeAdView;
//        nativeAdView = viewGroup.findViewById(R.id.ad_view);
//        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
//            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
//        } else {
//            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
//        }

        viewGroup.removeAllViews();
        viewGroup.addView(unifiedNativeAd);

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        Button button = viewGroup.findViewById(R.id.ad_call_to_action);
        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

    }

    public void inFlat_AppLovinbNativeAds100DP(MaxNativeAdView unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.z_admob_native_new_2)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();


        MaxNativeAdView maxNativeAdView = new MaxNativeAdView(binder, context);

        nativeAdLoader.loadAd(maxNativeAdView);


//        NativeAdView nativeAdView;
//        nativeAdView = viewGroup.findViewById(R.id.ad_view);
//        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
//            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
//        } else {
//            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
//        }

        viewGroup.removeAllViews();
        viewGroup.addView(unifiedNativeAd);

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        Button button = viewGroup.findViewById(R.id.ad_call_to_action);
        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

    }

    public void inFlat_AppLovinbNativeAds400DP(MaxNativeAdView unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.native_ad_main)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();


        MaxNativeAdView maxNativeAdView = new MaxNativeAdView(binder, context);

        nativeAdLoader.loadAd(maxNativeAdView);


//        NativeAdView nativeAdView;
//        nativeAdView = viewGroup.findViewById(R.id.ad_view);
//        if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
//            nativeAdView.setBackgroundResource(R.drawable.z_ad_border_tra);
//        } else {
//            nativeAdView.setBackgroundResource(R.drawable.z_ad_border);
//        }

        viewGroup.removeAllViews();
        viewGroup.addView(unifiedNativeAd);

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        Button button = viewGroup.findViewById(R.id.ad_call_to_action);
        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }

    }


    public void inFlat_AppLovinbSmallNativeAds(MaxNativeAdView unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.z_new_native_small_new)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();


        MaxNativeAdView maxNativeAdView = new MaxNativeAdView(binder, context);

        nativeAdLoaderSmall.loadAd(maxNativeAdView);

        viewGroup.removeAllViews();
        viewGroup.addView(unifiedNativeAd);

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        Button button = viewGroup.findViewById(R.id.ad_call_to_action);
        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }
    }

    public void inFlat_AppLovinbNativeBannerAds(MaxNativeAdView unifiedNativeAd, ViewGroup viewGroup, Activity context) {

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.z_admob_native_banner_layout_1)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();


        MaxNativeAdView maxNativeAdView = new MaxNativeAdView(binder, context);

        nativeAdLoaderNativeBanner.loadAd(maxNativeAdView);

//        if (loadedNativeAd != null) {
//            nativeAdLoader.destroy(loadedNativeAd);
//        }
//
//        // Save ad for cleanup.
//        loadedNativeAd = maxAd;

        viewGroup.removeAllViews();
        viewGroup.addView(unifiedNativeAd);

        String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");

        Button button = viewGroup.findViewById(R.id.ad_call_to_action);
        if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(1000)
//                    .repeat(10000)
//                    .playOn(button);
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            button.startAnimation(loadAnimation);
        }
    }


    public MaxNativeAdView CacheAppLovinNative() {
        return DevSpy_Utility.maxNativeAdView;
    }

    public MaxNativeAdView CacheAppLovinNativeSmall() {
        return DevSpy_Utility.maxNativeAdVieSmall;
    }

    public void _showCacheAppLovinNativeAd(Activity activity, final ViewGroup viewGroup) {
        inFlat_AppLovinbNativeAds(DevSpy_Utility.maxNativeAdView, viewGroup, activity);
    }

    public void _showCacheAppLovinNativeSmallAd(Activity activity, final ViewGroup viewGroup) {
        inFlat_AppLovinbSmallNativeAds(DevSpy_Utility.maxNativeAdVieSmall, viewGroup, activity);
    }


    public DevSpy_CustomAdModel ESCustomAdModel;

    public void setQurekaNativeBigAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            View layout2 = LayoutInflater.from(activity).inflate(R.layout.z_native_qureka_layout, viewGroup, false);

            ImageView ad_media = layout2.findViewById(R.id.ad_media);
            ImageView ad_app_icon = layout2.findViewById(R.id.ad_app_icon);
            TextView ad_headline = layout2.findViewById(R.id.ad_headline);
            TextView ad_body = layout2.findViewById(R.id.ad_body);
            RelativeLayout ad_view = layout2.findViewById(R.id.ad_view);
            Button ad_call_to_action = layout2.findViewById(R.id.ad_call_to_action);
            if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
                (ad_call_to_action.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
            }
            ESCustomAdModel = DevSpy_Admob_Full_AD_New.getInstance().getMyCustomAd("Interstitial");

//          Glide.with(activity).load(this.ESCustomAdModel.getApp_banner()).into(ad_media);
            ad_headline.setText(ESCustomAdModel.getApp_name());
            ad_body.setText(ESCustomAdModel.getApp_shortDecription());


            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
                ad_view.setBackgroundResource(R.drawable.z_ad_border_tra);
            } else {
                ad_view.setBackgroundResource(R.drawable.z_ad_border);
            }
            Random r = new Random();
            int random = r.nextInt(2 - 0) + 0;
            if (random == 1) {
                ad_call_to_action.setText("Play Game");
            } else {
                ad_call_to_action.setText("Play Now");
            }

            String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");
            if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
                Animation loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.z_btn_press);
                ad_call_to_action.startAnimation(loadAnimation);
            }

            Random r1 = new Random();
            int qurekaImage = r1.nextInt(4 - 0) + 0;
            if (qurekaImage == 1) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button1)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_1);
            } else if (qurekaImage == 2) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button3)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_2);
            } else if (qurekaImage == 3) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button6)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_3);
            } else {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button9)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_4);
            }

            ad_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DevSpy_Utility.OpenCustomQurekaBrowser(activity);
                }
            });

            viewGroup.removeAllViews();
            viewGroup.addView(layout2);
        }
    }

    public void setQurekaNativeSmallAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            View layout2 = LayoutInflater.from(activity).inflate(R.layout.z_native_qureka_small_layout, viewGroup, false);

            ImageView ad_media = layout2.findViewById(R.id.ad_media);
            ImageView ad_app_icon = layout2.findViewById(R.id.ad_app_icon);
            TextView ad_headline = layout2.findViewById(R.id.ad_headline);
            TextView ad_body = layout2.findViewById(R.id.ad_body);
            RelativeLayout ad_view = layout2.findViewById(R.id.ad_view);
            Button ad_call_to_action = layout2.findViewById(R.id.ad_call_to_action);
            if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
                (ad_call_to_action.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
            }
            ESCustomAdModel = DevSpy_Admob_Full_AD_New.getInstance().getMyCustomAd("Interstitial");


//        Glide.with(activity).load(this.ESCustomAdModel.getApp_banner()).into(ad_media);
            ad_headline.setText(ESCustomAdModel.getApp_name());
            ad_body.setText(ESCustomAdModel.getApp_shortDecription());


            Random r = new Random();
            int random = r.nextInt(2 - 0) + 0;
            if (random == 1) {
                ad_call_to_action.setText("Play Game");
            } else {
                ad_call_to_action.setText("Play Now");
            }

            String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");
            if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
                Animation loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.z_btn_press);
                ad_call_to_action.startAnimation(loadAnimation);
            }

            Random r1 = new Random();
            int qurekaImage = r1.nextInt(4 - 0) + 0;
            if (qurekaImage == 1) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button1)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_1);
            } else if (qurekaImage == 2) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button3)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_2);
            } else if (qurekaImage == 3) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button6)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_3);
            } else {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button9)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_4);
            }

            ad_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DevSpy_Utility.OpenCustomQurekaBrowser(activity);
                }
            });

            viewGroup.removeAllViews();
            viewGroup.addView(layout2);
        }
    }

    public void setQurekaNativeBannerAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            View layout2 = LayoutInflater.from(activity).inflate(R.layout.z_native_qureka_banner_layout, viewGroup, false);

            ImageView ad_media = layout2.findViewById(R.id.ad_media);
            ImageView ad_app_icon = layout2.findViewById(R.id.ad_app_icon);
            TextView ad_headline = layout2.findViewById(R.id.ad_headline);
            TextView ad_body = layout2.findViewById(R.id.ad_body);
            RelativeLayout ad_view = layout2.findViewById(R.id.ad_view);
            Button ad_call_to_action = layout2.findViewById(R.id.ad_call_to_action);
            if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
                (ad_call_to_action.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
            }
            ESCustomAdModel = DevSpy_Admob_Full_AD_New.getInstance().getMyCustomAd("Interstitial");

            Glide.with(activity).load(this.ESCustomAdModel.getApp_logo()).into(ad_app_icon);
            ad_headline.setText(ESCustomAdModel.getApp_name());
            ad_body.setText(ESCustomAdModel.getApp_shortDecription());


            Random r = new Random();
            int random = r.nextInt(2 - 0) + 0;
            if (random == 1) {
                ad_call_to_action.setText("Play Game");
            } else {
                ad_call_to_action.setText("Play Now");
            }

            String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");
            if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
                Animation loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.z_btn_press);
                ad_call_to_action.startAnimation(loadAnimation);
            }

            Random r1 = new Random();
            int qurekaImage = r1.nextInt(4 - 0) + 0;
            if (qurekaImage == 1) {
//            Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button1)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_1);
            } else if (qurekaImage == 2) {
//            Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button3)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_2);
            } else if (qurekaImage == 3) {
//            Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button6)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_3);
            } else {
//            Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button9)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_4);
            }

            ad_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DevSpy_Utility.OpenCustomQurekaBrowser(activity);
                }
            });

            viewGroup.removeAllViews();
            viewGroup.addView(layout2);
        }
    }


    public void setQurekaNativeBig400DPAds(Activity activity, final ViewGroup viewGroup) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            View layout2 = LayoutInflater.from(activity).inflate(R.layout.z_native_qureka_layout_2, viewGroup, false);

            ImageView ad_media = layout2.findViewById(R.id.ad_media);
            ImageView ad_app_icon = layout2.findViewById(R.id.ad_app_icon);
            TextView ad_headline = layout2.findViewById(R.id.ad_headline);
            TextView ad_body = layout2.findViewById(R.id.ad_body);
            RelativeLayout ad_view = layout2.findViewById(R.id.ad_view);
            Button ad_call_to_action = layout2.findViewById(R.id.ad_call_to_action);
            if (!SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("") && !SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "").equals("#ffffff")) {
                (ad_call_to_action.getBackground()).setColorFilter(Color.parseColor(SharePrefUtils.getString(Constant_ad.NATIVE_COLOR, "")), PorterDuff.Mode.SRC_IN);
            }
            ESCustomAdModel = DevSpy_Admob_Full_AD_New.getInstance().getMyCustomAd("Interstitial");

//        Glide.with(activity).load(this.ESCustomAdModel.getApp_banner()).into(ad_media);
            ad_headline.setText(ESCustomAdModel.getApp_name());
            ad_body.setText(ESCustomAdModel.getApp_shortDecription());

            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 1) {
                ad_view.setBackgroundResource(R.drawable.z_ad_border_tra);
            } else {
                ad_view.setBackgroundResource(R.drawable.z_ad_border);
            }
            Random r = new Random();
            int random = r.nextInt(2 - 0) + 0;
            if (random == 1) {
                ad_call_to_action.setText("Play Game");
            } else {
                ad_call_to_action.setText("Play Now");
            }

            String blink = SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0");
            if (blink.equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
                Animation loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.z_btn_press);
                ad_call_to_action.startAnimation(loadAnimation);
            }

            Random r1 = new Random();
            int qurekaImage = r1.nextInt(4 - 0) + 0;
            if (qurekaImage == 1) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button1)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_1);
            } else if (qurekaImage == 2) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button3)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_2);
            } else if (qurekaImage == 3) {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button6)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_3);
            } else {
                Glide.with(activity).load(activity.getResources().getDrawable(R.drawable.qureka_button9)).into(ad_app_icon);
                ad_media.setImageResource(R.drawable.z_qureka_lite_small_native_4);
            }

            ad_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DevSpy_Utility.OpenCustomQurekaBrowser(activity);
                }
            });

            viewGroup.removeAllViews();
            viewGroup.addView(layout2);
        }
    }
}
