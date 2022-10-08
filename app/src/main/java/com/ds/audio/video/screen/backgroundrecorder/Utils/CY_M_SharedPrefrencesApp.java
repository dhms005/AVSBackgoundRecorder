package com.ds.audio.video.screen.backgroundrecorder.Utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by India on 6/22/2016.
 */
public class CY_M_SharedPrefrencesApp {

    private Context mContext;
    private static final String MYPREFS = "BankBalance";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    public String Auth = "auth";
    public String FINISH_ACTIVITY = "finish_activity";
    public String Check_ID = "check_id";
    public String Activity = "finish";
    public String AppVersion = "appVersion";
    public String Disclaimer = "disclaimer";
    public String rec_current_date = "rec_current_date";
    public String rec_show_data = "rec_show_data";
    public String bankinq_current_date = "bankinq_current_date";
    public String bankinq_show_data = "bankinq_data";
    public String netbank_current_date = "netbank_current_date";
    public String netbank_show_data = "netbank_data";
    public String ifsc_current_date = "ifsc_current_date";
    public String ifsc_show_data = "ifsc_data";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public CY_M_SharedPrefrencesApp(Context Ctx) {
        mContext = Ctx;
        settings = mContext.getSharedPreferences(MYPREFS, 0);
        editor = settings.edit();
    }

    // Set the Access Token and Other Important Keys in SharedPrefernces
    public void setPreferences(String Key, String Value) {
        editor.putString(Key, Value);
        editor.commit();
    }

    // get the Access Token and Other Important Keys in SharedPrefernces
    public String getPreferences(String Key, String Value) {
        return settings.getString(Key, Value);
    }

    // Set the Access Token and Other Important Keys in SharedPrefernces
    public void setBooleanPreferences(String Key, boolean Status) {
        editor.putBoolean(Key, Status);
        editor.commit();
    }

    // get the Access Token and Other Important Keys in SharedPrefernces
    public boolean getBooleanPreferences(String Key, boolean Status) {
        return settings.getBoolean(Key, Status);
    }
}
