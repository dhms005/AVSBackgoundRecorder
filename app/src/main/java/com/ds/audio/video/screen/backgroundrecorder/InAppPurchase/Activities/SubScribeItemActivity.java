package com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.adapters.MyProductAdapter;
import com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.utils.BillingClientSetup;
import com.ds.audio.video.screen.backgroundrecorder.R;


import java.util.Arrays;
import java.util.List;

public class SubScribeItemActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener listener;
    RecyclerView recyclerView;
    TextView txtPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_scribe_item);
        setUpBillingClient();
        inita();
    }

    private void inita() {
        txtPremium = findViewById(R.id.txt_premium);
        recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
    }

    private void setUpBillingClient() {
        listener = billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                txtPremium.setVisibility(View.VISIBLE);
            }
        };

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(SubScribeItemActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();

                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                            .getPurchasesList();
                    if (purchases.size() > 0) {
                        recyclerView.setVisibility(View.GONE);
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    } else {
                        txtPremium.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loadAllSubscription();
                    }
                } else {
                    Toast.makeText(SubScribeItemActivity.this, "Error code->  " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(SubScribeItemActivity.this, "You are disconnect from billing server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllSubscription() {
        if (billingClient.isReady()) {
            SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList("3.month.ad.free","6.month.ad.free","12.month.ad.free"))
                    .setType(BillingClient.SkuType.SUBS)
                    .build();

            billingClient.querySkuDetailsAsync(skuDetailsParams, (billingResult, list) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    MyProductAdapter myProductAdapter = new MyProductAdapter(SubScribeItemActivity.this, list, billingClient);
                    recyclerView.setAdapter(myProductAdapter);
                } else {
                    Toast.makeText(SubScribeItemActivity.this, "Error code" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Billing client not ready", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleItemAlreadyPurchase(Purchase purchases) {
        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchases.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, listener);
            } else {
                txtPremium.setVisibility(View.VISIBLE);
                txtPremium.setText("your premium");
            }
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && list != null) {
            for (Purchase purchase : list) {
                handleItemAlreadyPurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "user has been cancelled", Toast.LENGTH_SHORT).show();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Toast.makeText(this, "ITEM_ALREADY_OWNED" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
        }
    }
}