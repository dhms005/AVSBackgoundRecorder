package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_Setting_Activity;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.itsxtt.patternlock.PatternLockView;

import java.util.ArrayList;

public class CY_M_PatterLock_FirstScreen extends AppCompatActivity {

    TextView tvPatterStatus;
    String _from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.activity_cy_mpatter_lock);

        Intent intent = getIntent();
        _from = intent.getStringExtra("from");

        tvPatterStatus = findViewById(R.id.tvPatterStatus);
        PatternLockView patternLockView = findViewById(R.id.patternLockView);

        if (_from.equals("splash")) {
            tvPatterStatus.setText(R.string.str_enter_pattern);
        } else if (_from.equals("change_pattern")) {
            tvPatterStatus.setText(R.string.str_enter_old_pattern);
        } else {
            tvPatterStatus.setText(R.string.str_enter_pattern);
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
                if (_from.equals("splash")) {
                    if (getPatternString(ids).equals(SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, ""))) {
                        Intent intent = new Intent(CY_M_PatterLock_FirstScreen.this, CY_M_FirstActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CY_M_PatterLock_FirstScreen.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
                    }
                } else if (_from.equals("change_pattern")) {
                    if (getPatternString(ids).equals(SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, ""))) {
                        SharePrefUtils.putString(Constant_ad.PATTERN_NUMBER, "");
                        SharePrefUtils.putBoolean(Constant_ad.PATTERN_CONFIRM, false);
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CY_M_PatterLock_FirstScreen.this);
                        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
                        editor.putBoolean("prefAppLock", false);
                        editor.commit();
                        Intent intent = new Intent(CY_M_PatterLock_FirstScreen.this, CY_M_PatterLock_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CY_M_PatterLock_FirstScreen.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getPatternString(ids).equals(SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, ""))) {
                        SharePrefUtils.putString(Constant_ad.PATTERN_NUMBER, "");
                        SharePrefUtils.putBoolean(Constant_ad.PATTERN_CONFIRM, false);
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CY_M_PatterLock_FirstScreen.this);
                        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
                        editor.putBoolean("prefAppLock", false);
                        editor.commit();
                        Intent intent = new Intent(CY_M_PatterLock_FirstScreen.this, Video_Setting_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CY_M_PatterLock_FirstScreen.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
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