package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.ds.audio.video.screen.backgroundrecorder.BuildConfig;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_ActivityHomeMenu;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.File;

public class Video_SaveVideoActivity extends AppCompatActivity {
    CardView whatsapp;
    CardView facebook;
    CardView insta;
    CardView more;
    CardView delete;
    ImageView playPuase;
    RelativeLayout rlPlaypause;
    ImageView back;

    VideoView videoView;
    private String videoUrl = "";
    private Activity context;
    ImageView mHeaderImage;
    LinearLayout nativeAdContainer2;
    Custom_Banner_Ad banner_ad;
    private String mediation;
    LinearLayout mAdView;
    LinearLayout ll_static_ad_inner;
    LinearLayout mNativeBannerAd;
    Dialog downloadDialog;

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.saved_video_activity);

//        mNativeAdNew();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        deleteDialog();

        initView();
        context = this;

        Intent intent1 = getIntent();
        if (intent1.getExtras() != null) {
            videoUrl = intent1.getStringExtra("videourl");
        }

        back.setOnClickListener(view -> {
            onBackPressed();
        });

        RelativeLayout backHome = (RelativeLayout) findViewById(R.id.home);


        backHome.setOnClickListener(view -> {

            CY_M_Admob_Full_AD_New.getInstance().showInter(Video_SaveVideoActivity.this, new CY_M_Admob_Full_AD_New.MyCallback() {
                @Override
                public void callbackCall() {
                    Intent intent = new Intent(Video_SaveVideoActivity.this, CY_M_ActivityHomeMenu.class);
                    startActivity(intent);
                    finish();
                }
            });


        });

        initVideo();

        this.whatsapp.setOnClickListener(view -> {

            shareImageVideoOnWhatsapp(context, videoUrl, true);


        });


        this.facebook.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/*");
            Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(videoUrl));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            String stringBuilder2 = getString(R.string.share) + getPackageName();
            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            intent.setPackage("com.facebook.katana");
            try {
                startActivity(Intent.createChooser(intent, "Share Video..."));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(Video_SaveVideoActivity.this, "Please Install Facebook", Toast.LENGTH_LONG).show();
            }
        });


        this.insta.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/*");
            Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(videoUrl));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            String stringBuilder2 = getString(R.string.share) + getPackageName();
            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            intent.setPackage("com.instagram.android");
            try {
                startActivity(Intent.createChooser(intent, "Share Video..."));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(Video_SaveVideoActivity.this, "Please Install Instagram", Toast.LENGTH_LONG).show();
            }
        });
        this.more = (CardView) findViewById(R.id.more);
        this.more.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(videoUrl));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
                intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(Intent.createChooser(intent, "Share Your Video!"));


            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (downloadDialog != null) {
                    downloadDialog.show();
                    TextView cancel = downloadDialog.findViewById(R.id.cancel);
                    TextView update = downloadDialog.findViewById(R.id.update);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteFile(videoUrl);
                            finish();
                            downloadDialog.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadDialog.dismiss();
                        }
                    });
                }
//                AlertDialog.Builder message3 = new AlertDialog.Builder(CY_M_SaveVideoActivity.this).setCancelable(true).setTitle(getString(R.string.confirm)).setMessage(getString(R.string.do_you_want_delete));
//                message3.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                message3.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        CY_M_FileHelper.removeAllForPaths(new String[]{videoUrl}, CY_M_SaveVideoActivity.this);
//                        deleteFile(videoUrl);
//
//
////                        Intent intent = new Intent(CY_M_SaveVideoActivity.this, CY_M_ActivityFolder.class);
////                        startActivity(intent);
//                        finish();
//                    }
//                });
//                message3.create().show();
            }
        });
    }

    public boolean deleteFile(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (file.delete()) {
//            removeAt(i);
            Toast.makeText(Video_SaveVideoActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
            Video_SavedList_Activity.check_deleted = true;

            return true;
        }
        Toast.makeText(Video_SaveVideoActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();

        return false;
    }

//    public void removeAt(int i) {
//        DownloadVideo_Activity.photos.remove(i);
//        notifyItemRemoved(i);
//        notifyItemRangeChanged(i, DownloadVideo_Activity.photos.size());
//        notifyDataSetChanged();
//    }

    @SuppressLint("ClickableViewAccessibility")
    private void initVideo() {

        if (getIntent() != null) {

            try {
                this.videoView.setVideoURI(Uri.parse(videoUrl));
                this.rlPlaypause.setVisibility(View.GONE);
                MediaController controller = new MediaController(Video_SaveVideoActivity.this);
                videoView.setMediaController(controller);
                controller.setAnchorView(videoView);
                this.videoView.requestFocus();
                this.videoView.start();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        this.videoView.setOnCompletionListener(mediaPlayer -> videoView.start());
        this.videoView.setOnTouchListener((view, motionEvent) -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                rlPlaypause.setVisibility(View.GONE);
                playPuase.setImageResource(R.drawable.playicon);
                return false;
            }
            playPuase.setImageResource(R.drawable.ic_pause_new);
            videoView.start();
            new Handler(Looper.getMainLooper()).postDelayed(() -> rlPlaypause.setVisibility(View.GONE), 2000);
            return false;
        });
    }

    private void initView() {


        this.back = (ImageView) findViewById(R.id.back);
        this.videoView = (VideoView) findViewById(R.id.vv);
        this.playPuase = (ImageView) findViewById(R.id.play_pause);
        this.rlPlaypause = (RelativeLayout) findViewById(R.id.playing_status);

        this.whatsapp = (CardView) findViewById(R.id.whatsapp);
        this.facebook = (CardView) findViewById(R.id.facebook);
        this.insta = (CardView) findViewById(R.id.insta);


    }

    @Override
    public void onPause() {
        super.onPause();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "0");
        this.videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharePrefUtils.putString(Constant_ad.AD_CHECK_RESUME, "1");
        this.rlPlaypause.setVisibility(View.GONE);
        this.videoView.start();
    }


    public static void shareImageVideoOnWhatsapp(Context context, String filePath, boolean isVideo) {
        Uri imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        Log.e("@imguri", "" + imageUri);
        if (isVideo) {
            shareIntent.setType("video/*");
        } else {
            shareIntent.setType("image/*");
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            setToast(context, "Whtasapp not installed.");
        }
    }

    public static void setToast(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @Override
    public void onBackPressed() {
        this.videoView.pause();
        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }


    private void mNativeAdNew(Dialog downloadDialog) {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = downloadDialog.findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            downloadDialog.findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                downloadDialog.findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = downloadDialog.findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_11);
            downloadDialog.findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                downloadDialog.findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) downloadDialog.findViewById(R.id.Admob_Native_Frame_two));
            }
        }
    }

    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);

        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_TYPE, "0").equals("0")) {
                if (custom_banner_ad.CheckAdCache() != null) {
                    custom_banner_ad.loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    custom_banner_ad.reload_admob_adpative_banner_Ad(this, mAdView);
                }
            }
        } else {
            custom_banner_ad.reload_applovin_banner_ad(this, mAdView);
        }

    }

    private void mNativeBanner() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1") || SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
            ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_banner_1);
            findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.mNativeBannerAd).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            new Custom_NativeAd_Admob().showNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        } else {
            new Custom_NativeAd_Admob().showAppLovinNativeBannerAd(this, (ViewGroup) findViewById(R.id.mNativeBannerAd));
        }

    }

    public void deleteDialog() {
        downloadDialog = new Dialog(Video_SaveVideoActivity.this);
        downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadDialog.setCancelable(false);
        downloadDialog.setContentView(R.layout.dialog_delete_video);
        Window window = downloadDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER | Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        downloadDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        downloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mNativeAdNew(downloadDialog);

    }

}
