package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.core.net.MailTo;

import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_MyApplication;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppConstants {
    public static String playStoreUrl = "https://play.google.com/store/apps/details?id=com.ds.audio.video.screen.backgroundrecorder";
    public static String title = "Support us by giving rate and your precious review !!\nIt will take few seconds only.";


    public static void EmailUs(Context context, String str) {
        try {
            String str2 = Build.MODEL;
            String str3 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            Intent intent2 = new Intent("android.intent.action.SENDTO");
            intent2.putExtra("android.intent.extra.EMAIL", new String[]{"getapps5454@gmail.com"});
            intent2.putExtra("android.intent.extra.SUBJECT", "Your Suggestion - AVS Background Recorder  (" + context.getPackageName() + ")");
            intent2.putExtra("android.intent.extra.TEXT", str + "\n\nDevice Manufacturer : " + str3 + "\nDevice Model : " + str2 + "\nAndroid Version : " + Build.VERSION.RELEASE + "\nApp Version : " + BuildConfig.VERSION_NAME);
            intent2.setSelector(intent);
            context.startActivity(intent2);
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    public static void uriparse(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1476919296);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(context, "No suitable Application found", 0).show();
        }
    }

    public static boolean checkdate(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance.set(10, 0);
        instance.set(12, 0);
        instance.set(14, 0);
        instance.set(13, 0);
        instance.set(9, 0);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(j2);
        instance2.set(10, 0);
        instance2.set(12, 0);
        instance2.set(14, 0);
        instance2.set(13, 0);
        instance2.set(9, 0);
        if (instance.getTimeInMillis() == instance2.getTimeInMillis()) {
            return true;
        }
        return false;
    }

    public static String formattedDate(long j, DateFormat dateFormat) {
        return dateFormat.format(new Date(j));
    }

    public static void refreshFiles(Context context, File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }


    public static String getRootPath(Context context) {
        File file = new File(context.getFilesDir(), "ImageEditor");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String getCachePath(Context context) {
        File file = new File(context.getCacheDir(), "ImageEditor");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static void deleteTempFile(Context context) {
        try {
            deleteFolder(new File(getRootPath(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteFolder(File file) {
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                deleteFolder(file2);
            } else {
                file2.delete();
            }
        }
        file.delete();
    }

    public static Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException {
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        int photoOrientation = getPhotoOrientation(uri);
        Matrix matrix = new Matrix();
        if (Build.VERSION.SDK_INT >= 24) {
            matrix.postRotate((float) photoOrientation);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(openInputStream, (Rect) null, options);
        openInputStream.close();
        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }
        int i = options.outHeight > options.outWidth ? options.outHeight : options.outWidth;
        double d = i > 4096 ? (double) (i / 4096) : 1.0d;
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = getPowerOfTwoForSampleRatio(d);
        options2.inDither = true;
        options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
        InputStream openInputStream2 = context.getContentResolver().openInputStream(uri);
        Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream2, (Rect) null, options2);
        openInputStream2.close();
        return Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
    }

    private static int getPowerOfTwoForSampleRatio(double d) {
        int highestOneBit = Integer.highestOneBit((int) Math.floor(d));
        if (highestOneBit == 0) {
            return 1;
        }
        return highestOneBit;
    }

    public static int getPhotoOrientation(Uri uri) {
        ExifInterface exifInterface;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                InputStream inputStream = null;
                try {
                    inputStream = DevSpy_MyApplication.getAppContext().getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                exifInterface = new ExifInterface(inputStream);
                inputStream.close();
            } else {
                ExifInterface exifInterface2 = new ExifInterface(uri.getPath());
                Log.e("ExifInterface", uri.getPath());
                exifInterface = exifInterface2;
            }
            int attributeInt = exifInterface.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt == 3) {
                return SubsamplingScaleImageView.ORIENTATION_180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return SubsamplingScaleImageView.ORIENTATION_270;
        } catch (Exception e2) {
            e2.printStackTrace();
            return 0;
        }
    }
}
