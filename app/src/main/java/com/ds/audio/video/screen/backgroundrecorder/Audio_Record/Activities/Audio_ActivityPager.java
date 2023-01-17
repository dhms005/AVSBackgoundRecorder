package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Adapter.AudioViewPagerAdapter;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Fragment.Audio_Recorder_Fragment;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_SharedPreHelper;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.material.tabs.TabLayout;

public class Audio_ActivityPager extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    int[] iArr = {R.drawable.cctv};
    private Audio_SharedPreHelper sharedPreHelper;
    LinearLayout mNativeBannerAd;
    SharedPreferences defaultSharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.audio_activity_pager);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Audio_ActivityPager.this);
        editor = defaultSharedPreferences.edit();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        ImageView setting = (ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(Audio_ActivityPager.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, "").equals("")) {
                            editor.putBoolean("prefAppLock", false);
                            editor.commit();
                        }
                        if (SharePrefUtils.getBoolean(Constant_ad.PATTERN_CONFIRM, false)) {
                            editor.putBoolean("prefChangePattern", true);
                            editor.commit();
                        } else {
                            editor.putBoolean("prefChangePattern", false);
                            editor.commit();
                        }
                        Intent intent = new Intent(Audio_ActivityPager.this, Audio_Setting_Activity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(Audio_ActivityPager.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {

                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        setupViewPager(this.viewPager);

        this.tabLayout.setupWithViewPager(this.viewPager);
        setupTabIcons();

        if (Audio_FileHelper.getAvailableExternalMemory() <= 100) {
            AlertDialog.Builder title = new AlertDialog.Builder(this).setCancelable(true).setTitle(getString(R.string.low_memory));
            AlertDialog.Builder message = title.setMessage(getString(R.string.you_have) + Audio_FileHelper.getAvailableExternalMemorySize(this) + getString(R.string.should_clear_data));
            message.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            message.create().show();
        }

        this.sharedPreHelper = new Audio_SharedPreHelper(this);
        this.sharedPreHelper.saveSendMsg();
    }

    private void setupViewPager(ViewPager viewPager2) {
        AudioViewPagerAdapter cCTVViewPagerAdapter = new AudioViewPagerAdapter(getSupportFragmentManager());
        cCTVViewPagerAdapter.addFrag(new Audio_Recorder_Fragment(), "Recorder");
//        cCTVViewPagerAdapter.addFrag(new CY_M_SettingTimeFragment(), "Timer");
        viewPager2.setAdapter(cCTVViewPagerAdapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(iArr[0]);
        //   tabLayout.getTabAt(1).setIcon(iArr[1]);
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
