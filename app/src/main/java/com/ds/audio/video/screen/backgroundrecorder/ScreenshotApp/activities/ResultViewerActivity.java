package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.BetterActivityResult;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.Constants;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_LocaleHelper;
import com.ds.audio.video.screen.backgroundrecorder.Utils.DevSpy_SharedPref;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.databinding.AfterScreenshotCaptureBinding;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResultViewerActivity extends AppCompatActivity {
    String action;
    BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    AfterScreenshotCaptureBinding binding;
    String destinationFileName;
    Dialog dialog;
    CompositeDisposable disposable = new CompositeDisposable();
    String extension;
    Bitmap.CompressFormat format;
    String imageName;
    boolean isFromCrop = false;
    boolean isFromSave;
    Intent mData;
    String path;
    boolean ref;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        this.binding = (AfterScreenshotCaptureBinding) DataBindingUtil.setContentView(this, R.layout.after_screenshot_capture);
        DevSpy_Conts.mOpenAppChecker = true;
        setToolBar();
        this.dialog = new Dialog(this);
        this.binding.imageCaptured.setMaxScale(1.0f);
        this.destinationFileName = AppPref.getPrefix(this) + System.currentTimeMillis() + "." + AppPref.getImageQuality(this);
        ((LinearLayout) findViewById(R.id.rootView)).setBackground(getDrawable(R.drawable.gradient_bg));
//        AdConstant.loadBanner(this, this.binding.frmMainBannerView, this.binding.frmShimmer, this.binding.bannerView);

        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        this.binding.editImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultViewerActivity.this.action = "editImage";
                ResultViewerActivity.this.start(ResultViewerActivity.this, ResultViewerActivity.this.path, 108, false);
            }
        });
        this.binding.shareImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultViewerActivity.this.isFromSave) {
                    ResultViewerActivity resultViewerActivity = ResultViewerActivity.this;
                    Constants.shareImage(resultViewerActivity, resultViewerActivity.path, true);
                    return;
                }
                ResultViewerActivity resultViewerActivity2 = ResultViewerActivity.this;
                Constants.shareImage(resultViewerActivity2, resultViewerActivity2.path, false);
            }
        });
        this.binding.cropImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultViewerActivity.this.openDisposal();
            }
        });
        ((ImageView) findViewById(R.id.cancelImage)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.binding.shareWhatsapp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultViewerActivity.this.isFromSave) {
                    ResultViewerActivity resultViewerActivity = ResultViewerActivity.this;
                    Constants.shareImageOnSicialMedia(resultViewerActivity, resultViewerActivity.path, "com.whatsapp", true);
                    return;
                }
                ResultViewerActivity resultViewerActivity2 = ResultViewerActivity.this;
                Constants.shareImageOnSicialMedia(resultViewerActivity2, resultViewerActivity2.path, "com.whatsapp", false);
            }
        });
        this.binding.shareFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultViewerActivity.this.isFromSave) {
                    ResultViewerActivity resultViewerActivity = ResultViewerActivity.this;
                    Constants.shareImageOnSicialMedia(resultViewerActivity, resultViewerActivity.path, "com.facebook.katana", true);
                    return;
                }
                ResultViewerActivity resultViewerActivity2 = ResultViewerActivity.this;
                Constants.shareImageOnSicialMedia(resultViewerActivity2, resultViewerActivity2.path, "com.facebook.katana", false);
            }
        });
        this.binding.shareInsta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultViewerActivity.this.isFromSave) {
                    ResultViewerActivity resultViewerActivity = ResultViewerActivity.this;
                    Constants.shareImageOnSicialMedia(resultViewerActivity, resultViewerActivity.path, "com.instagram.android", true);
                    return;
                }
                ResultViewerActivity resultViewerActivity2 = ResultViewerActivity.this;
                Constants.shareImageOnSicialMedia(resultViewerActivity2, resultViewerActivity2.path, "com.instagram.android", false);
            }
        });
        this.binding.shareGmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultViewerActivity.this.isFromSave) {
                    ResultViewerActivity resultViewerActivity = ResultViewerActivity.this;
                    Constants.shareImageOnSicialMedia(resultViewerActivity, resultViewerActivity.path, "com.google.android.gm", true);
                    return;
                }
                ResultViewerActivity resultViewerActivity2 = ResultViewerActivity.this;
                Constants.shareImageOnSicialMedia(resultViewerActivity2, resultViewerActivity2.path, "com.google.android.gm", false);
            }
        });
        if (AppPref.getShowRateUs(this)) {
            this.binding.cardBad.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (!ResultViewerActivity.this.dialog.isShowing()) {
                        ResultViewerActivity.this.openFeedBackDialog();
                    }
                }
            });
            this.binding.cardGood.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AppConstants.uriparse(ResultViewerActivity.this, AppConstants.playStoreUrl);
                    AppPref.setShowRateUs(ResultViewerActivity.this, false);
                }
            });
            this.binding.cardExcellent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AppConstants.uriparse(ResultViewerActivity.this, AppConstants.playStoreUrl);
                    AppPref.setShowRateUs(ResultViewerActivity.this, false);
                }
            });
        } else {
            this.binding.cardRateUs.setVisibility(View.GONE);
        }
        getIntentData();
    }

    private void setToolBar() {
        setSupportActionBar(this.binding.toolBar);
    }


    public void openFeedBackDialog() {
        this.dialog.setContentView(R.layout.dialog_feedback);
        this.dialog.setCancelable(true);
        this.dialog.getWindow().setLayout(-1, -2);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.show();
        final EditText editText = (EditText) this.dialog.findViewById(R.id.etFeedBack);
        ((CardView) this.dialog.findViewById(R.id.cardSent)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!editText.getText().toString().trim().isEmpty()) {
                    ResultViewerActivity.this.dialog.dismiss();
                    AppPref.setShowRateUs(ResultViewerActivity.this, false);
                    AppConstants.EmailUs(ResultViewerActivity.this, editText.getText().toString());
                    return;
                }
                Toast.makeText(ResultViewerActivity.this, "Please Write Feedback.", 0).show();
                editText.requestFocus();
            }
        });
        ((CardView) this.dialog.findViewById(R.id.cardCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultViewerActivity.this.dialog.dismiss();
            }
        });
    }

    private Uri storeImage(Bitmap bitmap) {
        this.imageName = AppPref.getPrefix(this) + "_" + System.currentTimeMillis() + "." + AppPref.getImageFormat(this);
        if (AppPref.getImageFormat(this).equalsIgnoreCase("JPG")) {
            this.extension = "jpeg";
            this.format = Bitmap.CompressFormat.JPEG;
        } else {
            this.extension = "png";
            this.format = Bitmap.CompressFormat.PNG;
        }
        File file = new File(AppConstants.getCachePath(this), this.imageName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(this.format, AppPref.getImageQuality(this), fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e2) {
            Log.d("TAG", "Error accessing file: " + e2.getMessage());
        }
        return Uri.parse(file.toString());
    }

    private void getIntentData() {
        FileInputStream fileInputStream;
        if (getIntent().getBooleanExtra("isFromCropService", false)) {
            this.path = getIntent().getStringExtra(EditImageActivity.FILE_PATH);
        } else {
            this.path = getIntent().getStringExtra(Constants.EditImageIntentKey);
        }
        this.isFromCrop = getIntent().getBooleanExtra("isFromCrop", false);
        if (getIntent() != null) {
            boolean booleanExtra = getIntent().getBooleanExtra(EditImageActivity.REFERENCE, false);
            this.ref = booleanExtra;
            if (booleanExtra) {
                this.action = "getIntent";
                String stringExtra = getIntent().getStringExtra(EditImageActivity.FILE_PATH);
                if (this.isFromCrop) {
                    start(this, stringExtra, 101, true);
                } else {
                    start(this, stringExtra, 108, false);
                }
            } else {
                if (getIntent().getBooleanExtra("finish", false)) {
                    this.binding.editImage.setVisibility(View.GONE);
                    this.binding.cropImage.setVisibility(View.GONE);
                }
                try {
                    fileInputStream = new FileInputStream(new File(this.path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileInputStream = null;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 0;
                options.inPurgeable = true;
                options.inScaled = true;
                this.binding.imageCaptured.setImage(ImageSource.bitmap(BitmapFactory.decodeStream(fileInputStream, (Rect) null, options)));
            }
        } else {
            this.binding.cropImage.setVisibility(View.GONE);
            this.binding.editImage.setVisibility(View.GONE);
            this.binding.shareImage.setVisibility(View.GONE);
        }
    }

    private Bitmap uriToBitmap(Uri uri) {
        try {
            ParcelFileDescriptor openFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor());
            openFileDescriptor.close();
            return decodeFileDescriptor;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setViewInteract(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            int i = 0;
            while (true) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (i < viewGroup.getChildCount()) {
                    setViewInteract(viewGroup.getChildAt(i), z);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void showProgressBar() {
        setViewInteract(this.binding.rlContainer, false);
        this.binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        setViewInteract(this.binding.rlContainer, true);
        this.binding.progressBar.setVisibility(View.GONE);
    }


    public void openDisposal() {
        showProgressBar();
        this.disposable.add(Observable.fromCallable(new Callable() {
            public final Object call() {
                try {
                    return SaveViewerActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
            public final void accept(Object obj) {
                hideProgressBar();
                Toast.makeText(ResultViewerActivity.this, "Image saved successful ", 1).show();
                finish();
            }
        }));
    }

    public Boolean SaveViewerActivity() throws Exception {
        saveImage(BitmapFactory.decodeFile(this.path));
        return false;
    }

    private void saveImage(Bitmap bitmap) {
        String str = AppPref.getPrefix(this) + "_" + System.currentTimeMillis();
        if (AppPref.getImageFormat(this).equalsIgnoreCase("JPG")) {
            this.extension = "jpeg";
            this.format = Bitmap.CompressFormat.JPEG;
        } else {
            this.extension = "png";
            this.format = Bitmap.CompressFormat.PNG;
        }

        String img_path = Constants.imagePath(ResultViewerActivity.this);


        File file = new File(img_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, AppPref.getPrefix(this) + "_" + System.currentTimeMillis() + "." + this.extension);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(this.format, AppPref.getImageQuality(this), fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        AppConstants.refreshFiles(this, file2);
        this.path = file2.getAbsolutePath();
        file2.getName();
    }

    private void handleEditorImage(Intent intent) {
        if (intent.getBooleanExtra("isBeenSaved", false)) {
            String stringExtra = intent.getStringExtra(EditImageActivity.FILE_PATH);
            this.binding.editImage.setVisibility(View.GONE);
            this.binding.cropImage.setVisibility(View.GONE);
            this.path = stringExtra;
            Toast.makeText(this, "Image saved successful", 1).show();
            this.isFromSave = true;
            finish();
            return;
        }
        finish();
    }

    public void start(Activity activity, String str, int i, boolean z) {
        if (z) {
            Intent intent = new Intent(activity, UCropActivity.class);
            intent.putExtra(UCrop.EXTRA_INPUT_URI, Uri.parse(str));
            Log.e("URI", "setImageData:1 " + str);
            intent.putExtra(UCrop.EXTRA_OUTPUT_URI, Uri.fromFile(new File(getCacheDir(), this.destinationFileName)));
            intent.putExtra(EditImageActivity.REFERENCE, false);
            this.activityLauncher.launch(intent, new BetterActivityResult.OnActivityResult<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    DevSpy_SharedPref.RESUME_OPEN_CHECKER = true;
                    if (activityResult.getResultCode() == -1) {
                        activityResult.getData();
                    }
                }
            });
            return;
        }
        Intent intent2 = new Intent(activity, EditImageActivity.class);
        intent2.putExtra(EditImageActivity.FILE_PATH, str);
        intent2.putExtra(EditImageActivity.REFERENCE, false);
        this.activityLauncher.launch(intent2, new BetterActivityResult.OnActivityResult() {
            public final void onActivityResult(Object obj) {
                ResultViewerActivity.this.start_ResultViewerActivity((ActivityResult) obj);
            }
        });
    }


    public  void start_ResultViewerActivity(ActivityResult activityResult) {
        if (activityResult.getResultCode() == -1) {
            Intent data = activityResult.getData();
            this.mData = data;
            handleEditorImage(data);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void onBackPressed() {

        finish();
    }


    public void Call_banner() {
        LinearLayout mAdView = (LinearLayout) findViewById(R.id.mNativeBannerAd);

        Custom_Banner_Ad custom_banner_ad = new Custom_Banner_Ad();
        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.AD_BANNER_TYPE, "0").equals("0")) {
                if (custom_banner_ad.CheckAdCache() != null) {
                    custom_banner_ad.loadNativeAdFromCache(this, mAdView);
                } else {
                    ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen.simple_banner_1);
                    findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
                    custom_banner_ad.reload_admob_banner_Ad(this, mAdView);
                }
            } else {
                if (custom_banner_ad.Adaptive_CheckAdCache() != null) {
                    custom_banner_ad.Adaptive_loadNativeAdFromCache(this, mAdView);
                } else {
                    ViewGroup.LayoutParams params = findViewById(R.id.mNativeBannerAd).getLayoutParams();
                    params.height = DevSpy_LocaleHelper.banner_adpative_size(this);
                    findViewById(R.id.mNativeBannerAd).setLayoutParams(params);
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

    @Override
    protected void onPause() {
        super.onPause();
        DevSpy_SharedPref.RESUME_OPEN_CHECKER = false;
    }

    @Override
    protected void onDestroy() {
        DevSpy_SharedPref.RESUME_OPEN_CHECKER = false;
        super.onDestroy();
    }
}
