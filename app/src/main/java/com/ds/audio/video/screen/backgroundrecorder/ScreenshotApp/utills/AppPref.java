package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;


public class AppPref {
    static final String AD_STRUCTURE = "AD_STRUCTURE";
    static final String FLOATING_BUTTON = "FLOATING_BUTTON";
    static final String GALLERY_OPEN = "GALLERY_OPEN";
    public static final String IMAGE_FORMAT = "IMAGE_FORMAT";
    public static final String IMAGE_QUALITY = "IMAGE_QUALITY";
    static final String IS_ADFREE = "IS_ADFREE";
    static final String IS_RATE_US = "IS_RATE_US";
    static final String IS_RATE_US_ACTION = "IS_RATE_US_ACTION";
    static final String IS_TERMS_ACCEPT = "IS_TERMS_ACCEPT";
    static final String MENU = "MENU";
    public static final String PARAM_X = "PARAM_X";
    public static final String PARAM_Y = "PARAM_Y";
    static final String PREFIX = "PREFIX";
    public static final String PREF_NAME = "ScreenshotAppPref";
    static final String SCREEN_SIUND = "SCREEN_SIUND";
    static final String SHOW_RATE_US = "SHOW_RATE_US";


    public static void ClearAdStructurePref() {
        SharedPreferences.Editor edit = CY_M_MyApplication.getAppContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.remove(AD_STRUCTURE);
        edit.apply();
    }

    public static boolean getIsAdfree() {
        return CY_M_MyApplication.getAppContext().getSharedPreferences(PREF_NAME, 0).getBoolean(IS_ADFREE, false);
    }

    public static void setIsAdfree(boolean z) {
        SharedPreferences.Editor edit = CY_M_MyApplication.getAppContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(IS_ADFREE, z);
        edit.commit();
    }

    public static boolean IsTermsAccept(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(IS_TERMS_ACCEPT, false);
    }

    public static void setIsTermsAccept(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(IS_TERMS_ACCEPT, z);
        edit.commit();
    }

    public static boolean IsRateUS(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(IS_RATE_US, false);
    }

    public static void setRateUS(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(IS_RATE_US, z);
        edit.commit();
    }

    public static boolean getShowRateUs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(SHOW_RATE_US, true);
    }

    public static void setShowRateUs(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(SHOW_RATE_US, z);
        edit.commit();
    }

    public static boolean IsRateUsAction(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(IS_RATE_US_ACTION, false);
    }

    public static void setRateUsAction(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(IS_RATE_US_ACTION, z);
        edit.commit();
    }

    public static final int getParamX(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getInt(PARAM_X, Integer.MIN_VALUE);
    }

    public static final void setParamX(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putInt(PARAM_X, i);
        edit.apply();
    }

    public static final int getParamY(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getInt(PARAM_Y, Integer.MIN_VALUE);
    }

    public static final void setParamY(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putInt(PARAM_Y, i);
        edit.apply();
    }

    public static final String getImageFormat(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getString(IMAGE_FORMAT, "JPG");
    }

    public static final void setImageFormat(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putString(IMAGE_FORMAT, str);
        edit.apply();
    }

    public static final int getImageQuality(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getInt(IMAGE_QUALITY, 90);
    }

    public static final void setImageQuality(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putInt(IMAGE_QUALITY, i);
        edit.apply();
    }

    public static boolean getScreenSound(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(SCREEN_SIUND, true);
    }

    public static void setScreenSound(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(SCREEN_SIUND, z);
        edit.commit();
    }

    public static boolean getMenu(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(MENU, true);
    }

    public static void setMenu(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(MENU, z);
        edit.commit();
    }

    public static boolean getShowButton(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(FLOATING_BUTTON, true);
    }

    public static void setShowButton(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(FLOATING_BUTTON, z);
        edit.commit();
    }

    public static final String getPrefix(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getString(PREFIX, Constants.FOLDER_NAME);
    }

    public static final void setPrefix(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_NAME, 0).edit();
        edit.putString(PREFIX, str);
        edit.apply();
    }

    public static boolean getGalleryOpen(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).getBoolean(GALLERY_OPEN, false);
    }

    public static void setGalleryOpen(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit();
        edit.putBoolean(GALLERY_OPEN, z);
        edit.commit();
    }
}
