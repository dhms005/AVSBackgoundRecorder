package com.github.mylibrary.Notification.Ads;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Keep;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

@Keep
public class Custom_Banner_Ad {

    AdView adView;
    AdManagerAdView adManagerAdView;

    private MaxAdView maxAdView;

    public void reload_fb_banner_Ad(Context context, LinearLayout adContainer) {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, SharePrefUtils.getString(Constant_ad.FACEBOOK_BANNER, Custom_Ad_Key.KEY_FACEBOOK_BANNER), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        adView.loadAd();
    }

    public void reload_fb_banner250_Ad(Context context, RelativeLayout adContainer) {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, SharePrefUtils.getString(Constant_ad.FACEBOOK_DIALOG, Custom_Ad_Key.KEY_FACEBOOK_DIALOG), com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        adContainer.addView(adView);
        adView.loadAd();
    }

    public void reload_admob_banner_Ad(final Activity context, final LinearLayout adContainer) {
        adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(SharePrefUtils.getString(Constant_ad.GOOGLE_BANNER, Custom_Ad_Key.KEY_ADMOB_BANNER));

        adContainer.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                reload_admob_banner_Ad_failedload(context, adContainer);
                // Code to be executed when an ad request fails.
                // reload_tapdaq_banner_Ad(context, adContainer);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    public void reload_admob_banner_Ad_failedload(final Activity context, final LinearLayout adContainer) {
        adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(SharePrefUtils.getString(Constant_ad.GOOGLE_BANNER2, Custom_Ad_Key.KEY_ADMOB_BANNER));

        adContainer.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {

                // Code to be executed when an ad request fails.
                // reload_tapdaq_banner_Ad(context, adContainer);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    public AdView CheckAdCache() {
        return adView;
    }


    public AdManagerAdView Adaptive_CheckAdCache() {
        return adManagerAdView;
    }


    public void loadNativeAdFromCache(final Activity context, final LinearLayout adContainer) {
//        adContainer.removeView(adContainer);
        ((ViewGroup) maxAdView.getParent()).removeView(maxAdView);
        adContainer.addView(maxAdView);
    }


    public void Adaptive_loadNativeAdFromCache(final Activity context, final LinearLayout adContainer) {
        ((ViewGroup) adManagerAdView.getParent()).removeView(adManagerAdView);
        adContainer.addView(adManagerAdView);
    }


    public void reload_admob_adpative_banner_Ad(final Activity context, final LinearLayout adContainer) {

//        Log.e("#DEBUG", "reload_admob_adpative_banner_Ad");

        adManagerAdView = new AdManagerAdView(context);
        adManagerAdView.setAdUnitId(SharePrefUtils.getString(Constant_ad.GOOGLE_BANNER, Custom_Ad_Key.KEY_ADMOB_BANNER));
        adContainer.addView(adManagerAdView);

        AdManagerAdRequest adRequest =
                (AdManagerAdRequest) new AdManagerAdRequest.Builder().build();

        AdSize adaptiveSize = getAdSize(context);

        adManagerAdView.setAdSizes(adaptiveSize, AdSize.BANNER);

        adManagerAdView.loadAd(adRequest);

        adManagerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                Log.e("#DEBUG", "onAdLoaded");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
//                ImageView ad = new ImageView(context); // Create ad view
                reload_admob_adpative_banner_Ad_failedload(context, adContainer);
                // Code to be executed when an ad request fails.
                //  reload_tapdaq_banner_Ad(context, adContainer);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    public void reload_admob_adpative_banner_Ad_failedload(final Activity context, final LinearLayout adContainer) {

//        Log.e("#DEBUG", "reload_admob_adpative_banner_Ad");

        adManagerAdView = new AdManagerAdView(context);
        adManagerAdView.setAdUnitId(SharePrefUtils.getString(Constant_ad.GOOGLE_BANNER2, Custom_Ad_Key.KEY_ADMOB_BANNER));
        adContainer.addView(adManagerAdView);

        AdManagerAdRequest adRequest =
                (AdManagerAdRequest) new AdManagerAdRequest.Builder().build();

        AdSize adaptiveSize = getAdSize(context);

        adManagerAdView.setAdSizes(adaptiveSize, AdSize.BANNER);

        adManagerAdView.loadAd(adRequest);

        adManagerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                Log.e("#DEBUG", "onAdLoaded");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                ImageView ad = new ImageView(context); // Create ad view

                // Code to be executed when an ad request fails.
                //  reload_tapdaq_banner_Ad(context, adContainer);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    public void reload_applovin_banner_ad(final Activity context, final LinearLayout adContainer) {
        String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_BANNER, Custom_Ad_Key.KEY_FACEBOOK_BANNER);
        if (!adID.equals("")) {
            maxAdView = new MaxAdView(adID, context);
//        maxAdView = new MaxAdView("1d2d4416f413635c", context);
            maxAdView.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {

                }

                @Override
                public void onAdLoaded(MaxAd ad) {

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {

                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    Log.e("@@@", "createInterstitialAd" + error.getMessage());
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    Log.e("@@@", "createInterstitialAd" + error.getMessage());
                }
            });

            // Stretch to the width of the screen for banners to be fully functional
            int width = ViewGroup.LayoutParams.MATCH_PARENT;

            // Banner height on phones and tablets is 50 and 90, respectively

            // Get the adaptive banner height.
//        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
//        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
//
//        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
//        adView.setExtraParameter( "adaptive_banner", "true" );

            int heightPx = context.getResources().getDimensionPixelSize(R.dimen.banner_height);

            maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));

            // Set background or background color for banners to be fully functional
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            adView.setBackgroundColor(getResources().getColor(R.color.black));
//        }

//        ViewGroup rootView = findViewById( android.R.id.content );
            adContainer.addView(maxAdView);

            // Load the ad
            maxAdView.loadAd();
        }
    }

    private AdSize getAdSize(Activity context) {

        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }


}
