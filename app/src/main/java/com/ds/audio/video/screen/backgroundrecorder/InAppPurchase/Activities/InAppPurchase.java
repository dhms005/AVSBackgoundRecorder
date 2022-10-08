package com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ds.audio.video.screen.backgroundrecorder.R;


public class InAppPurchase extends AppCompatActivity {
    CardView btnPurchase, btnSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        inita();
    }

    private void inita() {
        btnPurchase = findViewById(R.id.cart_purchase_item);
        btnSubscribe = findViewById(R.id.cart_subscribe_item);

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InAppPurchase.this, PurchaseItemActivity.class));
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InAppPurchase.this, SubScribeItemActivity.class));
            }
        });
    }
}