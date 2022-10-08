package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.ds.audio.video.screen.backgroundrecorder.Utils.CY_M_SharedPref;
import com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.utils.BillingClientSetup;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.util.Arrays;
import java.util.List;

public class InAppPurchaseActivity extends AppCompatActivity implements PurchasesUpdatedListener {


    ConstraintLayout tvPayment;
    TextView tvSkipNow,tv_terms_of_service;
    ImageView imgClose;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener listener;

    String month3sub = "3.month.ad.free";
    String month6sub = "6.month.ad.free";
    String month12sub = "12.month.ad.free";
    String lifeTime = "lifetime.ad.free";

    String purchaseID = "";

    LinearLayout ll3months, ll6months, ll9months, llliftimemonths;
    TextView tv_3month_title, tv_6month_title, tv_9month_title, tv_lifetime_title;
    TextView tv_3month_des, tv_6month_des, tv_9month_des, tv_lifetime_des;
    TextView tv_3month_price, tv_6month_price, tv_9month_price, tv_lifetime_price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cy_m_activity_inapp_purchase);

        findViews();
        ll3month_selected();
        clickLeitner();
        setUpBillingClient();
    }

    private void clickLeitner() {
        tvPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (purchaseID.equals("")) {
                    Toast.makeText(InAppPurchaseActivity.this, "Please select any one", Toast.LENGTH_SHORT).show();
                } else {
                    if (lifeTime.equals(purchaseID)) {
                        loadLifeTimePurchase(purchaseID);
                    } else {
                        loadAllSubscription(purchaseID);
                    }

                }
            }
        });

        tvSkipNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                disclaimerDialog();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ll3months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll3month_selected();
            }
        });

        ll6months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll6month_selected();
            }
        });

        ll9months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll9month_selected();
            }
        });

        llliftimemonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liftime_selected();
            }
        });
        tv_terms_of_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disclaimerDialog();
            }
        });
    }


    public void ll3month_selected() {

        purchaseID = month3sub;

        ll3months.setBackgroundResource(R.drawable.pur_payment_border_selected);
        ll6months.setBackgroundResource(R.drawable.pur_payment_border);
        ll9months.setBackgroundResource(R.drawable.pur_payment_border);
        llliftimemonths.setBackgroundResource(R.drawable.pur_payment_border);

        tv_3month_title.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_3month_des.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_3month_price.setTextColor(getResources().getColor(R.color.colorPrimary));

        tv_6month_title.setTextColor(getResources().getColor(R.color.white));
        tv_6month_des.setTextColor(getResources().getColor(R.color.white));
        tv_6month_price.setTextColor(getResources().getColor(R.color.white));

        tv_9month_title.setTextColor(getResources().getColor(R.color.white));
        tv_9month_des.setTextColor(getResources().getColor(R.color.white));
        tv_9month_price.setTextColor(getResources().getColor(R.color.white));


        tv_lifetime_title.setTextColor(getResources().getColor(R.color.white));
        tv_lifetime_des.setTextColor(getResources().getColor(R.color.white));
        tv_lifetime_price.setTextColor(getResources().getColor(R.color.white));

    }

    public void ll6month_selected() {

        purchaseID = month6sub;

        ll3months.setBackgroundResource(R.drawable.pur_payment_border);
        ll6months.setBackgroundResource(R.drawable.pur_payment_border_selected);
        ll9months.setBackgroundResource(R.drawable.pur_payment_border);
        llliftimemonths.setBackgroundResource(R.drawable.pur_payment_border);

        tv_3month_title.setTextColor(getResources().getColor(R.color.white));
        tv_3month_des.setTextColor(getResources().getColor(R.color.white));
        tv_3month_price.setTextColor(getResources().getColor(R.color.white));

        tv_6month_title.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_6month_des.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_6month_price.setTextColor(getResources().getColor(R.color.colorPrimary));

        tv_9month_title.setTextColor(getResources().getColor(R.color.white));
        tv_9month_des.setTextColor(getResources().getColor(R.color.white));
        tv_9month_price.setTextColor(getResources().getColor(R.color.white));


        tv_lifetime_title.setTextColor(getResources().getColor(R.color.white));
        tv_lifetime_des.setTextColor(getResources().getColor(R.color.white));
        tv_lifetime_price.setTextColor(getResources().getColor(R.color.white));
    }

    public void ll9month_selected() {

        purchaseID = month12sub;

        ll3months.setBackgroundResource(R.drawable.pur_payment_border);
        ll6months.setBackgroundResource(R.drawable.pur_payment_border);
        ll9months.setBackgroundResource(R.drawable.pur_payment_border_selected);
        llliftimemonths.setBackgroundResource(R.drawable.pur_payment_border);

        tv_3month_title.setTextColor(getResources().getColor(R.color.white));
        tv_3month_des.setTextColor(getResources().getColor(R.color.white));
        tv_3month_price.setTextColor(getResources().getColor(R.color.white));

        tv_6month_title.setTextColor(getResources().getColor(R.color.white));
        tv_6month_des.setTextColor(getResources().getColor(R.color.white));
        tv_6month_price.setTextColor(getResources().getColor(R.color.white));

        tv_9month_title.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_9month_des.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_9month_price.setTextColor(getResources().getColor(R.color.colorPrimary));


        tv_lifetime_title.setTextColor(getResources().getColor(R.color.white));
        tv_lifetime_des.setTextColor(getResources().getColor(R.color.white));
        tv_lifetime_price.setTextColor(getResources().getColor(R.color.white));
    }

    public void liftime_selected() {

        purchaseID = lifeTime;

        ll3months.setBackgroundResource(R.drawable.pur_payment_border);
        ll6months.setBackgroundResource(R.drawable.pur_payment_border);
        ll9months.setBackgroundResource(R.drawable.pur_payment_border);
        llliftimemonths.setBackgroundResource(R.drawable.pur_payment_border_selected);

        tv_3month_title.setTextColor(getResources().getColor(R.color.white));
        tv_3month_des.setTextColor(getResources().getColor(R.color.white));
        tv_3month_price.setTextColor(getResources().getColor(R.color.white));

        tv_6month_title.setTextColor(getResources().getColor(R.color.white));
        tv_6month_des.setTextColor(getResources().getColor(R.color.white));
        tv_6month_price.setTextColor(getResources().getColor(R.color.white));

        tv_9month_title.setTextColor(getResources().getColor(R.color.white));
        tv_9month_des.setTextColor(getResources().getColor(R.color.white));
        tv_9month_price.setTextColor(getResources().getColor(R.color.white));


        tv_lifetime_title.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_lifetime_des.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_lifetime_price.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void findViews() {

        tvPayment = findViewById(R.id.tvPayment);
        tvSkipNow = findViewById(R.id.tvSkipNow);
        tv_terms_of_service = findViewById(R.id.tv_terms_of_service);
        imgClose = findViewById(R.id.imgClose);
        ll3months = findViewById(R.id.ll3months);
        ll6months = findViewById(R.id.ll6months);
        ll9months = findViewById(R.id.ll9months);
        llliftimemonths = findViewById(R.id.llliftimemonths);

        tv_3month_title = findViewById(R.id.tv_3month_title);
        tv_6month_title = findViewById(R.id.tv_6month_title);
        tv_9month_title = findViewById(R.id.tv_9month_title);
        tv_lifetime_title = findViewById(R.id.tv_lifetime_title);

        tv_3month_des = findViewById(R.id.tv_3month_des);
        tv_6month_des = findViewById(R.id.tv_6month_des);
        tv_9month_des = findViewById(R.id.tv_9month_des);
        tv_lifetime_des = findViewById(R.id.tv_lifetime_des);

        tv_3month_price = findViewById(R.id.tv_3month_price);
        tv_6month_price = findViewById(R.id.tv_6month_price);
        tv_9month_price = findViewById(R.id.tv_9month_price);
        tv_lifetime_price = findViewById(R.id.tv_lifetime_price);
    }

    private void setUpBillingClient() {
        listener = billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                txtPremium.setVisibility(View.VISIBLE);
            }
        };

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
                    if (purchases.size() > 0) {
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    }
                } else {
                    Toast.makeText(InAppPurchaseActivity.this, "Error code->  " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
//                Toast.makeText(InAppPurchaseActivity.this, "You are disconnect from billing server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleItemAlreadyPurchase(Purchase purchases) {
        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchases.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, listener);
            }
        }
    }

    private void loadAllSubscription(String subscriptionId) {
        if (billingClient.isReady()) {
            SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList(subscriptionId))
                    .setType(BillingClient.SkuType.SUBS)
                    .build();
            billingClient.querySkuDetailsAsync(skuDetailsParams, (billingResult, list) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(list.get(0))
                            .build();
                    int response = billingClient.launchBillingFlow(InAppPurchaseActivity.this, billingFlowParams).getResponseCode();

                    showMessage(response);
                } else {
                    Toast.makeText(InAppPurchaseActivity.this, "Please try again after sometime...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Billing client not ready", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLifeTimePurchase(String purchaseID) {
        if (billingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList(purchaseID))
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(list.get(0))
                            .build();

                    int response = billingClient.launchBillingFlow(InAppPurchaseActivity.this, billingFlowParams).getResponseCode();
                    showMessage(response);
                } else {
                    Toast.makeText(InAppPurchaseActivity.this, "Error Codee-> " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for (Purchase purchase : list) {
                handleItemAlreadyPurchase(purchase);
            }
            Intent intent = new Intent(InAppPurchaseActivity.this, CY_M_FirstActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "purchase completed", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage(int response) {
        switch (response) {
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                Toast.makeText(InAppPurchaseActivity.this, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Toast.makeText(InAppPurchaseActivity.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                Toast.makeText(InAppPurchaseActivity.this, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Toast.makeText(InAppPurchaseActivity.this, "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                Toast.makeText(InAppPurchaseActivity.this, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                Toast.makeText(InAppPurchaseActivity.this, "SERVICE_TIMEOUT", Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                Toast.makeText(InAppPurchaseActivity.this, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void disclaimerDialog() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                    .setTitle("Terms of Service")
                    .setCancelable(true)
                    .setMessage(Html.fromHtml(CY_M_SharedPref.TERMS_OF_SERVICES, Html.FROM_HTML_MODE_COMPACT))
                    .create()
                    .show();
        }else {
            new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                    .setTitle("Terms of Service")
                    .setCancelable(true)
                    .setMessage(Html.fromHtml(CY_M_SharedPref.TERMS_OF_SERVICES))
                    .create()
                    .show();
        }
    }
}