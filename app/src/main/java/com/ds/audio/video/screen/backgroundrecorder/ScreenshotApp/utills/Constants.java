package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class Constants {
    public static final int ACTION_REQUEST_EDITIMAGE = 108;
    public static final String EditImageIntentKey = "passUri";
    public static final String FOLDER_NAME = "Screenshot";
    public static final int PICK_PICTURE_FROM_GALLERY = 107;
    public static final String PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE";
    public static final int REQUEST_CODE = 1009;
    public static final int REQUEST_CODE_FOR_PROJECTION = 105;
    public static final String SEARCH_URL = "https://www.google.com/search?q=%s";
    public static final int STORAGE_PERMISIION_CODE_FLOATING_BUTTON = 108;

    public static void shareImage(Context context, String str, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.SUBJECT", "Share image");
        if (!z) {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, "com.ds.audio.video.screen.backgroundrecorder.provider", new File(str)));
        } else if (Build.VERSION.SDK_INT >= 29) {
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(str));
        } else {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, "com.ds.audio.video.screen.backgroundrecorder.provider", new File(str)));
        }
        context.startActivity(Intent.createChooser(intent, "Share via").setFlags(268435456));
    }

    public static void shareImageOnSicialMedia(Context context, String str, String str2, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setPackage(str2);
        intent.setType("image/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.SUBJECT", "Share image");
        if (!z) {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, "com.ds.audio.video.screen.backgroundrecorder.provider", new File(str)));
        } else if (Build.VERSION.SDK_INT >= 29) {
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(str));
        } else {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(context, "com.ds.audio.video.screen.backgroundrecorder.provider", new File(str)));
        }
        context.startActivity(Intent.createChooser(intent, "Share via").setFlags(268435456));
    }

    public static String postfixed_path = "/CM_CCTV/Images";


    public static String imagePath(Context context) {
        String video_path = context.getExternalFilesDir(null) + postfixed_path + File.separator;
        return video_path;
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
