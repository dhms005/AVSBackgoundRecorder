package com.ds.audio.video.screen.backgroundrecorder.exit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities.DevSpy_Master_SplashScreen;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Push.PushUser;
import com.github.mylibrary.Notification.Server.Server;
import com.github.mylibrary.Notification.Server.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2/1/17.
 */
public class DevSpy_RecomendedAppsDialog extends AppCompatActivity {

    private ProgressBar progress_HistoryLoader;
    private ArrayList<DevSpy_RecommandDatabaseModel> recomendedAppModelArrayList = new ArrayList<>();
    private DevSpy_RecomendedAppsDailogAdapter_New recomendedAppsAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    SimpleDateFormat currentFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private ImageView ok;
    private ImageView cancel;
    LinearLayout ll_rec_list;
    TextView tv_rec_text;

    Dialog mail_report_dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.devspy_recomended_apps_dialog);

        progress_HistoryLoader = (ProgressBar) findViewById(R.id.progress_HistoryLoader);

        ll_rec_list = (LinearLayout) findViewById(R.id.ll_rec_list);
        tv_rec_text = (TextView) findViewById(R.id.tv_rec_text);


        mDeclaration();

        if (SharePrefUtils.getString(Constant_ad.AD_EXIT_REC_APPS, "0").equals("1")) {
            ll_rec_list.setVisibility(View.GONE);
            tv_rec_text.setVisibility(View.GONE);
        } else {
            ll_rec_list.setVisibility(View.GONE);
            tv_rec_text.setVisibility(View.GONE);
        }

        if (SharePrefUtils.getString(Constant_ad.AD_EXIT_NATIVE, "0").equals("0")) {

        } else {
            mNativeAdNew();
        }

        rate_Function();

//        String recommandedData = SharePrefUtils.getString(Constant_ad.RECOMMANDED_DATA, "");
//        String date = SharePrefUtils.getString(Constant_ad.RECOMMANDED_DATE, "");
//
//        if (date.equals("") || recommandedData.equals("")) {
//            getDataService();
//
//        } else if (isCurrentDate(date)) {
//            getDataService();
//        } else {
//
//            try {
//                JSONObject recommandedObject = new JSONObject(recommandedData.toString());
//                setData(recommandedObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void rate_Function() {

        CardView cardBad = findViewById(R.id.cardBad);
        CardView cardGood = findViewById(R.id.cardGood);
        CardView cardExcellent = findViewById(R.id.cardExcellent);
        this.mail_report_dialog = new Dialog(this);

        cardBad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!DevSpy_RecomendedAppsDialog.this.mail_report_dialog.isShowing()) {
                    DevSpy_RecomendedAppsDialog.this.openFeedBackDialog();
                }
            }
        });
        cardGood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppConstants.uriparse(DevSpy_RecomendedAppsDialog.this, AppConstants.playStoreUrl);

            }
        });
        cardExcellent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppConstants.uriparse(DevSpy_RecomendedAppsDialog.this, AppConstants.playStoreUrl);

            }
        });
    }


    public void openFeedBackDialog() {
        this.mail_report_dialog.setContentView(R.layout.dialog_feedback);
        this.mail_report_dialog.setCancelable(true);
        this.mail_report_dialog.getWindow().setLayout(-1, -2);
        this.mail_report_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mail_report_dialog.show();
        final EditText editText = (EditText) this.mail_report_dialog.findViewById(R.id.etFeedBack);
        ((CardView) this.mail_report_dialog.findViewById(R.id.cardSent)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!editText.getText().toString().trim().isEmpty()) {
                    mail_report_dialog.dismiss();

                    AppConstants.EmailUs(DevSpy_RecomendedAppsDialog.this, editText.getText().toString());
                    return;
                }
                Toast.makeText(DevSpy_RecomendedAppsDialog.this, "Please Write Feedback.", 0).show();
                editText.requestFocus();
            }
        });
        ((CardView) this.mail_report_dialog.findViewById(R.id.cardCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mail_report_dialog.dismiss();
            }
        });
    }


    private void mDeclaration() {

        //mShare = findViewById(R.id.img_share);
        ok = (ImageView) findViewById(R.id.txtYes);
        cancel = (ImageView) findViewById(R.id.txtNo);
        /*mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
        mScrollView.scrollTo(0, 0);*/

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        cancel.setClickable(true);
                    }
                };
                handler.postDelayed(r, 500);
                cancel.setClickable(false);

                DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_RecomendedAppsDialog.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        finish();
                    }
                });
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CY_M_Master_SplashScreen.isSplashLoading = false;
//                finishAffinity();
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        ok.setClickable(true);
                    }
                };
                handler.postDelayed(r, 500);
                ok.setClickable(false);

                if (SharePrefUtils.getString(Constant_ad.IN_HOUSE, "0").equals("1")) {
                    DevSpy_Admob_Full_AD_New.getInstance().showInter(DevSpy_RecomendedAppsDialog.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
                        @Override
                        public void callbackCall() {
                            Intent intent = new Intent(DevSpy_RecomendedAppsDialog.this, DevSpy_ThankYou_Screen.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    DevSpy_Master_SplashScreen.isSplashLoading = false;
                    finishAffinity();
                }


//                Intent intent = new Intent(RecomendedAppsDialog.this, Thanks_activity.class);
//                startActivity(intent);
//                finish();

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_Recomended);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);

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


    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_11);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNativeBig() == null) {
                    new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheBigNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNativeBig() == null) {
                    new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheBigNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                }
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                if (DevSpy_Master_SplashScreen.custom_nativeAd_admob.CacheNativeSmall() == null) {
                    new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
                } else {
                    DevSpy_Master_SplashScreen.custom_nativeAd_admob._showCacheSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
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


    private String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        String formattedDate = currentFormat.format(c.getTime());

        return formattedDate;
    }

    private void setData(JSONObject response) {
        try {


            SharePrefUtils.putString(PushUser.APP_VERSION, response.getString("App_Version").toString());


            progress_HistoryLoader.setVisibility(View.GONE);

            try {
                JSONArray jsonArray = response.getJSONArray("RecomendedApps");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    DevSpy_RecommandDatabaseModel appModel = new DevSpy_RecommandDatabaseModel();
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
            ArrayList<DevSpy_RecommandDatabaseModel> typeOneArrayList = new ArrayList<DevSpy_RecommandDatabaseModel>();
            for (int i = 0; i < recomendedAppModelArrayList.size(); i++) {
                if (recomendedAppModelArrayList.get(i).getType().equals("2")) {
                    typeOneArrayList.add(recomendedAppModelArrayList.get(i));
                }
            }
            recomendedAppsAdapter = new DevSpy_RecomendedAppsDailogAdapter_New(DevSpy_RecomendedAppsDialog.this, typeOneArrayList, 9, "1");
            mRecyclerView.setAdapter(recomendedAppsAdapter);


            if (!SharePrefUtils.getString(PushUser.APP_VERSION, "" + PushUser.APP_VERSION_VALUE).isEmpty()
                    && Double.parseDouble(SharePrefUtils.getString(PushUser.APP_VERSION, "" + PushUser.APP_VERSION_VALUE)) > Double.parseDouble(BuildConfig.VERSION_NAME)) {

//                upgradeAppDialogBox();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataService() {
        if (DevSpy_Utility.isConnectivityAvailable(DevSpy_RecomendedAppsDialog.this)) {

            progress_HistoryLoader.setVisibility(View.GONE);
            RequestQueue mRequestQueue = Volley.newRequestQueue(DevSpy_RecomendedAppsDialog.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getAbsoluteUrl(ServerConfig.GET_RECOMMAND), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.e("response", "" + response);
                    try {

                        JSONObject jsonObject = new JSONObject(response);

//                        Log.d("response", "" + response);
                        progress_HistoryLoader.setVisibility(View.GONE);
                        SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATA, response.toString());
                        SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATE, getCurrentDate());
                        setData(jsonObject);

                    } catch (JSONException e) {
//                        Log.e("response", "" + e.getMessage());
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.e("goterror",""+error);
                    SharePrefUtils.putString(Constant_ad.RECOMMANDED_DATE, "");
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
            };

            mRequestQueue.add(stringRequest);

        }
    }


    public static boolean isConnectivityAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        DevSpy_Admob_Full_AD_New.getInstance().showInterBack(DevSpy_RecomendedAppsDialog.this, new DevSpy_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDeclaration();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
    }

}
