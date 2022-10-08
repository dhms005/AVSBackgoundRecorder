package com.github.mylibrary.Notification.Ads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

public class Custom_Fb_Native_Banner_Ad {

    private MyAdViewAdListener mMyAdViewAdListener;
    private LinearLayout adView;
    private NativeBannerAd nativeBannerAd;
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


        nativeBannerAd = new NativeBannerAd(context, SharePrefUtils.getString(Constant_ad.FACEBOOK_NATIVE_BANNER, Custom_Ad_Key.KEY_FACEBOOK_NATIVE_BANNER));

        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (Custom_Fb_Native_Banner_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Native_Banner_Ad.this.mMyAdViewAdListener.onAdFailedToLoad();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeBannerAd, context, linearAdLayout, type);
                if (Custom_Fb_Native_Banner_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Native_Banner_Ad.this.mMyAdViewAdListener.onAdLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (Custom_Fb_Native_Banner_Ad.this.mMyAdViewAdListener != null) {
                    Custom_Fb_Native_Banner_Ad.this.mMyAdViewAdListener.onAdOpened();
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        nativeBannerAd.loadAd(
                nativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());

    }


    private void inflateAd(NativeBannerAd nativeBannerAd, Context context, LinearLayout linearAdLayout, String type) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.

        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);

        if (type.equals("1")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_banner_ad_layout_1, nativeAdLayout, false);
        } else if (type.equals("2")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_banner_ad_layout_2, nativeAdLayout, false);
        }else if (type.equals("3")) {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_banner_ad_layout_3, nativeAdLayout, false);
        } else {
            adView = (LinearLayout) inflater.inflate(R.layout.z_fb_native_banner_ad_layout_1, nativeAdLayout, false);
        }

        nativeAdLayout.addView(adView);
        linearAdLayout.addView(nativeAdLayout);
        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView native_ad_body = adView.findViewById(R.id.native_ad_body);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        native_ad_body.setText(nativeBannerAd.getAdBodyText());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }
}
