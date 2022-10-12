package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Adapters.DevSpy_PowerModeAdapter;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Model.DevSpy_ItemPower;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class DevSpy_UPSActivity extends AppCompatActivity {
    ImageView applied;
    SharedPreferences.Editor editor;
    TextView extendedtime;
    TextView extendedtimedetail;
    int hour;
    List<DevSpy_ItemPower> items;
    DevSpy_PowerModeAdapter mAdapter;
    int min;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;

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
        setContentView((int) R.layout.devspy_activity_ups);
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button9)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_UPSActivity.this);
            }
        });
//        mAdFunction();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getSharedPreferences("was", 0);
        this.sharedpreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
        this.extendedtime = (TextView) findViewById(R.id.addedtime);
        this.extendedtimedetail = (TextView) findViewById(R.id.addedtimedetail);
        try {
            this.hour = Integer.parseInt(extras.getString("hour").replaceAll("[^0-9]", "")) - Integer.parseInt(extras.getString("hournormal").replaceAll("[^0-9]", ""));
            this.min = Integer.parseInt(extras.getString("minutes").replaceAll("[^0-9]", "")) - Integer.parseInt(extras.getString("minutesnormal").replaceAll("[^0-9]", ""));
        } catch (Exception unused) {
            this.hour = 3;
            this.min = 5;
        }
        if (this.hour == 0 && this.min == 0) {
            this.hour = 3;
            this.min = 5;
        }
        TextView textView = this.extendedtime;
        textView.setText("( +" + this.hour + "h " + Math.abs(this.min) + "m )");
        TextView textView2 = this.extendedtimedetail;
        textView2.setText("\n" + Math.abs(this.hour) + "h " + Math.abs(this.min) + "m");
        this.items = new ArrayList();
        ImageView imageView = (ImageView) findViewById(R.id.applied);
        this.applied = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_UPSActivity.this.editor.putString("mode", "1");
                DevSpy_UPSActivity.this.editor.commit();
                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_UPSActivity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        DevSpy_UPSActivity.this.startActivity(new Intent(DevSpy_UPSActivity.this.getApplicationContext(), DevSpy_InUPSActivity.class));
                        DevSpy_UPSActivity.this.finish();
                    }
                });
            }
        });
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView = recyclerView2;
        recyclerView2.setItemAnimator(new SlideInLeftAnimator());
        this.recyclerView.getItemAnimator().setAddDuration(200);
        this.mAdapter = new DevSpy_PowerModeAdapter(this.items);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 1, false));
        this.recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1.0f)));
        this.recyclerView.computeHorizontalScrollExtent();
        this.recyclerView.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                DevSpy_UPSActivity.this.add("Limit Brightness Upto 80%", 0);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                DevSpy_UPSActivity.this.add("Decrease Device Performance", 1);
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                DevSpy_UPSActivity.this.add("Close All Battery Consuming Apps", 2);
            }
        }, 3000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                DevSpy_UPSActivity.this.add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 3);
            }
        }, 4000);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_UPSActivity.this.onBackPressed();
            }
        });
    }


    public void add(String str, int i) {
        DevSpy_ItemPower CYMItemPower = new DevSpy_ItemPower();
        CYMItemPower.setText(str);
        this.items.add(CYMItemPower);
        this.mAdapter.notifyItemInserted(i);
    }

    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");

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
