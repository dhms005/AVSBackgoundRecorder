package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ds.audio.video.screen.backgroundrecorder.R;

public class ExploreActivity extends AppCompatActivity {

    ImageView imgExplore1;
    ImageView imgExplore2;
    ImageView imgExplore3;
    ImageView imgExplore4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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