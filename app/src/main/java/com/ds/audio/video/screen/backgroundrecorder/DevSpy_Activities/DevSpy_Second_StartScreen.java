package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_LocaleHelper;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_PrivacyPolicyActivity;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class DevSpy_Second_StartScreen extends AppCompatActivity {

    private String mediation;
    ImageView mTextPrivacyPolicy;
    private ImageView mMoreApp, mRateApp, mShareApp, mHeaderImage;
    private LinearLayout nativeAdContainer2;
    Button img_start;
    LinearLayout mNativeBannerAd;
    int startCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.devspy_activity_second_start_screen);
        startCount = Integer.parseInt(SharePrefUtils.getString(Constant_ad.START_SCREEN_COUNT, ""));
        img_start = findViewById(R.id.img_start);

        if(DevSpy_Utility.startStringText[DevSpy_Utility.temPValue].contains("0")){
            img_start.setText(DevSpy_Utility.startStringText[DevSpy_Utility.temPValue].replace("0","'"));
        }else{
            img_start.setText(DevSpy_Utility.startStringText[DevSpy_Utility.temPValue]);
        }


        DevSpy_Utility.temPValue++;
        load_qureka();
        mNativeAdNew();

        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }


        findview();
        clickdata();
        ((ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void load_qureka() {
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.QUREKA_BTN, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button6)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_Second_StartScreen.this);
            }
        });


        LinearLayout ll_qureka_ad = findViewById(R.id.ll_qureka_ad);
        ImageView img_qureka1 = findViewById(R.id.img_qureka1);
        ImageView img_qureka2 = findViewById(R.id.img_qureka2);
        ImageView img_qureka3 = findViewById(R.id.img_qureka3);

        if (SharePrefUtils.getString(Constant_ad.QUREKA_BTN, "0").equals("0")) {
            ll_qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button1)).into(img_qureka1);
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button3)).into(img_qureka2);
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button6)).into(img_qureka3);
        img_qureka1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_Second_StartScreen.this);
            }
        });
        img_qureka2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_Second_StartScreen.this);
            }
        });
        img_qureka3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_Second_StartScreen.this);
            }
        });
    }

    private void findview() {
        mTextPrivacyPolicy = (ImageView) findViewById(R.id.text_privacy_policy);

        mMoreApp = (ImageView) findViewById(R.id.img_moreapp);
        mRateApp = (ImageView) findViewById(R.id.img_rateapp);
        mShareApp = (ImageView) findViewById(R.id.img_shareapp);
    }

    private void clickdata() {
        img_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Second_StartScreen.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (DevSpy_Utility.temPValue < startCount) {
                            Intent intent = new Intent(DevSpy_Second_StartScreen.this, DevSpy_Second_StartScreen.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DevSpy_Second_StartScreen.this, DevSpy_ActivityHomeMenu.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });


        mTextPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!DevSpy_Utility.isConnectivityAvailable(DevSpy_Second_StartScreen.this)) {
                    Toast.makeText(DevSpy_Second_StartScreen.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(DevSpy_Second_StartScreen.this, DevSpy_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
            }
        });


        mMoreApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + SharePrefUtils.getString(Constant_ad.ACCOUNT, ""))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + SharePrefUtils.getString(Constant_ad.ACCOUNT, ""))));
                }
            }
        });

        mRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        });

        mShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

                startActivity(Intent.createChooser(share, "Share link!"));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startCount = Integer.parseInt(SharePrefUtils.getString(Constant_ad.START_SCREEN_COUNT, ""));
        if (startCount == 0) {
            Intent intent = new Intent(DevSpy_Second_StartScreen.this, DevSpy_ActivityHomeMenu.class);
            startActivity(intent);
            finish();
        }
    }

    public void onBackPressed() {
        DevSpy_Utility.temPValue--;
        DevSpy_Admob_Full_AD_New.getInstance().showInterBack(this, new DevSpy_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
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
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNative400Dp() == null) {
                    new Custom_NativeAd_Admob().showNative400DpAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheNative400DpAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNative400Dp() == null) {
                    new Custom_NativeAd_Admob().showNative400DpAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheNative400DpAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
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
                    ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen.simple_banner_1);
                    findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
                    params.height = DevSpy_LocaleHelper.banner_adpative_size(this);
                    findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
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
}