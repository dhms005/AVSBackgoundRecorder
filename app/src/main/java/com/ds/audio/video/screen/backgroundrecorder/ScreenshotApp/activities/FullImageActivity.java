package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.DiaryImageData;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.adapters.SlidingImageAdapter;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.databinding.ActivityFullImageBinding;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.io.File;
import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {
    ActivityFullImageBinding binding;
    int currentPos = 0;
    ArrayList<Integer> deletedPos = new ArrayList<>();
    ArrayList<DiaryImageData> imageList = new ArrayList<>();


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActivityFullImageBinding activityFullImageBinding = (ActivityFullImageBinding) DataBindingUtil.setContentView(this, R.layout.activity_full_image);
        this.binding = activityFullImageBinding;
        setSupportActionBar(activityFullImageBinding.toolbar);
        this.currentPos = getIntent().getIntExtra("ImagePosition", 0);
        this.imageList = GalleryActivity.diaryImageData;
        initMethods();
        clicks();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
    }

    private void clicks() {
        this.binding.cardDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                callDialog();
            }
        });
        this.binding.cardShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FullImageActivity fullImageActivity = FullImageActivity.this;
                fullImageActivity.shareFile(fullImageActivity.imageList.get(FullImageActivity.this.binding.viewImagePager.getCurrentItem()).getPath());
            }
        });
        this.binding.cardEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(FullImageActivity.this, EditImageActivity.class);
                intent.setFlags(268435456);
                intent.putExtra(EditImageActivity.FROM_GALLARY, true);
                intent.putExtra("isFromFullImageView", true);
                intent.putExtra("FromPrivate", true);
                intent.putExtra(EditImageActivity.FILE_PATH, FullImageActivity.this.imageList.get(FullImageActivity.this.binding.viewImagePager.getCurrentItem()).getPath());
                FullImageActivity.this.startActivity(intent);
                FullImageActivity.this.finish();
            }
        });
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FullImageActivity.this.onBackPressed();
            }
        });
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(FullImageActivity.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);

        TextView title = dialog.findViewById(R.id.title);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        CardView cardCancel = dialog.findViewById(R.id.cardCancel);
        CardView cardSave = dialog.findViewById(R.id.cardSave);

        // dialog.show();

        title.setText("Delete Image");
        txtMsg.setText("Are you sure want to delete this image?");
        cardCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cardSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= 29) {
//                        FullImageActivity.this.getContentResolver().delete(Uri.parse(), (String) null, (String[]) null);

                        String videoUrl = FullImageActivity.this.imageList.get(FullImageActivity.this.binding.viewImagePager.getCurrentItem()).getPath();
//                        CY_M_FileHelper.removeAllForPaths(new String[]{videoUrl}, FullImageActivity.this);
                        deleteFile(videoUrl);

                    } else {
                        File file = new File(FullImageActivity.this.imageList.get(FullImageActivity.this.binding.viewImagePager.getCurrentItem()).getPath());
                        if (file.exists()) {
                            file.delete();
                        }
                        MediaScannerConnection.scanFile(FullImageActivity.this, new String[]{file.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                    FullImageActivity.this.deletedPos.add(Integer.valueOf(FullImageActivity.this.binding.viewImagePager.getCurrentItem()));
                    FullImageActivity.this.imageList.remove(FullImageActivity.this.binding.viewImagePager.getCurrentItem());
                    FullImageActivity.this.binding.viewImagePager.getAdapter().notifyDataSetChanged();
                    if (FullImageActivity.this.imageList.size() <= 0) {
                        FullImageActivity.this.onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    public void onBackPressed() {
        if (this.deletedPos.size() > 0) {
            Intent intent = getIntent();
            intent.putExtra("DeletedPosList", this.deletedPos);
            setResult(-1, intent);
            super.onBackPressed();
            return;
        }
        setResult(0);
        super.onBackPressed();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void initMethods() {
        ArrayList<DiaryImageData> arrayList = this.imageList;
        if (arrayList != null && arrayList.size() > 0) {
            this.binding.viewImagePager.setAdapter(new SlidingImageAdapter(getApplicationContext(), this.imageList, 0));
            this.binding.viewImagePager.setCurrentItem(this.currentPos);
            this.binding.viewImagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageScrolled(int i, float f, int i2) {
                }

                public void onPageSelected(int i) {
                    FullImageActivity.this.currentPos = i;
                }
            });
        }
    }

    public void shareFile(String str) {
        File file = new File(str);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("*/*");
        intent.addFlags(1073741824);
        intent.addFlags(1);
//        if (Build.VERSION.SDK_INT >= 29) {
//            intent.putExtra("android.intent.extra.STREAM", Uri.parse(str));
//        } else {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.ds.audio.video.screen.backgroundrecorder.provider", file));
//        }
        try {
            startActivity(Intent.createChooser(intent, "Share File "));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (file.delete()) {
//            removeAt(i);
            Toast.makeText(FullImageActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();


            return true;
        }
        Toast.makeText(FullImageActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();

        return false;
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
}
