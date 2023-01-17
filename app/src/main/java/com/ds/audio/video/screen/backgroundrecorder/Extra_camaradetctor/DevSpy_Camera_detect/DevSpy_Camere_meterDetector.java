package com.ds.audio.video.screen.backgroundrecorder.Extra_camaradetctor.DevSpy_Camera_detect;

import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.github.anastr.speedviewlib.Gauge;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class DevSpy_Camere_meterDetector extends AppCompatActivity implements SensorEventListener {
    int[] beep = {R.raw.beep};
    private Gauge gauge;
    MediaPlayer mediaPlayer;
    private SensorManager mySensorManager;
    private TextView sensor;
    private SensorManager sensorManager;
    private TextView status;
    String str;
    private TextView txtx;
    private TextView txty;
    private TextView txtz;
    private TextView value;
    LinearLayout mNativeBannerAd;

    public void onAccuracyChanged(Sensor sensor2, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.devspy_camere_activity_meter_detector);
        ImageView back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }

        this.sensor = (TextView) findViewById(R.id.sensor_speed);
        this.str = "sensor";
        this.mySensorManager = (SensorManager) getSystemService(this.str);
        if (this.mySensorManager.getDefaultSensor(2) == null) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.devspy_camere_diloge);
            dialog.setTitle("Sensor Info");
            dialog.show();
            this.sensor.setText("Sorry Your Device Does Not Support This App");
        } else {
            this.sensor.setText("Move Phone Near Suspected Devices");
        }
        this.value = (TextView) findViewById(R.id.m_value_speed);
        this.status = (TextView) findViewById(R.id.status_speed);
        this.gauge = (Gauge) findViewById(R.id.onlyspeed);
        this.txtx = (TextView) findViewById(R.id.txtx);
        this.txty = (TextView) findViewById(R.id.txty);
        this.txtz = (TextView) findViewById(R.id.txtz);
        this.sensorManager = (SensorManager) getSystemService(this.str);
        this.mediaPlayer = MediaPlayer.create(this, this.beep[0]);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        MediaPlayer mediaPlayer2;
        if (sensorEvent.sensor.getType() == 2) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            long sqrt = (long) Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
            if (sqrt > 80 && sqrt < 140) {
                if (this.mediaPlayer == null) {
                    this.mediaPlayer = MediaPlayer.create(this, this.beep[0]);
                }
                this.mediaPlayer.start();
            }
            if (sqrt <= 80 && sqrt >= 140 && (mediaPlayer2 = this.mediaPlayer) != null) {
                mediaPlayer2.stop();
            }
            if (sqrt < 45) {
                this.status.setText("NOTHING DETECTED");
            } else if (sqrt >= 45 && sqrt <= 80) {
                this.status.setText("BUG DETECTED");
            } else if (sqrt > 80 && sqrt <= 120) {
                this.status.setText("POTENTIAL BUG DETECTED");
            } else if (sqrt > 120 && sqrt <= 140) {
                this.status.setText("POTENTIAL BUG DETECTED");
            } else if (sqrt > 140) {
                this.status.setText("DETECTED BUG HIGH RADIATIONS");
                if (this.mediaPlayer == null) {
                    this.mediaPlayer = MediaPlayer.create(this, this.beep[0]);
                }
                this.mediaPlayer.start();
            }
            this.txtx.setText(String.valueOf((int) f));
            this.txty.setText(String.valueOf((int) f2));
            this.txtz.setText(String.valueOf((int) f3));
            TextView textView = this.value;
            textView.setText(sqrt + " µT");
            double d = (double) sqrt;
            Double.isNaN(d);
            Gauge gauge2 = this.gauge;
            Double.isNaN(d);
            gauge2.speedTo((float) ((long) ((d / 2000.0d) * 100.0d)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SensorManager sensorManager2 = this.sensorManager;
        sensorManager2.registerListener(this, sensorManager2.getDefaultSensor(2), 3);

        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this); SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
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
