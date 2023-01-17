package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSaveHelper implements LifecycleObserver {
    private final ExecutorService executor;
    private final MutableLiveData<FileMeta> fileCreatedResult;
    private final ContentResolver mContentResolver;
    private final Observer<FileMeta> observer;
    private OnFileCreateResult resultListener;

    public interface OnFileCreateResult {
        void onFileCreateResult(boolean z, String str, String str2, Uri uri);
    }

    public void FileSaveHelperMeta(FileMeta fileMeta) {
        OnFileCreateResult onFileCreateResult = this.resultListener;
        if (onFileCreateResult != null) {
            onFileCreateResult.onFileCreateResult(fileMeta.isCreated, fileMeta.filePath, fileMeta.error, fileMeta.uri);
        }
    }

    public FileSaveHelper(ContentResolver contentResolver) {
        this.observer = new Observer() {
            public final void onChanged(Object obj) {


                FileSaveHelper.this.FileSaveHelperMeta((FileMeta) obj);
            }
        };
        this.mContentResolver = contentResolver;
        this.executor = Executors.newSingleThreadExecutor();
        this.fileCreatedResult = new MutableLiveData<>();
    }

    public FileSaveHelper(AppCompatActivity appCompatActivity) {
        this(appCompatActivity.getContentResolver());
        addObserver(appCompatActivity);
    }

    private void addObserver(LifecycleOwner lifecycleOwner) {
        this.fileCreatedResult.observe(lifecycleOwner, this.observer);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void release() {
        ExecutorService executorService = this.executor;
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    public static boolean isSdkHigherThan28() {
        return Build.VERSION.SDK_INT >= 29;
    }

    public void createFile(String str, OnFileCreateResult onFileCreateResult) {
        this.resultListener = onFileCreateResult;
        this.executor.submit(new Runnable() {
            public final void run() {
                try {
                    String cachePath = AppConstants.getCachePath(DevSpy_MyApplication.getAppContext());
                    File file = new File(cachePath, System.currentTimeMillis() + ".png");
                    updateResult(true, file.getAbsolutePath(), (String) null, Uri.parse(file.getAbsolutePath()), (ContentValues) null);
                } catch (Exception e) {
                    e.printStackTrace();
                    updateResult(false, (String) null, e.getMessage(), (Uri) null, (ContentValues) null);
                }
            }
        });
    }


    private String getFilePath(Cursor cursor, Uri uri) {
        Cursor query = this.mContentResolver.query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.moveToFirst();
        return query.getString(columnIndexOrThrow);
    }

    private Uri getEditedImageUri(String str, ContentValues contentValues, Uri uri) throws IOException {
        contentValues.put("_display_name", str);
        Uri insert = this.mContentResolver.insert(uri, contentValues);
        this.mContentResolver.openOutputStream(insert).close();
        return insert;
    }

    private Uri buildUriCollection(ContentValues contentValues) {
        if (!isSdkHigherThan28()) {
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        Uri contentUri = MediaStore.Images.Media.getContentUri("external_primary");
        contentValues.put("is_pending", 1);
        return contentUri;
    }

    public void notifyThatFileIsNowPubliclyAvailable(ContentResolver contentResolver) {
        if (isSdkHigherThan28()) {
            this.executor.submit(new Runnable() {

                public final void run() {

                    FileMeta value = fileCreatedResult.getValue();
                    if (value != null) {
                        value.imageDetails.clear();
                        value.imageDetails.put("is_pending", 0);
                        contentResolver.update(value.uri, value.imageDetails, (String) null, (String[]) null);
                    }
                }
            });
        }
    }


    private static class FileMeta {
        public String error;
        public String filePath;
        public ContentValues imageDetails;
        public boolean isCreated;
        public Uri uri;

        public FileMeta(boolean z, String str, Uri uri2, String str2, ContentValues contentValues) {
            this.isCreated = z;
            this.filePath = str;
            this.uri = uri2;
            this.error = str2;
            this.imageDetails = contentValues;
        }
    }

    private void updateResult(boolean z, String str, String str2, Uri uri, ContentValues contentValues) {
        this.fileCreatedResult.postValue(new FileMeta(z, str, uri, str2, contentValues));
    }
}
