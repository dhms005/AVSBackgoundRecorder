package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Service.FloatingService;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.FileUtills;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;
import com.hbisoft.hbrecorder.HBRecorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class FloatingSSCapService extends Service {
    private IBinder binder = new ServiceBinder();
    private Intent mScreenCaptureIntent = null;
    String screenshotOutput;
    private int ssDensity;
    public Handler ssHandler;
    private int ssHeight;
    public ImageReader ssImageReader;
    public MediaProjection ssMediaProjection;
    public VirtualDisplay ssVirtualDisplay;
    private int ssWidth;
    String finalPath;
    String extension;

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.mScreenCaptureIntent = (Intent) intent.getParcelableExtra("android.intent.extra.INTENT");
            captureScreen();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private static int getVirtualDisplayFlags() {
        return 16;
    }

    public void onCreate() {
        super.onCreate();
        new Thread() {
            public void run() {
                Looper.prepare();
                FloatingSSCapService.this.ssHandler = new Handler(Looper.getMainLooper());
                Looper.loop();
            }
        }.start();
    }

    public IBinder onBind(Intent intent) {
        Log.d("TAG", "Binding successful!");
        return this.binder;
    }

    public class ServiceBinder extends Binder {
        public ServiceBinder() {
        }

        public FloatingSSCapService getService() {
            return FloatingSSCapService.this;
        }
    }

    public void captureScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initScreenshotRecorder();
                initImageReader();
                createImageVirtualDisplay();
            }
        }, 300);
    }


    public void initScreenshotRecorder() {
        int intExtra = this.mScreenCaptureIntent.getIntExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, Utils.RESULT_CODE_FAILED);
        Object systemService = getSystemService("media_projection");
        Objects.requireNonNull(systemService);
        MediaProjection mediaProjection = ((MediaProjectionManager) systemService).getMediaProjection(intExtra, this.mScreenCaptureIntent);
        this.ssMediaProjection = mediaProjection;

        AudioManager audioManager = (AudioManager) FloatingSSCapService.this.getSystemService("audio");
        if (AppPref.getScreenSound(CY_M_MyApplication.getAppContext()) && audioManager.getRingerMode() == 2) {
            new MediaActionSound().play(0);
        }

        mediaProjection.registerCallback(new MediaProjectionStopCallback(), this.ssHandler);
    }

    public void initImageReader() {
        if (this.ssImageReader == null) {
            this.ssDensity = Resources.getSystem().getDisplayMetrics().densityDpi;
            this.ssWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int i = Resources.getSystem().getDisplayMetrics().heightPixels;
            this.ssHeight = i;
            ImageReader newInstance = ImageReader.newInstance(this.ssWidth, i, 1, 1);
            this.ssImageReader = newInstance;
            newInstance.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    try {
                        cutOutFrame();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }, this.ssHandler);
        }
    }


    public void createImageVirtualDisplay() {
        this.ssVirtualDisplay = this.ssMediaProjection.createVirtualDisplay("mediaprojection", this.ssWidth, this.ssHeight, this.ssDensity, getVirtualDisplayFlags(), this.ssImageReader.getSurface(), (VirtualDisplay.Callback) null, this.ssHandler);
    }

    public void cutOutFrame() {
        Image image = this.ssImageReader.acquireLatestImage();
        if (image != null) {
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int i = this.ssWidth;
            Bitmap bitmap = Bitmap.createBitmap(i + ((rowStride - (pixelStride * i)) / pixelStride), this.ssHeight, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
//            this.screenshotOutput = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + Utils.VIDEO_DIRECTORY_NAME).getPath();
            this.screenshotOutput = AppConstants.getCachePath(this);

            if (AppPref.getImageFormat(CY_M_MyApplication.getAppContext()).equalsIgnoreCase("JPG")) {
                extension = "jpeg";
            } else {
                extension = "png";
            }

            finalPath = this.screenshotOutput + File.separator + ("ss_" + Utils.getUnixTimeStamp() + "." + extension);

            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(finalPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            } catch (Exception e2) {
                bitmap = null;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    image.close();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            stopProjection();
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (Build.VERSION.SDK_INT >= 26) {
                stopForeground(true);
            } else {
                stopSelf();
            }
        }
    }


    private void stopProjection() {
        Handler handler = this.ssHandler;
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ssMediaProjection != null) {
                        ssMediaProjection.stop();
                    }
                }
            });
        }
    }


    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        private MediaProjectionStopCallback() {
        }

        public void onStop() {
            Log.e("screenshot", "stopping projection.");
            FloatingSSCapService.this.ssHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (FloatingSSCapService.this.ssVirtualDisplay != null) {
                        FloatingSSCapService.this.ssVirtualDisplay.release();
                    }
                    if (FloatingSSCapService.this.ssImageReader != null) {
                        FloatingSSCapService.this.ssImageReader.setOnImageAvailableListener((ImageReader.OnImageAvailableListener) null, (Handler) null);
                        FloatingSSCapService.this.ssImageReader = null;
                    }
                    FloatingSSCapService.this.ssMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
//                    Utils.toast(FloatingSSCapService.this.getApplicationContext(), "Screenshot Saved", 0);
                    FileUtills.startScreens(CY_M_MyApplication.getAppContext(), finalPath);
                }
            });
        }


    }

    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        } else {
            stopSelf();
        }
    }
}
