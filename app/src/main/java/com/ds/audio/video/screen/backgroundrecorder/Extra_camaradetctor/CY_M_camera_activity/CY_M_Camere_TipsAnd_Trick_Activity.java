package com.ds.audio.video.screen.backgroundrecorder.Extra_camaradetctor.CY_M_camera_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class CY_M_Camere_TipsAnd_Trick_Activity extends AppCompatActivity {
    public static String F;
    public static String footertitle;
    public static String title;
    LinearLayout Outside;
    LinearLayout bathroom;
    LinearLayout bedroom;
    LinearLayout changignroom;
    ImageView mHeaderImage;
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
        setContentView(R.layout.cy_m_camere_tipsand_trick_activity);


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
                CY_M_Utility.OpenCustomQurekaBrowser(CY_M_Camere_TipsAnd_Trick_Activity.this);
            }
        });
        this.bedroom = (LinearLayout) findViewById(R.id.bedroom);
        this.bedroom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(CY_M_Camere_TipsAnd_Trick_Activity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        CY_M_Camere_TipsAnd_Trick_Activity.title = "Bedroom";
                        CY_M_Camere_TipsAnd_Trick_Activity.F = "Now A days Hotel Room Are Famous For Spy Camera\n\nScan Every part Of The Room When You Entered By The Hidden Camera Detector\n\n1. Side Table - Precaution : Apply sticker/sellotape over the view able part of the detector.\n\n2.Air Condition Search in Room - Precaution : If possible use fans and turn of the AC. Turn rotating bald fans OFF.\n\n3.Television behind - Precaution : Turn off main power supply. Keep objects front of the lens looking pat of the TV.\n\n4.Night Lamp - Precaution : Keep it off or change its direction.\n\n5.Flower Pot - Change position to least view able area\n\n6.Coffee Maker - Precaution : Unplug it and put it inside cupboard\n\nFinally do not forget to turn lights off";
                        CY_M_Camere_TipsAnd_Trick_Activity.footertitle = "Possblity Spy Camera In Hotel Room";
                        CY_M_Camere_TipsAnd_Trick_Activity.this.startActivity(new Intent(CY_M_Camere_TipsAnd_Trick_Activity.this, CY_M_Camere_TipaTricke_Detail_Activity.class));

                    }
                });
            }
        });
        this.bathroom = (LinearLayout) findViewById(R.id.bathroom);
        this.bathroom.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(CY_M_Camere_TipsAnd_Trick_Activity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        CY_M_Camere_TipsAnd_Trick_Activity.title = "Bathroom";
                        CY_M_Camere_TipsAnd_Trick_Activity.F = "Scan for below devices(for manual scan, scan for lens looking part over below devices) \n\n1. Water Heater - Precaution - Turn off heater and then bath.\n\n2. Mirror - Precaution : Touch the mirror. Do you feel any gap between your finger and its reflection? If No, there could be camera behind the mirror glass.\n\n3. Ceiling/Smoke Detector - Precaution : Apply sticker/Sellotape over the view able part of the detector.\n\n4. Lamps or Bulbs - Precaution : Can't be much, Use curtains whenever possible\n\nIf you find anything suspicious, complain the authority";
                        CY_M_Camere_TipsAnd_Trick_Activity.footertitle = "Possblity Spy Camera In Washroom";
                        CY_M_Camere_TipsAnd_Trick_Activity.this.startActivity(new Intent(CY_M_Camere_TipsAnd_Trick_Activity.this, CY_M_Camere_TipaTricke_Detail_Activity.class));

                    }
                });
            }
        });
        this.changignroom = (LinearLayout) findViewById(R.id.changignroom);
        this.changignroom.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(CY_M_Camere_TipsAnd_Trick_Activity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        CY_M_Camere_TipsAnd_Trick_Activity.title = "Changing room";
                        CY_M_Camere_TipsAnd_Trick_Activity.F = "Scan for below devices(for manual scan, scan for lens looking part over below devices) \n\n1. Mirror - Precaution : Touch the mirror. Do you feel any gap between your finger and its reflection? If No, there could be camera behind the mirror glass.\n\n2. Hanger - Check the lens looking aperture for hanger especially screw looking objects. Put cloths covering all the screws.\n\n3. Ceiling/Smoke Detector - Apply sticker/sellotape over the view able part of the detector.\n\nIf you find anything suspicious, complain the authority\n\nSome easy steps can save you from Spy camera.";
                        CY_M_Camere_TipsAnd_Trick_Activity.footertitle = "Possblity Spy Camera In Changing Room";
                        CY_M_Camere_TipsAnd_Trick_Activity.this.startActivity(new Intent(CY_M_Camere_TipsAnd_Trick_Activity.this, CY_M_Camere_TipaTricke_Detail_Activity.class));

                    }
                });
            }
        });
        this.Outside = (LinearLayout) findViewById(R.id.Outside);
        this.Outside.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                CY_M_Admob_Full_AD_New.getInstance().showInter(CY_M_Camere_TipsAnd_Trick_Activity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        CY_M_Camere_TipsAnd_Trick_Activity.title = "Outside";
                        CY_M_Camere_TipsAnd_Trick_Activity.F = "In outside world, you are not supposed to hide your identity in official places so just be cautious and behave accordingly.\n\nThe Security Areas the CCTV Camera fixed For Only Security Reason not For Spy Purpose So Don't Be Worry About It.\nThe Shopping Mall and Like The Army Camp and School Hospital, Library etc \n\nSome easy steps can save you from uneasy situations.";
                        CY_M_Camere_TipsAnd_Trick_Activity.footertitle = "CCTV Camera Outside";
                        CY_M_Camere_TipsAnd_Trick_Activity.this.startActivity(new Intent(CY_M_Camere_TipsAnd_Trick_Activity.this, CY_M_Camere_TipaTricke_Detail_Activity.class));

                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {

        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
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
