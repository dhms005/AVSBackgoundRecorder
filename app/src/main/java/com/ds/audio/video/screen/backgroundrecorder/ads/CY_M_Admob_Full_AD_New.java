package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.ds.audio.video.screen.backgroundrecorder.exit.CY_M_Utility;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Ad_Key;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.github.mylibrary.Notification.Push.PushUser;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CY_M_Admob_Full_AD_New implements androidx.lifecycle.LifecycleObserver {

    public InterstitialAd interstitialOne;
    public InterstitialAd interstitialTwo;
    MyCallback myCallback;
    private static CY_M_Admob_Full_AD_New mInstance;

    public static Dialog Qureka_dialog1;
    public static Dialog loadingAdDailog;


    private static boolean isShowingAd = false;
    private AppOpenAd appOpenAdOne = null;
    private AppOpenAd appOpenAdTwo = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback_one;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback_two;
    private long loadTime = 0;

    public static ArrayList qurekadata;
    public static int count_click = -1;
    public static int count_click_for_alt = -1;
    public static int count_custAppOpenAd = 0;
    public static int count_custBannerAd = 0;
    public static int count_custIntAd = 0;
    public static int count_custNBAd = 0;
    public static int count_custNativeAd = 0;
    public static int count_native = -1;
    public static int facebook_AdStatus = 0;

    public static List<CY_M_CustomAdModel> myAppMarketingList = new ArrayList();

    private static final String TAG = "Admob_Full_AD_New";
    private MaxInterstitialAd interstitialAppLovinAd;
    private int retryAttempt;


    public static CY_M_Admob_Full_AD_New getInstance() {
        if (mInstance == null) {
            mInstance = new CY_M_Admob_Full_AD_New();
        }
        return mInstance;
    }

    public interface MyCallback {
        void callbackCall();
    }


    public void loadInterOne(Activity context) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            loadingAdDialog(context);
            getQurekaData(context);
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            String admobinter = SharePrefUtils.getString(Constant_ad.GOOGLE_FULL_AD, Custom_Ad_Key.KEY_ADMOB_FULL);

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(
                    context,
                    admobinter,
                    adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            interstitialOne = interstitialAd;
                            Log.e("@TESTING", "onAdLoaded 1");
                            interstitialOne.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Called when fullscreen content is dismissed.
                                            interstitialOne = null;
                                            Constant_ad.IsAdShowing = false;
                                            Log.e("@TESTING", "onAdDismissedFullScreenContent 1");
                                            loadInterOne(context);

                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            Constant_ad.IsAdShowing = false;
                                            interstitialOne = null;
                                            Log.e("@TESTING", "onAdFailedToShowFullScreenContent 1");
                                            loadInterOne(context);
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            Constant_ad.IsAdShowing = true;
                                        }
                                    });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            interstitialOne = null;
                        }
                    });
        }
    }

    public void loadInterTwo(final Activity context) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });


            String admobinter02 = SharePrefUtils.getString(Constant_ad.GOOGLE_FULL_AD2, Custom_Ad_Key.KEY_ADMOB_FULL2);

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(
                    context,
                    admobinter02,
                    adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            interstitialTwo = interstitialAd;
                            Log.i("TAG", "onAdLoaded 2");
                            interstitialTwo.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            Constant_ad.IsAdShowing = false;
                                            interstitialTwo = null;
                                            Log.d("TAG", "The ad 2 was dismissed.");

                                            loadInterTwo(context);
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            Constant_ad.IsAdShowing = false;
                                            interstitialTwo = null;
                                            Log.d("TAG", "The ad 2 failed to show.");
                                            loadInterTwo(context);
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            Constant_ad.IsAdShowing = true;
                                        }
                                    });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d("TAG", "The ad 2 Load Error." + loadAdError.getMessage());
                            interstitialTwo = null;
                        }
                    });
        }
    }

    public void loadOpenAdOne(final Activity context) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            getQurekaData(context);
            if (SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD).equals("")) {
                appOpenAdOne = null;
            } else {
                loadCallback_one = new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAdOne = ad;
                        loadTime = (new Date()).getTime();
                        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Constant_ad.IsAdShowing = false;
                                appOpenAdOne = null;
                                isShowingAd = false;
                                loadOpenAdOne(context);
                                if (SharePrefUtils.getString(Constant_ad.OPEN_INTER, "0").equals("1")) {
                                    if (interstitialOne != null) {
                                        interstitialOne.show(context);
                                    } else if (interstitialTwo != null) {
                                        interstitialTwo.show(context);
                                    } else if (interstitialAppLovinAd != null) {
                                        if (interstitialAppLovinAd.isReady()) {
                                            interstitialAppLovinAd.showAd();
                                        }
                                    } else {
                                        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        } else {
                                            Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                                            context.startActivity(intent);
                                        }
                                    }
                                } else {
                                    if (SharePrefUtils.getBoolean(Constant_ad.CLOSE_FROM, false)) {
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (myCallback != null) {
                                                    myCallback.callbackCall();
                                                    myCallback = null;
                                                }
                                            }
                                        }, 200);
                                    } else {
                                        if (myCallback != null) {
                                            myCallback.callbackCall();
                                            myCallback = null;
                                        }
                                    }

                                }


                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (myCallback != null) {
                                    myCallback.callbackCall();
                                    myCallback = null;
                                }
                                Constant_ad.IsAdShowing = false;
                                loadOpenAdOne(context);
//                            //                            Log.e(LOG_TAG, "onAdFailedToShowFullScreenContent");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
//                        if (myCallback != null) {
//                            myCallback.callbackCall();
//                            myCallback = null;
//                        }
                                Constant_ad.IsAdShowing = true;
//                            Log.e(LOG_TAG, "onAdShowedFullScreenContent");
                                isShowingAd = true;
                            }
                        };

                        appOpenAdOne.setFullScreenContentCallback(fullScreenContentCallback);

                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        appOpenAdOne = null;
                        // Handle the error.
                    }
                };
                AdRequest request = getAdRequest();
                AppOpenAd.load(
                        context, SharePrefUtils.getString(Constant_ad.OPEN_AD, Custom_Ad_Key.KEY_AO_AD), request,
                        AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback_one);
            }
        }
    }

    public void loadOpenAdTwo(final Activity context) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            getQurekaData(context);
            if (SharePrefUtils.getString(Constant_ad.OPEN_AD2, Custom_Ad_Key.KEY_AO_AD2).equals("")) {
                appOpenAdTwo = null;
            } else {
                loadCallback_two = new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAdTwo = ad;
                        loadTime = (new Date()).getTime();
                        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Constant_ad.IsAdShowing = false;
//                            Log.e(LOG_TAG, "onAdDismissedFullScreenContent");
                                appOpenAdTwo = null;
                                isShowingAd = false;
                                loadOpenAdTwo(context);
                                if (SharePrefUtils.getString(Constant_ad.OPEN_INTER, "0").equals("1")) {
                                    if (interstitialTwo != null) {
                                        interstitialTwo.show(context);
                                    } else if (interstitialOne != null) {
                                        interstitialOne.show(context);
                                    } else if (interstitialAppLovinAd != null) {
                                        if (interstitialAppLovinAd.isReady()) {
                                            interstitialAppLovinAd.showAd();
                                        }
                                    } else {
                                        if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        } else {
                                            Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                                            context.startActivity(intent);
                                        }
                                    }
                                } else {
                                    if (SharePrefUtils.getBoolean(Constant_ad.CLOSE_FROM, false)) {
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (myCallback != null) {
                                                    myCallback.callbackCall();
                                                    myCallback = null;
                                                }
                                            }
                                        }, 200);
                                    } else {
                                        if (myCallback != null) {
                                            myCallback.callbackCall();
                                            myCallback = null;
                                        }
                                    }
                                }


                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (myCallback != null) {
                                    myCallback.callbackCall();
                                    myCallback = null;
                                }
                                Constant_ad.IsAdShowing = false;
                                loadOpenAdTwo(context);
//                            Log.e(LOG_TAG, "onAdFailedToShowFullScreenContent");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
//                        if (myCallback != null) {
//                            myCallback.callbackCall();
//                            myCallback = null;
//                        }
                                Constant_ad.IsAdShowing = true;
//                            Log.e(LOG_TAG, "onAdShowedFullScreenContent");
                                isShowingAd = true;
                            }
                        };

                        appOpenAdTwo.setFullScreenContentCallback(fullScreenContentCallback);
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        appOpenAdTwo = null;
                        // Handle the error.
                    }

                };
                AdRequest request = getAdRequest();
                AppOpenAd.load(
                        context, SharePrefUtils.getString(Constant_ad.OPEN_AD2, Custom_Ad_Key.KEY_AO_AD2), request,
                        AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback_two);
            }
        }
    }

    public void loadAppLovinOne(Activity context) {
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            loadingAdDialog(context);
            getQurekaData(context);
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            String adID = SharePrefUtils.getString(Constant_ad.FACEBOOK_FULL, Custom_Ad_Key.KEY_FACEBOOK_FULL);

            if (!adID.equals("")) {
                interstitialAppLovinAd = new MaxInterstitialAd(adID, context);
//        interstitialAppLovinAd = new MaxInterstitialAd("98a4df8a33f515a3", context);
                interstitialAppLovinAd.loadAd();
                interstitialAppLovinAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        Log.e(TAG, "onAdLoaded: ");
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        Constant_ad.IsAdShowing = true;
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        Log.e(TAG, "onAdHidden: ");
                        Constant_ad.IsAdShowing = false;
                        interstitialAppLovinAd = null;
                        loadAppLovinOne(context);
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        Log.e(TAG, "onAdLoadFailed: ");
                        Constant_ad.IsAdShowing = false;
                        interstitialAppLovinAd = null;
                        loadAppLovinOne(context);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });
            }
        }
    }

    public void showInter(Activity context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            loadingAdDialog(context);

            int click = SharePrefUtils.getInt(Constant_ad.COUNT, 1);
            int total_count = SharePrefUtils.getInt(Constant_ad.AD_COUNT, 2);

            if (total_count <= click && !Constant_ad.IsAdShowing) {
                if (SharePrefUtils.getInt(Constant_ad.MINUTE, 0) != 0) {
                    if (CY_M_Utility.isTimerAdShow) {
                        CY_M_Utility.isTimerAdShow = false;
                        click = 1;
                        SharePrefUtils.putInt(Constant_ad.COUNT, click);

                        if (SharePrefUtils.getString(Constant_ad.AD_DIALOGUE, "0").equals("1")) {
                            loadingAdDailog.show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingAdDailog.dismiss();
                                    _showInterAdDialog(context, _myCallback);
//                        handler.postDelayed(this, 2000);
                                }
                            }, 2000);
                        } else {
                            _showInterAdDialog(context, _myCallback);
                        }
                    } else {
                        click = click + 1;
                        SharePrefUtils.putInt(Constant_ad.COUNT, click);
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                } else {
                    click = 1;
                    SharePrefUtils.putInt(Constant_ad.COUNT, click);

                    if (SharePrefUtils.getString(Constant_ad.AD_DIALOGUE, "0").equals("1")) {
                        loadingAdDailog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAdDailog.dismiss();
                                _showInterAdDialog(context, _myCallback);
//                        handler.postDelayed(this, 2000);
                            }
                        }, 2000);
                    } else {
                        _showInterAdDialog(context, _myCallback);
                    }
                }

            } else {
                click = click + 1;
                SharePrefUtils.putInt(Constant_ad.COUNT, click);
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }

    public void showInter1(Activity context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            loadingAdDialog(context);

            int click = SharePrefUtils.getInt(Constant_ad.COUNT, 1);
            int total_count = SharePrefUtils.getInt(Constant_ad.AD_COUNT, 2);

            if (total_count <= click && !Constant_ad.IsAdShowing) {
                if (SharePrefUtils.getInt(Constant_ad.MINUTE, 0) != 0) {
                    if (CY_M_Utility.isTimerAdShow) {
                        CY_M_Utility.isTimerAdShow = false;
                        click = 1;
                        SharePrefUtils.putInt(Constant_ad.COUNT, click);

                        if (SharePrefUtils.getString(Constant_ad.AD_DIALOGUE, "0").equals("1")) {
                            loadingAdDailog.show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingAdDailog.dismiss();
                                    _showInterAdDialog1(context, _myCallback);
//                        handler.postDelayed(this, 2000);
                                }
                            }, 2000);
                        } else {
                            _showInterAdDialog1(context, _myCallback);
                        }
                    } else {
                        click = click + 1;
                        SharePrefUtils.putInt(Constant_ad.COUNT, click);
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                } else {
                    click = 1;
                    SharePrefUtils.putInt(Constant_ad.COUNT, click);

                    if (SharePrefUtils.getString(Constant_ad.AD_DIALOGUE, "0").equals("1")) {
                        loadingAdDailog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAdDailog.dismiss();
                                _showInterAdDialog1(context, _myCallback);
//                        handler.postDelayed(this, 2000);
                            }
                        }, 2000);
                    } else {
                        _showInterAdDialog1(context, _myCallback);
                    }
                }

            } else {
                click = click + 1;
                SharePrefUtils.putInt(Constant_ad.COUNT, click);
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }

    public void showInterBack(Activity context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        if (!SharePrefUtils.getBoolean(Constant_ad.IS_PURCHASE, false)) {
            loadingAdDialog(context);


            int isback = SharePrefUtils.getInt(Constant_ad.AD_BACK_TOTAL_COUNT, 1);
            int click = SharePrefUtils.getInt(Constant_ad.AD_BACK_COUNT, 1);

            if (isback != 0) {
                if (isback <= click && !Constant_ad.IsAdShowing) {

                    if (SharePrefUtils.getInt(Constant_ad.MINUTE, 0) != 0) {
                        if (CY_M_Utility.isTimerAdShow) {
                            CY_M_Utility.isTimerAdShow = false;
                            click = 1;
                            SharePrefUtils.putInt(Constant_ad.AD_BACK_COUNT, click);
                            if (SharePrefUtils.getString(Constant_ad.AD_DIALOGUE, "0").equals("1")) {
                                loadingAdDailog.show();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingAdDailog.dismiss();
                                        _showBackInterAdDialog(context, _myCallback);

//                        handler.postDelayed(this, 2000);
                                    }
                                }, 2000);
                            } else {
                                _showBackInterAdDialog(context, _myCallback);
                            }
                        } else {
                            click = click + 1;

                            SharePrefUtils.putInt(Constant_ad.AD_BACK_COUNT, click);
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }
                    } else {
                        click = 1;
                        SharePrefUtils.putInt(Constant_ad.AD_BACK_COUNT, click);
                        if (SharePrefUtils.getString(Constant_ad.AD_DIALOGUE, "0").equals("1")) {
                            loadingAdDailog.show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingAdDailog.dismiss();
                                    _showBackInterAdDialog(context, _myCallback);

//                        handler.postDelayed(this, 2000);
                                }
                            }, 2000);
                        } else {
                            _showBackInterAdDialog(context, _myCallback);
                        }
                    }
                } else {

                    click = click + 1;

                    SharePrefUtils.putInt(Constant_ad.AD_BACK_COUNT, click);
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            } else {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }


    public void _showInterAdDialog(Activity context, MyCallback _myCallback) {

        SharePrefUtils.putBoolean(Constant_ad.CLOSE_FROM, false);
        int forward_type = SharePrefUtils.getInt(Constant_ad.AD_FORWARD_TYPE, 0);
        int position1 = SharePrefUtils.getInt(Constant_ad.POSITION1, 0);
        int position2 = SharePrefUtils.getInt(Constant_ad.POSITION2, 0);
        int position3 = SharePrefUtils.getInt(Constant_ad.POSITION3, 0);

        if (position1 == 0) {
            if (interstitialOne != null) {
                interstitialOne.show(context);
            } else if (interstitialTwo != null) {
                interstitialTwo.show(context);
            } else if (interstitialAppLovinAd != null) {
                if (interstitialAppLovinAd.isReady()) {
                    interstitialAppLovinAd.showAd();
                }
            } else {
                loadInterOne(context);
                loadInterTwo(context);
                loadAppLovinOne(context);
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                    context.startActivity(intent);
                }
            }
        } else {
            if (appOpenAdOne != null) {
                appOpenAdOne.show(context);
            } else if (appOpenAdTwo != null) {
                appOpenAdTwo.show(context);
            } else if (interstitialAppLovinAd != null) {
                if (interstitialAppLovinAd.isReady()) {
                    interstitialAppLovinAd.showAd();
                }
            } else {
                loadOpenAdOne(context);
                loadOpenAdTwo(context);
                loadAppLovinOne(context);
                if (SharePrefUtils.getString(Constant_ad.OPEN_INTER, "0").equals("1")) {
                    loadInterOne(context);
                    loadInterTwo(context);
                }
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                    context.startActivity(intent);
                }
            }
        }
    }

    public void _showInterAdDialog1(Activity context, MyCallback _myCallback) {

        SharePrefUtils.putBoolean(Constant_ad.CLOSE_FROM, false);
        int forward_type = SharePrefUtils.getInt(Constant_ad.AD_FORWARD_TYPE, 0);
        int position1 = SharePrefUtils.getInt(Constant_ad.POSITION1, 0);
        int position2 = SharePrefUtils.getInt(Constant_ad.POSITION2, 0);
        int position3 = SharePrefUtils.getInt(Constant_ad.POSITION3, 0);

        if (position1 == 0) {
            if (interstitialOne != null) {
                interstitialOne.show(context);
            } else if (interstitialTwo != null) {
                interstitialTwo.show(context);
            } else if (interstitialAppLovinAd != null) {
                if (interstitialAppLovinAd.isReady()) {
                    interstitialAppLovinAd.showAd();
                }
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                    context.startActivity(intent);
                }
            }
        } else {
            if (appOpenAdOne != null) {
                appOpenAdOne.show(context);
            } else if (appOpenAdTwo != null) {
                appOpenAdTwo.show(context);
            } else if (interstitialAppLovinAd != null) {
                if (interstitialAppLovinAd.isReady()) {
                    interstitialAppLovinAd.showAd();
                }
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                    context.startActivity(intent);
                }
            }
        }
    }

    public void _showBackInterAdDialog(Activity context, MyCallback _myCallback) {

        SharePrefUtils.putBoolean(Constant_ad.CLOSE_FROM, true);
        int back_type = SharePrefUtils.getInt(Constant_ad.AD_BACK_TYPE, 0);
        int position1 = SharePrefUtils.getInt(Constant_ad.POSITION1, 0);
        int position2 = SharePrefUtils.getInt(Constant_ad.POSITION2, 0);
        int position3 = SharePrefUtils.getInt(Constant_ad.POSITION3, 0);

        if (position2 == 0) {
            if (interstitialTwo != null) {
                interstitialTwo.show(context);
            } else if (interstitialOne != null) {
                interstitialOne.show(context);
            } else if (interstitialAppLovinAd != null) {
                if (interstitialAppLovinAd.isReady()) {
                    interstitialAppLovinAd.showAd();
                }
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                    context.startActivity(intent);
                }
            }
        } else {
            if (appOpenAdTwo != null) {
                appOpenAdTwo.show(context);
            } else if (appOpenAdOne != null) {
                appOpenAdOne.show(context);
            } else if (interstitialAppLovinAd != null) {
                if (interstitialAppLovinAd.isReady()) {
                    interstitialAppLovinAd.showAd();
                }
            } else {
                if (SharePrefUtils.getString(Constant_ad.AD_QUREKA_Ad, "0").equals("0")) {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    Intent intent = new Intent(context, CY_M_QurekaAd_Activity.class);
                    context.startActivity(intent);
                }
            }
        }
    }


    public void interstitialCallBack() {
        if (myCallback != null) {
            myCallback.callbackCall();
            myCallback = null;
        }
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */

    public void loadingAdDialog(Activity context) {
        loadingAdDailog = new Dialog(context);
        loadingAdDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingAdDailog.setCancelable(false);
        loadingAdDailog.setContentView(R.layout.z_dialog_loadingad);
        Window window = loadingAdDailog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        loadingAdDailog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        loadingAdDailog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


    }


    public CY_M_CustomAdModel getMyCustomAd(String str) {
        if (myAppMarketingList.size() == 0) {
            myAppMarketingList = get_CustomAdData();
        }
        ArrayList arrayList = new ArrayList();
        if (myAppMarketingList.size() != 0) {
            for (int i2 = 0; i2 < myAppMarketingList.size(); i2++) {
                if (!myAppMarketingList.get(i2).getApp_AdFormat().isEmpty() && Arrays.asList(myAppMarketingList.get(i2).getApp_AdFormat().split(",")).contains(str)) {
                    arrayList.add(myAppMarketingList.get(i2));
                }
            }
        }
        Random r = new Random();
        int i1 = r.nextInt(arrayList.size() - 0) + 0;
        CY_M_CustomAdModel customAdModel = null;
        if (arrayList.size() != 0) {
            for (int i4 = 0; i4 <= arrayList.size(); i4++) {
                customAdModel = (CY_M_CustomAdModel) arrayList.get(i1);
            }
        }
        return customAdModel;
    }


    public List<CY_M_CustomAdModel> get_CustomAdData() {
        String res = SharePrefUtils.getString(Constant_ad.RESPO, "");
        ArrayList arrayList = new ArrayList();
        try {

            JSONObject jsonObject = new JSONObject(res);
            JSONArray jSONArray = jsonObject.getJSONArray("Advertise_List");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                CY_M_CustomAdModel customAdModel = new CY_M_CustomAdModel();
                customAdModel.setAd_id(jSONObject.getInt("ad_id"));
                customAdModel.setApp_name(jSONObject.getString("app_name"));
                customAdModel.setApp_packageName(jSONObject.getString("app_packageName"));
                customAdModel.setApp_logo(jSONObject.getString("app_logo"));
                customAdModel.setApp_banner(jSONObject.getString("app_banner"));
                customAdModel.setApp_shortDecription(jSONObject.getString("app_shortDecription"));
                customAdModel.setApp_rating(jSONObject.getString("app_rating"));
                customAdModel.setApp_download(jSONObject.getString("app_download"));
                customAdModel.setApp_AdFormat(jSONObject.getString("app_AdFormat"));
                arrayList.add(customAdModel);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }


    public void getQurekaData(Activity context) {
        qurekadata = new ArrayList();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api2.s4apps.in/cricchamp_ad_images/qureka_test.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    SharePrefUtils.putString(Constant_ad.RESPO, response);
//                    qurekadata.add(jsonObject.getJSONArray("Advertise_List"));
//                    get_CustomAdData(jsonObject);
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(PushUser.APP_CODE, PushUser.APP_CODE_VALUE);
                params.put(PushUser.APP_ANDROID_ID, "1234");
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }


    public void get_CustomAdData(JSONObject jsonObject) {

        try {
            JSONArray jSONArray = jsonObject.getJSONArray("Advertise_List");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                CY_M_CustomAdModel customAdModel = new CY_M_CustomAdModel();
                customAdModel.setAd_id(jSONObject.getInt("ad_id"));
                customAdModel.setApp_name(jSONObject.getString("app_name"));
                customAdModel.setApp_packageName(jSONObject.getString("app_packageName"));
                customAdModel.setApp_logo(jSONObject.getString("app_logo"));
                customAdModel.setApp_banner(jSONObject.getString("app_banner"));
                customAdModel.setApp_shortDecription(jSONObject.getString("app_shortDecription"));
                customAdModel.setApp_rating(jSONObject.getString("app_rating"));
                customAdModel.setApp_download(jSONObject.getString("app_download"));
                customAdModel.setApp_AdFormat(jSONObject.getString("app_AdFormat"));
                qurekadata.add(customAdModel);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }

    }

}
