package com.ds.audio.video.screen.backgroundrecorder.Extra_camaradetctor.DevSpy_camera_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class DevSpy_Camere_Camere_Test_Sensore_Activity extends AppCompatActivity {
    public static String textdetails;
    public static String title;
    LinearLayout accelemotor;
    LinearLayout compass;
    LinearLayout gyroscope;
    LinearLayout light;
    LinearLayout megentometer;
    LinearLayout proximity;
    LinearLayout mHeaderImage;
    LinearLayout nativeAdContainer2;
    Custom_Banner_Ad banner_ad;
    private String mediation;
    LinearLayout mAdView;
    LinearLayout ll_static_ad_inner;
    LinearLayout mNativeBannerAd;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.devspy_camere_test_sensore_activity);


        mNativeAdNew();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button9)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_Camere_Camere_Test_Sensore_Activity.this);
            }
        });
        this.light = (LinearLayout) findViewById(R.id.light);
        this.light.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Camere_Camere_Test_Sensore_Activity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_Camere_Camere_Test_Sensore_Activity.title = "Light Sensor";
                        DevSpy_Camere_Camere_Test_Sensore_Activity.textdetails = DevSpy_Camere_Camere_Test_Sensore_Activity.this.getResources().getString(R.string.Light);
                        DevSpy_Camere_Camere_Test_Sensore_Activity.this.startActivity(new Intent(DevSpy_Camere_Camere_Test_Sensore_Activity.this, DevSpy_Camere_Sensore_Detail_Activity.class));
                    }
                });
            }
        });
        this.megentometer = (LinearLayout) findViewById(R.id.megentometer);
        this.megentometer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Camere_Camere_Test_Sensore_Activity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_Camere_Camere_Test_Sensore_Activity.title = "Magnetometer Sensor";
                        DevSpy_Camere_Camere_Test_Sensore_Activity.textdetails = DevSpy_Camere_Camere_Test_Sensore_Activity.this.getResources().getString(R.string.Magnetometer);
                        DevSpy_Camere_Camere_Test_Sensore_Activity.this.startActivity(new Intent(DevSpy_Camere_Camere_Test_Sensore_Activity.this, DevSpy_Camere_Sensore_Detail_Activity.class));
                    }
                });
            }
        });
        this.accelemotor = (LinearLayout) findViewById(R.id.accelemotor);
        this.accelemotor.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Camere_Camere_Test_Sensore_Activity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_Camere_Camere_Test_Sensore_Activity.title = "Accelerometer Sensor";
                        DevSpy_Camere_Camere_Test_Sensore_Activity.textdetails = DevSpy_Camere_Camere_Test_Sensore_Activity.this.getResources().getString(R.string.Accelerometer);
                        DevSpy_Camere_Camere_Test_Sensore_Activity.this.startActivity(new Intent(DevSpy_Camere_Camere_Test_Sensore_Activity.this, DevSpy_Camere_Sensore_Detail_Activity.class));
                    }
                });
            }
        });
        this.proximity = (LinearLayout) findViewById(R.id.proximity);
        this.proximity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Camere_Camere_Test_Sensore_Activity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_Camere_Camere_Test_Sensore_Activity.title = "Proximity Sensor";
                        DevSpy_Camere_Camere_Test_Sensore_Activity.textdetails = DevSpy_Camere_Camere_Test_Sensore_Activity.this.getResources().getString(R.string.Proximity);
                        DevSpy_Camere_Camere_Test_Sensore_Activity.this.startActivity(new Intent(DevSpy_Camere_Camere_Test_Sensore_Activity.this, DevSpy_Camere_Sensore_Detail_Activity.class));
                    }
                });
            }
        });

        this.compass = (LinearLayout) findViewById(R.id.compass);
        this.compass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Camere_Camere_Test_Sensore_Activity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_Camere_Camere_Test_Sensore_Activity.title = "Compass Sensor";
                        DevSpy_Camere_Camere_Test_Sensore_Activity.textdetails = DevSpy_Camere_Camere_Test_Sensore_Activity.this.getResources().getString(R.string.Compass);
                        DevSpy_Camere_Camere_Test_Sensore_Activity.this.startActivity(new Intent(DevSpy_Camere_Camere_Test_Sensore_Activity.this, DevSpy_Camere_Sensore_Detail_Activity.class));
                    }
                });
            }
        });

        this.gyroscope = (LinearLayout) findViewById(R.id.gyroscope);
        this.gyroscope.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_Camere_Camere_Test_Sensore_Activity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_Camere_Camere_Test_Sensore_Activity.title = "Gyroscope Sensor";
                        DevSpy_Camere_Camere_Test_Sensore_Activity.textdetails = DevSpy_Camere_Camere_Test_Sensore_Activity.this.getResources().getString(R.string.Gyroscope);
                        DevSpy_Camere_Camere_Test_Sensore_Activity.this.startActivity(new Intent(DevSpy_Camere_Camere_Test_Sensore_Activity.this, DevSpy_Camere_Sensore_Detail_Activity.class));
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {

        DevSpy_Admob_Full_AD_New.getInstance().showInterBack(this, new DevSpy_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
    }


    @Override
    protected void onPause() {
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
        super.onPause();
    }

    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_11);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
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
}
