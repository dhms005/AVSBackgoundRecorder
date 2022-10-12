package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import static com.github.mylibrary.Notification.Push.FCMActivity.isNetworkAvailable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_AdmobSplash_OpenAd;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_CommonFunction;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_MyApplication;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Operators;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Push.PushUser;
import com.github.mylibrary.Notification.Server.ServerConfig;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DevSpy_Master_SplashScreen extends AppCompatActivity {

    private String mediation;
    private ImageView mImgLoader;
    private FirebaseAnalytics mFirebaseAnalytics;
    //    private AVLoadingIndicatorView avi;animation_view
    Animation uptodown, downtoup;
    private ImageView img_name, img;
    int i = 0;
    Timer timer;
    TimerTask timerTask;

    public static boolean isSplashLoading = false;
    public static Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();

    public static Custom_NativeAd_Admob DIGICustom_nativeAd_admob = new Custom_NativeAd_Admob();

    Dialog downloadDialog;
    ImageView mHeaderImage;
    private LinearLayout nativeAdContainer;
    private LinearLayout mNativeBannerAd;
    RelativeLayout rl_static_layout_12;

    private String adDate;
    SimpleDateFormat currentFormat = new SimpleDateFormat("dd-MMM-yyyy");


    private int currentApiVersion;
    String publicIP = "";
    boolean vpnInUse = false;


    private DevSpy_AdmobSplash_OpenAd appOpenAdManager;
    public static Custom_NativeAd_Admob custom_nativeAd_admob = new Custom_NativeAd_Admob();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView((int) R.layout.devspy_activity_splash_screen);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SharePrefUtils.putInt(Constant_ad.AD_STOP, 0);
        mediation = SharePrefUtils.getString(Constant_ad.MEDIATION, "0");
        if (isNetworkAvailable(DevSpy_Master_SplashScreen.this)) {
            loadDataTxt(DevSpy_Master_SplashScreen.this, false);
            GetPublicIP();
        } else {
            Internet_dialog();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    public void Internet_dialog() {
        final Dialog dialog = new Dialog(DevSpy_Master_SplashScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.devspy_m_internet);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        ImageView txtCancel = (ImageView) dialog.findViewById(R.id.no);
        TextView txtOk = (TextView) dialog.findViewById(R.id.yes);
        TextView action_settings = (TextView) dialog.findViewById(R.id.action_settings);
        action_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                check_internet();
            }
        });

//        txtCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//
//
//            }
//        });
        dialog.show();
    }


    private void check_internet() {
        if (isNetworkAvailable(DevSpy_Master_SplashScreen.this)) {
            loadDataTxt(DevSpy_Master_SplashScreen.this, false);
            GetPublicIP();
        } else {
            Internet_dialog();
        }
    }

    public void loadDataService(final Context context) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerConfig.BASE_URL + ServerConfig.GET_AD_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonResponse = jsonObject.getJSONObject("ResponseState");
                    status = jsonResponse.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("0")) {
                    if (SharePrefUtils.getString(Constant_ad.TEXT_RESPONSE, "").equals("")) {
                        loadDataTxt(context, true);
                    } else {
                        decryptionData(SharePrefUtils.getString(Constant_ad.TEXT_RESPONSE, ""));
                    }
                } else {
                    decryptionData(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (SharePrefUtils.getString(Constant_ad.TEXT_RESPONSE, "").equals("")) {
                    loadDataTxt(context, true);
                } else {
                    decryptionData(SharePrefUtils.getString(Constant_ad.TEXT_RESPONSE, ""));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PushUser.APP_CODE, PushUser.APP_CODE_VALUE);
                if (!vpnInUse) {
                    params.put(PushUser.IP_ADDRESS, SharePrefUtils.getString(Constant_ad.CITY_IP, "0.0.0.0"));
                } else {
                    params.put(PushUser.IP_ADDRESS, SharePrefUtils.getString(Constant_ad.VPN_IP, "0.0.0.0"));
                }
//                params.put(PushUser.IP_ADDRESS, SharePrefUtils.getString(Constant_ad.CITY_IP, "0.0.0.0"));
                return params;
            }
        };

        mRequestQueue.getCache().clear();
        mRequestQueue.add(stringRequest);

    }

    public void loadDataTxt(final Context context, boolean fromAPI) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerConfig.BASE_URLTxt + PushUser.APP_CODE_VALUE + ".txt", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (fromAPI) {
                    decryptionData(response);
                } else {
                    SharePrefUtils.putString(Constant_ad.TEXT_RESPONSE, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        mRequestQueue.getCache().clear();
        mRequestQueue.add(stringRequest);
    }

    public void setData(JSONObject response) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(DevSpy_Master_SplashScreen.this);

        try {

            Log.e("@@@", "" + response.toString());
            JSONObject ad_ids = response.getJSONObject("ad_ids");

            String mediation = ad_ids.getString("mediation");
            String google_fullad = ad_ids.getString("google_fullad");

            String google_fullad_2 = ad_ids.getString("google_fullad_2");
            String google_fullad_3 = ad_ids.getString("google_fullad_3");
            String google_banner = ad_ids.getString("google_banner");
            String google_native = ad_ids.getString("google_native");
            String google_native_banner = ad_ids.getString("google_native_banner");
            String google_splash_ad = ad_ids.getString("google_fullad_splash");
            String rewared_ad = ad_ids.getString("google_reward_ad");
            String series_id = ad_ids.getString("fb_dialog");
            String fb_full_ad = ad_ids.getString("fb_full_ad");
            String fb_banner = ad_ids.getString("fb_banner");
            String fb_full_native = ad_ids.getString("fb_full_native");
            String open_ad_id = ad_ids.getString("google_appopen");
            String google_appopen_2 = ad_ids.getString("google_appopen_2");
            String google_appopen_3 = ad_ids.getString("google_appopen_3");
            String in_house = ad_ids.getString("in_house");

            SharePrefUtils.putString(Constant_ad.MEDIATION, mediation);
            SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD, google_fullad);
            SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD2, google_fullad_2);
            SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD3, google_fullad_3);
            SharePrefUtils.putString(Constant_ad.GOOGLE_BANNER, google_banner);
            SharePrefUtils.putString(Constant_ad.GOOGLE_NATIVE, google_native);
            SharePrefUtils.putString(Constant_ad.GOOGLE_NATIVE_BANNER, google_native_banner);
            SharePrefUtils.putString(Constant_ad.GOOGLE_REWAREDED, rewared_ad);

            SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD_SPLASH, google_splash_ad);
//            if (!series_id.equals("")) {
//                SharePrefUtils.putString(Constant_ad.SERIES_POINTTABLE_ID, series_id);
//            }
            SharePrefUtils.putString(Constant_ad.FACEBOOK_FULL, fb_full_ad);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_BANNER, fb_banner);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_NATIVE, fb_full_native);
            SharePrefUtils.putString(Constant_ad.OPEN_AD, open_ad_id);
            SharePrefUtils.putString(Constant_ad.OPEN_AD2, google_appopen_2);
            SharePrefUtils.putString(Constant_ad.OPEN_AD3, google_appopen_3);

            SharePrefUtils.putString(Constant_ad.IN_HOUSE, in_house);
            SharePrefUtils.putString(Constant_ad.OPEN_INTER, ad_ids.getString("open_inter"));
            SharePrefUtils.putString(Constant_ad.AD_DIALOGUE, ad_ids.getString("ad_dialogue"));

//          JSONObject ads = response.getJSONObject("ads");

            SharePrefUtils.putString(Constant_ad.ACCOUNT, ad_ids.getString("ac_name"));
            SharePrefUtils.putString(Constant_ad.EMAIL, ad_ids.getString("email"));
            SharePrefUtils.putString(Constant_ad.AD_CALL, ad_ids.getString("ad_call"));

            SharePrefUtils.putString(Constant_ad.AD_QUREKA, ad_ids.getString("qureka_url"));
            SharePrefUtils.putString(Constant_ad.AD_QUREKA_Ad, ad_ids.getString("qureka_ad"));
            SharePrefUtils.putString(Constant_ad.AD_EXIT_NATIVE, ad_ids.getString("exit_native_ad"));

            SharePrefUtils.putString(Constant_ad.NATIVE_SIZE, ad_ids.getString("native_size"));

            SharePrefUtils.putString(Constant_ad.AD_NAV_BAR, ad_ids.getString("nav_bar"));
            SharePrefUtils.putString(Constant_ad.AD_BANNER_TYPE, ad_ids.getString("adptive_banner"));
            SharePrefUtils.putString(Constant_ad.AD_BANNER_NATIVE, ad_ids.getString("banner_native"));
            SharePrefUtils.putString(Constant_ad.VPN_DETECT, ad_ids.getString("vpn_detect"));

            SharePrefUtils.putString(Constant_ad.AD_BUTTON_ANIM, ad_ids.getString("anim"));
            SharePrefUtils.putInt(Constant_ad.AD_BACK_TOTAL_COUNT, Integer.parseInt(ad_ids.getString("back_ad_count")));
            SharePrefUtils.putInt(Constant_ad.AD_COUNT, Integer.parseInt(ad_ids.getString("forward_ad_count")));

            SharePrefUtils.putInt(Constant_ad.AD_BACK_TYPE, Integer.parseInt(ad_ids.getString("back_ad")));
            SharePrefUtils.putInt(Constant_ad.AD_FORWARD_TYPE, Integer.parseInt(ad_ids.getString("forward_ad")));

            SharePrefUtils.putInt(Constant_ad.POSITION1, Integer.parseInt(ad_ids.getString("position1")));
            SharePrefUtils.putInt(Constant_ad.POSITION2, Integer.parseInt(ad_ids.getString("position2")));
            SharePrefUtils.putInt(Constant_ad.POSITION3, Integer.parseInt(ad_ids.getString("position3")));
            SharePrefUtils.putString(Constant_ad.START_SCREEN_COUNT, ad_ids.getString("start_screen"));
            SharePrefUtils.putString(Constant_ad.START_SCREEN_TEXTs, ad_ids.getString("start_texts"));
            SharePrefUtils.putString(Constant_ad.NATIVE_COLOR, "#" + ad_ids.getString("native_color"));

            SharePrefUtils.putString(Constant_ad.START_TIME, ad_ids.getString("native_start_time"));
            SharePrefUtils.putString(Constant_ad.END_TIME, ad_ids.getString("native_end_time"));

            String _startTime = ad_ids.getString("native_start_time");
            String _endTime = ad_ids.getString("native_end_time");

            DevSpy_CommonFunction.checkBetweenTime(_startTime, _endTime);

            SharePrefUtils.putInt(Constant_ad.MINUTE, Integer.parseInt(ad_ids.getString("xminute")));
            SharePrefUtils.putInt(Constant_ad.AD_TOTAL_SHOW_TOME, Integer.parseInt(ad_ids.getString("xcount")));

            SharePrefUtils.putString(Constant_ad.PATH, ad_ids.getString("path"));
            SharePrefUtils.putString(Constant_ad.STATUS, ad_ids.getString("app_status"));
            SharePrefUtils.putInt(Constant_ad.NATIVEAD_TRASPARENT, Integer.parseInt(ad_ids.getString("transparent")));

            SharePrefUtils.putString(Constant_ad.AD_EXIT_REC_APPS, ad_ids.getString("rec_apps"));
            SharePrefUtils.putString(Constant_ad.AD_NATIVE_PRE_LOAD, ad_ids.getString("native_pre_load"));
            SharePrefUtils.putString(Constant_ad.AD_MULTIPLE_START, ad_ids.getString("multiple_start"));

            SharePrefUtils.putString(Constant_ad.AD_STATIC_AD_TITLE, "");
            SharePrefUtils.putString(Constant_ad.AD_STATIC_AD_BODY, "");
            SharePrefUtils.putString(Constant_ad.AD_STATIC_AD_ICON, "");
            SharePrefUtils.putString(Constant_ad.AD_STATIC_AD_BANNER_ICON, "");
            SharePrefUtils.putString(Constant_ad.AD_STATIC_AD_PATH, "");


            appOpenAdManager = new DevSpy_AdmobSplash_OpenAd(DevSpy_Master_SplashScreen.this);

            if (SharePrefUtils.getBoolean(Constant_ad.FIRST_TIME, true)) {
                DevSpy_MyApplication.getInstance().callAd();
            }

//            DIGICustom_nativeAd_admob.showBigNativeAds(this, findViewById(R.id.Admob_Native_Frame_two));
//            DIGICustom_nativeAd_admob.showNativeSmallAds(this, findViewById(R.id.Admob_Native_Frame_two));
            custom_nativeAd_admob.showBigNativeAds(this, findViewById(R.id.Admob_Native_Frame_two));
            custom_nativeAd_admob.showNativeSmallAds(this, findViewById(R.id.Admob_Native_Frame_two));


            if (SharePrefUtils.getInt(Constant_ad.POSITION1, 0) == 0 &&
                    SharePrefUtils.getInt(Constant_ad.POSITION2, 0) == 0) {
                DevSpy_Admob_Full_AD_New.getInstance().loadInterOne(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadInterTwo(DevSpy_Master_SplashScreen.this);
            } else if (SharePrefUtils.getInt(Constant_ad.POSITION1, 0) == 0 &&
                    SharePrefUtils.getInt(Constant_ad.POSITION2, 0) == 1) {
                DevSpy_Admob_Full_AD_New.getInstance().loadInterOne(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadInterTwo(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadOpenAdOne(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadOpenAdTwo(DevSpy_Master_SplashScreen.this);
            } else if (SharePrefUtils.getInt(Constant_ad.POSITION1, 0) == 1 &&
                    SharePrefUtils.getInt(Constant_ad.POSITION2, 0) == 0) {
                DevSpy_Admob_Full_AD_New.getInstance().loadInterOne(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadInterTwo(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadOpenAdOne(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadOpenAdTwo(DevSpy_Master_SplashScreen.this);
            } else if (SharePrefUtils.getInt(Constant_ad.POSITION1, 0) == 1 &&
                    SharePrefUtils.getInt(Constant_ad.POSITION2, 0) == 1) {
                DevSpy_Admob_Full_AD_New.getInstance().loadOpenAdOne(DevSpy_Master_SplashScreen.this);
                DevSpy_Admob_Full_AD_New.getInstance().loadOpenAdTwo(DevSpy_Master_SplashScreen.this);
                if (SharePrefUtils.getString(Constant_ad.OPEN_INTER, "0").equals("1")) {
                    DevSpy_Admob_Full_AD_New.getInstance().loadInterOne(DevSpy_Master_SplashScreen.this);
                    DevSpy_Admob_Full_AD_New.getInstance().loadInterTwo(DevSpy_Master_SplashScreen.this);
                }
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    appOpenAdManager.showAdIfAvailable(DevSpy_Master_SplashScreen.this,
                            new DevSpy_MyApplication.OnShowAdCompleteListener() {
                                @Override
                                public void onShowAdComplete() {
                                    //       Intent mainIntent = new Intent(Master_SplashScreen.this, Start_Activity.class);
                                    isSplashLoading = true;
                                    if (defaultSharedPreferences.getBoolean("prefAppLock", false) &&
                                            SharePrefUtils.getBoolean(Constant_ad.PATTERN_CONFIRM, false)) {
                                        Intent i = new Intent(DevSpy_Master_SplashScreen.this, DevSpy_PatterLock_FirstScreen.class);
                                        i.putExtra("from", "splash");
                                        startActivity(i);
                                    } else {
                                        if (SharePrefUtils.getBoolean(Constant_ad.FEATURE_SCREEN, false)) {
                                            Intent i = new Intent(DevSpy_Master_SplashScreen.this, DevSpy_FirstActivity.class);
                                            startActivity(i);
                                        } else {
                                            Intent i = new Intent(DevSpy_Master_SplashScreen.this, DevSpy_FeatureActivity.class);
                                            startActivity(i);
                                        }

                                    }
                                }
                            });
                }
            }, 7000);

        } catch (JSONException e) {
            e.printStackTrace();
            isSplashLoading = true;
            if (defaultSharedPreferences.getBoolean("prefAppLock", false) &&
                    SharePrefUtils.getBoolean(Constant_ad.PATTERN_CONFIRM, false)) {
                Intent i = new Intent(DevSpy_Master_SplashScreen.this, DevSpy_PatterLock_FirstScreen.class);
                i.putExtra("from", "splash");
                startActivity(i);
                finish();
            } else {
                if (SharePrefUtils.getBoolean(Constant_ad.FEATURE_SCREEN, false)) {
                    Intent i = new Intent(DevSpy_Master_SplashScreen.this, DevSpy_FirstActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(DevSpy_Master_SplashScreen.this, DevSpy_FeatureActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void GetPublicIP() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(DevSpy_Master_SplashScreen.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, decryptionDataURL("[jjfi4//fhe^2f-(f2^1ec/;ied/?a>o*yIHrcsRIN(yXYhH"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    publicIP = jsonObject.getString("query");

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    Network activeNetwork;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        activeNetwork = cm.getActiveNetwork();
                        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
                        vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                    }
                    if (!vpnInUse) {
                        SharePrefUtils.putString(Constant_ad.CITY_IP, publicIP);
                        SharePrefUtils.putString(Constant_ad.VPN_IP, publicIP);
                    } else {
                        SharePrefUtils.putString(Constant_ad.VPN_IP, publicIP);
                    }

                    loadDataService(DevSpy_Master_SplashScreen.this);
                } catch (JSONException e) {
                    publicIP = "0.0.0.0";

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    Network activeNetwork;
                    boolean vpnInUse = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        activeNetwork = cm.getActiveNetwork();
                        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
                        vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                    }
                    if (!vpnInUse) {
                        SharePrefUtils.putString(Constant_ad.CITY_IP, publicIP);
                        SharePrefUtils.putString(Constant_ad.VPN_IP, publicIP);
                    } else {
                        SharePrefUtils.putString(Constant_ad.VPN_IP, publicIP);
                    }

                    loadDataService(DevSpy_Master_SplashScreen.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        mRequestQueue.getCache().clear();
        mRequestQueue.add(stringRequest);
    }

    public void decryptionData(String data) {

        String Newstr = " ";
        try {
            for (int i = 0; i < data.length(); i++) {
                char ch = data.charAt(i);
                int value = (int) ch;
                DevSpy_Operators operators;
                switch (value) {
                    case 64:
                        operators = DevSpy_Operators.A1;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 63:
                        operators = DevSpy_Operators.A2;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 37:
                        operators = DevSpy_Operators.A3;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 38:
                        operators = DevSpy_Operators.A4;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 40:
                        operators = DevSpy_Operators.A5;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 41:
                        operators = DevSpy_Operators.A6;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 49:
                        operators = DevSpy_Operators.A7;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 60:
                        operators = DevSpy_Operators.A8;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 62:
                        operators = DevSpy_Operators.A9;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 124:
                        operators = DevSpy_Operators.A11;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 93:
                        operators = DevSpy_Operators.A12;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 91:
                        operators = DevSpy_Operators.A13;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 50:
                        operators = DevSpy_Operators.A14;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 59:
                        operators = DevSpy_Operators.A15;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 97:
                        operators = DevSpy_Operators.A16;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 98:
                        operators = DevSpy_Operators.A17;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 99:
                        operators = DevSpy_Operators.A18;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 100:
                        operators = DevSpy_Operators.A19;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 101:
                        operators = DevSpy_Operators.A20;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 102:
                        operators = DevSpy_Operators.A21;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 103:
                        operators = DevSpy_Operators.A22;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 104:
                        operators = DevSpy_Operators.A23;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 105:
                        operators = DevSpy_Operators.A24;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 106:
                        operators = DevSpy_Operators.A25;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 107:
                        operators = DevSpy_Operators.A26;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 108:
                        operators = DevSpy_Operators.A27;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 109:
                        operators = DevSpy_Operators.A28;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 110:
                        operators = DevSpy_Operators.A29;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 111:
                        operators = DevSpy_Operators.A30;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 112:
                        operators = DevSpy_Operators.A31;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 32:
                        operators = DevSpy_Operators.A32;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 126:
                        operators = DevSpy_Operators.A33;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 33:
                        operators = DevSpy_Operators.A34;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 52:
                        operators = DevSpy_Operators.A35;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 35:
                        operators = DevSpy_Operators.A36;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 36:
                        operators = DevSpy_Operators.A37;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 94:
                        operators = DevSpy_Operators.A38;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 51:
                        operators = DevSpy_Operators.A39;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 42:
                        operators = DevSpy_Operators.A40;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 47:
                        operators = DevSpy_Operators.A41;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 95:
                        operators = DevSpy_Operators.A42;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 45:
                        operators = DevSpy_Operators.A43;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;

                    case 113:
                        operators = DevSpy_Operators.A44;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 114:
                        operators = DevSpy_Operators.A45;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 115:
                        operators = DevSpy_Operators.A46;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 116:
                        operators = DevSpy_Operators.A47;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 117:
                        operators = DevSpy_Operators.A48;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 118:
                        operators = DevSpy_Operators.A49;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 119:
                        operators = DevSpy_Operators.A50;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 120:
                        operators = DevSpy_Operators.A51;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 121:
                        operators = DevSpy_Operators.A52;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 122:
                        operators = DevSpy_Operators.A53;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 65:
                        operators = DevSpy_Operators.A54;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 66:
                        operators = DevSpy_Operators.A55;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 67:
                        operators = DevSpy_Operators.A56;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 68:
                        operators = DevSpy_Operators.A57;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 69:
                        operators = DevSpy_Operators.A58;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 70:
                        operators = DevSpy_Operators.A59;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 71:
                        operators = DevSpy_Operators.A60;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 72:
                        operators = DevSpy_Operators.A61;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 73:
                        operators = DevSpy_Operators.A62;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 74:
                        operators = DevSpy_Operators.A63;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 75:
                        operators = DevSpy_Operators.A64;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 76:
                        operators = DevSpy_Operators.A65;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 77:
                        operators = DevSpy_Operators.A66;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 78:
                        operators = DevSpy_Operators.A67;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 79:
                        operators = DevSpy_Operators.A68;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 80:
                        operators = DevSpy_Operators.A69;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 81:
                        operators = DevSpy_Operators.A70;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 82:
                        operators = DevSpy_Operators.A71;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 83:
                        operators = DevSpy_Operators.A72;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 84:
                        operators = DevSpy_Operators.A73;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 85:
                        operators = DevSpy_Operators.A74;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 86:
                        operators = DevSpy_Operators.A75;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 87:
                        operators = DevSpy_Operators.A76;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 88:
                        operators = DevSpy_Operators.A77;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 89:
                        operators = DevSpy_Operators.A78;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 90:
                        operators = DevSpy_Operators.A79;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 92:
                        operators = DevSpy_Operators.A80;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    default:
                        Newstr = Newstr + "0";
                        break;
                }


            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(Newstr);
            setData(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public char getOperatorValue(DevSpy_Operators operators) {

        switch (operators) {
            case A80:
                return (char) DevSpy_Operators.A80.getOutput();
            case A79:
                return (char) DevSpy_Operators.A79.getOutput();
            case A78:
                return (char) DevSpy_Operators.A78.getOutput();
            case A77:
                return (char) DevSpy_Operators.A77.getOutput();
            case A76:
                return (char) DevSpy_Operators.A76.getOutput();
            case A75:
                return (char) DevSpy_Operators.A75.getOutput();
            case A74:
                return (char) DevSpy_Operators.A74.getOutput();
            case A73:
                return (char) DevSpy_Operators.A73.getOutput();
            case A72:
                return (char) DevSpy_Operators.A72.getOutput();
            case A71:
                return (char) DevSpy_Operators.A71.getOutput();
            case A70:
                return (char) DevSpy_Operators.A70.getOutput();
            case A69:
                return (char) DevSpy_Operators.A69.getOutput();
            case A68:
                return (char) DevSpy_Operators.A68.getOutput();
            case A67:
                return (char) DevSpy_Operators.A67.getOutput();
            case A66:
                return (char) DevSpy_Operators.A66.getOutput();
            case A65:
                return (char) DevSpy_Operators.A65.getOutput();
            case A64:
                return (char) DevSpy_Operators.A64.getOutput();
            case A63:
                return (char) DevSpy_Operators.A63.getOutput();
            case A62:
                return (char) DevSpy_Operators.A62.getOutput();
            case A61:
                return (char) DevSpy_Operators.A61.getOutput();
            case A60:
                return (char) DevSpy_Operators.A60.getOutput();
            case A59:
                return (char) DevSpy_Operators.A59.getOutput();
            case A58:
                return (char) DevSpy_Operators.A58.getOutput();
            case A57:
                return (char) DevSpy_Operators.A57.getOutput();
            case A56:
                return (char) DevSpy_Operators.A56.getOutput();
            case A55:
                return (char) DevSpy_Operators.A55.getOutput();
            case A54:
                return (char) DevSpy_Operators.A54.getOutput();
            case A53:
                return (char) DevSpy_Operators.A53.getOutput();
            case A52:
                return (char) DevSpy_Operators.A52.getOutput();
            case A51:
                return (char) DevSpy_Operators.A51.getOutput();
            case A50:
                return (char) DevSpy_Operators.A50.getOutput();
            case A49:
                return (char) DevSpy_Operators.A49.getOutput();
            case A48:
                return (char) DevSpy_Operators.A48.getOutput();
            case A47:
                return (char) DevSpy_Operators.A47.getOutput();
            case A46:
                return (char) DevSpy_Operators.A46.getOutput();
            case A45:
                return (char) DevSpy_Operators.A45.getOutput();
            case A44:
                return (char) DevSpy_Operators.A44.getOutput();
            case A43:
                return (char) DevSpy_Operators.A43.getOutput();
            case A42:
                return (char) DevSpy_Operators.A42.getOutput();
            case A41:
                return (char) DevSpy_Operators.A41.getOutput();
            case A40:
                return (char) DevSpy_Operators.A40.getOutput();
            case A39:
                return (char) DevSpy_Operators.A39.getOutput();
            case A38:
                return (char) DevSpy_Operators.A38.getOutput();
            case A37:
                return (char) DevSpy_Operators.A37.getOutput();
            case A36:
                return (char) DevSpy_Operators.A36.getOutput();
            case A35:
                return (char) DevSpy_Operators.A35.getOutput();
            case A34:
                return (char) DevSpy_Operators.A34.getOutput();
            case A33:
                return (char) DevSpy_Operators.A33.getOutput();
            case A32:
                return (char) DevSpy_Operators.A32.getOutput();
            case A31:
                return (char) DevSpy_Operators.A31.getOutput();
            case A30:
                return (char) DevSpy_Operators.A30.getOutput();
            case A29:
                return (char) DevSpy_Operators.A29.getOutput();
            case A28:
                return (char) DevSpy_Operators.A28.getOutput();
            case A27:
                return (char) DevSpy_Operators.A27.getOutput();
            case A26:
                return (char) DevSpy_Operators.A26.getOutput();
            case A25:
                return (char) DevSpy_Operators.A25.getOutput();
            case A24:
                return (char) DevSpy_Operators.A24.getOutput();
            case A23:
                return (char) DevSpy_Operators.A23.getOutput();
            case A22:
                return (char) DevSpy_Operators.A22.getOutput();
            case A21:
                return (char) DevSpy_Operators.A21.getOutput();
            case A20:
                return (char) DevSpy_Operators.A20.getOutput();
            case A19:
                return (char) DevSpy_Operators.A19.getOutput();
            case A18:
                return (char) DevSpy_Operators.A18.getOutput();
            case A17:
                return (char) DevSpy_Operators.A17.getOutput();
            case A16:
                return (char) DevSpy_Operators.A16.getOutput();
            case A15:
                return (char) DevSpy_Operators.A15.getOutput();
            case A1:
                return (char) DevSpy_Operators.A1.getOutput();
            case A2:
                return (char) DevSpy_Operators.A2.getOutput();
            case A3:
                return (char) DevSpy_Operators.A3.getOutput();
            case A4:
                return (char) DevSpy_Operators.A4.getOutput();
            case A5:
                return (char) DevSpy_Operators.A5.getOutput();
            case A6:
                return (char) DevSpy_Operators.A6.getOutput();
            case A7:
                return (char) DevSpy_Operators.A7.getOutput();
            case A8:
                return (char) DevSpy_Operators.A8.getOutput();
            case A9:
                return (char) DevSpy_Operators.A9.getOutput();
            case A11:
                return (char) DevSpy_Operators.A11.getOutput();
            case A12:
                return (char) DevSpy_Operators.A12.getOutput();
            case A13:
                return (char) DevSpy_Operators.A13.getOutput();
            case A14:
                return (char) DevSpy_Operators.A14.getOutput();
            default:
                return (char) DevSpy_Operators.A1.getOutput();
        }
    }

    public String decryptionDataURL(String data) {

        String Newstr = " ";
        try {
            for (int i = 0; i < data.length(); i++) {
                char ch = data.charAt(i);
                int value = (int) ch;
                DevSpy_Operators operators;
                switch (value) {
                    case 64:
                        operators = DevSpy_Operators.A1;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 63:
                        operators = DevSpy_Operators.A2;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 37:
                        operators = DevSpy_Operators.A3;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 38:
                        operators = DevSpy_Operators.A4;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 40:
                        operators = DevSpy_Operators.A5;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 41:
                        operators = DevSpy_Operators.A6;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 49:
                        operators = DevSpy_Operators.A7;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 60:
                        operators = DevSpy_Operators.A8;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 62:
                        operators = DevSpy_Operators.A9;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 124:
                        operators = DevSpy_Operators.A11;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 93:
                        operators = DevSpy_Operators.A12;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 91:
                        operators = DevSpy_Operators.A13;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 50:
                        operators = DevSpy_Operators.A14;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 59:
                        operators = DevSpy_Operators.A15;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 97:
                        operators = DevSpy_Operators.A16;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 98:
                        operators = DevSpy_Operators.A17;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 99:
                        operators = DevSpy_Operators.A18;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 100:
                        operators = DevSpy_Operators.A19;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 101:
                        operators = DevSpy_Operators.A20;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 102:
                        operators = DevSpy_Operators.A21;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 103:
                        operators = DevSpy_Operators.A22;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 104:
                        operators = DevSpy_Operators.A23;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 105:
                        operators = DevSpy_Operators.A24;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 106:
                        operators = DevSpy_Operators.A25;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 107:
                        operators = DevSpy_Operators.A26;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 108:
                        operators = DevSpy_Operators.A27;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 109:
                        operators = DevSpy_Operators.A28;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 110:
                        operators = DevSpy_Operators.A29;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 111:
                        operators = DevSpy_Operators.A30;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 112:
                        operators = DevSpy_Operators.A31;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 32:
                        operators = DevSpy_Operators.A32;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 126:
                        operators = DevSpy_Operators.A33;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 33:
                        operators = DevSpy_Operators.A34;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 52:
                        operators = DevSpy_Operators.A35;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 35:
                        operators = DevSpy_Operators.A36;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 36:
                        operators = DevSpy_Operators.A37;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 94:
                        operators = DevSpy_Operators.A38;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 51:
                        operators = DevSpy_Operators.A39;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 42:
                        operators = DevSpy_Operators.A40;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 47:
                        operators = DevSpy_Operators.A41;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 95:
                        operators = DevSpy_Operators.A42;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 45:
                        operators = DevSpy_Operators.A43;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;

                    case 113:
                        operators = DevSpy_Operators.A44;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 114:
                        operators = DevSpy_Operators.A45;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 115:
                        operators = DevSpy_Operators.A46;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 116:
                        operators = DevSpy_Operators.A47;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 117:
                        operators = DevSpy_Operators.A48;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 118:
                        operators = DevSpy_Operators.A49;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 119:
                        operators = DevSpy_Operators.A50;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 120:
                        operators = DevSpy_Operators.A51;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 121:
                        operators = DevSpy_Operators.A52;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 122:
                        operators = DevSpy_Operators.A53;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 65:
                        operators = DevSpy_Operators.A54;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 66:
                        operators = DevSpy_Operators.A55;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 67:
                        operators = DevSpy_Operators.A56;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 68:
                        operators = DevSpy_Operators.A57;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 69:
                        operators = DevSpy_Operators.A58;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 70:
                        operators = DevSpy_Operators.A59;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 71:
                        operators = DevSpy_Operators.A60;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 72:
                        operators = DevSpy_Operators.A61;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 73:
                        operators = DevSpy_Operators.A62;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 74:
                        operators = DevSpy_Operators.A63;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 75:
                        operators = DevSpy_Operators.A64;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 76:
                        operators = DevSpy_Operators.A65;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 77:
                        operators = DevSpy_Operators.A66;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 78:
                        operators = DevSpy_Operators.A67;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 79:
                        operators = DevSpy_Operators.A68;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 80:
                        operators = DevSpy_Operators.A69;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 81:
                        operators = DevSpy_Operators.A70;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 82:
                        operators = DevSpy_Operators.A71;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 83:
                        operators = DevSpy_Operators.A72;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 84:
                        operators = DevSpy_Operators.A73;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 85:
                        operators = DevSpy_Operators.A74;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 86:
                        operators = DevSpy_Operators.A75;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 87:
                        operators = DevSpy_Operators.A76;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 88:
                        operators = DevSpy_Operators.A77;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 89:
                        operators = DevSpy_Operators.A78;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 90:
                        operators = DevSpy_Operators.A79;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    case 92:
                        operators = DevSpy_Operators.A80;
                        Newstr = Newstr + getOperatorValue(operators);
                        break;
                    default:
                        Newstr = Newstr + "0";
                        break;
                }


            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return Newstr;
    }
}
