package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.display.VirtualDisplay;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.Keep;
import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment.Screen_Shot_Tab;
import com.ds.audio.video.screen.backgroundrecorder.Utils.CY_M_SharedPref;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView.FloatingListener;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView.FloatingViewManager;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.GetIntentForImage;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.ScreenshotRequestActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.SplashActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.Constants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.FileUtills;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.ScreenshotNotificationReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
public class FloatingService extends Service implements FloatingListener {
    public static final String CHANNEL_ID = "CHANNEL_ID2";
    public static final String EXTRA_CUTOUT_SAFE_AREA = "cutout_safe_area";
    public static final String NOTIFICATION_ID = "com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp";
    private static final String TAG = "FloatingViewService";
    private static FloatingService mInstance;
    public static MediaProjectionManager mProjectionManager;
    public static MediaProjection sMediaProjection;
    long delay;
    String extension;
    Bitmap.CompressFormat format;
    File imagefile;
    LayoutInflater inflater;
    int mDensity = 0;
    Display mDisplay;
    private FloatingViewManager mFloatingViewManager;

    public Handler mHandler;
    int mHeight = 0;
    ImageReader mImageReader;
    int mWidth = 0;
    DisplayMetrics metrics;
    NotificationManager notificationManager;
    String path;
    public ImageView takeScreenshot;
    long timeStampL = 0;
    VirtualDisplay virtualDisplay;
    WindowManager windowManager;
    private int NOTIFCATION_ID = 2;
    private static final String ACTION_STOP_SERVICE = "stop_service";

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onTouchFinished(boolean z, int i, int i2) {
    }

    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService("notification");
        EventBus.getDefault().register(this);
    }

    public static FloatingService getmInstance() {
        return mInstance;
    }

    public int onStartCommand(Intent intent, int i, int i2) {


        mInstance = this;
        if (this.mFloatingViewManager != null) {
            return 1;
        }
        this.mHandler = new Handler();

        mProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager2 = (WindowManager) getSystemService("window");
        this.windowManager = windowManager2;
        windowManager2.getDefaultDisplay().getMetrics(displayMetrics);
        LayoutInflater from = LayoutInflater.from(this);
        this.inflater = from;
        this.takeScreenshot = (ImageView) from.inflate(R.layout.floating_button, (ViewGroup) null, false);
        if (AppPref.getShowButton(this)) {
            this.takeScreenshot.setVisibility(0);
        } else {
            this.takeScreenshot.setVisibility(8);
        }
        this.takeScreenshot.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CY_M_SharedPref.RESUME_OPEN_CHECKER = true;
                FloatingService.this.startProjection();
            }
        });
        Intent intent2 = new Intent(this, ScreenshotNotificationReceiver.class);
        intent2.setAction(Constants.FOLDER_NAME);

        PendingIntent broadcast ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            broadcast = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            broadcast = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            broadcast = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            broadcast = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent activity;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            activity = PendingIntent.getActivity(this, 0, new Intent(this, SplashActivity.class), PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            activity = PendingIntent.getActivity(this, 0, new Intent(this, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        }


        Intent intent3 = new Intent(this, ScreenshotNotificationReceiver.class);
        intent3.setAction("exit");


        PendingIntent broadcast2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            broadcast2 = PendingIntent.getBroadcast(this, 0, intent3, PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            broadcast2 = PendingIntent.getBroadcast(this, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
        }

//        NotificationCompat.Builder builder;
//
//        if (Build.VERSION.SDK_INT >= 26) {
//            builder = new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID);
//            ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Screen Shot", 3));
//        } else {
//            builder = new NotificationCompat.Builder(this);
//        }
//
//        builder.setContentTitle(getString(R.string.screenshot_running));
//        builder.setContentText(getString(R.string.tap_to_open));
//        builder.setPriority(1);
//        builder.setSmallIcon(R.drawable.notify_icon);
//        builder.setWhen(0);
//        builder.setPriority(2);
//        builder.addAction(R.drawable.notify_stop, getString(R.string.stop), broadcast2);
////        builder.addAction(R.drawable.notify_stop, getString(R.string.capture), broadcast);
//
//        Intent intentopen = new Intent(getApplicationContext(), MainActivity.class);
//        TaskStackBuilder create = TaskStackBuilder.create(getApplicationContext());
//        create.addNextIntent(intentopen);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            builder.setContentIntent(create.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
//        } else {
//            builder.setContentIntent(create.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            builder.addAction(R.drawable.notify_stop, getString(R.string.stop), PendingIntent.getService(this, 1, intent3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
//        } else {
//            builder.addAction(R.drawable.notify_stop, getString(R.string.stop), PendingIntent.getService(this, 1, intent3, PendingIntent.FLAG_UPDATE_CURRENT));
//        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custrom_nitification);
        if (AppPref.getMenu(CY_M_MyApplication.getAppContext())) {
            remoteViews.setViewVisibility(R.id.linMains, 0);
        } else {
            remoteViews.setViewVisibility(R.id.linMains, 8);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, NOTIFICATION_ID, 2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound((Uri) null, (AudioAttributes) null);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }
        remoteViews.setOnClickPendingIntent(R.id.screenShot, broadcast);
        remoteViews.setOnClickPendingIntent(R.id.exit, broadcast2);
        remoteViews.setOnClickPendingIntent(R.id.home, activity);
        startForeground(NOTIFCATION_ID, new NotificationCompat.Builder((Context) this, CHANNEL_ID).setPriority(-2).setSmallIcon((int) R.mipmap.app_icon).setAutoCancel(true).setCustomContentView(remoteViews).build());

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(this.NOTIFCATION_ID, builder.build());
//
//        startForeground(this.NOTIFCATION_ID, builder.build());

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            return START_REDELIVER_INTENT;
        }
        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage((int) R.drawable.ic_trash_fixed);
        this.mFloatingViewManager.setActionTrashIconImage((int) R.drawable.ic_trash_action);
        this.mFloatingViewManager.setSafeInsetRect((Rect) intent.getParcelableExtra(EXTRA_CUTOUT_SAFE_AREA));
        FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (displayMetrics.density * 16.0f);
        if (!AppPref.getShowButton(this)) {
            return START_REDELIVER_INTENT;
        }
        this.mFloatingViewManager.addViewToWindow(this.takeScreenshot, options);
        return START_REDELIVER_INTENT;
    }

    public void onFinishFloatingView() {
        stopSelf();
    }

    private void destroy() {
        FloatingViewManager floatingViewManager = this.mFloatingViewManager;
        if (floatingViewManager != null) {
            floatingViewManager.removeAllViewToWindow();
            this.mFloatingViewManager = null;
        }
        if (sMediaProjection != null) {
            sMediaProjection = null;
        }
        if (Screen_Shot_Tab.getInstance() != null) {
            Screen_Shot_Tab.getInstance().setStartStopButton();
        }
    }

    public void startProjection() {
        if (sMediaProjection == null) {
            startActivity(new Intent(this, ScreenshotRequestActivity.class).addFlags(335544320));
            return;
        }
        Log.e(TAG, "onGetIntentForImage: 2");
        this.delay = 10;
        createVirtualDisplay();
    }

    private void stopProjection() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() {
                public void run() {
                    if (FloatingService.sMediaProjection != null) {
                        FloatingService.sMediaProjection.stop();
                    }
                    if (FloatingService.this.mImageReader != null) {
                        if (FloatingService.this.virtualDisplay != null) {
                            FloatingService.this.virtualDisplay.release();
                        }
                        FloatingService.this.mImageReader.close();
                    }
                }
            });
        }
    }

    private void createVirtualDisplay() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                try {
                    if (FloatingService.sMediaProjection != null) {
                        Display defaultDisplay = FloatingService.this.windowManager.getDefaultDisplay();
                        FloatingService.this.metrics = new DisplayMetrics();
                        defaultDisplay.getMetrics(FloatingService.this.metrics);
                        int i = FloatingService.this.metrics.widthPixels;
                        int i2 = FloatingService.this.metrics.heightPixels;
                        int i3 = FloatingService.this.metrics.densityDpi;
                        FloatingService.this.timeStampL = System.currentTimeMillis();
                        FloatingService.this.takeScreenshot.setVisibility(4);
                        Log.e(FloatingService.TAG, "onGetIntentForImage: 3");
                        FloatingService.this.mImageReader = ImageReader.newInstance(i, i2, 1, 1);
                        FloatingService.this.virtualDisplay = FloatingService.sMediaProjection.createVirtualDisplay("screen-mirror", i, i2, i3, 9, FloatingService.this.mImageReader.getSurface(), (VirtualDisplay.Callback) null, FloatingService.this.mHandler);
                        AudioManager audioManager = (AudioManager) FloatingService.this.getSystemService("audio");
                        if (AppPref.getScreenSound(CY_M_MyApplication.getAppContext()) && audioManager.getRingerMode() == 2) {
                            new MediaActionSound().play(0);
                        }
                        FloatingService floatingService = FloatingService.this;
                        floatingService.onImageAvailable(floatingService.mImageReader);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this.delay);
    }

    public void onDestroy() {


        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        stopProjection();
        EventBus.getDefault().unregister(this);
        destroy();
    }

    public boolean stopService(Intent intent) {
        stopProjection();
        EventBus.getDefault().unregister(this);
        destroy();
        Log.i(TAG, "stopService: ");
        return super.stopService(intent);
    }

    public void onImageAvailable(final ImageReader imageReader) {
        this.takeScreenshot.setVisibility(4);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                if (AppPref.getImageFormat(CY_M_MyApplication.getAppContext()).equalsIgnoreCase("JPG")) {
                    FloatingService.this.extension = "jpeg";
                } else {
                    FloatingService.this.extension = "png";
                }
                Image image = null;
                try {
                    image = imageReader.acquireLatestImage();
                    if (image != null) {
                        Log.e(FloatingService.TAG, "onGetIntentForImage: 4");
                        Image.Plane[] planes = image.getPlanes();
                        ByteBuffer buffer = planes[0].getBuffer();
                        int pixelStride = planes[0].getPixelStride();
                        Bitmap createBitmap = Bitmap.createBitmap(FloatingService.this.metrics.widthPixels + ((int) (((float) (planes[0].getRowStride() - (FloatingService.this.metrics.widthPixels * pixelStride))) / ((float) pixelStride))), FloatingService.this.metrics.heightPixels, Bitmap.Config.ARGB_8888);
                        Log.i(FloatingService.TAG, "onImageAvailable: " + buffer);
                        createBitmap.copyPixelsFromBuffer(buffer);
                        Bitmap createBitmap2 = Bitmap.createBitmap(createBitmap, 0, 0, FloatingService.this.metrics.widthPixels, createBitmap.getHeight());
                        FloatingService floatingService = FloatingService.this;
                        floatingService.path = floatingService.storeImage("WebFile." + FloatingService.this.extension, createBitmap2);
                        if (AppPref.getShowButton(CY_M_MyApplication.getAppContext())) {
                            FloatingService.this.takeScreenshot.setVisibility(View.VISIBLE);
                        } else {
                            FloatingService.this.takeScreenshot.setVisibility(View.GONE);
                        }
                        FileUtills.startScreens(CY_M_MyApplication.getAppContext(), FloatingService.this.path);
                    }
                    if (image == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (image == null) {
                        return;
                    }
                } catch (Throwable th) {
                    if (image != null) {
                        image.close();
                        Log.i(FloatingService.TAG, "onImageAvailable: close");
                    }
                    throw th;
                }
                image.close();
                Log.i(FloatingService.TAG, "onImageAvailable: close");
            }
        }, 500);
    }


    public String storeImage(String str, Bitmap bitmap) {
        File file = new File(AppConstants.getCachePath(this), str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e2) {
            Log.d("TAG", "Error accessing file: " + e2.getMessage());
        }
        return file.toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetIntentForImage(GetIntentForImage getIntentForImage) {
        if (getIntentForImage != null) {
            Log.i("OLLLLLLLLL", "onGetIntentForImage: " + getIntentForImage);
            Intent intent = getIntentForImage.getIntent();
            if (intent != null) {
                sMediaProjection = mProjectionManager.getMediaProjection(-1, (Intent) intent.clone());
                Log.e(TAG, "onGetIntentForImage: 1");
                this.delay = 200;
                createVirtualDisplay();
                return;
            }
            ImageView imageView = this.takeScreenshot;
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }
}
