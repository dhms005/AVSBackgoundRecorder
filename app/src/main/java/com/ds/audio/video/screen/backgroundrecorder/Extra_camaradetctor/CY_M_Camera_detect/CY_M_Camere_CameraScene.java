package com.ds.audio.video.screen.backgroundrecorder.Extra_camaradetctor.CY_M_Camera_detect;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class CY_M_Camere_CameraScene extends AppCompatActivity implements SurfaceHolder.Callback {
    Camera l;
    SurfaceView m;
    SeekBar n;
    Camera.Parameters o;
    TextView p;
    TextView q;
    LinearLayout mNativeBannerAd;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.cy_m_camere_activity_camera_scene);

        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }

        this.m = (SurfaceView) findViewById(R.id.preview);
        this.m.getHolder().setType(3);
        this.m.getHolder().addCallback(this);
        try {
            this.l = Camera.open();
            this.l.setDisplayOrientation(90);
            this.o = this.l.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        this.n = (SeekBar) findViewById(R.id.seekbar);
        this.n.setProgress(0);
        this.p = (TextView) findViewById(R.id.minusZoom);
        this.q = (TextView) findViewById(R.id.plusZoom);
        try {
            this.n.setMax(this.o.getMaxZoom());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (n.getProgress() > 10) {
                    n.setProgress(n.getProgress() - 10);
                } else {
                    n.setProgress(0);
                }
            }
        });
        this.q.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (n.getProgress() < o.getMaxZoom() - 10) {
                    n.setProgress(n.getProgress() + 10);
                } else {
                    n.setProgress(o.getMaxZoom());
                }
            }
        });
        this.n.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Log.d("meralog", "progress:" + i);
                if (l.getParameters().isZoomSupported()) {
                    o.setZoom(i);
//                    l.setParameters(o);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("meralog", "onStartTrackingTouch");
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("meralog", "onStartTrackingTouch");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Camera camera = this.l;
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Camera camera = this.l;
        if (camera != null) {
            camera.stopPreview();
        }

        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
    }

    public void onRestart() {
        super.onRestart();
        if (this.l != null) {
            this.l = Camera.open();
            this.l.setDisplayOrientation(90);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Camera camera = this.l;
        if (camera != null) {
            camera.release();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return true;
        }
        Log.e("down", "focusing now");
        this.l.autoFocus(null);
        return true;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Camera camera = this.l;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getSupportedPreviewSizes().get(0);
            parameters.setPreviewSize(size.width, size.height);
            this.l.setParameters(parameters);
            this.l.startPreview();
            this.l.stopPreview();
            Camera.Parameters parameters2 = this.l.getParameters();
            parameters2.setFocusMode("auto");
            this.l.setParameters(parameters2);
            try {
                this.l.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable th) {
                this.l.startPreview();
                this.l.autoFocus(null);
                throw th;
            }
            this.l.startPreview();
            this.l.autoFocus(null);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            this.l.setPreviewDisplay(this.m.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("PREVIEW", "surfaceDestroyed");
    }






    @Override
    public void onBackPressed() {
        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                CY_M_Camere_CameraScene.this.finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
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
