package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class DevSpy_NPMActivity extends AppCompatActivity {
    DecoView arcView;
    int check = 0;
    TextView completion;
    SharedPreferences.Editor editor;
    TextView fou;
    ImageView foupic;
    TextView ist;
    ImageView istpic;
    TextView sec;
    ImageView secpic;
    SharedPreferences sharedpreferences;
    TextView thir;
    ImageView thirpic;
    ImageView mHeaderImage;
    LinearLayout nativeAdContainer2;
    Custom_Banner_Ad banner_ad;
    private String mediation;
    LinearLayout mAdView;
    LinearLayout ll_static_ad_inner;
    LinearLayout mNativeBannerAd;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle); if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView((int) R.layout.devspy_activity_npm);
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button1)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_NPMActivity.this);
            }
        });
//        mAdFunction();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        this.ist = (TextView) findViewById(R.id.ist);
        this.sec = (TextView) findViewById(R.id.sec);
        this.thir = (TextView) findViewById(R.id.thi);
        this.fou = (TextView) findViewById(R.id.fou);
        this.istpic = (ImageView) findViewById(R.id.istpic);
        this.secpic = (ImageView) findViewById(R.id.secpic);
        this.thirpic = (ImageView) findViewById(R.id.thipic);
        this.foupic = (ImageView) findViewById(R.id.foupic);
        this.completion = (TextView) findViewById(R.id.completion);
        SharedPreferences sharedPreferences = getSharedPreferences("was", 0);
        this.sharedpreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
        DecoView decoView = (DecoView) findViewById(R.id.dynamicArcView2);
        this.arcView = decoView;
        decoView.addSeries(new SeriesItem.Builder(Color.parseColor("#C5E6FF")).setRange(0.0f, 100.0f, 100.0f).setInitialVisibility(false).setLineWidth(12.0f).build());
        new SeriesItem.Builder(Color.parseColor("#C5E6FF")).setRange(0.0f, 100.0f, 0.0f).setLineWidth(20.0f).build();
        SeriesItem build = new SeriesItem.Builder(Color.parseColor("#136af6")).setRange(0.0f, 100.0f, 0.0f).setLineWidth(22.0f).build();
        int addSeries = this.arcView.addSeries(build);
        build.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            public void onSeriesItemDisplayProgress(float f) {
            }

            public void onSeriesItemAnimationProgress(float f, float f2) {
                int intValue = new Float(f2).intValue();
                TextView textView = DevSpy_NPMActivity.this.completion;
                textView.setText(intValue + "%");
                if (f2 >= 10.0f && f2 < 50.0f) {
                    DevSpy_NPMActivity.this.ist.setTextColor(Color.parseColor("#136af6"));
                    DevSpy_NPMActivity.this.istpic.setImageResource(R.drawable.ic_brown_dot);
                } else if (f2 >= 50.0f && f2 < 75.0f) {
                    DevSpy_NPMActivity.this.sec.setTextColor(Color.parseColor("#136af6"));
                    DevSpy_NPMActivity.this.secpic.setImageResource(R.drawable.ic_brown_dot);
                } else if (f2 >= 75.0f && f2 < 90.0f) {
                    DevSpy_NPMActivity.this.thir.setTextColor(Color.parseColor("#136af6"));
                    DevSpy_NPMActivity.this.thirpic.setImageResource(R.drawable.ic_brown_dot);
                } else if (f2 >= 90.0f && f2 <= 100.0f) {
                    DevSpy_NPMActivity.this.fou.setTextColor(Color.parseColor("#136af6"));
                    DevSpy_NPMActivity.this.foupic.setImageResource(R.drawable.ic_brown_dot);
                }
            }
        });
        this.arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true).setDelay(0).setDuration(0).setListener(new DecoEvent.ExecuteEventListener() {
            public void onEventEnd(DecoEvent decoEvent) {
            }

            public void onEventStart(DecoEvent decoEvent) {
            }
        }).build());
        this.arcView.addEvent(new DecoEvent.Builder(100.0f).setIndex(addSeries).setDelay(1000).setListener(new DecoEvent.ExecuteEventListener() {
            public void onEventStart(DecoEvent decoEvent) {
            }

            public void onEventEnd(DecoEvent decoEvent) {
                DevSpy_NPMActivity.this.check = 1;
                DevSpy_NPMActivity nPMActivityCYM = DevSpy_NPMActivity.this;
                nPMActivityCYM.youDesirePermissionCode(nPMActivityCYM);
                DevSpy_NPMActivity.this.editor.putString("mode", "0");
                DevSpy_NPMActivity.this.editor.commit();
            }
        }).build());
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_NPMActivity.this.onBackPressed();
            }
        });
    }





    public void enablesall() {
        Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 1);
        Settings.System.putInt(getContentResolver(), "screen_brightness", 255);
        ContentResolver.setMasterSyncAutomatically(true);
    }

    public void youDesirePermissionCode(Activity activity) {
        boolean z;
        if (Build.VERSION.SDK_INT >= 23) {
            z = Settings.System.canWrite(activity);
        } else {
            z = ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_SETTINGS") == 0;
        }
        if (z) {
            enablesall();
            finish();
        } else if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, 1);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_SETTINGS"}, 1);
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (i == 1 && Settings.System.canWrite(this)) {
                Log.d("TAG", "CODE_WRITE_SETTINGS_PERMISSION success");
                enablesall();
                onBackPressed();
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1 && iArr[0] == 0) {
            enablesall();
            onBackPressed();
        }
    }


    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
        if (this.check == 1) {
            try {
                enablesall();
            } catch (Exception unused) {
                onBackPressed();
            }
            onBackPressed();
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
