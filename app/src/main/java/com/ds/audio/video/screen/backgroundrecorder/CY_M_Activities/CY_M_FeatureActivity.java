package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_PrivacyPolicyActivity;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class CY_M_FeatureActivity extends AppCompatActivity {

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
        setContentView(R.layout.cy_m_feature_activity);

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
                Intent i = new Intent(CY_M_FeatureActivity.this, CY_M_PermissionScreen.class);
                startActivity(i);
            }
        });
        tvTurmCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CY_M_Utility.isConnectivityAvailable(CY_M_FeatureActivity.this)) {
                    Toast.makeText(CY_M_FeatureActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(CY_M_FeatureActivity.this, CY_M_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
            }
        });
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CY_M_Utility.isConnectivityAvailable(CY_M_FeatureActivity.this)) {
                    Toast.makeText(CY_M_FeatureActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(CY_M_FeatureActivity.this, CY_M_PrivacyPolicyActivity.class);
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