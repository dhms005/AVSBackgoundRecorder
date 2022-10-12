package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Utils.DevSpy_BoostAlarm;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DevSpy_SpeedBoosterActivity extends AppCompatActivity {
    public static ImageView optimizebutton;
    TextView appsfreed;
    TextView appused;
    DecoView arcView;
    DecoView arcView2;
    TextView bottom;
    TextView centree;
    int counter = 0;
    SharedPreferences.Editor editor;
    int mb = 1048576;
    private LinearLayout myPage;
    LinearLayout optimizelay;
    TextView processes;
    TextView ramperct;
    LinearLayout scanlay;
    TextView scanning;
    SharedPreferences sharedpreferences;
    TimerTask timer = null;
    TimerTask timer2 = null;
    private LinearLayout tools;
    TextView top;
    TextView totalram;
    TextView usedram;
    int x;
    int y;
    ImageView mHeaderImage;
    LinearLayout nativeAdContainer2;
    Custom_Banner_Ad banner_ad;
    private String mediation;
    LinearLayout mAdView;
    LinearLayout ll_static_ad_inner;
    LinearLayout mNativeBannerAd;

    public void killall() {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView((int) R.layout.devspy_activity_speed_booster);
        ImageView qureka_ad = findViewById(R.id.qureka_ad);
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button6)).into(qureka_ad);
        qureka_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_SpeedBoosterActivity.this);
            }
        });
//        mAdFunction();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }

        this.arcView = (DecoView) findViewById(R.id.dynamicArcView2);
        DecoView decoView = (DecoView) findViewById(R.id.dynamicArcView3);
        this.arcView2 = decoView;
        decoView.setVisibility(View.GONE);
        this.arcView.setVisibility(View.VISIBLE);
        this.scanning = (TextView) findViewById(R.id.scanning);
        this.scanlay = (LinearLayout) findViewById(R.id.scanlay);
        this.optimizelay = (LinearLayout) findViewById(R.id.optimizelay);
        optimizebutton = (ImageView) findViewById(R.id.optbutton);
        this.centree = (TextView) findViewById(R.id.centree);
        this.totalram = (TextView) findViewById(R.id.totalram);
        this.usedram = (TextView) findViewById(R.id.usedram);
        this.appsfreed = (TextView) findViewById(R.id.appsfreed);
        this.appused = (TextView) findViewById(R.id.appsused);
        this.processes = (TextView) findViewById(R.id.processes);
        this.top = (TextView) findViewById(R.id.top);
        this.bottom = (TextView) findViewById(R.id.bottom);
        this.ramperct = (TextView) findViewById(R.id.ramperct);
        this.sharedpreferences = getSharedPreferences(DevSpy_BoostAlarm.PREFERENCES_RES_BOOSTER, 0);
        try {
            Random random = new Random();
            TextView textView = this.ramperct;
            textView.setText((random.nextInt(60) + 40) + "%");
            if (this.sharedpreferences.getString("booster", "1").equals("0")) {
                this.centree.setText(this.sharedpreferences.getString("value", "50MB"));
            }
            start();
            optimizebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (DevSpy_SpeedBoosterActivity.this.sharedpreferences.getString("booster", "1").equals("1")) {
                        DevSpy_SpeedBoosterActivity.this.optimize();
                        DevSpy_SpeedBoosterActivity CYMSpeedBoosterActivity = DevSpy_SpeedBoosterActivity.this;
                        CYMSpeedBoosterActivity.editor = CYMSpeedBoosterActivity.sharedpreferences.edit();
                        DevSpy_SpeedBoosterActivity.this.editor.putString("booster", "0");
                        DevSpy_SpeedBoosterActivity.this.editor.commit();
                        SharedPreferences.Editor edit = DevSpy_SpeedBoosterActivity.this.getSharedPreferences("APPS_CONFIGS", 0).edit();
                        edit.putLong("BOOSTER_LAST_UPDATE", System.currentTimeMillis());
                        edit.commit();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((AlarmManager) DevSpy_SpeedBoosterActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, System.currentTimeMillis() + 100000, PendingIntent.getBroadcast(DevSpy_SpeedBoosterActivity.this, 0, new Intent(DevSpy_SpeedBoosterActivity.this, DevSpy_BoostAlarm.class), PendingIntent.FLAG_MUTABLE));

                        }else{
                            ((AlarmManager) DevSpy_SpeedBoosterActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, System.currentTimeMillis() + 100000, PendingIntent.getBroadcast(DevSpy_SpeedBoosterActivity.this, 0, new Intent(DevSpy_SpeedBoosterActivity.this, DevSpy_BoostAlarm.class), PendingIntent.FLAG_ONE_SHOT));

                        }

                        return;
                    }
                    Toast.makeText(DevSpy_SpeedBoosterActivity.this, "Phone is Aleady Optimized", 0).show();
                }
            });
        } catch (Exception unused) {

        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DevSpy_SpeedBoosterActivity.this.onBackPressed();
            }
        });
//        AdNative.getInstance().showNative100(this, (FrameLayout) findViewById(R.id.Admob_Banner_Frame));
    }


    public void optimize() {
        this.arcView2.setVisibility(View.VISIBLE);
        this.arcView.setVisibility(View.GONE);
        this.arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218)).setRange(0.0f, 100.0f, 0.0f).setInterpolator(new AccelerateInterpolator()).build());
        new SeriesItem.Builder(Color.parseColor("#00000000")).setRange(0.0f, 100.0f, 0.0f).setLineWidth(32.0f).build();
        int addSeries = this.arcView2.addSeries(new SeriesItem.Builder(Color.parseColor("#ffffff")).setRange(0.0f, 100.0f, 0.0f).setLineWidth(32.0f).build());
        this.arcView2.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true).setDelay(500).setDuration(2000).setListener(new DecoEvent.ExecuteEventListener() {
            public void onEventEnd(DecoEvent decoEvent) {
            }

            public void onEventStart(DecoEvent decoEvent) {
                DevSpy_SpeedBoosterActivity.this.bottom.setText("");
                DevSpy_SpeedBoosterActivity.this.top.setText("");
                DevSpy_SpeedBoosterActivity.this.centree.setText("Optimizing...");
            }
        }).build());
        this.arcView2.addEvent(new DecoEvent.Builder(25.0f).setIndex(addSeries).setDelay(2000).setListener(new DecoEvent.ExecuteEventListener() {
            public void onEventStart(DecoEvent decoEvent) {
                DevSpy_SpeedBoosterActivity.this.bottom.setText("");
                DevSpy_SpeedBoosterActivity.this.top.setText("");
                DevSpy_SpeedBoosterActivity.this.centree.setText("Optimizing...");
            }

            public void onEventEnd(DecoEvent decoEvent) {
                DevSpy_SpeedBoosterActivity.this.bottom.setText("Found");
                DevSpy_SpeedBoosterActivity.this.top.setText("Storage");
                Random random = new Random();
                TextView textView = DevSpy_SpeedBoosterActivity.this.ramperct;
                textView.setText((random.nextInt(40) + 20) + "%");
                DevSpy_SpeedBoosterActivity.this.centree.setText("00.00");
            }
        }).build());
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(5000);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setFillAfter(true);
        ((ImageView) findViewById(R.id.waves)).startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                DevSpy_SpeedBoosterActivity.this.scanlay.setVisibility(View.VISIBLE);
                DevSpy_SpeedBoosterActivity.this.optimizelay.setVisibility(View.GONE);
                DevSpy_SpeedBoosterActivity.this.scanning.setText("SCANNING...");
                DevSpy_SpeedBoosterActivity.this.killall();
            }

            public void onAnimationEnd(Animation animation) {
                DevSpy_SpeedBoosterActivity.this.scanlay.setVisibility(View.GONE);
                DevSpy_SpeedBoosterActivity.this.optimizelay.setVisibility(View.VISIBLE);
                Random random = new Random();
                DevSpy_SpeedBoosterActivity.this.x = random.nextInt(100) + 30;
                TextView textView = DevSpy_SpeedBoosterActivity.this.centree;
                textView.setText((DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() - ((long) DevSpy_SpeedBoosterActivity.this.x)) + " MB");
                DevSpy_SpeedBoosterActivity CYMSpeedBoosterActivity = DevSpy_SpeedBoosterActivity.this;
                CYMSpeedBoosterActivity.editor = CYMSpeedBoosterActivity.sharedpreferences.edit();
                SharedPreferences.Editor editor = DevSpy_SpeedBoosterActivity.this.editor;
                editor.putString("value", (DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() - ((long) DevSpy_SpeedBoosterActivity.this.x)) + " MB");
                DevSpy_SpeedBoosterActivity.this.editor.commit();
                Log.e("used mem", DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() + " MB");
                Log.e("used mem", DevSpy_SpeedBoosterActivity.this.getTotalRAM());
                DevSpy_SpeedBoosterActivity.this.totalram.setText(DevSpy_SpeedBoosterActivity.this.getTotalRAM());
                TextView textView2 = DevSpy_SpeedBoosterActivity.this.usedram;
                textView2.setText((DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() - ((long) DevSpy_SpeedBoosterActivity.this.x)) + " MB/ ");
                DevSpy_SpeedBoosterActivity.this.appsfreed.setText(DevSpy_SpeedBoosterActivity.this.getTotalRAM());
                TextView textView3 = DevSpy_SpeedBoosterActivity.this.appused;
                textView3.setText(Math.abs((DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() - ((long) DevSpy_SpeedBoosterActivity.this.x)) - 30) + " MB/ ");
                TextView textView4 = DevSpy_SpeedBoosterActivity.this.processes;
                textView4.setText((DevSpy_SpeedBoosterActivity.this.y - (new Random().nextInt(10) + 5)) + "");
            }
        });
    }

    public void start() {
        final Timer timer3 = new Timer();
        TimerTask r1 = new TimerTask() {
            public void run() {
                try {
                    DevSpy_SpeedBoosterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            DevSpy_SpeedBoosterActivity.this.counter++;
                            DevSpy_SpeedBoosterActivity.this.centree.setText(DevSpy_SpeedBoosterActivity.this.counter + "MB");
                        }
                    });
                } catch (Exception unused) {
                }
            }
        };
        this.timer = r1;
        timer3.schedule(r1, 30, 30);
        this.arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218)).setRange(0.0f, 100.0f, 0.0f).setInterpolator(new AccelerateInterpolator()).build());
        new SeriesItem.Builder(Color.parseColor("#00000000")).setRange(0.0f, 100.0f, 0.0f).setLineWidth(32.0f).build();
        int addSeries = this.arcView.addSeries(new SeriesItem.Builder(Color.parseColor("#ffffff")).setRange(0.0f, 100.0f, 0.0f).setLineWidth(32.0f).build());
        this.arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true).setDelay(0).setDuration(600).build());
        this.arcView.addEvent(new DecoEvent.Builder((float) (new Random().nextInt(60) + 30)).setIndex(addSeries).setDelay(2000).setListener(new DecoEvent.ExecuteEventListener() {
            public void onEventStart(DecoEvent decoEvent) {
            }

            public void onEventEnd(DecoEvent decoEvent) {
                timer3.cancel();
                DevSpy_SpeedBoosterActivity.this.timer.cancel();
                timer3.purge();
                TextView textView = DevSpy_SpeedBoosterActivity.this.centree;
                textView.setText(DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() + " MB");
                if (DevSpy_SpeedBoosterActivity.this.sharedpreferences.getString("booster", "1").equals("0")) {
                    DevSpy_SpeedBoosterActivity.this.centree.setText(DevSpy_SpeedBoosterActivity.this.sharedpreferences.getString("value", "50MB"));
                }
                new Timer();
                final Timer timer = new Timer();
                try {
                    DevSpy_SpeedBoosterActivity.this.timer2 = new TimerTask() {
                        public void run() {
                            try {
                                DevSpy_SpeedBoosterActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        TextView textView = DevSpy_SpeedBoosterActivity.this.centree;
                                        textView.setText(DevSpy_SpeedBoosterActivity.this.getUsedMemorySize() + " MB");
                                        if (DevSpy_SpeedBoosterActivity.this.sharedpreferences.getString("booster", "1").equals("0")) {
                                            DevSpy_SpeedBoosterActivity.this.centree.setText(DevSpy_SpeedBoosterActivity.this.sharedpreferences.getString("value", "50MB"));
                                        }
                                        timer.cancel();
                                        DevSpy_SpeedBoosterActivity.this.timer2.cancel();
                                        timer.purge();
                                    }
                                });
                            } catch (Exception unused) {
                            }
                        }
                    };
                } catch (Exception unused) {
                }
                timer.schedule(DevSpy_SpeedBoosterActivity.this.timer2, 100, 100);
            }
        }).build());
        Log.e("used mem", getUsedMemorySize() + " MB");
        Log.e("used mem", getTotalRAM());
        this.totalram.setText(getTotalRAM());
        TextView textView = this.usedram;
        textView.setText(getUsedMemorySize() + " MB/ ");
        this.appsfreed.setText(getTotalRAM());
        TextView textView2 = this.appused;
        textView2.setText(((getUsedMemorySize() - ((long) this.x)) - 30) + " MB/ ");
        this.y = new Random().nextInt(50) + 15;
        TextView textView3 = this.processes;
        textView3.setText(this.y + "");
    }


    public String getTotalRAM() {
    RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }

    public long getUsedMemorySize() {
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) getSystemService("activity")).getMemoryInfo(memoryInfo);
            return memoryInfo.availMem / 1048576;
        } catch (Exception unused) {
            return 200;
        }
    }

    public void checkshowint() {
        if (!getSharedPreferences("config", 0).getBoolean("speedint", false)) {
            showint();
        }
    }

    public void showint() {
        new FancyAlertDialog.Builder(this).setTitle(getResources().getString(R.string.speed_booster)).setBackgroundColor(Color.parseColor("#0c7944")).setMessage(getResources().getString(R.string.speedtxt)).setPositiveBtnBackground(Color.parseColor("#FF4081")).setPositiveBtnText("Ok").setNegativeBtnBackground(Color.parseColor("#FFA9A7A8")).setNegativeBtnText("Cancel").setAnimation(com.shashank.sony.fancydialoglib.Animation.POP).isCancellable(false).setIcon(R.drawable.booster, Icon.Visible).OnPositiveClicked(new FancyAlertDialogListener() {
            public void OnClick() {
                DevSpy_SpeedBoosterActivity.this.getSharedPreferences("config", 0).edit().putBoolean("speedint", true).apply();
            }
        }).OnNegativeClicked(new FancyAlertDialogListener() {
            public void OnClick() {
                DevSpy_SpeedBoosterActivity.this.getSharedPreferences("config", 0).edit().putBoolean("speedint", true).apply();
            }
        }).build();
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
