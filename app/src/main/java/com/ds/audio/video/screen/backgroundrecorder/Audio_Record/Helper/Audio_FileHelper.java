package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper;

import static com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_SaveVideoActivity.setToast;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.Locale;

public class Audio_FileHelper {
    public static final String ERROR = "ERROR";
    public static final int MEDIA_TYPE_VIDEO = 2;
    //public static String dirPath = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/CCTVCameraRecorder");
//    public static String dirPath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/CM_CCTV");
    public static String postfixed_path = "/CM_CCTV/Audio";

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
        return new File(file.getPath() + File.separator + "Audio_" + format + ".3gp");
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

    public static void removeAllForPaths(String[] strArr, Context context) {
        String[] strArr2 = {"_id", "_data", "title"};
        if (!(strArr == null || strArr.length == 0)) {
            String str = "";
            for (String str2 : strArr) {
                if (!str.equals("")) {
                    str = str + " OR ";
                }
                str = str + "_data=?";
            }
            Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr2, str, strArr, null);
            query.moveToFirst();
            while (!query.isAfterLast()) {
                context.getContentResolver().delete(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (long) query.getInt(query.getColumnIndex("_id"))), null, null);
                query.moveToNext();
            }
            query.close();
        }
    }

    public static void renameFile(String str, String str2, Context context) {
        File file = new File(videoPath(context));
        File file2 = new File(str);
        File file3 = new File(file.getPath() + File.separator + str2 + ".mp4");
        file2.renameTo(file3);
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file3));
        context.sendBroadcast(intent);

        removeAllForPaths(new String[]{str}, context);
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
        Log.e("@imguriaudio", "" + imageUri.toString());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("audio/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivity(shareIntent);
        } catch (Exception e) {
            e.printStackTrace();
            setToast(activity, "Could Not Be Share");
        }

       /* MediaScannerConnection.scanFile(activity, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("video/*");
                intent.putExtra("android.intent.extra.STREAM", uri);
                intent.addFlags(524288);
                activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.str_share_this_video)));
            }
        });*/
    }
}
