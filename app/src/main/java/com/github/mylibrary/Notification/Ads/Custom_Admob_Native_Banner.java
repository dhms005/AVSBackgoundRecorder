package com.github.mylibrary.Notification.Ads;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.ds.audio.video.screen.backgroundrecorder.R;


public class Custom_Admob_Native_Banner {

    private MyAdViewAdListener mMyAdViewAdListener;

    NativeAd adViewCache;
    // private NativeAdLayout nativeAdLayout;


    public interface MyAdViewAdListener {
        void onAdClosed();

        void onAdFailedToLoad();

        void onAdLeftApplication();

        void onAdLoaded();

        void onAdOpened();
    }


    public void setMyNativeAdListener(MyAdViewAdListener myNativeAdListener) {
        this.mMyAdViewAdListener = myNativeAdListener;
    }

    public void loadNativeAd(final Activity context, final LinearLayout linearAdLayout, final String type) {

        AdLoader adLoader = new AdLoader.Builder(context, SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        // Show the ad.

                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdLoaded();
                        }

                        LayoutInflater inflater = LayoutInflater.from(context);

                        NativeAdView adView;

                        if (context.isDestroyed()) {
                            NativeAd.destroy();
                            return;
                        }

                        if (type.equals("1")) {
                            adView = (NativeAdView) inflater.inflate(R.layout.z_admob_native_banner_layout_1, linearAdLayout, false);
                        } else {
                            adView = (NativeAdView) inflater.inflate(R.layout.z_admob_native_banner_layout_1, linearAdLayout, false);
                        }

                        populateUnifiedNativeAdView(NativeAd, adView, type, context);
                        linearAdLayout.removeAllViews();
                        linearAdLayout.addView(adView);
                        adViewCache = NativeAd;
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdFailedToLoad();
                        }
//                        Log.e("getErrror", "Native Load" + adError.getMessage());

                    }

                    @Override
                    public void onAdOpened() {

                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdOpened();
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdLoaded();
                        }
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdClosed();
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

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView, String type, Context context) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);

        if (type.equals("1")) {
            mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        }
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (SharePrefUtils.getString(Constant_ad.AD_BUTTON_ANIM, "0").equals("1") && SharePrefUtils.getInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1) == 1) {
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.z_btn_press);
            adView.getCallToActionView().startAnimation(loadAnimation);
        }


        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {

            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            if (type.equals("2") || type.equals("7") || type.equals("8") || type.equals("9")) {
                adView.getBodyView().setVisibility(View.VISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {

            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());

            if (type.equals("2") || type.equals("3") || type.equals("7") || type.equals("8") || type.equals("9") || type.equals("20")) {
                adView.getPriceView().setVisibility(View.GONE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            if (type.equals("2") || type.equals("3") || type.equals("7") || type.equals("8") || type.equals("9") || type.equals("20")) {
                adView.getStoreView().setVisibility(View.GONE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());

            if (type.equals("0") || type.equals("4") || type.equals("3") || type.equals("2") || type.equals("7") || type.equals("8") || type.equals("9") || type.equals("20")) {
                adView.getStarRatingView().setVisibility(View.GONE);
            } else {
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getAdvertiser() == null) {

            if (type.equals("2") || type.equals("3") || type.equals("4") || type.equals("7") || type.equals("8") || type.equals("9") || type.equals("20")) {
                adView.getAdvertiserView().setVisibility(View.GONE);
            } else {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            }

        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());

            if (type.equals("2") || type.equals("7") || type.equals("8") || type.equals("9") || type.equals("20")) {
                adView.getAdvertiserView().setVisibility(View.GONE);
            } else if (type.equals("4")) {
                adView.getAdvertiserView().setVisibility(View.GONE);
            } else {
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.

    }

    public NativeAd CheckAdCache() {
        if (SharePrefUtils.getString(Constant_ad.AD_NATIVE_PRE_LOAD, "1").equals("1")) {
            return adViewCache;
        } else {
            return null;
        }
    }

    public void loadNativeAdFromCache(final Activity context, final LinearLayout linearAdLayout, final String type) {
        LayoutInflater inflater = LayoutInflater.from(context);

        NativeAdView adView;

        if (type.equals("1")) {
            adView = (NativeAdView) inflater.inflate(R.layout.z_admob_native_banner_layout_1, linearAdLayout, false);
        } else {
            adView = (NativeAdView) inflater.inflate(R.layout.z_admob_native_banner_layout_1, linearAdLayout, false);
        }

        populateUnifiedNativeAdView(adViewCache, adView, type, context);
        linearAdLayout.removeAllViews();
        linearAdLayout.addView(adView);


        loadNewNativeAd(context);
    }

    public void loadNewNativeAd(final Activity context) {

        AdLoader adLoader = new AdLoader.Builder(context, SharePrefUtils.getString(Constant_ad.GOOGLE_NATIVE, Custom_Ad_Key.KEY_ADMOB_NATIVE))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        // Show the ad.
                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdLoaded();
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
                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdFailedToLoad();
                        }
                    }

                    @Override
                    public void onAdOpened() {


                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdOpened();
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdLoaded();
                        }
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        if (Custom_Admob_Native_Banner.this.mMyAdViewAdListener != null) {
                            Custom_Admob_Native_Banner.this.mMyAdViewAdListener.onAdClosed();
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

}
