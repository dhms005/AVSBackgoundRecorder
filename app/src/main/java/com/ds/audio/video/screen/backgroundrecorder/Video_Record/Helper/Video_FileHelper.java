package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper;

import static com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_SaveVideoActivity.setToast;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Video_FileHelper {
    public static final String ERROR = "ERROR";
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static String dirPath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/CM_CCTV");
    public static String postfixed_path = "/CM_CCTV/Videos";

    public static File getOutputMediaFile(int i, Context context) {
        File file = new File(videoPath(context));
        if (!file.exists() && !file.mkdirs()) {
            file.mkdirs();
//            return null;
        }
        String format = generateFileName();
        if (i != 2) {
            return null;
        }
        return new File(file.getPath() + File.separator + "BVR_" + format + ".mp4");
    }

    public static void CreateFolder(Context context) {
        File file = new File(videoPath(context));
        if (!file.exists() && !file.mkdirs()) {
            file.mkdirs();
//            return null;
        }
    }

    public static String videoPath(Context context) {
        String video_path = context.getExternalFilesDir(null) + postfixed_path + File.separator;
        return video_path;
    }

    private static String generateFileName() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(new java.sql.Date(System.currentTimeMillis())).replace(" ", "");
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
