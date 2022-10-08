package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static final String ACTION_VIDEO_RECORDED = "ACTION_VIDEO_RECORDED";
    public static final String IS_ADS_DISABLED = "is_ad_disabled";
    public static final String IS_REWARD_VIDEO = "is_reward_video";
    public static final int RESULT_CODE_FAILED = -999999;
    public static final String SCREEN_CAPTURE_INTENT_RESULT_CODE = "SCREEN_CAPTURE_INTENT_RESULT_CODE";
    public static final String VIDEO_DIRECTORY_NAME = "ScreenRecorder";
    public static final List<String> storagePermissionList = Arrays.asList(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"});

    public static void showPermissionAlert(Context context, String message) {
        AlertDialog show = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle).setTitle((CharSequence) "Need Permission").setMessage((CharSequence) message).setCancelable(false).setPositiveButton((CharSequence) "SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openSettings(context);

            }
        }).show();
    }

    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), (String) null));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static void showSnackBarNotification(View view, String message, int result) {
        Snackbar.make(view, (CharSequence) message, result).show();
    }

    public static void toast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void createFolder() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), VIDEO_DIRECTORY_NAME);
        if (!file.exists() && file.mkdirs()) {
            Log.i("Folder ", "created");
        }
    }

    public static void createScreenshotFolder() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), VIDEO_DIRECTORY_NAME);
        if (!file.exists() && file.mkdirs()) {
            Log.i("Folder ", "created");
        }
    }

    public static long getUnixTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    public static boolean isServiceRunning(String serviceName, Context context) {
        boolean serviceRunning = false;
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                serviceRunning = true;
                boolean z = runningServiceInfo.foreground;
            }
        }
        return serviceRunning;
    }
}
