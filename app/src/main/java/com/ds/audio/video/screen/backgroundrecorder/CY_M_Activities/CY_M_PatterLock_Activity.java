package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_Setting_Activity;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.itsxtt.patternlock.PatternLockView;

import java.util.ArrayList;

public class CY_M_PatterLock_Activity extends AppCompatActivity {

    TextView tvPatterStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cy_mpatter_lock);
        tvPatterStatus = findViewById(R.id.tvPatterStatus);
        PatternLockView patternLockView = findViewById(R.id.patternLockView);

        if (SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, "").equals("")) {
            tvPatterStatus.setText(R.string.str_enter_pattern);
        } else {
            tvPatterStatus.setText(R.string.str_confirm_pattern);
        }

        patternLockView.setOnPatternListener(new PatternLockView.OnPatternListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(ArrayList<Integer> ids) {

            }

            @Override
            public boolean onComplete(ArrayList<Integer> ids) {
                /*
                 * A return value required
                 * if the pattern is not correct and you'd like change the pattern to error state, return false
                 * otherwise return true
                 */
                if (SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, "").equals("")) {
                    SharePrefUtils.putString(Constant_ad.PATTERN_NUMBER, getPatternString(ids));
                    Intent intent = new Intent(CY_M_PatterLock_Activity.this, CY_M_PatterLock_Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, "").equals(getPatternString(ids))) {
                        Toast.makeText(CY_M_PatterLock_Activity.this, "Pattern Match", Toast.LENGTH_SHORT).show();
                        SharePrefUtils.putBoolean(Constant_ad.PATTERN_CONFIRM, true);
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CY_M_PatterLock_Activity.this);
                        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
                        editor.putBoolean("prefAppLock", true);
                        editor.commit();
                        Intent intent = new Intent(CY_M_PatterLock_Activity.this, Video_Setting_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CY_M_PatterLock_Activity.this, "Pattern not Match", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });
    }

    public String getPatternString(ArrayList<Integer> ids) {
        String result = "";
        for (int id : ids) {
            result += id;
        }
        return result;
    }
}