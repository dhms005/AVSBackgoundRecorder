package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_LocaleHelper;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DevSpy_SystemUsageActivity extends AppCompatActivity {
    private static final String ERROR = null;
    private long free = 0;
    private TextView ramFree;
    private TextView ramSize;
    private TextView ramUsed;
    private TextView sd_free;
    private TextView sd_total;
    private TextView sdcard_free;
    private TextView sdcard_total;
    ScrollView system_ll_1;
    ScrollView system_ll_2;
    private long total = 0;

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
        setContentView((int) R.layout.devspy_activity_system_usage);
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.QUREKA_BTN, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button1)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_SystemUsageActivity.this);
            }
        });
        mNativeAdNew();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        this.ramSize = (TextView) findViewById(R.id.ram_size);
        this.ramUsed = (TextView) findViewById(R.id.ram_used);
        this.ramFree = (TextView) findViewById(R.id.ram_free);
        this.sd_total = (TextView) findViewById(R.id.sd_total);
        this.sd_free = (TextView) findViewById(R.id.sd_free);
        this.sdcard_free = (TextView) findViewById(R.id.sd_used);
        this.sdcard_total = (TextView) findViewById(R.id.sdcard_total);
        this.system_ll_2 = (ScrollView) findViewById(R.id.system_ll_2);
        this.system_ll_1 = (ScrollView) findViewById(R.id.system_ll_1);
        getMemorySize();
        Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_SystemUsageActivity.this.onBackPressed();
            }
        });
    }



    private static boolean externalMemoryAvailable() {
        return !Environment.getExternalStorageState().equals("mounted");
    }

    private static String getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize((double) (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())));
    }

    private static String getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize((double) (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())));
    }

    private static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            return ERROR;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return formatSize((double) (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())));
    }

    private static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            return ERROR;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return formatSize((double) (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())));
    }

    private static String formatSize(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double d2 = d / 1048576.0d;
        double d3 = d / 1.073741824E9d;
        double d4 = d / 1.099511627776E12d;
        if (d4 > 1.0d) {
            return decimalFormat.format(d4).concat(" TB");
        }
        if (d3 > 1.0d) {
            return decimalFormat.format(d3).concat(" GB");
        }
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format(d).concat(" KB");
    }

    private void updateInfo() {
        getMemorySize();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        TextView textView = this.ramSize;
        if (textView != null) {
            textView.setText(calSize((double) this.total));
        }
        if (this.ramSize != null) {
            this.ramFree.setText(calSize((double) this.free));
        }
        TextView textView2 = this.ramUsed;
        if (textView2 != null) {
            textView2.setText(calSize((double) (this.total - this.free)));
        }
        TextView textView3 = this.sd_total;
        if (textView3 != null) {
            textView3.setText(getTotalInternalMemorySize());
        }
        TextView textView4 = this.sd_free;
        if (textView4 != null) {
            textView4.setText(getAvailableInternalMemorySize());
        }
        TextView textView5 = this.sdcard_free;
        if (textView5 != null) {
            textView5.setText(getAvailableExternalMemorySize());
        }
        TextView textView6 = this.sdcard_total;
        if (textView6 != null) {
            textView6.setText(getTotalExternalMemorySize());
        }
    }

    private String calSize(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double d2 = d / 1048576.0d;
        double d3 = d / 1.073741824E9d;
        double d4 = d / 1.099511627776E12d;
        if (d4 > 1.0d) {
            return decimalFormat.format(d4).concat(" TB");
        }
        if (d3 > 1.0d) {
            return decimalFormat.format(d3).concat(" GB");
        }
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format(d).concat(" KB");
    }


    private void getMemorySize() {
        Pattern compile = Pattern.compile("([a-zA-Z]+):\\s*(\\d+)");
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/proc/meminfo", "r");
            while (true) {
                String readLine = randomAccessFile.readLine();
                if (readLine != null) {
                    Matcher matcher = compile.matcher(readLine);
                    if (matcher.find()) {
                        String group = matcher.group(1);
                        String group2 = matcher.group(2);
                        if (group.equalsIgnoreCase("MemTotal")) {
                            this.total = Long.parseLong(group2);
                        } else if (group.equalsIgnoreCase("MemFree") || group.equalsIgnoreCase("SwapFree")) {
                            this.free = Long.parseLong(group2);
                        }
                    }
                } else {
                    randomAccessFile.close();
                    this.total *= 1024;
                    this.free *= 1024;
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        updateInfo();
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
