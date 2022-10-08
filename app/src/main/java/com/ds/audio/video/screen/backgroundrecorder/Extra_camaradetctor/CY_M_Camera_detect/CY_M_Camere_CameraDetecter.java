package com.ds.audio.video.screen.backgroundrecorder.Extra_camaradetctor.CY_M_Camera_detect;

import android.app.Dialog;
import android.graphics.Color;
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

import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.github.anastr.speedviewlib.Gauge;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class CY_M_Camere_CameraDetecter extends AppCompatActivity implements SensorEventListener, OnChartValueSelectedListener {
    int[] beep = {R.raw.beep};
    private Gauge gauge;
    private LineChart mChart;
    MediaPlayer mediaPlayer;
    private SensorManager mySensorManager;
    private TextView sensor;
    private SensorManager sensorManager;
    private TextView status;
    String str;
    private TextView value;
    LinearLayout mNativeBannerAd;

    public void onAccuracyChanged(Sensor sensor2, int i) {
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
    }

    private void addEntry(float f) {
        LineData lineData = (LineData) this.mChart.getData();
        if (lineData != null) {
            ILineDataSet iLineDataSet = (ILineDataSet) lineData.getDataSetByIndex(0);
            if (iLineDataSet == null) {
                iLineDataSet = createSet();
                lineData.addDataSet(iLineDataSet);
            }
            lineData.addEntry(new Entry((float) iLineDataSet.getEntryCount(), f), 0);
            lineData.notifyDataChanged();
            this.mChart.notifyDataSetChanged();
            this.mChart.setVisibleXRangeMaximum(2000.0f);
            this.mChart.moveViewToX((float) lineData.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet lineDataSet = new LineDataSet(null, "Dynamic Data");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setLineWidth(2.0f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setValueTextColor(-1);
        lineDataSet.setValueTextSize(9.0f);
        lineDataSet.setDrawValues(false);
        return lineDataSet;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.cy_m_camere_activity_camera_detecter);
        ImageView back = (ImageView) findViewById(R.id.back);
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


        getWindow().setFlags(1024, 1024);
        this.sensor = (TextView) findViewById(R.id.sensor);
        this.str = "sensor";
        this.mySensorManager = (SensorManager) getSystemService(this.str);
        if (this.mySensorManager.getDefaultSensor(2) == null) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.cy_m_camere_diloge);
            dialog.setTitle("Sensor Info");
            dialog.show();
            this.sensor.setText("Sorry Your Device Does Not Support This App");
        } else {
            this.sensor.setText("Move Phone Near Suspected Devices");
        }
        this.value = (TextView) findViewById(R.id.m_value);
        this.status = (TextView) findViewById(R.id.status);
        this.gauge = (Gauge) findViewById(R.id.AwesomeSpeedometer);
        this.sensorManager = (SensorManager) getSystemService(this.str);
        this.mediaPlayer = MediaPlayer.create(this, this.beep[0]);
        LineChart lineChart = (LineChart) findViewById(R.id.mygraph);
        this.mChart = lineChart;
        lineChart.setOnChartValueSelectedListener(this);
        this.mChart.getDescription().setEnabled(true);
        this.mChart.setTouchEnabled(true);
        this.mChart.setDragEnabled(true);
        this.mChart.setScaleEnabled(true);
        this.mChart.setDrawGridBackground(false);
        this.mChart.setPinchZoom(true);
        this.mChart.setBackgroundColor(Color.parseColor("#050505"));
        LineData lineData = new LineData();
        lineData.setValueTextColor(-1);
        this.mChart.setData(lineData);
        Legend legend = this.mChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(-1);
        XAxis xAxis = this.mChart.getXAxis();
        xAxis.setTextColor(-1);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);
        YAxis axisLeft = this.mChart.getAxisLeft();
        axisLeft.setTextColor(-1);
        axisLeft.setAxisMaximum(100.0f);
        axisLeft.setAxisMinimum(0.0f);
        axisLeft.setDrawGridLines(true);
        this.mChart.getAxisRight().setEnabled(false);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        MediaPlayer mediaPlayer2;
        if (sensorEvent.sensor.getType() == 2) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            double sqrt = Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
            float f4 = (float) ((int) sqrt);
            addEntry(f4);
            this.mChart.getAxisLeft().setAxisMaximum(f4 + 40.0f);
            long j = (long) sqrt;
            if (j > 80 && j < 140) {
                if (this.mediaPlayer == null) {
                    this.mediaPlayer = MediaPlayer.create(this, this.beep[0]);
                }
                this.mediaPlayer.start();
            }
            if (j <= 80 && j >= 140 && (mediaPlayer2 = this.mediaPlayer) != null) {
                mediaPlayer2.stop();
            }
            if (j < 45) {
                this.status.setText("NOTHING DETECTED");
            } else if (j >= 45 && j <= 80) {
                this.status.setText("POTENTIAL CAMERA DETECTED");
            } else if (j > 80 && j <= 120) {
                this.status.setText("POTENTIAL CAMERA DETECTED");
            } else if (j > 120 && j <= 140) {
                this.status.setText("POTENTIAL CAMERA DETECTED");
            } else if (j > 140) {
                this.status.setText("DETECTED HIGH CAMERA RADIATIONS");
                if (this.mediaPlayer == null) {
                    this.mediaPlayer = MediaPlayer.create(this, this.beep[0]);
                }
                this.mediaPlayer.start();
            }
            TextView textView = this.value;
            textView.setText(j + " µT");
            double d = (double) j;
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
        this.sensorManager.unregisterListener(this);
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
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
