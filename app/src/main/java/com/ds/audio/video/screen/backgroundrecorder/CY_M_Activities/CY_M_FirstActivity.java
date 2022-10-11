package com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Custom_AppLovin_Timer;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Custom_Full_Ad_Timer;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_PrivacyPolicyActivity;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.Video_Database_Helper;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_RecomendedAppsDailogAdapter_New;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_RecomendedAppsDialog;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_RecommandDatabaseModel;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_Utility;
import com.ds.audio.video.screen.backgroundrecorder.InAppPurchase.utils.BillingClientSetup;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.facebook.ads.AdSettings;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Call_Api;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Push.FCMActivity;
import com.github.mylibrary.Notification.Push.PushUser;
import com.github.mylibrary.Notification.Server.Server;
import com.github.mylibrary.Notification.Server.ServerConfig;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lenovo on 2/1/17.
 */
public class CY_M_FirstActivity extends FCMActivity implements PurchasesUpdatedListener {

    public String api_key;
    private LinearLayout btnStart;
    private ImageView btnStart_F;
    private TextView mTextTitle, mDisclaimer, text_title_treading_app, text_title_new_update_app;
    ImageView mTextPrivacyPolicy;
    private String mediation;
    private String adDate;
    SimpleDateFormat currentFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private CY_M_RecomendedAppsDailogAdapter_New mTreadingAppAdapter;
    private CY_M_RecomendedAppsDailogAdapter_New mRecomendedAppsDailogAdapterNew;
    private RecyclerView mRecyclerViewStatic;
    private RecyclerView mRecyclerViewTreadingApp, mRecyclerViewNewUpdate;
    private LinearLayout nativeAdContainer2;
    private ImageView mMoreApp, mRateApp, mShareApp, mHeaderImage;
    private LinearLayout mCardViewTreadingApp;
    private ProgressBar progress_HistoryLoader;
    private ArrayList<CY_M_RecommandDatabaseModel> recomendedAppModelArrayList = new ArrayList<>();
    private ScrollView mScrollView;
    private TextView text_load;
    private LinearLayout mCardViewNewUpdateApp;

    private SharedPreferences preferences;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppUpdateManager mAppUpdateManager;
    private int RC_APP_UPDATE = 100;
    private LinearLayout coordinatorLayout;

    CY_M_Custom_Full_Ad_Timer ESCustom_full_ad_timer;
    CY_M_Custom_AppLovin_Timer custom_appLovin_timer;
    int _showAdTimer;
    Handler handler;
    Runnable runnable;
    Dialog downloadDialog;


    LinearLayout mNativeBannerAd;
    Dialog downloadDialogVPN;
    ProgressBar vpn_progressBar;
    TextView txtCityName;
    TextView txtStateName;
    TextView txtCountryName;
    TextView txtIpAdressName;
    TextView txtTimeZone;
    TextView txtZipcode;
    LinearLayout details;
    InstallReferrerClient referrerClient;

    BillingClient billingClient;
    Video_Database_Helper videoDatabaseHelper;


    @SuppressLint("NewApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.cy_m_first_activity);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        videoDatabaseHelper =new Video_Database_Helper(this);

        AdSettings.setDataProcessingOptions(new String[]{});
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinPrivacySettings.setHasUserConsent(true, this);
        AppLovinPrivacySettings.setIsAgeRestrictedUser(false, this);
        AppLovinPrivacySettings.setDoNotSell(false, this);

        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
            }
        });

        preferences = this.getSharedPreferences("GCM", Context.MODE_PRIVATE);
//        Log.e("api_key_1","" + api_key);
        mDeclarationMethod();
        mClickListener();
        checkForUpdate();

//        getQurekaData();
        api_key = preferences.getString(PushUser.API_KEY, "");
        if (api_key.equals("")) {
            registerMethod();
        }

        String recommandedData = SharePrefUtils.getString(Constant_ad.RECOMMANDED_DATA, "");
        String date = SharePrefUtils.getString(Constant_ad.RECOMMANDED_DATE, "");

        if (date.equals("") || recommandedData.equals("")) {
            getDataService();
        } else if (isCurrentDate(date)) {
            getDataService();
        } else {
            try {
                JSONObject recommandedObject = new JSONObject(recommandedData.toString());
                setData(recommandedObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinearLayout ll_qureka_ad = findViewById(R.id.ll_qureka_ad);
        ImageView img_qureka1 = findViewById(R.id.img_qureka1);
        ImageView img_qureka2 = findViewById(R.id.img_qureka2);
        ImageView img_qureka3 = findViewById(R.id.img_qureka3);

        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
            ll_qureka_ad.setVisibility(View.GONE);
        }
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button1)).into(img_qureka1);
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button3)).into(img_qureka2);
        Glide.with(this).load(getResources().getDrawable(R.drawable.qureka_button6)).into(img_qureka3);
        img_qureka1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CY_M_Utility.OpenCustomQurekaBrowser(CY_M_FirstActivity.this);
            }
        });
        img_qureka2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CY_M_Utility.OpenCustomQurekaBrowser(CY_M_FirstActivity.this);
            }
        });
        img_qureka3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CY_M_Utility.OpenCustomQurekaBrowser(CY_M_FirstActivity.this);
            }
        });

        CallADAPI();

        timer_ad(SharePrefUtils.getInt(Constant_ad.MINUTE, 0), SharePrefUtils.getInt(Constant_ad.AD_TOTAL_SHOW_TOME, 0));

        if (SharePrefUtils.getString(Constant_ad.STATUS, "0").equals("1")) {
            installAppDialog(SharePrefUtils.getString(Constant_ad.PATH, ""));
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork;
        boolean vpnInUse = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeNetwork = cm.getActiveNetwork();
            NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
            vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
        }
        VPNDialog();
        if (vpnInUse) {
            if (SharePrefUtils.getString(Constant_ad.VPN_DETECT, "1").equals("1")) {
                downloadDialogVPN.show();
            }
        }


        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, referrerUrl);
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                            Bundle params = new Bundle();
                            params.putString("referrerUrl", referrerUrl);
                            mFirebaseAnalytics.logEvent("install_referrer", params);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        checkPurchaseItem();
    }

    private void checkPurchaseItem() {
        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<Purchase> subPurchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
                    List<Purchase> LifeTimePurchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                    if (subPurchases.size() > 0 || LifeTimePurchases.size() > 0) {
                        for (Purchase purchase : subPurchases) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                SharePrefUtils.putBoolean(Constant_ad.IS_PURCHASE, true);
                                SharePrefUtils.putString(Constant_ad.NATIVE_SIZE, "5");
                            }
                        }
                        for (Purchase purchase : LifeTimePurchases) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                SharePrefUtils.putBoolean(Constant_ad.IS_PURCHASE, true);
                            }
                        }
                    } else {
                        SharePrefUtils.putBoolean(Constant_ad.IS_PURCHASE, false);
                        mNativeAdNew();
                        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
                            Call_banner();
                        } else {
                            mNativeBanner();
                        }
                        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "").equals("0")) {
                            ESCustom_full_ad_timer = new CY_M_MyApplication().getmanagerFullAdTimer();
                            ESCustom_full_ad_timer.reload_admob_full_Ad(CY_M_FirstActivity.this);
                            load_full_ad();
                        } else {
                            custom_appLovin_timer = new CY_M_MyApplication().getCustom_appLovin_timer();
                            custom_appLovin_timer.reload_admob_full_Ad(CY_M_FirstActivity.this);
                            load_app_Lovin_full_ad();
                        }
                    }
                } else {
                    Toast.makeText(CY_M_FirstActivity.this, "Error code->  " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
//                Toast.makeText(CY_M_FirstActivity.this, "You are disconnect from billing server", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mClickListener() {
        btnStart_F.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("btnStart_F", "btnStart_F");
                mFirebaseAnalytics.logEvent("FirstActivity", params);
                if (Permission_check(CY_M_FirstActivity.this)) {
                    CY_M_Admob_Full_AD_New.getInstance().showInter(CY_M_FirstActivity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                        @Override
                        public void callbackCall() {
                            if (SharePrefUtils.getString(Constant_ad.AD_MULTIPLE_START, "1").equals("1")) {
                                Intent i = new Intent(CY_M_FirstActivity.this, CY_M_Second_StartScreen.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(CY_M_FirstActivity.this, CY_M_ActivityHomeMenu.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });

        mTextPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("mTextPrivacyPolicy", "mTextPrivacyPolicy");
                mFirebaseAnalytics.logEvent("FirstActivity", params);
                if (!CY_M_Utility.isConnectivityAvailable(CY_M_FirstActivity.this)) {
                    Toast.makeText(CY_M_FirstActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(CY_M_FirstActivity.this, CY_M_PrivacyPolicyActivity.class);
                    startActivity(in);
                }
            }
        });


        mMoreApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + SharePrefUtils.getString(Constant_ad.ACCOUNT, ""))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + SharePrefUtils.getString(Constant_ad.ACCOUNT, ""))));
                }
            }
        });

        mRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("mRateApp", "mRateApp");
                mFirebaseAnalytics.logEvent("FirstActivity", params);

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        });

        mShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("mShareApp", "mShareApp");
                mFirebaseAnalytics.logEvent("FirstActivity", params);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

                startActivity(Intent.createChooser(share, "Share link!"));

            }
        });

        mDisclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disclaimerDialog();
            }
        });
    }

    private void disclaimerDialog() {


        new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setTitle("Disclaimer")
                .setCancelable(false)
                .setMessage(getString(R.string.disclaimer) + SharePrefUtils.getString(Constant_ad.EMAIL, "") + ".")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }
                )
                .create()
                .show();


    }


    private void mDeclarationMethod() {
        progress_HistoryLoader = (ProgressBar) findViewById(R.id.progress_HistoryLoader);

        btnStart_F = findViewById(R.id.img_start);
//        btnStart_F = (MaterialButton) findViewById(R.id.img_start);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextPrivacyPolicy = (ImageView) findViewById(R.id.text_privacy_policy);

        mMoreApp = (ImageView) findViewById(R.id.img_moreapp);
        mRateApp = (ImageView) findViewById(R.id.img_rateapp);
        mShareApp = (ImageView) findViewById(R.id.img_shareapp);
        mCardViewTreadingApp = findViewById(R.id.cardview_treading);
        mCardViewNewUpdateApp = findViewById(R.id.cardview_new_update);
        mDisclaimer = (TextView) findViewById(R.id.text_disclaimer);
        text_title_treading_app = findViewById(R.id.text_title_treading_app);
        text_title_new_update_app = findViewById(R.id.text_title_new_update_app);
        text_load = findViewById(R.id.text_load);
        nativeAdContainer2 = findViewById(R.id.hscrollContainer2);
        mHeaderImage = findViewById(R.id.img_header);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
        mScrollView.scrollTo(0, 0);

        mRecyclerViewTreadingApp = (RecyclerView) findViewById(R.id.recycler_view_trading_app);
        mRecyclerViewStatic = (RecyclerView) findViewById(R.id.recycler_view_static);
        mRecyclerViewNewUpdate = (RecyclerView) findViewById(R.id.recycler_view_new_update_app);
        mRecyclerViewStatic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewNewUpdate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTreadingApp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    private void setData(JSONObject response) {
        try {
            SharePrefUtils.putString(PushUser.APP_VERSION, response.getString("App_Version").toString());
            progress_HistoryLoader.setVisibility(View.GONE);

            try {
                JSONArray jsonArray = response.getJSONArray("RecomendedApps");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    CY_M_RecommandDatabaseModel appModel = new CY_M_RecommandDatabaseModel();
                    appModel.setName(object.getString("name"));
                    appModel.setIcon(object.getString("icon"));
                    appModel.setIcon2(object.getString("icon2"));
                    appModel.setIcon3(object.getString("icon3"));
                    appModel.setLink(object.getString("link"));
                    appModel.setType(object.getString("type"));
                    recomendedAppModelArrayList.add(appModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<CY_M_RecommandDatabaseModel> typeOneArrayList = new ArrayList<CY_M_RecommandDatabaseModel>();
            ArrayList<CY_M_RecommandDatabaseModel> typeThreeArrayList = new ArrayList<CY_M_RecommandDatabaseModel>();


            for (int i = 0; i < recomendedAppModelArrayList.size(); i++) {
                if (recomendedAppModelArrayList.get(i).getType().equals("1")) {
                    typeOneArrayList.add(recomendedAppModelArrayList.get(i));
                } else if (recomendedAppModelArrayList.get(i).getType().equals("3")) {
                    typeThreeArrayList.add(recomendedAppModelArrayList.get(i));
                }
            }

            if (typeOneArrayList.isEmpty()) {
                text_title_new_update_app.setVisibility(View.GONE);
                mCardViewNewUpdateApp.setVisibility(View.GONE);
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_EXIT_REC_APPS, "0").equals("1")) {
                    text_title_new_update_app.setVisibility(View.VISIBLE);
                    mCardViewNewUpdateApp.setVisibility(View.VISIBLE);
                } else {
                    text_title_new_update_app.setVisibility(View.GONE);
                    mCardViewNewUpdateApp.setVisibility(View.GONE);
                }
                mRecomendedAppsDailogAdapterNew = new CY_M_RecomendedAppsDailogAdapter_New(CY_M_FirstActivity.this, typeOneArrayList, 8, "1");
                mRecyclerViewNewUpdate.setAdapter(mRecomendedAppsDailogAdapterNew);
            }

            if (typeThreeArrayList.isEmpty()) {
                text_title_treading_app.setVisibility(View.GONE);
                mCardViewTreadingApp.setVisibility(View.GONE);
            } else {
                text_title_treading_app.setVisibility(View.VISIBLE);
                mCardViewTreadingApp.setVisibility(View.VISIBLE);
                mTreadingAppAdapter = new CY_M_RecomendedAppsDailogAdapter_New(CY_M_FirstActivity.this, typeThreeArrayList, 9, "2");
                mRecyclerViewTreadingApp.setAdapter(mTreadingAppAdapter);
            }


        } catch (JSONException e) {
            SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATE, "");
            e.printStackTrace();
        }
    }

    private void getDataService() {
        if (CY_M_Utility.isConnectivityAvailable(CY_M_FirstActivity.this)) {
            progress_HistoryLoader.setVisibility(View.VISIBLE);
            RequestQueue mRequestQueue = Volley.newRequestQueue(CY_M_FirstActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getAbsoluteUrl(ServerConfig.GET_RECOMMAND), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Log.e("response", "" + response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        progress_HistoryLoader.setVisibility(View.GONE);
                        SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATA, response.toString());
                        SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATE, getCurrentDate());
                        setData(jsonObject);

                    } catch (JSONException e) {

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                Log.e("goterror",""+error);
                    SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATE, "");
//                    Log.e("Error", "" + error);
                    progress_HistoryLoader.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(PushUser.APP_CODE, PushUser.APP_CODE_VALUE);
                    params.put(PushUser.APP_ANDROID_ID, "1234");
                    return params;
                }

//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
//                    params.put(PushUser.APP_API_KEY, api_key);
//                    return params;
//                }
            };

            mRequestQueue.add(stringRequest);

        }


    }

    private boolean isCurrentDate(String date) {

        String oldDate = date;
        Date strDate = null;
        try {
            strDate = currentFormat.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date strCurrentDate = null;
        try {
            strCurrentDate = currentFormat.parse(getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (strDate.compareTo(strCurrentDate) < 0) {
                System.out.println("strCurrentDate is Greater than strDate");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        String formattedDate = currentFormat.format(c.getTime());

        return formattedDate;
    }

    @Override
    public void onBackPressed() {
        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                Intent intent = new Intent(CY_M_FirstActivity.this, CY_M_RecomendedAppsDialog.class);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    public void registerMethod() {
        if (isNetworkAvailable(getApplicationContext())) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String token = instanceIdResult.getToken();
                            // Do whatever you want with your token now
                            // i.e. store it on SharedPreferences or DB
                            // or directly send it to server
                            final SharedPreferences prefs = CY_M_FirstActivity.this.getSharedPreferences("GCM",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            Server sr = new Server();
                            sr.setRegListener(new Server.RegListener() {
                                @Override
                                public void onRegSuccess(String s) {


                                }

                                @Override
                                public void onRegJsonExcetion(String s) {
                                    registerMethod();
                                }


                                @Override
                                public void onRegError(String s) {

                                    registerMethod();
                                }
                            });

                            if (token == null) {
                                Server.updateToken(CY_M_FirstActivity.this, "1234");
                                editor.putString(PushUser.TOKEN, "1234");
                                editor.commit();
                            } else if (prefs.getString(PushUser.TOKEN, "").equals("1234") && token != null) {
                                editor.putString(PushUser.TOKEN, token);
                                editor.commit();
                                Server.updateToken(CY_M_FirstActivity.this, token);
                            } else if (prefs.getString(PushUser.TOKEN, "").equals("") && token != null) {
                                editor.putString(PushUser.TOKEN, token);
                                editor.commit();
                                Server.updateToken(CY_M_FirstActivity.this, token);
                            }
                        }


                    });


                    return null;
                }
            }.execute();
        } else {
            Toast.makeText(getBaseContext(), "No Internet Available", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
//                Log.e("code", "onActivityResult: app download failed");
            }
        }
    }

    public void checkForUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, CY_M_FirstActivity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, IMMEDIATE, CY_M_FirstActivity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else {
//                Log.e("error", "checkForAppUpdateAvailability: something else");
            }
        });

    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
//                        Log.e("error", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };


    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        coordinatorLayout,
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.install_color));
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            mNativeAdNew();
        }
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
        mAppUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    mAppUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo, AppUpdateType.IMMEDIATE, CY_M_FirstActivity.this, RC_APP_UPDATE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    public void CallADAPI() {
        adDate = SharePrefUtils.getString(Constant_ad.AD_DATE, "");
        String adData = SharePrefUtils.getString(Constant_ad.AD_DATA, "");

        if (SharePrefUtils.getString(Constant_ad.AD_CALL, "0").equals("0")) {
            if (adDate.equals("") || adData.equals("")) {
                Custom_Ad_Call_Api.loadDataService(CY_M_FirstActivity.this);
            } else if (isCurrentDate(adDate)) {
                Custom_Ad_Call_Api.loadDataService(CY_M_FirstActivity.this);
            } else {

            }
        } else {
            Custom_Ad_Call_Api.loadDataService(CY_M_FirstActivity.this);
        }
    }


    public void timer_ad(int total_minute, int total_show_count) {
        _showAdTimer = (total_minute * 60000) / total_show_count;

        if (total_minute == 0) {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
        } else {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            handler = new Handler();
            runnable = new Runnable() {
                public void run() {
                    String resume_check = SharePrefUtils.getString(Constant_ad.AD_CHECK_RESUME, "1");
                    if (resume_check.equals("1")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                CY_M_Utility.isTimerAdShow = true;
                                handler.postDelayed(runnable, _showAdTimer);
                            }
                        });
                    } else {
                        handler.postDelayed(runnable, _showAdTimer);
                    }
                }
            };
            handler.postDelayed(runnable, _showAdTimer);
        }
    }

    public void load_full_ad() {

        ESCustom_full_ad_timer.setMyNativeAdListener(new CY_M_Custom_Full_Ad_Timer.MyAdViewAdListener() {
            @Override
            public void onAdClosed() {
                handler.postDelayed(runnable, _showAdTimer);
            }

            @Override
            public void onAdFailedToLoad() {
                handler.postDelayed(runnable, _showAdTimer);
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdShowing() {

            }
        });
    }


    public void load_app_Lovin_full_ad() {

        custom_appLovin_timer.setMyNativeAdListener(new CY_M_Custom_AppLovin_Timer.MyAdViewAdListener() {
            @Override
            public void onAdClosed() {
                handler.postDelayed(runnable, _showAdTimer);
            }

            @Override
            public void onAdFailedToLoad() {
//                Log.e("#DEBUG", "First ad onAdFailedToLoad");
                handler.postDelayed(runnable, _showAdTimer);
            }

            @Override
            public void onAdLeftApplication() {
//                Log.e("#DEBUG", "First ad onAdFailedToLoad");
            }

            @Override
            public void onAdLoaded() {
//                Log.e("#DEBUG", "First ad onAdLoaded");
            }

            @Override
            public void onAdOpened() {
//                Log.e("#DEBUG", "First ad onAdOpened");
            }

            @Override
            public void onAdShowing() {

            }
        });
    }


    private void installAppDialog(String path) {

        SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD, "");
        SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD2, "");
        SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD3, "");
        SharePrefUtils.putString(Constant_ad.GOOGLE_BANNER, "");
        SharePrefUtils.putString(Constant_ad.GOOGLE_NATIVE, "");
        SharePrefUtils.putString(Constant_ad.GOOGLE_REWAREDED, "");

        SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD_SPLASH, "");
        SharePrefUtils.putString(Constant_ad.FACEBOOK_FULL, "");
        SharePrefUtils.putString(Constant_ad.FACEBOOK_BANNER, "");
        SharePrefUtils.putString(Constant_ad.FACEBOOK_NATIVE, "");
        SharePrefUtils.putString(Constant_ad.OPEN_AD, "");
        SharePrefUtils.putString(Constant_ad.OPEN_AD2, "");
        SharePrefUtils.putString(Constant_ad.OPEN_AD3, "");

        downloadDialog = new Dialog(CY_M_FirstActivity.this);
        downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadDialog.setCancelable(false);
        downloadDialog.setContentView(R.layout.cy_m_dialog_installapp);
        Window window = downloadDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER | Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        downloadDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        downloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView install_now = downloadDialog.findViewById(R.id.update);

        install_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path)));

            }
        });
        downloadDialog.show();
    }


    @Override
    protected void onPause() {
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }


    public boolean Permission_check(Activity context) {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(PERMISSIONS, 2);
            }
        } else {
            return true;
        }

        return false;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.e("code123", "onActivityResult: app download failed"+requestCode);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                CY_M_Admob_Full_AD_New.getInstance().showInter(CY_M_FirstActivity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (SharePrefUtils.getString(Constant_ad.AD_MULTIPLE_START, "1").equals("1")) {
                            Intent i = new Intent(CY_M_FirstActivity.this, CY_M_Second_StartScreen.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(CY_M_FirstActivity.this, CY_M_ActivityHomeMenu.class);
                            startActivity(i);
                        }
                    }
                });
            } else {
                int permissionCount = SharePrefUtils.getInt(Constant_ad.PERMISSION_COUNT, 0) + 1;
                SharePrefUtils.putInt(Constant_ad.PERMISSION_COUNT, permissionCount);
                if (permissionCount >= 2) {
                    SharePrefUtils.putInt(Constant_ad.PERMISSION_COUNT, 0);
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void VPNDialog() {
        downloadDialogVPN = new Dialog(CY_M_FirstActivity.this);
        downloadDialogVPN.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadDialogVPN.setCancelable(false);
        downloadDialogVPN.setContentView(R.layout.cy_m_dialog_vpn_connection);
        Window window = downloadDialogVPN.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER | Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        downloadDialogVPN.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        downloadDialogVPN.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView install_now = downloadDialogVPN.findViewById(R.id.update);
        vpn_progressBar = downloadDialogVPN.findViewById(R.id.progress_circular);
        txtCityName = downloadDialogVPN.findViewById(R.id.txtCityName);
        txtStateName = downloadDialogVPN.findViewById(R.id.txtStateName);
        txtCountryName = downloadDialogVPN.findViewById(R.id.txtCountryName);
        txtIpAdressName = downloadDialogVPN.findViewById(R.id.txtIpAdressName);
        txtTimeZone = downloadDialogVPN.findViewById(R.id.txtTimeZone);
        txtZipcode = downloadDialogVPN.findViewById(R.id.txtZipcode);
        details = downloadDialogVPN.findViewById(R.id.details);

        getCityDetails();

        install_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }

    public void getCityDetails() {

        vpn_progressBar.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(CY_M_FirstActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://pro.ip-api.com/json/" + SharePrefUtils.getString(Constant_ad.CITY_IP, "") + "?key=9IH2m3RINa9XYrH", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vpn_progressBar.setVisibility(View.GONE);
                details.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    txtCityName.setText(jsonObject.getString("city"));
                    txtStateName.setText(jsonObject.getString("regionName"));
                    txtCountryName.setText(jsonObject.getString("country"));
                    txtIpAdressName.setText(jsonObject.getString("query"));
                    txtTimeZone.setText(jsonObject.getString("timezone"));
                    txtZipcode.setText(jsonObject.getString("zip"));
                } catch (JSONException e) {
                    vpn_progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                vpn_progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNative400Dp() == null) {
                    new Custom_NativeAd_Admob().showNative400DpAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheNative400DpAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNative400Dp() == null) {
                    new Custom_NativeAd_Admob().showNative400DpAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheNative400DpAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                if (CY_M_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    CY_M_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            }
        }
    }

    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);
        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_TYPE, "0").equals("0")) {
                if (custom_banner_ad.CheckAdCache() != null) {
                    custom_banner_ad.loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_adpative_banner_Ad(this, mAdView);
                }
            }
        } else {
            custom_banner_ad.reload_applovin_banner_ad(this, mAdView);
        }

    }

    private void mNativeBanner() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1") || SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_banner_1);
            findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.mNativeBannerAd).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            new Custom_NativeAd_Admob().showNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        } else {
            new Custom_NativeAd_Admob().showAppLovinNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        }

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

    }
}