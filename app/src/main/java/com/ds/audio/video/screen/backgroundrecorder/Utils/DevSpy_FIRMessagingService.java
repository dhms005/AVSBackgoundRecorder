/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licenced under the Apache Licence, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the Licence.
 * You may obtain a copy of the Licence at
 *
 *      http://www.apache.org/Licences/Licence-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

package com.ds.audio.video.screen.backgroundrecorder.Utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities.DevSpy_FirstActivity;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Database.DatabaseHandler;
import com.github.mylibrary.Notification.Database.News;
import com.github.mylibrary.Notification.Push.FCMActivity;
import com.github.mylibrary.Notification.Server.Server;
import com.github.mylibrary.Notification.Server.ServerConfig;
import com.github.mylibrary.Notification.UI.DailogeNotice;
import com.github.mylibrary.Notification.UI.Dailoge_Option;
import com.github.mylibrary.Notification.UI.Dailoge_Text;
import com.github.mylibrary.Notification.UI.Notification_MainActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */

public class DevSpy_FIRMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    TaskStackBuilder stackBuilder;
    String mTitle;
    SharedPreferences pref;
    Editor edit;
    boolean is_noty = false;
    Handler mHandler;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//		Log.e("Msg", remoteMessage.getData().get("title"));
        //sendNotification(remoteMessage.getData());

        try {
            sendNotification(remoteMessage.getData());
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String token) {


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        //Log.e("Token", refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("global");
        FirebaseMessaging.getInstance().subscribeToTopic(ServerConfig.APP_TYPE);
        Server.updateToken(this, token);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mHandler = new Handler();
    }

    public static final String TAG = "FCM Demo";

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Map<String, String> data) {

        // handle notification here
        /*
         * types of notification 1. result update 2. circular update 3. student
         * corner update 4. App custom update 5. Custom Message 6. Notice from
         * College custom
         */
        int num = ++NOTIFICATION_ID;
        Bundle msg = new Bundle();
        for (String key : data.keySet()) {
            //Log.e(key, data.get(key));
            msg.putString(key, data.get(key));
        }

        pref = getSharedPreferences("UPDATE_INSTANCE", MODE_PRIVATE);
        edit = pref.edit();
        Intent backIntent;
        Intent intent = null;
        PendingIntent pendingIntent = null;
        backIntent = new Intent(getApplicationContext(), Notification_MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SharedPreferences sp;
        Editor editor;

        switch (Integer.parseInt(msg.getString("type"))) {

            case 1:
                backIntent = new Intent(getApplicationContext(),
                        DevSpy_FirstActivity.class);

                // The stack builder object will contain an artificial back stack
                // for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out
                // of
                // your application to the Home screen.
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(AppUpdate.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(backIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_IMMUTABLE);
                }else{
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }

                break;
            case 2:

                backIntent = new Intent(getApplicationContext(),
                        DailogeNotice.class);
                backIntent.putExtras(msg);
//			sp = getSharedPreferences("APP_CUSTOM",
//					getApplicationContext().MODE_PRIVATE);
//			editor = sp.edit();
//			editor.putString("title", msg.getString("title"));
//			editor.putString("emotion", msg.getString("emotion"));
//			editor.putString("message", msg.getString("message"));
//
//			editor.putString("link", msg.getString("link"));
//			editor.commit();

                // The stack builder object will contain an artificial back stack
                // for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out
                // of
                // your application to the Home screen.
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(AppUpdate.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(backIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_IMMUTABLE);
                }else{
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }
                break;
            case 3:
                backIntent = new Intent(getApplicationContext(),
                        Dailoge_Text.class);
                backIntent.putExtras(msg);
//			sp = getSharedPreferences("APP_CUSTOM",
//					getApplicationContext().MODE_PRIVATE);
//			editor = sp.edit();
//			editor.putString("title", msg.getString("title"));
//			editor.putString("emotion", msg.getString("emotion"));
//			editor.putString("message", msg.getString("message"));
//
//			editor.putString("link", msg.getString("link"));
//			editor.commit();

                // The stack builder object will contain an artificial back stack
                // for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out
                // of
                // your application to the Home screen.
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(AppUpdate.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(backIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_IMMUTABLE);
                }else{
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }
                break;
            case 4:
                backIntent = new Intent(getApplicationContext(),
                        Dailoge_Option.class);
                backIntent.putExtras(msg);
//			sp = getSharedPreferences("APP_CUSTOM",
//					getApplicationContext().MODE_PRIVATE);
//			editor = sp.edit();
//			editor.putString("title", msg.getString("title"));
//			editor.putString("emotion", msg.getString("emotion"));
//			editor.putString("message", msg.getString("message"));
//
//			editor.putString("link", msg.getString("link"));
//			editor.commit();

                // The stack builder object will contain an artificial back stack
                // for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out
                // of
                // your application to the Home screen.
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(AppUpdate.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(backIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_IMMUTABLE);
                }else{
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }

                break;
            case 5:
                //                backIntent = new Intent(getApplicationContext(),
//                        CustomeWebView.class);
//
//                backIntent.putExtras(msg);

                Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
                notificationIntent.setData(Uri.parse(msg.getString("link1")));

                // The stack builder object will contain an artificial back stack
                // for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out
                // of
                // your application to the Home screen.
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(AppUpdate.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(notificationIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_IMMUTABLE);
                }else{
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }

                break;
            case 6:
                backIntent = new Intent(getApplicationContext(),
                        Notification_MainActivity.class);

                // The stack builder object will contain an artificial back stack
                // for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out
                // of
                // your application to the Home screen.
                stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                // stackBuilder.addParentStack(AppUpdate.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(backIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_IMMUTABLE);
                }else{
                    pendingIntent = stackBuilder.getPendingIntent(num,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.addContact(
                        new News(msg.getString("title"), msg.getString("msg"),
                                msg.getString("link1"), msg.getString("image")),
                        DatabaseHandler.TABLE_NEWS);

                Intent intent_notify = new Intent(FCMActivity.NEW_NOTIFICATION);
                intent_notify.putExtra("DUMMY", "MUST");
                sendBroadcast(intent_notify);
                break;
            default:
                break;
        }
        if (!is_noty) {

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String CHANNEL_ID = getString(R.string.app_name) + "channel_01";// The id of the channel.
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            NotificationCompat.Builder mBuilder;

            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                mBuilder = new NotificationCompat.Builder(
                        this, CHANNEL_ID);


                mNotificationManager.createNotificationChannel(mChannel);

            } else {
                mBuilder = new NotificationCompat.Builder(
                        this, CHANNEL_ID);


            }

            mBuilder.setSmallIcon(R.drawable.z_ic_stat_fcm)
                    .setContentTitle(msg.getString("title"))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg
                                    .getString("msg").toString()))
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setContentText(msg.getString("msg"));

            //if (Integer.parseInt(msg.getString("type")) != 1) {
            mBuilder.setContentIntent(pendingIntent);
            //}

            mBuilder.setDefaults(Notification.DEFAULT_ALL);

            mNotificationManager.notify(++NOTIFICATION_ID, mBuilder.build());

        }
        /*if (!is_noty) {
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);

            mBuilder.setSmallIcon(R.drawable.ic_stat_fcm)
                    .setContentTitle(msg.getString("title"))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg
                                    .getString("msg").toString()))
                    .setAutoCancel(true)
                    .setContentText(msg.getString("msg"));

            //if (Integer.parseInt(msg.getString("type")) != 1) {
            mBuilder.setContentIntent(pendingIntent);
            //}

            mBuilder.setDefaults(Notification.DEFAULT_ALL);

            mNotificationManager.notify(++NOTIFICATION_ID, mBuilder.build());
        }*/
    }

    private class DisplayToast implements Runnable {
        String mText;

        public DisplayToast(String text) {
            mText = text;
        }

        public void run() {
            Toast.makeText(DevSpy_FIRMessagingService.this, mText, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        this.stopForeground(true);
    }
}
