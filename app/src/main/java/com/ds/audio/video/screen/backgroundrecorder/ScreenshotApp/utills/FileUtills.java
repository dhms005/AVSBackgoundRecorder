package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.EditImageActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.GalleryActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.ResultViewerActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.DiaryImageData;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_MyApplication;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtills {
    public static String lastName;

    public static File saveScreenshotToPicturesFolder(Context context, Bitmap bitmap, long j) throws Exception {
        File outputMediaFile = getOutputMediaFile(context, j, false);
        if (outputMediaFile == null) {
            return null;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(outputMediaFile);
        bitmap.compress(AppPref.getImageFormat(context).equalsIgnoreCase("jpeg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, AppPref.getImageQuality(context), fileOutputStream);
        fileOutputStream.close();
        AppConstants.refreshFiles(DevSpy_MyApplication.getAppContext(), outputMediaFile);
        return outputMediaFile;
    }

    public static File rootStoreDir() {
        return new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME);
    }

    public static File getOutputMediaFile(Context context, long j, boolean z) {
        File rootStoreDir = rootStoreDir();
        if (!rootStoreDir.exists() && !rootStoreDir.mkdirs()) {
            return null;
        }
        String str = AppPref.getPrefix(DevSpy_MyApplication.getAppContext()) + "_" + System.currentTimeMillis() + AppPref.getImageFormat(DevSpy_MyApplication.getAppContext());
        if (AppPref.getImageFormat(DevSpy_MyApplication.getAppContext()).equalsIgnoreCase("JPG")) {
            lastName = ".jpeg";
        } else {
            lastName = ".png";
        }
        String str2 = str + lastName;
        File file = new File(rootStoreDir.getPath() + File.separator + str2);
        if (GalleryActivity.getInstance() != null) {
            GalleryActivity.getInstance().addData(new DiaryImageData(file.getAbsolutePath(), str2, System.currentTimeMillis()));
        }
        return file;
    }


    public static void start(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(context, R.string.no_choose, 0).show();
            return;
        }
        Intent intent = new Intent(context, ResultViewerActivity.class);
        intent.setFlags(268435456);
        intent.putExtra(EditImageActivity.REFERENCE, true);
        intent.putExtra(EditImageActivity.FILE_PATH, str);
        context.startActivity(intent);
    }

    public static void startScreen(Context context, String str, int i) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(context, R.string.no_choose, 0).show();
            return;
        }
        Intent intent = new Intent(context, ResultViewerActivity.class);
        intent.setFlags(268435456);
        intent.putExtra(EditImageActivity.REFERENCE, true);
        intent.putExtra(EditImageActivity.FILE_PATH, str);
        intent.putExtra("isFromCrop", true);
        context.startActivity(intent);
    }

    public static void startScreens(Context context, String str) {
        Intent intent = new Intent(context, ResultViewerActivity.class);
        intent.setFlags(335544320);
        intent.putExtra(EditImageActivity.REFERENCE, false);
        intent.putExtra(EditImageActivity.FILE_PATH, str);
        intent.putExtra("isFromCropService", true);
        context.startActivity(intent);
    }
}
