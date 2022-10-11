package com.github.mylibrary.Notification.Push;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Server.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class FCMActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    String possibleEmail;
    Context context;
    public static String REG_EMAIL = "user_email";
    private double latPoint;
    private double lngPoint;
    int MY_PERMISSIONS_REQUEST = 8888;
    int MY_PERMISSIONS_EMAIL = 8889;
    public static String NEW_NOTIFICATION = "NEW_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (isNetworkAvailable(getApplicationContext())) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {

                            final SharedPreferences prefs = FCMActivity.this.getSharedPreferences("GCM",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            if (!task.isSuccessful()) {
                                // Log.w(TAG, "Fetching FCM registration token failed", task.getException());

                               /* Server.updateToken(FCMActivity.this, "1234");
                                editor.putString(PushUser.TOKEN, "1234");
                                editor.apply();*/
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            String saved_token = prefs.getString(PushUser.TOKEN, "");
                            // Log and toast


                            if (token == null) {
                                Server.updateToken(FCMActivity.this, "1234");
                                editor.putString(PushUser.TOKEN, "1234");
                                editor.apply();
                            } else if (saved_token.equals("1234")) {
                                editor.putString(PushUser.TOKEN, token);
                                editor.apply();
                                Server.updateToken(FCMActivity.this, token);
                            } else if (saved_token.equals("")) {
                                editor.putString(PushUser.TOKEN, token);
                                editor.apply();
                                Server.updateToken(FCMActivity.this, token);
                            }
                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 8889: {

                if (isNetworkAvailable(getApplicationContext())) {
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {

                                    final SharedPreferences prefs = FCMActivity.this.getSharedPreferences("GCM",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();

                                    if (!task.isSuccessful()) {
                                        // Log.w(TAG, "Fetching FCM registration token failed", task.getException());

                                       /* Server.updateToken(FCMActivity.this, "1234");
                                        editor.putString(PushUser.TOKEN, "1234");
                                        editor.apply();*/
                                        return;
                                    }

                                    // Get new FCM registration token
                                    String token = task.getResult();

                                    // Log and toast


                                    if (token == null) {
                                        Server.updateToken(FCMActivity.this, "1234");
                                        editor.putString(PushUser.TOKEN, "1234");
                                        editor.apply();
                                    } else if (prefs.getString(PushUser.TOKEN, "").equals("1234") && token != null) {
                                        editor.putString(PushUser.TOKEN, token);
                                        editor.apply();
                                        Server.updateToken(FCMActivity.this, token);
                                    } else if (prefs.getString(PushUser.TOKEN, "").equals("") && token != null) {
                                        editor.putString(PushUser.TOKEN, token);
                                        editor.apply();
                                        Server.updateToken(FCMActivity.this, token);
                                    }
                                }
                            });
                }
                //getLocationPermission();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getToken(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences("GCM",
                c.MODE_PRIVATE);
        return prefs.getString(PushUser.TOKEN, "");
    }


}
