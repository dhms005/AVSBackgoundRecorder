package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class ExploreActivity extends AppCompatActivity {

    ImageView imgExplore1;
    ImageView imgExplore2;
    ImageView imgExplore3;
    ImageView imgExplore4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.activity_explore);

        findViews();
        clickEvents();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void clickEvents() {
        imgExplore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExploreActivity.this,InAppPurchaseActivity.class));
            }
        });
        imgExplore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExploreActivity.this,InAppPurchaseActivity.class));
            }
        });
        imgExplore3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExploreActivity.this,InAppPurchaseActivity.class));
            }
        });
        imgExplore4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExploreActivity.this,InAppPurchaseActivity.class));
            }
        });

    }

    private void findViews() {
        imgExplore1 = findViewById(R.id.imgExplore1);
        imgExplore2 = findViewById(R.id.imgExplore2);
        imgExplore3 = findViewById(R.id.imgExplore3);
        imgExplore4 = findViewById(R.id.imgExplore4);
    }
}