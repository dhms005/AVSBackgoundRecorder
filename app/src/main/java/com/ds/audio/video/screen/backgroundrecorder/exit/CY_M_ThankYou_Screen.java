package com.ds.audio.video.screen.backgroundrecorder.exit;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_Master_SplashScreen;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Admob_Native_Ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.Custom_Fb_Native_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;


public class CY_M_ThankYou_Screen extends AppCompatActivity {

    String mediation;
    LinearLayout nativeAdContainer2;
    Button ok;
    LinearLayout mAdView;
    private Custom_Admob_Native_Ad full_Native;
    private Custom_Fb_Native_Ad full_Native1;
    private int back_count;
    LinearLayout mNativeBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.cy_m_activity_thank_you__screen);

        ok = findViewById(R.id.ok_btn);

        if (SharePrefUtils.getString(Constant_ad.AD_EXIT_NATIVE, "0").equals("0")) {

        } else {
            mNativeAdNew();
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
                Call_banner();
            } else {
                mNativeBanner();
            }
        }
        // mAdFunction1();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CY_M_Master_SplashScreen.isSplashLoading = false;
                finishAffinity();
            }
        });
    }


    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }
//        else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
//            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
//            params.height = (int) getResources().getDimension(R.dimen.native_11);
//            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
//            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
//                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
//            }
//        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNative400Dp() == null) {
                    new Custom_NativeAd_Admob().showNative400DpAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheNative400DpAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNative400Dp() == null) {
                    new Custom_NativeAd_Admob().showNative400DpAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheNative400DpAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            }
        }
    }

    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);

        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_TYPE, "0").equals("0")) {
                if (custom_banner_ad.CheckAdCache() != null) {
                    custom_banner_ad.loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_adpative_banner_Ad(this, mAdView);
                }
            }
        } else {
            custom_banner_ad.reload_applovin_banner_ad(this, mAdView);
        }

    }

    private void mNativeBanner() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1") || SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_banner_1);
            findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.mNativeBannerAd).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            new Custom_NativeAd_Admob().showNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        } else {
            new Custom_NativeAd_Admob().showAppLovinNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        }

    }


    @Override
    public void onBackPressed() {

        CY_M_Admob_Full_AD_New.getInstance().showInterBack(CY_M_ThankYou_Screen.this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
    }


    @Override
    protected void onPause() {
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
        super.onPause();
    }

}