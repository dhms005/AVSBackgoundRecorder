package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkRequest;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Adapters.DevSpy_RecyclerAdapter;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Model.DevSpy_Apps;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class DevSpy_CPUCoolerActivity extends AppCompatActivity {
    public static List<DevSpy_Apps> apps;
    List<DevSpy_Apps> CYMApps2;
    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            DevSpy_CPUCoolerActivity.this.makeStabilityScanning(intent);
        }
    };
    TextView batterytemp;
    int check = 0;
    ImageView coolbutton;
    ImageView ivtemping;
    DevSpy_RecyclerAdapter mAdapter;
    TextView nooverheating;
    RecyclerView recyclerView;
    float temp;
    ImageView tempimg;
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
        setContentView((int) R.layout.devspy_activity_cpu_cooler);

        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button9)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_CPUCoolerActivity.this);
            }
        });
//        mAdFunction();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        try {
            this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            this.ivtemping = (ImageView) findViewById(R.id.iv_tempimg);
            this.tempimg = (ImageView) findViewById(R.id.tempimg);
            this.coolbutton = (ImageView) findViewById(R.id.coolbutton);
            this.nooverheating = (TextView) findViewById(R.id.nooverheating);
            this.ivtemping.setImageResource(R.drawable.ic_after_cooling_icon);
            this.tempimg.setImageResource(R.drawable.ic_cooling_complete);
            this.nooverheating.setText("No Apps Overheating");
            this.nooverheating.setTextColor(Color.parseColor("#136af6"));
            this.coolbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(DevSpy_CPUCoolerActivity.this, "Temperature of CPU is Already Normal", 0).show();
                }
            });
            this.batterytemp = (TextView) findViewById(R.id.batterytemp);
            if (System.currentTimeMillis() - getSharedPreferences("APPS_CONFIGS", 0).getLong("COOLER_LAST_UPDATE", 0) >= 1200000) {
                makeStabilityScanning((Intent) null);
            }
            Log.e("Temperrature", this.temp + "");
        } catch (Exception unused) {
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_CPUCoolerActivity.this.onBackPressed();
            }
        });
    }


    public void onDestroy() {
        try {
            unregisterReceiver(this.batteryReceiver);
        } catch (Exception unused) {
        }
        super.onDestroy();
    }

    public void getAllICONS() {
        int i = 0;
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(128);
        if (installedApplications != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= installedApplications.size()) {
                    break;
                }
                String str = installedApplications.get(i2).packageName;
                Log.e("packageName-->", "" + str);
                if (!str.equals("fast.cleaner.battery.saver")) {
                    try {
                        String str2 = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128));
                        DevSpy_Apps CYMApps3 = new DevSpy_Apps();
                        long length = new File(packageManager.getApplicationInfo(str, 128).publicSourceDir).length();
                        StringBuilder sb = new StringBuilder();
                        int i3 = i2;
                        try {
                            sb.append(length / 1000000);
                            sb.append("");
                            Log.e("SIZE", sb.toString());
                            CYMApps3.setSize(((length / 1000000) + 20) + "MB");
                            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 128);
                            i = i3;
                            try {
                                Drawable applicationIcon = getPackageManager().getApplicationIcon(installedApplications.get(i).packageName);
                                CYMApps3.setImage(applicationIcon);
                                getPackageManager();
                                Log.e("ico-->", "" + applicationIcon);
                                if ((applicationInfo.flags & 1) == 0) {
                                    int i4 = this.check;
                                    if (i4 > 5) {
                                        unregisterReceiver(this.batteryReceiver);
                                        break;
                                    } else {
                                        this.check = i4 + 1;
                                        apps.add(CYMApps3);
                                    }
                                }
                                this.mAdapter.notifyDataSetChanged();
                            } catch (PackageManager.NameNotFoundException unused) {
                                continue;
                            }
                        } catch (PackageManager.NameNotFoundException unused2) {
                            i = i3;
                        }
                    } catch (PackageManager.NameNotFoundException unused3) {
                    }
                    i2 = i + 1;
                }
                i = i2;
                i2 = i + 1;
            }
        }
        if (apps.size() > 1) {
            DevSpy_RecyclerAdapter CYMRecyclerAdapter = new DevSpy_RecyclerAdapter(apps);
            this.mAdapter = CYMRecyclerAdapter;
            CYMRecyclerAdapter.notifyDataSetChanged();
        }
    }


    public void makeStabilityScanning(Intent intent) {
        if (intent == null) {
            try {
                intent = registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            } catch (Exception unused) {
                return;
            }
        }
        intent.getIntExtra(FirebaseAnalytics.Param.LEVEL, 0);
        this.temp = ((float) intent.getIntExtra("temperature", 21)) / 10.0f;
        TextView textView = this.batterytemp;
        textView.setText(this.temp + "°C");
        if (((double) this.temp) >= 30.0d) {
            apps = new ArrayList();
            this.CYMApps2 = new ArrayList();
            this.tempimg.setImageResource(R.drawable.ic_cpu_cooler_bg);
            this.ivtemping.setImageResource(R.drawable.ic_before_cpu_cooler_icon);
            this.nooverheating.setText("");
            this.coolbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    SharedPreferences.Editor edit = DevSpy_CPUCoolerActivity.this.getSharedPreferences("APPS_CONFIGS", 0).edit();
                    edit.putLong("COOLER_LAST_UPDATE", System.currentTimeMillis());
                    edit.commit();

                    DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_CPUCoolerActivity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                        @Override
                        public void callbackCall() {
                            DevSpy_CPUCoolerActivity.this.startActivity(new Intent(DevSpy_CPUCoolerActivity.this, DevSpy_ScannerCPUActivity.class));
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DevSpy_CPUCoolerActivity.this.nooverheating.setText("No App Overheating");
                            DevSpy_CPUCoolerActivity.this.nooverheating.setTextColor(Color.parseColor("#136af6"));
                            DevSpy_CPUCoolerActivity.this.ivtemping.setImageResource(R.drawable.ic_after_cooling_icon);
                            DevSpy_CPUCoolerActivity.this.tempimg.setImageResource(R.drawable.ic_cooling_complete);
                            DevSpy_CPUCoolerActivity.this.batterytemp.setText("25.3°C");
                            DevSpy_CPUCoolerActivity.this.recyclerView.setAdapter((RecyclerView.Adapter) null);
                        }
                    }, 2000);
                    DevSpy_CPUCoolerActivity.this.coolbutton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Toast.makeText(DevSpy_CPUCoolerActivity.this, "Temperature of CPU is Already Normal", 0).show();
                        }
                    });
                }
            });
            this.recyclerView.setItemAnimator(new SlideInLeftAnimator());
            this.recyclerView.getItemAnimator().setAddDuration(WorkRequest.MIN_BACKOFF_MILLIS);
            this.mAdapter = new DevSpy_RecyclerAdapter(apps);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
            this.recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1.0f)));
            this.recyclerView.computeHorizontalScrollExtent();
            this.recyclerView.setAdapter(this.mAdapter);
            getAllICONS();
        }
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();

        return true;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
