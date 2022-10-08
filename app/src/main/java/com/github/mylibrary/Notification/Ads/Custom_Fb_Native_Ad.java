package com.github.mylibrary.Notification.Ads;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;

import java.util.ArrayList;
import java.util.List;

import com.ds.audio.video.screen.backgroundrecorder.R;

public class Custom_Fb_Native_Ad {

    private MyAdViewAdListener mMyAdViewAdListener;
    InterstitialAd interstitialAd;
    private LinearLayout adView;
    private NativeAd nativeAd;
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

    public void loadNativeAd(final Context context, final LinearLayout linearAdLayout, final String type) {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).

//        Log.e("ad",SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL));
        nativeAd = new NativeAd(context, SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL));

        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (Custom_Fb_Native_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Native_Ad.this.mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                linearAdLayout.removeAllViews();
                inflateAd(nativeAd, context, linearAdLayout, type);
                if (Custom_Fb_Native_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Native_Ad.this.mMyAdViewAdListener.onAdLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (Custom_Fb_Native_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Native_Ad.this.mMyAdViewAdListener.onAdOpened();
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());

        // Request an ad
       // nativeAd.loadAd();
    }

    public void loadNativeAdCache(final Context context, final LinearLayout linearAdLayout, final String type) {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).

//        Log.e("ad",SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_FULL));
        inflateAd(nativeAd, context, linearAdLayout, type);
    }

    private void inflateAd(NativeAd nativeAd, Context context, LinearLayout linearAdLayout, String type) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        if (type.equals("1")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_1, nativeAdLayout, false);
        } else if (type.equals("2")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_2_with_border_180, nativeAdLayout, false);
        }else if (type.equals("3")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_3_with_border, nativeAdLayout, false);
        }else if (type.equals("4")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_4_with_border, nativeAdLayout, false);
        }else if (type.equals("5")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_5, nativeAdLayout, false);
        }else if (type.equals("6")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_6, nativeAdLayout, false);
        }else if (type.equals("7")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_7_without_border_180, nativeAdLayout, false);
        }else if (type.equals("8")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_8_without_border, nativeAdLayout, false);
        }else if (type.equals("9")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_9_with_border, nativeAdLayout, false);
        }else  if (type.equals("21")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_21, nativeAdLayout, false);
        } else {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_ad_layout_1, nativeAdLayout, false);
        }

        if (type.equals("21")) {
            View adViewTmp = NativeAdView.render(context, nativeAd);
            adView.addView(adViewTmp, new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT));
            linearAdLayout.addView(nativeAdLayout);
            nativeAdLayout.removeAllViewsInLayout();
            nativeAdLayout.addView(adView);
        } else {
            nativeAdLayout.removeAllViewsInLayout();
            nativeAdLayout.addView(adView);
            // nativeAdLayout.addView(adView,new LinearLayout.LayoutParams(MATCH_PARENT, 800));
            linearAdLayout.addView(nativeAdLayout);
            // Add the AdOptionsView
            LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            // Create native UI using the ad metadata.
            MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            // Create a list of clickable views
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews);
        }
    }

    public NativeAd CheckAdCache(){
        return nativeAd;
    }
}
