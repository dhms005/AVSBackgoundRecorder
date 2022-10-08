package com.github.mylibrary.Notification.Ads;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mylibrary.Notification.Push.PushUser;
import com.github.mylibrary.Notification.Server.ServerConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Custom_Ad_Call_Api {
    static SimpleDateFormat currentFormat = new SimpleDateFormat("dd-MMM-yyyy");
   // private static Context context;

    public static void loadDataService(Context context) {


        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerConfig.BASE_URL+ ServerConfig.GET_AD_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //    Log.e("response", "" + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    SharePrefUtils.putString(Constant_ad.AD_DATA, response.toString());
                    SharePrefUtils.putString(Constant_ad.AD_DATE, getCurrentDate());
                    setData(jsonObject);

                } catch (JSONException e) {
                   // Log.e("response", "" + e.getMessage());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //    e("goterror",""+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(PushUser.APP_CODE, PushUser.APP_CODE_VALUE);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);

    }
    private static void setData(JSONObject response) {

        try {
            JSONObject jsonObject = response.getJSONObject("ad_ids");
            String mediation = jsonObject.getString("mediation");
            String google_app_id = jsonObject.getString("google_app_id");
            String google_fullad = jsonObject.getString("google_fullad");
            String google_banner = jsonObject.getString("google_banner");
            String google_native = jsonObject.getString("google_native");
            String google_splash_ad = jsonObject.getString("google_fullad_splash");
            String google_native_banner = jsonObject.getString("google_native_banner");
            //String fb_id = jsonObject.getString("fb_id");
            String fb_full_ad = jsonObject.getString("fb_full_ad");
            String fb_banner = jsonObject.getString("fb_banner");
            String fb_full_native = jsonObject.getString("fb_full_native");
            String fb_native_banner = jsonObject.getString("fb_native_banner");
            String fb_dialog = jsonObject.getString("fb_dialog");
            String open_ad_id = jsonObject.getString("google_appopen");

            SharePrefUtils.putString(Constant_ad.MEDIATION, mediation);
            SharePrefUtils.putString(Constant_ad.GOOGLE_APP_ID, google_app_id);
            SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD, google_fullad);
            SharePrefUtils.putString(Constant_ad.GOOGLE_BANNER, google_banner);
            SharePrefUtils.putString(Constant_ad.GOOGLE_NATIVE, google_native);
            SharePrefUtils.putString(Constant_ad.GOOGLE_NATIVE_BANNER, google_native_banner);
            SharePrefUtils.putString(Constant_ad.GOOGLE_FULL_AD_SPLASH, google_splash_ad);
            SharePrefUtils.putString(Constant_ad.OPEN_AD, open_ad_id);

            // SharePrefUtils.putString(Constant_ad.FACEBOOK_ID, fb_id);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_FULL, fb_full_ad);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_BANNER, fb_banner);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_NATIVE, fb_full_native);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_NATIVE_BANNER, fb_native_banner);
            SharePrefUtils.putString(Constant_ad.FACEBOOK_DIALOG, fb_dialog);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        String formattedDate = currentFormat.format(c.getTime());

        return formattedDate;
    }
}
