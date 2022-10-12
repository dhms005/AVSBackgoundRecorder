package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities.DevSpy_Master_SplashScreen;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_LocaleHelper;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_SharedPref;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Key;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;

import java.util.Arrays;
import java.util.Date;

public class DevSpy_MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {


    private AppOpenAdManager appOpenManager;

    private Activity currentActivity;
    Activity activity;
    private static Context context;
    private static DevSpy_Custom_Full_Ad_Timer custom_full_ad_timer;
    private static DevSpy_MyApplication mInstance;
    public static final boolean HIDE_INCOMPLETE_FEATURES = true;

    private static DevSpy_Custom_AppLovin_Timer custom_appLovin_timer;


    public DevSpy_MyApplication(Activity activity) {
        this.activity = activity;
    }

    public DevSpy_MyApplication() {
        this.activity = null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this;
        SharePrefUtils.init(getApplicationContext());
        FirebaseApp.initializeApp(this);
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        //  Log.e("device id", "onCreate: "+android_id );


        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
        appOpenManager = new AppOpenAdManager(getApplicationContext());
        custom_full_ad_timer = new DevSpy_Custom_Full_Ad_Timer(this);
        custom_appLovin_timer = new DevSpy_Custom_AppLovin_Timer(this);
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(android_id)).build();
        MobileAds.setRequestConfiguration(configuration);
        // Realm.init(this);


        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
//        Pul_AdBlocker.init(getApplicationContext());
//        if (new Pul_StoreUserData(mInstance.getApplicationContext()).getSettings() == null) {
//            new Pul_StoreUserData(mInstance.getApplicationContext()).setSettings(new Pul_SettingsItem());
//        }
//        FileDownloader.setupOnApplicationOnCreate(this).connectionCreator(new FileDownloadUrlConnection.Creator(new FileDownloadUrlConnection.Configuration().connectTimeout(DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS).readTimeout(DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS))).commit();
//        ThreadDebugger.install(ThreadDebuggers.create().add(CommonThreadKey.OpenSource.OKHTTP).add(CommonThreadKey.OpenSource.OKIO).add(CommonThreadKey.System.BINDER).add(FileDownloadUtils.getThreadPoolName("Network"), "Network").add(FileDownloadUtils.getThreadPoolName("Flow"), "FlowSingle").add(FileDownloadUtils.getThreadPoolName("EventPool"), "Event").add(FileDownloadUtils.getThreadPoolName("LauncherTask"), "LauncherTask").add(FileDownloadUtils.getThreadPoolName("ConnectionBlock"), "Connection").add(FileDownloadUtils.getThreadPoolName("RemitHandoverToDB"), "RemitHandoverToDB").add(FileDownloadUtils.getThreadPoolName("BlockCompleted"), "BlockCompleted"), 2000, new ThreadDebugger.ThreadChangedCallback() {
//            public void onChanged(IThreadDebugger iThreadDebugger) {
//                Log.d(TAG, iThreadDebugger.drawUpEachThreadInfoDiff());
//                Log.d(TAG, iThreadDebugger.drawUpEachThreadSizeDiff());
//                Log.d(TAG, iThreadDebugger.drawUpEachThreadSize());
//            }
//        });
        registerActivityLifecycleCallbacks(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

//        AppLangSessionManager appLangSessionManager2 = new AppLangSessionManager(getApplicationContext());
//        this.appLangSessionManager = appLangSessionManager2;
//        setLocale(appLangSessionManager2.getLanguage());
    }

//    public void setLocale(String str) {
//        if (str.equals("")) {
//            str = AppLangSessionManager.KEY_APP_LANGUAGE;
//        }
//        Log.d("Support", str + "");
//        Locale locale = new Locale(str);
//        Resources resources = getResources();
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = locale;
//        resources.updateConfiguration(configuration, displayMetrics);
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(DevSpy_LocaleHelper.onAttach(base, "en"));
        MultiDex.install(this);
    }


    public DevSpy_Custom_Full_Ad_Timer getmanagerFullAdTimer() {
        DevSpy_Custom_Full_Ad_Timer customfulladtimer = custom_full_ad_timer;
        if (customfulladtimer == null) {
            return null;
        }
        return customfulladtimer;
    }


    public DevSpy_Custom_AppLovin_Timer getCustom_appLovin_timer() {
        DevSpy_Custom_AppLovin_Timer customfulladtimer = custom_appLovin_timer;
        if (customfulladtimer == null) {
            return null;
        }
        return customfulladtimer;
    }


    public static synchronized DevSpy_MyApplication getInstance() {

        if (mInstance == null) {
            mInstance = new DevSpy_MyApplication();
        }
        return mInstance;
    }

    public void callAd() {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            appOpenManager.loadAd1(currentActivity);
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        DevSpy_Conts.Video_Backgorund_Forgroundchecker = true;
        Log.e("Foreground", "************* onMoveToForeground");
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {

            if (DevSpy_Conts.mOpenAppChecker) {
                appOpenManager.showAdIfAvailable(currentActivity);
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onAppBackgrounded() {
        //App in background
        DevSpy_Conts.Video_Backgorund_Forgroundchecker = false;
        Log.e("Background", "************* backgrounded");
        Log.e("Background", "************* ${isInForeground()}");
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenManager.isShowingAd) {
            currentActivity = activity;
        }

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull DevSpy_MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenManager.showAdIfAvailable(activity, onShowAdCompleteListener);

    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }


    public class AppOpenAdManager {

        private static final String LOG_TAG = "AppOpenAdManager_1";
        //   private static final String AD_UNIT_ID = "/21939239661,22594620179/apl/jigaraplinapp/appopen";


        private String AD_UNIT_ID;
        private String AD_UNIT_ID_Failed;

        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        /**
         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
         */
        private long loadTime = 0;

        /**
         * Constructor.
         */
        public AppOpenAdManager(Context context) {
            //   loadAd(context);
        }

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        private void loadAd(Context context) {
            AD_UNIT_ID = SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD);

            // AD_UNIT_ID = SharedPref.openads;

            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    AD_UNIT_ID,
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();

                            //Toast.makeText(context, "onAdLoaded Aop", Toast.LENGTH_SHORT).show();
                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;
                            loadAdFailed(context);
                            //Toast.makeText(context, "onAdFailedToLoad Aop", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        public void loadAd1(Context context) {
            Log.d(LOG_TAG, "loadAd.");
            AD_UNIT_ID = SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD);
            SharePrefUtils.putBoolean(Constant_ad.FIRST_TIME, false);
            isLoadingAd = true;
            // AD_UNIT_ID = SharedPref.openads;

            // Do not load ad if there is an unused ad or one is already loading.
            if (isAdAvailable()) {
                Log.d(LOG_TAG, "isLoadingAd: ");
                return;
            }

            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    AD_UNIT_ID,
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();
                            Log.d(LOG_TAG, "onAdLoaded.");
                            //Toast.makeText(context, "onAdLoaded Aop", Toast.LENGTH_SHORT).show();
                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;
                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                            loadAdFailed(activity);
                            //Toast.makeText(context, "onAdFailedToLoad Aop", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        /**
         * Check if ad was loaded more than n hours ago.
         */
        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        /**
         * Check if ad exists and can be shown.
         */
        private boolean isAdAvailable() {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(
                    activity,
                    new DevSpy_MyApplication.OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            // Empty because the user will go back to the activity that shows the ad.
                        }
                    });
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity                 the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull DevSpy_MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }


            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            onShowAdCompleteListener.onShowAdComplete();

                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {

                            //Toast.makeText(activity, "onAdShowedFullScreenContent Aop", Toast.LENGTH_SHORT).show();
                        }
                    });

            isShowingAd = true;
            if (DevSpy_Master_SplashScreen.isSplashLoading) {

                if (!DevSpy_SharedPref.RESUME_OPEN_CHECKER) {
                    appOpenAd.show(activity);
                }

            }

        }

        public void loadAdFailed(Context context) {
            AD_UNIT_ID_Failed = SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD);

            // AD_UNIT_ID = SharedPref.openads;

            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    AD_UNIT_ID_Failed,
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();

                            //Toast.makeText(context, "onAdLoaded FialeoAop", Toast.LENGTH_SHORT).show();
                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;

                            //Toast.makeText(context, "onAdFailedToLoad FialeoAop Aop", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}