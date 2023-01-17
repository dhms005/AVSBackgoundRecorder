package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_PrivacyPolicyActivity;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class DevSpy_FeatureActivity extends AppCompatActivity {

    TextView tvHeading;
    TextView tvDecline;
    TextView tvAccept;
    TextView tvTurmCondition;
    TextView tvPrivacyPolicy;

    final int[] array = {R.string.str_fea_press_valume_key,
            R.string.str_fea_schedule_recording,
            R.string.str_fea_screenshot};


    private Handler handler;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.devspy_feature_activity);

        findViews();
        clickListeners();

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvHeading.setText(array[i]);
                     if (i >= array.length-1) {
                        i = 0;
                    } else {
                        i++;
                    }
                }
            });
            handler.postDelayed(runnable, 1000);
        }
    };

    private void clickListeners() {
        tvDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                finishAffinity();
            }
        });
        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                SharePrefUtils.putBoolean(Constant_ad.FEATURE_SCREEN, true);
                Intent i = new Intent(DevSpy_FeatureActivity.this, DevSpy_PermissionScreen.class);
                startActivity(i);
            }
        });
        tvTurmCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DevSpy_Utility.isConnectivityAvailable(DevSpy_FeatureActivity.this)) {
                    Toast.makeText(DevSpy_FeatureActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(DevSpy_FeatureActivity.this, DevSpy_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
            }
        });
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DevSpy_Utility.isConnectivityAvailable(DevSpy_FeatureActivity.this)) {
                    Toast.makeText(DevSpy_FeatureActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(DevSpy_FeatureActivity.this, DevSpy_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
            }
        });

    }

    private void findViews() {
        tvHeading = findViewById(R.id.tvHeading);
        tvDecline = findViewById(R.id.tvDecline);
        tvAccept = findViewById(R.id.tvAccept);
        tvTurmCondition = findViewById(R.id.tvTurmCondition);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
    }
}