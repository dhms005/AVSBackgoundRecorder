package com.ds.audio.video.screen.backgroundrecorder.exit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CY_M_Utility extends AppCompatActivity {

    public static NativeAd _nativeAdBig, _nativeAdSmall, _nativeBanner;
    public static Dialog dialog;
    private Context context;
    public static CY_M_OnTaskCompleted listener;


    public static MaxNativeAdView maxNativeAdView;
    public static MaxNativeAdView maxNativeAdVieSmall;
    public static MaxNativeAdView maxNativeAdVieNativeBanner;
    public static boolean isTimerAdShow = true;




    public  static int temPValue = 0;
    static String startString = SharePrefUtils.getString(Constant_ad.START_SCREEN_TEXTs, "");
    public static String[] startStringText = startString.split(",");

    public CY_M_Utility(Context context) {
        this.context = context;
    }

    public static void displaySnackBarWithMessage(View view, String Message) {
        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
    }

    public static boolean isConnectivityAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

//    public static void alertDialog(final Context context, OnTaskCompleted onTaskCompleted, String msg) {
//
//        listener = onTaskCompleted;
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//
//        alertDialog.setTitle("No Internet Connection"); // Sets title for your alertbox
//
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage(msg);
//
//        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                //dialog.cancel();
//                Intent i = new Intent(context, RecomendedAppsActivity.class);
//                context.startActivity(i);
//
//            }
//
//        });
//
//        alertDialog.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                listener.onTaskCompleted(true);
//            }
//        });
//
//        AlertDialog alert = alertDialog.create();
//        alert.show();
//        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//        nbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//        pbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//
//    }


    public static void OpenCustomGameZopBrowser(Activity activity){
        String PACKAGE_NAME = "com.android.chrome";
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setData(Uri.parse("https://www.gamezop.com/?id=4303"));
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, PACKAGE_NAME))
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
        }
        customTabsIntent.launchUrl(activity, Uri.parse("https://www.gamezop.com/?id=4303"));
    }


    public static void OpenCustomQurekaBrowser(Context activity){
        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA, "").equals("")) {

        } else {
            String PACKAGE_NAME = "com.android.chrome";
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.intent.setData(Uri.parse(SharePrefUtils.getString(Constant_ad.AD_QUREKA, "")));
            PackageManager packageManager = activity.getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resolveInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                if (TextUtils.equals(packageName, PACKAGE_NAME))
                    customTabsIntent.intent.setPackage(PACKAGE_NAME);
            }
            customTabsIntent.launchUrl(activity, Uri.parse(SharePrefUtils.getString(Constant_ad.AD_QUREKA, "")));

        }
    }
}
