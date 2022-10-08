package com.github.mylibrary.Notification.Server;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Push.DeviceUtils;
import com.github.mylibrary.Notification.Push.PushUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static RegListener regListener;

    public Server() {


    }

    public interface RegListener {

        void onRegSuccess(String s);

        void onRegJsonExcetion(String s);

        void onRegError(String s);
    }


    public static String getAbsoluteUrl(String relativeUrl) {
        return ServerConfig.BASE_URL + relativeUrl;
    }


    public static String getkey(Context c) {
        try {
            PackageInfo info = c.getPackageManager().getPackageInfo(
                    c.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return null;
    }

    public static void updateToken(final Context context, final String token) {
//        String key = "" + Server.getkey(context).replaceAll("\\n", "");
//        Log.e("key1234", "" + key);


        final SharedPreferences prefs = context.getSharedPreferences("GCM",
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getAbsoluteUrl(ServerConfig.REGISTRATION_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("response", "" + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    final SharedPreferences prefs = context.getSharedPreferences("GCM", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(PushUser.API_KEY, jsonObject.getString("api_key"));
//                     Log.d("api key ", "" + jsonObject.getString("api_key"));
                    editor.commit();
                    if (regListener != null) {
                        regListener.onRegSuccess(response.toString());
                    }
                    //Get_Data(response.getString("api_key").toString());

                } catch (JSONException e) {
//                    Log.e("response", "" + e.getMessage());
                    e.printStackTrace();
                    if (regListener != null) {
                        regListener.onRegJsonExcetion(e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("goterror",""+error);
                if (regListener != null) {
                    regListener.onRegJsonExcetion(error.toString());
                }
                editor.putString(PushUser.TOKEN, "1234");
                editor.apply();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(PushUser.EMAIL, "");
                params.put(PushUser.APP_TYPE, ServerConfig.APP_TYPE);
                params.put(PushUser.TOKEN, token);
                params.put(PushUser.DEVICE_MODEL, DeviceUtils.getDeviceModel());
                params.put(PushUser.DEVICE_API, DeviceUtils.getDeviceAPILevel());
                params.put(PushUser.DEVICE_OS, DeviceUtils.getDeviceOS());
                params.put(PushUser.DEVICE_NAME, DeviceUtils.getDeviceName());
                params.put(PushUser.TIMEZONE, DeviceUtils.getDeviceTimeZone());
                params.put(PushUser.LAST_LAT, DeviceUtils.getLastlat(context) + "");
                params.put(PushUser.LAST_LONG, DeviceUtils.getLastLng(context) + "");
                params.put(PushUser.MEMORY, DeviceUtils.getDeviceMemory(context) + "");
                params.put(PushUser.DEVICE_ID, DeviceUtils.getDeviceId(context) + "");
                params.put(PushUser.PIN_CODE, DeviceUtils.getPinCode(context) + "");
                params.put(PushUser.APP_CODE, "" + PushUser.APP_CODE_VALUE);
                params.put(PushUser.APP_ANDROID_ID, DeviceUtils.getDeviceId(context) + "");
                params.put(PushUser.APP_DEVICE_TYPE, "2" + "");
                params.put(PushUser.APP_VERSION, VersionName(context));
                params.put(PushUser.CITY_IP, SharePrefUtils.getString(Constant_ad.CITY_IP,"0.0.0.0"));


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put(PushUser.APP_CLIENT_KEY, Server.getkey(context).replaceAll("\\n", ""));
                return params;
            }
        };

        mRequestQueue.add(stringRequest);


    }

    public void setRegListener(RegListener regListener) {
        this.regListener = regListener;
    }
    public static String VersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        return version;
    }
}
