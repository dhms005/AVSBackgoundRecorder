package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.app.Activity;
import android.app.Dialog;

import com.ds.audio.video.screen.backgroundrecorder.Utils.CY_M_SharedPrefrencesApp;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CY_M_CommonFunction {

    public static Dialog dialog1, loader_dialog, q_dialog1;
    public static boolean isQurekaADShow = false;
    ;
    public Activity mContext;
    public String key;
    String format, action, type;
    int position;
    String rank2, population, rank;
    String villageName;

    private CY_M_SharedPrefrencesApp sharedPreferencesApp;

    public static void checkBetweenTime(String startTime, String endTime) {
        try {


            Date time2 = new SimpleDateFormat("HH:mm").parse(startTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            Date time1 = new SimpleDateFormat("HH:mm").parse(endTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Date d = new SimpleDateFormat("HH:mm").parse(currentDateandTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar2.getTime()) && x.before(calendar1.getTime())) {
//                Log.e("getTime", "avaialable");
                SharePrefUtils.putInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 1);
            } else {
//                Log.e("getTime", "not avaialable");
                SharePrefUtils.putInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
//            Log.e("getTime", e.getMessage());
            SharePrefUtils.putInt(Constant_ad.AD_NATIVE_TIME_BETWEEN, 0);
        }
    }

    public CY_M_CommonFunction(Activity mContext, String key) {
        this.mContext = mContext;
        this.key = key;

        sharedPreferencesApp = new CY_M_SharedPrefrencesApp(mContext);
    }


    public CY_M_CommonFunction(Activity mContext, String key, String format, String action) {
        this.mContext = mContext;
        this.key = key;
        this.format = format;
        this.action = action;

    }

    public CY_M_CommonFunction(Activity mContext, String key, int position, String action) {
        this.mContext = mContext;
        this.key = key;
        this.position = position;
        this.action = action;

    }

    public CY_M_CommonFunction(Activity mContext, String key, String rank, String population, String rank2) {
        this.mContext = mContext;
        this.key = key;
        this.rank = rank;
        this.population = population;
        this.rank2 = rank2;
    }

    public CY_M_CommonFunction(Activity context, String key, String villageName) {

        this.key = key;
        this.mContext = context;
        this.villageName = villageName;

    }



}
