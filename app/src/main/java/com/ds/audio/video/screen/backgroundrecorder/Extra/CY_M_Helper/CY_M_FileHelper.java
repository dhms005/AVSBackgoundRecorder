package com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Helper;

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


import com.ds.audio.video.screen.backgroundrecorder.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CY_M_FileHelper {
    public static final String ERROR = "ERROR";
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static String dirPath = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/CCTVCameraRecorder");

    public static File getOutputMediaFile(int i) {
        File file = new File(dirPath);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        if (i != 2) {
            return null;
        }
        return new File(file.getPath() + File.separator + "BVR" + format + ".mp4");
    }

    public static void removeAllForPaths(String[] strArr, Context context) {
        String[] strArr2 = {"_id", "_data", "title"};
        if (strArr != null && strArr.length != 0) {
            String str = "";
            for (String str2 : strArr) {
                if (!str.equals("")) {
                    str = str + " OR ";
                }
                str = str + "_data=?";
            }
            Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr2, str, strArr, (String) null);
            query.moveToFirst();
            while (!query.isAfterLast()) {
                context.getContentResolver().delete(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (long) query.getInt(query.getColumnIndex("_id"))), (String) null, (String[]) null);
                query.moveToNext();
            }
            query.close();
        }
    }

    public static void renameFile(String str, String str2, Context context) {
        File file = new File(dirPath);
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
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return Formatter.formatFileSize(context, ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks()));
    }

    public static long getAvailableExternalMemory() {
        if (!externalMemoryAvailable()) {
            return 0;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks())) / 1) << 20;
    }

    public static void shareVideo(final Activity activity, String str) {
        MediaScannerConnection.scanFile(activity, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("video/*");
                intent.putExtra("android.intent.extra.STREAM", uri);
                intent.addFlags(524288);
                Activity activity = null;
                activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.str_share_this_video)));
            }
        });
    }
}
