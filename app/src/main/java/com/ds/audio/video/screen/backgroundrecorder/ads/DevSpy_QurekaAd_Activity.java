package com.ds.audio.video.screen.backgroundrecorder.ads;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.exit.DevSpy_Utility;
import com.facebook.ads.AdError;

import java.util.Random;

public class DevSpy_QurekaAd_Activity extends AppCompatActivity {
    public int random;
    public String action_str;
    private LinearLayout adPersonalCloseBtn;
    private LinearLayout adPersonalLlPlayStore;
    private ImageView ad_media_view;
    private RatingBar ad_stars;
    private RelativeLayout int_bg;
    private LinearLayout llPersonalAd;
    private LinearLayout llPersonalAdCenter;
    public Context mContext;
    private TextView native_ad_call_to_action;
    private TextView querkaText;
    private TextView txt_body;
    private TextView txt_download;
    private TextView txt_rate;
    private TextView txt_title;
    private LinearLayout userCount;
    TextView textView;
    String str;
    public DevSpy_CustomAdModel ESCustomAdModel;

    public void FadeIn(final View view) {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.qu_fade_in);
        view.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }

    public void SlideToAbove(final View view, int i2) {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 5.0f, 1, 0.0f);
        translateAnimation.setDuration((long) i2);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void SlideToAbove20(final View view, int i2) {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.qu_zoom_in);
        loadAnimation.setFillAfter(true);
        view.startAnimation(loadAnimation);
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.5f, 1, 0.0f);
        translateAnimation.setDuration((long) i2);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                Animation loadAnimation = AnimationUtils.loadAnimation(DevSpy_QurekaAd_Activity.this, R.anim.qu_zoom_out);
                loadAnimation.setFillAfter(true);
                view.startAnimation(loadAnimation);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void SlideToAbove30(final View view, int i2) {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.5f, 1, 0.0f);
        translateAnimation.setDuration((long) i2);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void SlideToTop(final View view, int i2) {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -2.0f, 1, 0.0f);
        translateAnimation.setDuration((long) i2);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void onBackPressed() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int i2 = 0;
        Random r = new Random();
        random = r.nextInt(3 - 0) + 0;
        if (random == 2) {
            i2 = R.layout.z_qureka_layout_3;
        } else if (random == 1) {
            i2 = R.layout.z_qureka_layout_2;
        } else if (random == 0) {
            i2 = R.layout.z_qureka_layout_1;
        }
        setContentView(i2);


        ESCustomAdModel = DevSpy_Admob_Full_AD_New.getInstance().getMyCustomAd("Interstitial");
        this.llPersonalAd = (LinearLayout) findViewById(R.id.llPersonalAd);
        this.llPersonalAdCenter = (LinearLayout) findViewById(R.id.llPersonalAdCenter);
        this.ad_media_view = (ImageView) findViewById(R.id.native_ad_media);
        this.txt_title = (TextView) findViewById(R.id.native_ad_title);
        this.txt_body = (TextView) findViewById(R.id.native_ad_social_context);
        this.txt_rate = (TextView) findViewById(R.id.txt_rate);
        this.txt_download = (TextView) findViewById(R.id.txt_download);
        this.native_ad_call_to_action = (TextView) findViewById(R.id.native_ad_call_to_action);
        this.adPersonalCloseBtn = (LinearLayout) findViewById(R.id.adPersonalCloseBtn);
        this.userCount = (LinearLayout) findViewById(R.id.userCount);
        this.adPersonalLlPlayStore = (LinearLayout) findViewById(R.id.adPersonalLlPlayStore);
        this.querkaText = (TextView) findViewById(R.id.querkaText);
        this.ad_stars = (RatingBar) findViewById(R.id.ad_stars);
        this.int_bg = (RelativeLayout) findViewById(R.id.int_bg);
        RequestBuilder<Drawable> load = Glide.with((FragmentActivity) this).load(this.ESCustomAdModel.getApp_logo());
        DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.ALL;
        ((RequestBuilder) load.diskCacheStrategy(diskCacheStrategy)).into((ImageView) findViewById(R.id.native_ad_icon));
        ((RequestBuilder) Glide.with((FragmentActivity) this).load(this.ESCustomAdModel.getApp_banner()).diskCacheStrategy(diskCacheStrategy)).into(this.ad_media_view);
        this.txt_title.setText(this.ESCustomAdModel.getApp_name());
        this.txt_body.setText(this.ESCustomAdModel.getApp_shortDecription());
        this.txt_rate.setText(this.ESCustomAdModel.getApp_rating());
        this.ad_stars.setRating(Float.parseFloat(this.ESCustomAdModel.getApp_rating()));
        this.txt_download.setText(this.ESCustomAdModel.getApp_download());
        String app_packageName = this.ESCustomAdModel.getApp_packageName();
        this.action_str = app_packageName;
        if (app_packageName.contains("http")) {
            this.userCount.setVisibility(View.GONE);
            this.adPersonalCloseBtn.setVisibility(View.GONE);
            this.adPersonalLlPlayStore.setVisibility(View.GONE);
            if (random == 1) {
                textView = this.native_ad_call_to_action;
                str = "Play Game";
            } else {
                textView = this.native_ad_call_to_action;
                str = "Play Now";
            }
        } else {
            this.userCount.setVisibility(View.VISIBLE);
            this.adPersonalCloseBtn.setVisibility(View.VISIBLE);
            this.adPersonalLlPlayStore.setVisibility(View.VISIBLE);
            if (random == 1) {
                textView = this.native_ad_call_to_action;
                str = "Install";
            } else {
                textView = this.native_ad_call_to_action;
                str = "Download";
            }
        }
        textView.setText(str);
        int i7 = random;
        if (i7 == 2) {
            random = 0;
            SlideToAbove30(findViewById(R.id.llcus3), AdError.SERVER_ERROR_CODE);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    int i1;
                    FadeIn(findViewById(R.id.llPersonalAd));
                    FadeIn(findViewById(R.id.main));
                    FadeIn(findViewById(R.id.aa));
                    action_str = ESCustomAdModel.getApp_packageName();
                    if (action_str.contains("http")) {
                        i1 = R.id.querkaText;
                    } else {
                        i1 = R.id.adPersonalLlPlayStore;
                    }
                    FadeIn(findViewById(i1));
                    FadeIn(findViewById(R.id.adPersonalLlCloseInstallBtns));
                }
            }, 1000);
        } else if (i7 == 1) {
            random = i7 + 1;
            SlideToAbove20(findViewById(R.id.native_ad_icon), AdError.SERVER_ERROR_CODE);
            SlideToAbove30(findViewById(R.id.cvTopAd), AdError.SERVER_ERROR_CODE);
            String app_packageName2 = this.ESCustomAdModel.getApp_packageName();
            this.action_str = app_packageName2;
            if (app_packageName2.contains("http")) {
                findViewById(R.id.querkaText).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.querkaText).setVisibility(View.GONE);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    FadeIn(findViewById(R.id.aa));
                    FadeIn(findViewById(R.id.adPersonalLlCloseInstallBtnsCenter));
                }
            }, 2200);
        } else {
            random = i7 + 1;
            SlideToTop(findViewById(R.id.native_ad_icon), 1000);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    int i2;
                    View view;
                    SlideToAbove(findViewById(R.id.native_ad_title), 600);
                    SlideToAbove(findViewById(R.id.banner), 1000);
                    SlideToAbove(findViewById(R.id.native_ad_social_context), 1200);
                    action_str = ESCustomAdModel.getApp_packageName();
                    if (action_str.contains("http")) {
                        view = findViewById(R.id.querkaText);
                        i2 = 900;
                    } else {
                        SlideToAbove(findViewById(R.id.userCount), 800);
                        view = findViewById(R.id.adPersonalLlPlayStore);
                        i2 = 1400;
                    }
                    SlideToAbove(view, i2);
                    SlideToAbove(findViewById(R.id.adPersonalLlCloseInstallBtns), 1600);
                }
            }, 800);
        }
        findViewById(R.id.native_ad_call_to_action).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                action_str = ESCustomAdModel.getApp_packageName();
                if (action_str.contains("http")) {
//                    try {
//                        if (isAppInstalled(SMS_QurekaAd_Activity.this, "com.android.chrome")) {
//                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                            builder.setToolbarColor(Color.parseColor("#66bb6a"));
//                            builder.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                            builder.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                            CustomTabsIntent build = builder.build();
//                            build.intent.setPackage("com.android.chrome");
//                            build.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            build.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                            return;
//                        }
//                        CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
//                        builder2.setToolbarColor(Color.parseColor("#66bb6a"));
//                        builder2.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                        builder2.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                        CustomTabsIntent build2 = builder2.build();
//                        build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        build2.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
//                        builder2.setToolbarColor(Color.parseColor("#66bb6a"));
//                        builder2.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                        builder2.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                        CustomTabsIntent build2 = builder2.build();
//                        build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        build2.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                    }

                    DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_QurekaAd_Activity.this);
                    return;
                }
                StringBuilder v = new StringBuilder("market://details?id=");
                v.append(action_str);
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(v.toString())));
            }
        });
        findViewById(R.id.native_ad_media).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                action_str = ESCustomAdModel.getApp_packageName();
                if (action_str.contains("http")) {
//                    try {
//                        if (isAppInstalled(SMS_QurekaAd_Activity.this, "com.android.chrome")) {
//                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                            builder.setToolbarColor(Color.parseColor("#66bb6a"));
//                            builder.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                            builder.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                            CustomTabsIntent build = builder.build();
//                            build.intent.setPackage("com.android.chrome");
//                            build.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            build.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                            return;
//                        }
//                        CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
//                        builder2.setToolbarColor(Color.parseColor("#66bb6a"));
//                        builder2.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                        builder2.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                        CustomTabsIntent build2 = builder2.build();
//                        build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        build2.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
//                        builder2.setToolbarColor(Color.parseColor("#66bb6a"));
//                        builder2.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                        builder2.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                        CustomTabsIntent build2 = builder2.build();
//                        build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        build2.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                    }

                    DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_QurekaAd_Activity.this);
                    return;
                }
                StringBuilder v = new StringBuilder("market://details?id=");
                v.append(action_str);
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(v.toString())));
            }
        });

        findViewById(R.id.native_ad_icon).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                action_str = ESCustomAdModel.getApp_packageName();
                if (action_str.contains("http")) {
//                    try {
//                        if (isAppInstalled(SMS_QurekaAd_Activity.this, "com.android.chrome")) {
//                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                            builder.setToolbarColor(Color.parseColor("#66bb6a"));
//                            builder.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                            builder.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                            CustomTabsIntent build = builder.build();
//                            build.intent.setPackage("com.android.chrome");
//                            build.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            build.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                            return;
//                        }
//                        CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
//                        builder2.setToolbarColor(Color.parseColor("#66bb6a"));
//                        builder2.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                        builder2.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                        CustomTabsIntent build2 = builder2.build();
//                        build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        build2.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
//                        builder2.setToolbarColor(Color.parseColor("#66bb6a"));
//                        builder2.setStartAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_right, R.anim.slide_out_left);
//                        builder2.setExitAnimations(SMS_QurekaAd_Activity.this, R.anim.slide_in_left, R.anim.slide_out_right);
//                        CustomTabsIntent build2 = builder2.build();
//                        build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        build2.launchUrl(SMS_QurekaAd_Activity.this, Uri.parse(str));
//                    }
                    DevSpy_Utility.OpenCustomQurekaBrowser(DevSpy_QurekaAd_Activity.this);
                    return;
                }
                StringBuilder v = new StringBuilder("market://details?id=");
                v.append(action_str);
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(v.toString())));
            }
        });
        findViewById(R.id.ImgClose).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                DevSpy_Admob_Full_AD_New.getInstance().interstitialCallBack();
            }
        });
        this.adPersonalCloseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                DevSpy_Admob_Full_AD_New.getInstance().interstitialCallBack();
            }
        });
    }

    public static boolean isAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
