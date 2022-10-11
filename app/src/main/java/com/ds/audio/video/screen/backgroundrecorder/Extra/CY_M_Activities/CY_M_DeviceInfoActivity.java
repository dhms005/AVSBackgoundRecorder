package com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Helper.CY_M_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CY_M_DeviceInfoActivity extends AppCompatActivity {
    public static boolean IS_DOWN = true;
    public static boolean IS_UP = true;
    public static final int PERMISSION_CAMERA = 40;
    ImageView iv_moreads;
    TextView tv_android_version;
    TextView tv_api_level;
    TextView tv_back_camera;
    TextView tv_brand_name;
    TextView tv_build_id;
    TextView tv_device_name;
    TextView tv_front_camera;
    TextView tv_hardware;
    TextView tv_host;
    TextView tv_instruction_set;
    TextView tv_model_name;
    TextView tv_os_version;
    TextView tv_physical_size;
    TextView tv_product_code;
    TextView tv_refresh_rate;
    TextView tv_resolution;
    TextView tv_screen_density;

    ImageView mHeaderImage;
    LinearLayout nativeAdContainer2;
    Custom_Banner_Ad banner_ad;
    private String mediation;
    LinearLayout mAdView;
    LinearLayout ll_static_ad_inner;
    LinearLayout mNativeBannerAd;


    public void onNegativeButtonClicked() {
    }

    public void onNeutralButtonClicked() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView((int) R.layout.cy_m_activity_device_info);
        mNativeAdNew();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        this.tv_device_name = (TextView) findViewById(R.id.tv_device_name);
        this.tv_model_name = (TextView) findViewById(R.id.tv_model_name);
        this.tv_brand_name = (TextView) findViewById(R.id.tv_brand_name);
        this.tv_product_code = (TextView) findViewById(R.id.tv_product_code);
        this.tv_resolution = (TextView) findViewById(R.id.tv_resolution);
        this.tv_screen_density = (TextView) findViewById(R.id.tv_screen_density);
        this.tv_refresh_rate = (TextView) findViewById(R.id.tv_refresh_rate);
        this.tv_physical_size = (TextView) findViewById(R.id.tv_physical_size);
        this.tv_front_camera = (TextView) findViewById(R.id.tv_front_camera);
        this.tv_back_camera = (TextView) findViewById(R.id.tv_back_camera);
        this.tv_android_version = (TextView) findViewById(R.id.tv_android_version);
        this.tv_api_level = (TextView) findViewById(R.id.tv_api_level);
        this.tv_os_version = (TextView) findViewById(R.id.tv_os_version);
        this.tv_instruction_set = (TextView) findViewById(R.id.tv_instruction_set);
        this.tv_hardware = (TextView) findViewById(R.id.tv_hardware);
        this.tv_host = (TextView) findViewById(R.id.tv_host);
        this.tv_build_id = (TextView) findViewById(R.id.tv_build_id);
        getDeviceInfo();
        getScreenInfo();
        getCameraMegapixels();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CY_M_DeviceInfoActivity.this.onBackPressed();
            }
        });
    }


    public void getDeviceInfo() {
        try {
            String str = Build.MODEL;
            String str2 = Build.BRAND;
            String str3 = Build.PRODUCT;
            TextView textView = this.tv_device_name;
            textView.setText(Build.BRAND + " " + Build.DEVICE);
            this.tv_model_name.setText(str);
            this.tv_brand_name.setText(str2);
            this.tv_product_code.setText(str3);
            String str4 = Build.VERSION.RELEASE;
            String valueOf = String.valueOf(Build.VERSION.SDK_INT);
            this.tv_android_version.setText(str4);
            this.tv_api_level.setText(valueOf);
            TextView textView2 = this.tv_os_version;
            textView2.setText(System.getProperty("os.version") + " (" + Build.VERSION.INCREMENTAL + ")");
            String str5 = Build.CPU_ABI;
            String str6 = Build.HARDWARE;
            String str7 = Build.HOST;
            String str8 = Build.ID;
            this.tv_instruction_set.setText(str5);
            this.tv_hardware.setText(str6);
            this.tv_host.setText(str7);
            this.tv_build_id.setText(str8);
        } catch (Exception unused) {
            Log.d(CY_M_FileHelper.ERROR, "Error getting Device INFO");
        }
    }

    public void getScreenInfo() {
        String screenResolution = getScreenResolution();
        String screenDensity = getScreenDensity();
        String screenRefreshRate = getScreenRefreshRate();
        String screenPhysicalSize = getScreenPhysicalSize();
        this.tv_resolution.setText(screenResolution);
        this.tv_screen_density.setText(screenDensity);
        this.tv_refresh_rate.setText(screenRefreshRate);
        this.tv_physical_size.setText(screenPhysicalSize);
    }

    public String getScreenResolution() {
        Display defaultDisplay = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        return i + "w * " + i2 + "h";
    }

    public String getScreenDensity() {
        String str;
        double d = (double) getResources().getDisplayMetrics().density;
        String str2 = EnvironmentCompat.MEDIA_UNKNOWN;
        if (d == 0.75d) {
            str2 = "100 DPI";
            str = "Low";
        } else if (d == 1.0d) {
            str2 = "160 DPI";
            str = "Medium";
        } else if (d == 1.5d) {
            str2 = "240 DPI";
            str = "High";
        } else if (d == 2.0d) {
            str2 = "320 DPI";
            str = "X High";
        } else if (d == 3.0d) {
            str2 = "480 DPI";
            str = "XX High";
        } else if (d == 4.0d) {
            str2 = "640 DPI";
            str = "XXX High";
        } else {
            str = str2;
        }
        return str2 + " (" + str + ")";
    }

    public String getScreenRefreshRate() {
        Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        return String.valueOf(defaultDisplay.getRefreshRate()) + " Hz";
    }

    public String getScreenPhysicalSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        double d = (double) i;
        double d2 = (double) displayMetrics.xdpi;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d / d2;
        double d4 = (double) i2;
        double d5 = (double) displayMetrics.ydpi;
        Double.isNaN(d4);
        Double.isNaN(d5);
        double sqrt = Math.sqrt(Math.pow(d3, 2.0d) + Math.pow(d4 / d5, 2.0d));
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        double parseDouble = Double.parseDouble(decimalFormat.format(sqrt));
        double parseDouble2 = Double.parseDouble(decimalFormat.format(2.54d * parseDouble));
        return parseDouble + "\"" + " (" + parseDouble2 + " cm" + ")";
    }

    public void getCameraMegapixels() {
        String str;
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 40);
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
            Camera open = Camera.open(0);
            List<Camera.Size> supportedPictureSizes = open.getParameters().getSupportedPictureSizes();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < supportedPictureSizes.size(); i++) {
                Camera.Size size = supportedPictureSizes.get(i);
                arrayList.add(Integer.valueOf(size.width));
                arrayList2.add(Integer.valueOf(size.height));
            }
            String str2 = "";
            if (arrayList.size() == 0 || arrayList2.size() == 0) {
                str = str2;
            } else {
                str = ((((Integer) Collections.max(arrayList)).intValue() * ((Integer) Collections.max(arrayList2)).intValue()) / 1024000) + " Megapixel";
            }
            open.release();
            arrayList.clear();
            arrayList2.clear();
            Camera open2 = Camera.open(1);
            List<Camera.Size> supportedPictureSizes2 = open2.getParameters().getSupportedPictureSizes();
            for (int i2 = 0; i2 < supportedPictureSizes2.size(); i2++) {
                Camera.Size size2 = supportedPictureSizes2.get(i2);
                arrayList.add(Integer.valueOf(size2.width));
                arrayList2.add(Integer.valueOf(size2.height));
            }
            if (!(arrayList.size() == 0 || arrayList2.size() == 0)) {
                str2 = ((((Integer) Collections.max(arrayList)).intValue() * ((Integer) Collections.max(arrayList2)).intValue()) / 1024000) + " Megapixel";
            }
            open2.release();
            this.tv_front_camera.setText(str2);
            this.tv_back_camera.setText(str);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 40) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            Toast.makeText(this, "Please allow permission to access", Toast.LENGTH_SHORT).show();
        } else {
            getCameraMegapixels();
        }
    }

    public void onPositiveButtonClicked(int i, String str) {
        if (i >= 4) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName()));
            try {
                intent.setFlags(67108864);
                startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())).setFlags(67108864));
            }
        } else {
            Toast.makeText(this, "Thank You For Valuable Feedback", 1).show();
        }
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
