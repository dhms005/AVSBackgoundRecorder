package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers;

import static com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_SaveVideoActivity.setToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import androidx.core.content.FileProvider;

import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.io.File;

public class ScreenShot_Video_FileHelper {
    public static final String ERROR = "ERROR";
    public static String dirPath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/CM_CCTV");
    public static String postfixed_path = "/CM_CCTV/ScreenRecorder";

    public static void CreateFolder(Context context) {
        File file = new File(videoPath(context));
        if (!file.exists() && !file.mkdirs()) {
            file.mkdirs();
//            return null;
        }
    }

    public static String videoPath(Context context) {
        String video_path = context.getExternalFilesDir(null) + postfixed_path + File.separator;

        File file = new File(video_path);
        if (!file.exists() && !file.mkdirs()) {
            file.mkdirs();
//            return null;
        }

        return video_path;
    }



    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getAvailableExternalMemorySize(Context context) {
        if (!externalMemoryAvailable()) {
            return ERROR;
        }

        StatFs statFs = new StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        return Formatter.formatFileSize(context, ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks()));
    }

    public static long getAvailableExternalMemory() {
        if (!externalMemoryAvailable()) {
            return 0;
        }

        StatFs statFs = new StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        return (((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks())) / 1048576;
    }

    public static void shareVideo(final Activity activity, String str) {
        Uri imageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(str));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("video/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivity(shareIntent);
        } catch (Exception e) {
            e.printStackTrace();
            setToast(activity, "Could Not Be Share");
        }


    }
}
