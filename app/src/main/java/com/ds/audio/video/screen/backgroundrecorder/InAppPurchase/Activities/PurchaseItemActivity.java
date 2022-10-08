package com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.adapters.MyProductAdapter;
import com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.utils.BillingClientSetup;
import com.ds.audio.video.screen.backgroundrecorder.R;
import java.util.Arrays;
import java.util.List;

public class PurchaseItemActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    ConsumeResponseListener listener;
    Button loadProduct;
    RecyclerView recyclerView;
    TextView txtPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_item);
        setUpBillingClient();
        inita();
    }

    private void inita() {
        txtPremium = findViewById(R.id.txt_premium);
        loadProduct = findViewById(R.id.btn_load_product);
        recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));

        loadProduct.setOnClickListener(view -> {
            if (billingClient.isReady()) {
                SkuDetailsParams params = SkuDetailsParams.newBuilder()
                        .setSkusList(Arrays.asList("lifetime.ad.free"))
                        .setType(BillingClient.SkuType.INAPP)
                        .build();

                billingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        loadProductToRecyclerView(list);
                    } else {
                        Toast.makeText(PurchaseItemActivity.this, "Error Codee-> " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadProductToRecyclerView(List<SkuDetails> list) {
        MyProductAdapter myProductAdapter = new MyProductAdapter(this, list, billingClient);
        recyclerView.setAdapter(myProductAdapter);
    }

    private void setUpBillingClient() {
        listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Toast.makeText(PurchaseItemActivity.this, "Costume ok..", Toast.LENGTH_SHORT).show();
            }
        };

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchaseItemActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();

                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList();
                    if (purchases != null) {
                        handleItemAlreadyPurchase(purchases);
                    }
                } else {
                    Toast.makeText(PurchaseItemActivity.this, "Error code->  " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(PurchaseItemActivity.this, "You are disconnect from billing server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleItemAlreadyPurchase(List<Purchase> purchases) {
        StringBuilder purchaseItem = new StringBuilder(txtPremium.getText());
        for (Purchase purchase : purchases) {
            if (purchase.getSku().equals("lifetime.ad.free")) {
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.consumeAsync(consumeParams, listener);
            }
            purchaseItem.append("\n").append(purchase.getSku())
                    .append("\n");
        }

        txtPremium.setText(purchaseItem.toString());
        txtPremium.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && list != null) {
            handleItemAlreadyPurchase(list);
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "user has been cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error code-> " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
        }
    }
}