package com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.utils;

import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.PurchasesUpdatedListener;

public class BillingClientSetup {
    private static BillingClient instance;

    public static BillingClient getInstance(Context context, PurchasesUpdatedListener listener) {
        return instance == null ? setUpBillingClient(context,listener):instance;
    }

    private static BillingClient setUpBillingClient(Context context, PurchasesUpdatedListener listener) {
        return BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(listener)
                .build();
    }
}
