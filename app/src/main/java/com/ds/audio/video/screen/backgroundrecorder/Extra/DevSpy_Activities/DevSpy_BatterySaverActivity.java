package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Utils.WaveLoadingView;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;


public class DevSpy_BatterySaverActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    TextView hourmain;
    TextView hourn;
    TextView hourp;
    TextView houru;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            CharSequence charSequence;
            CharSequence charSequence2;
            CharSequence charSequence3;
            int intExtra = intent.getIntExtra(FirebaseAnalytics.Param.LEVEL, 0);
            DevSpy_BatterySaverActivity.this.mWaveLoadingView.setProgressValue(intExtra);
            WaveLoadingView waveLoadingView = DevSpy_BatterySaverActivity.this.mWaveLoadingView;
            waveLoadingView.setCenterTitle(intExtra + "%");
            if (intExtra <= 5) {
                DevSpy_BatterySaverActivity.this.hourn.setText("0");
                DevSpy_BatterySaverActivity.this.minutes.setText("15");
                DevSpy_BatterySaverActivity.this.hourp.setText(ExifInterface.GPS_MEASUREMENT_2D);
                DevSpy_BatterySaverActivity.this.minutep.setText("25");
                DevSpy_BatterySaverActivity.this.houru.setText(ExifInterface.GPS_MEASUREMENT_3D);
                DevSpy_BatterySaverActivity.this.minutesu.setText("55");
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("0");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("15");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText(ExifInterface.GPS_MEASUREMENT_2D);
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("25");
                }
            }
            if (intExtra > 5 && intExtra <= 10) {
                DevSpy_BatterySaverActivity.this.hourn.setText("0");
                DevSpy_BatterySaverActivity.this.minutes.setText("30");
                DevSpy_BatterySaverActivity.this.hourp.setText(ExifInterface.GPS_MEASUREMENT_3D);
                DevSpy_BatterySaverActivity.this.minutep.setText("5");
                DevSpy_BatterySaverActivity.this.houru.setText("6");
                DevSpy_BatterySaverActivity.this.minutesu.setText("0");
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("0");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("30");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText(ExifInterface.GPS_MEASUREMENT_3D);
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("5");
                }
            }
            if (intExtra <= 10 || intExtra > 15) {
                charSequence = "15";
            } else {
                DevSpy_BatterySaverActivity.this.hourn.setText("0");
                DevSpy_BatterySaverActivity.this.minutes.setText("45");
                DevSpy_BatterySaverActivity.this.hourp.setText(ExifInterface.GPS_MEASUREMENT_3D);
                DevSpy_BatterySaverActivity.this.minutep.setText("50");
                charSequence = "15";
                DevSpy_BatterySaverActivity.this.houru.setText("8");
                DevSpy_BatterySaverActivity.this.minutesu.setText("25");
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("0");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("45");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText(ExifInterface.GPS_MEASUREMENT_3D);
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("50");
                }
            }
            if (intExtra > 15 && intExtra <= 25) {
                DevSpy_BatterySaverActivity.this.hourn.setText("1");
                DevSpy_BatterySaverActivity.this.minutes.setText("30");
                DevSpy_BatterySaverActivity.this.hourp.setText("4");
                DevSpy_BatterySaverActivity.this.minutep.setText("45");
                DevSpy_BatterySaverActivity.this.houru.setText("12");
                DevSpy_BatterySaverActivity.this.minutesu.setText("55");
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("1");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("30");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("4");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("45");
                }
            }
            if (intExtra > 25 && intExtra <= 35) {
                DevSpy_BatterySaverActivity.this.hourn.setText(ExifInterface.GPS_MEASUREMENT_2D);
                DevSpy_BatterySaverActivity.this.minutes.setText("20");
                DevSpy_BatterySaverActivity.this.hourp.setText("6");
                DevSpy_BatterySaverActivity.this.minutep.setText(ExifInterface.GPS_MEASUREMENT_2D);
                DevSpy_BatterySaverActivity.this.houru.setText("19");
                DevSpy_BatterySaverActivity.this.minutesu.setText(ExifInterface.GPS_MEASUREMENT_2D);
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText(ExifInterface.GPS_MEASUREMENT_2D);
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("20");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("6");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText(ExifInterface.GPS_MEASUREMENT_2D);
                }
            }
            if (intExtra > 35 && intExtra <= 50) {
                DevSpy_BatterySaverActivity.this.hourn.setText("5");
                DevSpy_BatterySaverActivity.this.minutes.setText("20");
                DevSpy_BatterySaverActivity.this.hourp.setText("9");
                DevSpy_BatterySaverActivity.this.minutep.setText("25");
                DevSpy_BatterySaverActivity.this.houru.setText("22");
                DevSpy_BatterySaverActivity.this.minutesu.setText("0");
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("5");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("20");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("9");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("20");
                }
            }
            if (intExtra <= 50 || intExtra > 65) {
                charSequence2 = charSequence;
            } else {
                DevSpy_BatterySaverActivity.this.hourn.setText("7");
                DevSpy_BatterySaverActivity.this.minutes.setText("30");
                DevSpy_BatterySaverActivity.this.hourp.setText("11");
                DevSpy_BatterySaverActivity.this.minutep.setText("1");
                DevSpy_BatterySaverActivity.this.houru.setText("28");
                charSequence2 = charSequence;
                DevSpy_BatterySaverActivity.this.minutesu.setText(charSequence2);
                DevSpy_BatterySaverActivity.this.mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("7");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("30");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("11");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("1");
                }
            }
            if (intExtra <= 65 || intExtra > 75) {
                charSequence3 = "55";
            } else {
                DevSpy_BatterySaverActivity.this.hourn.setText("9");
                DevSpy_BatterySaverActivity.this.minutes.setText("10");
                DevSpy_BatterySaverActivity.this.hourp.setText("14");
                DevSpy_BatterySaverActivity.this.minutep.setText("25");
                DevSpy_BatterySaverActivity.this.houru.setText("30");
                DevSpy_BatterySaverActivity.this.minutesu.setText("55");
                charSequence3 = "55";
                DevSpy_BatterySaverActivity.this.mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("9");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("10");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("14");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("25");
                }
            }
            if (intExtra > 75 && intExtra <= 85) {
                DevSpy_BatterySaverActivity.this.hourn.setText("14");
                DevSpy_BatterySaverActivity.this.minutes.setText(charSequence2);
                DevSpy_BatterySaverActivity.this.hourp.setText("17");
                DevSpy_BatterySaverActivity.this.minutep.setText("10");
                DevSpy_BatterySaverActivity.this.houru.setText("38");
                DevSpy_BatterySaverActivity.this.minutesu.setText("5");
                DevSpy_BatterySaverActivity.this.mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("14");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText(charSequence2);
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("17");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("10");
                }
            }
            if (intExtra > 85 && intExtra <= 100) {
                DevSpy_BatterySaverActivity.this.hourn.setText("20");
                DevSpy_BatterySaverActivity.this.minutes.setText("45");
                DevSpy_BatterySaverActivity.this.hourp.setText("30");
                DevSpy_BatterySaverActivity.this.minutep.setText("0");
                DevSpy_BatterySaverActivity.this.houru.setText("60");
                DevSpy_BatterySaverActivity.this.minutesu.setText(charSequence3);
                DevSpy_BatterySaverActivity.this.mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("0")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("20");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("45");
                }
                if (DevSpy_BatterySaverActivity.this.sharedpreferences.getString("mode", "0").equals("1")) {
                    DevSpy_BatterySaverActivity.this.hourmain.setText("30");
                    DevSpy_BatterySaverActivity.this.minutesmain.setText("0");
                }
            }
        }
    };
    WaveLoadingView mWaveLoadingView;
    TextView minutep;
    TextView minutes;
    TextView minutesmain;
    TextView minutesu;
    private LinearLayout myPage;
    ImageView normal;
    int pos;
    ImageView powersaving;
    SharedPreferences sharedpreferences;
    private LinearLayout tools;
    ImageView ultrasaving;

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
        setContentView((int) R.layout.devspy_activity_battery_saver);
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.QUREKA_BTN, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button6)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_BatterySaverActivity.this);
            }
        });
//        mAdFunction();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        this.mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveView);
        this.powersaving = (ImageView) findViewById(R.id.powersaving);
        this.ultrasaving = (ImageView) findViewById(R.id.ultra);
        this.normal = (ImageView) findViewById(R.id.normal);
        this.hourn = (TextView) findViewById(R.id.hourn);
        this.minutes = (TextView) findViewById(R.id.minutes);
        this.hourp = (TextView) findViewById(R.id.hourp);
        this.minutep = (TextView) findViewById(R.id.minutesp);
        this.houru = (TextView) findViewById(R.id.houru);
        this.minutesu = (TextView) findViewById(R.id.minutesu);
        this.hourmain = (TextView) findViewById(R.id.hourmain);
        this.minutesmain = (TextView) findViewById(R.id.minutesmain);
        this.sharedpreferences = getSharedPreferences("was", 0);
        registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        try {
            this.normal.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_BatterySaverActivity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                        @Override
                        public void callbackCall() {
                            DevSpy_BatterySaverActivity.this.startActivity(new Intent(DevSpy_BatterySaverActivity.this, DevSpy_NPMActivity.class));
                        }
                    });
                }
            });
            this.powersaving.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_BatterySaverActivity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                        @Override
                        public void callbackCall() {
                            Intent intent = new Intent(DevSpy_BatterySaverActivity.this, DevSpy_UPSActivity.class);
                            intent.putExtra("hour", DevSpy_BatterySaverActivity.this.hourp.getText());
                            intent.putExtra("minutes", DevSpy_BatterySaverActivity.this.minutep.getText());
                            intent.putExtra("minutesnormal", DevSpy_BatterySaverActivity.this.minutes.getText());
                            intent.putExtra("hournormal", DevSpy_BatterySaverActivity.this.hourn.getText());
                            DevSpy_BatterySaverActivity.this.startActivity(intent);
                        }
                    });
                }
            });
            this.ultrasaving.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_BatterySaverActivity.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                        @Override
                        public void callbackCall() {
                            Intent intent = new Intent(DevSpy_BatterySaverActivity.this, DevSpy_EPSActivity.class);
                            intent.putExtra("hour", DevSpy_BatterySaverActivity.this.houru.getText());
                            intent.putExtra("minutes", DevSpy_BatterySaverActivity.this.minutesu.getText());
                            intent.putExtra("minutesnormal", DevSpy_BatterySaverActivity.this.minutes.getText());
                            intent.putExtra("hournormal", DevSpy_BatterySaverActivity.this.hourn.getText());
                            DevSpy_BatterySaverActivity.this.startActivity(intent);
                        }
                    });
                }
            });
            this.mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
            this.mWaveLoadingView.setCenterTitleColor(Color.parseColor("#136af6"));
            this.mWaveLoadingView.setBottomTitleColor(Color.parseColor("#136af6"));
            this.mWaveLoadingView.setAmplitudeRatio(30);
            this.mWaveLoadingView.setWaveColor(Color.parseColor("#AEDBFD"));
            this.mWaveLoadingView.setTopTitleStrokeWidth(3.0f);
            this.mWaveLoadingView.setAnimDuration(3000);
            this.mWaveLoadingView.startAnimation();
        } catch (Exception unused) {
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_BatterySaverActivity.this.onBackPressed();
            }
        });

    }




    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(this.mBatInfoReceiver);
        } catch (Exception unused) {
        }
    }

    public void checkshowint() {
        if (!getSharedPreferences("config", 0).getBoolean("batterysaverint", false)) {
            showint();
        }
    }

    public void showint() {

        new FancyAlertDialog.Builder(this).setTitle(getResources().getString(R.string.battery_saver)).setBackgroundColor(Color.parseColor("#0c7944")).setMessage(getResources().getString(R.string.batterysavertxt)).setPositiveBtnBackground(Color.parseColor("#FF4081")).setPositiveBtnText("Ok").setNegativeBtnBackground(Color.parseColor("#FFA9A7A8")).setNegativeBtnText("Cancel").setAnimation(Animation.POP).isCancellable(false).OnPositiveClicked(new FancyAlertDialogListener() {
            @Override
            public void OnClick() {
                DevSpy_BatterySaverActivity.this.getSharedPreferences("config", 0).edit().putBoolean("batterysaverint", true).apply();
            }

        }).OnNegativeClicked(new FancyAlertDialogListener() {
            @Override
            public void OnClick() {

                DevSpy_BatterySaverActivity.this.getSharedPreferences("config", 0).edit().putBoolean("batterysaverint", true).apply();

            }

        }).build();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
        registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
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
