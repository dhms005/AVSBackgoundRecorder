package com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Adapters.CY_M_CPUApplicationsScanningAdapter;
import com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Model.CY_M_Apps;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.skyfishjy.library.RippleBackground;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class CY_M_ScannerCPUActivity extends AppCompatActivity {
    private static final String TAG = "ScannerCPUActivity";
    List<CY_M_Apps> app = null;

    TextView cooledcpu;
    ImageView cpu;
    ImageView img_animation;
    ImageView ivCompltecheck;
    CY_M_CPUApplicationsScanningAdapter mAdapter;
    List<ApplicationInfo> packages;
    PackageManager pm;
    RecyclerView recyclerView;
    RelativeLayout rel;
    ImageView scanner;
    ImageView shadowCpu;

    ImageView mHeaderImage;
    LinearLayout nativeAdContainer2;
    Custom_Banner_Ad banner_ad;
    private String mediation;
    LinearLayout mAdView;
    LinearLayout ll_static_ad_inner;
    LinearLayout mNativeBannerAd;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView((int) R.layout.cy_m_activity_scanner_cpu);
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button3)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CY_M_Utility.OpenCustomQurekaBrowser(CY_M_ScannerCPUActivity.this);
            }
        });
//        mAdFunction();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        this.scanner = (ImageView) findViewById(R.id.scann);
        this.cpu = (ImageView) findViewById(R.id.cpu);
        this.cooledcpu = (TextView) findViewById(R.id.cpucooler);
        this.img_animation = (ImageView) findViewById(R.id.heart);
        this.rel = (RelativeLayout) findViewById(R.id.rel);
        this.ivCompltecheck = (ImageView) findViewById(R.id.iv_completecheck);
        this.app = new ArrayList();
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setRepeatCount(3);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        this.scanner.startAnimation(rotateAnimation);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(5000);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setFillAfter(true);
        this.img_animation.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                CY_M_ScannerCPUActivity.this.img_animation.setImageResource(0);
                CY_M_ScannerCPUActivity.this.img_animation.setBackgroundResource(0);
            }
        });
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView = recyclerView2;
        recyclerView2.setItemAnimator(new SlideInLeftAnimator());
        this.mAdapter = new CY_M_CPUApplicationsScanningAdapter(CY_M_CPUCoolerActivity.apps);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1.0f)));
        this.recyclerView.computeHorizontalScrollExtent();
        this.recyclerView.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.add("Limit Brightness Upto 80%", 0);
                }
            }, 0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.remove(0);
                    CY_M_ScannerCPUActivity.this.add("Decrease Device Performance", 1);
                }
            }, 900);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.remove(0);
                    CY_M_ScannerCPUActivity.this.add("Close All Battery Consuming Apps", 2);
                }
            }, 1800);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.remove(0);
                    CY_M_ScannerCPUActivity.this.add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 3);
                }
            }, 2700);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.remove(0);
                    CY_M_ScannerCPUActivity.this.add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 4);
                }
            }, 3700);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.remove(0);
                    CY_M_ScannerCPUActivity.this.add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 5);
                }
            }, 4400);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CY_M_ScannerCPUActivity.this.add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 6);
                    CY_M_ScannerCPUActivity.this.remove(0);
                    final RippleBackground rippleBackground = (RippleBackground) CY_M_ScannerCPUActivity.this.findViewById(R.id.content);
                    ImageView imageView = (ImageView) CY_M_ScannerCPUActivity.this.findViewById(R.id.centerImage);
                    rippleBackground.startRippleAnimation();
                    CY_M_ScannerCPUActivity.this.img_animation.setImageResource(0);
                    CY_M_ScannerCPUActivity.this.img_animation.setBackgroundResource(0);
                    CY_M_ScannerCPUActivity.this.cpu.setImageResource(R.drawable.ic_cooling_complete);
                    CY_M_ScannerCPUActivity.this.scanner.setVisibility(View.GONE);
                    CY_M_ScannerCPUActivity.this.ivCompltecheck.setImageResource(R.drawable.ic_cooling_complete_check);
                    CY_M_ScannerCPUActivity.this.ivCompltecheck.setVisibility(View.VISIBLE);
                    ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(CY_M_ScannerCPUActivity.this.getApplicationContext(), R.animator.flipping);
                    objectAnimator.setTarget(CY_M_ScannerCPUActivity.this.scanner);
                    objectAnimator.setDuration(3000);
                    objectAnimator.start();
                    CY_M_ScannerCPUActivity.this.rel.setVisibility(View.GONE);
                    CY_M_ScannerCPUActivity.this.cooledcpu.setText("Cooled CPU to 25.3°C");
                    objectAnimator.addListener(new Animator.AnimatorListener() {
                        public void onAnimationCancel(Animator animator) {
                        }

                        public void onAnimationRepeat(Animator animator) {
                        }

                        public void onAnimationStart(Animator animator) {
                            CY_M_ScannerCPUActivity.this.img_animation.setImageResource(0);
                            CY_M_ScannerCPUActivity.this.img_animation.setBackgroundResource(0);
                        }

                        public void onAnimationEnd(Animator animator) {
                            rippleBackground.stopRippleAnimation();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    CY_M_ScannerCPUActivity.this.finish();
                                }
                            }, 1000);
                        }
                    });
                }
            }, 5500);
        } catch (Exception unused) {
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CY_M_ScannerCPUActivity.this.onBackPressed();
            }
        });

    }



    public void add(String str, int i) {
        try {
            this.mAdapter.notifyItemInserted(i);
        } catch (Exception unused) {
        }
    }

    public void remove(int i) {
        this.mAdapter.notifyItemRemoved(i);
        try {
            CY_M_CPUCoolerActivity.apps.remove(i);
        } catch (Exception unused) {
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
      onBackPressed();
        return true;
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

        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
            new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
            new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
        }
    }

    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);

        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
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

        new Custom_NativeAd_Admob().showNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
    }

}
