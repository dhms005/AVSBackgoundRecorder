package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.FloatingControlCaptureService;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.HBService;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.GetIntentForImage;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Service.FloatingService;

import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.BetterActivityResult;
import com.ds.audio.video.screen.backgroundrecorder.databinding.ActivityMainBinding;
import com.ds.audio.video.screen.backgroundrecorder.databinding.DialogFormatBinding;
import com.ds.audio.video.screen.backgroundrecorder.databinding.DialogPrefixBinding;
import com.ds.audio.video.screen.backgroundrecorder.databinding.DialogQualityBinding;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.Custom_Banner_Ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.util.List;

import org.greenrobot.eventbus.EventBus;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private static final int STORAGE_PERMISIION_CODE_EDITOR = 109;
    private static final int STORAGE_PERMISIION_CODE_FLOATING_BUTTON = 108;
    

    public static Activity context = null;
    private static MainActivity instance = null;
    public static Context maincontext = null;
    static boolean shownOnMain = false;
    BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    ActivityMainBinding binding;
    Dialog dialog;
    Dialog dialogFormat;
    Dialog dialogQuality;
    String format;
    DialogFormatBinding formatBinding;
    public boolean isCheckPermission = false;
    boolean isFromService;
    boolean isImageSetting = false;
    String[] permsa = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    DialogPrefixBinding prefixBinding;
    int quality;
    DialogQualityBinding qualityBinding;

    private boolean isDrawOverlyAllowed = false;
    private static final int PERMISSION_RECORD_DISPLAY = 3006;
    private Intent mScreenCaptureIntent = null;

    SharedPreferences prefs;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        shownOnMain = false;
        ActivityMainBinding activityMainBinding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.binding = activityMainBinding;
        context = this;
        instance = this;
        maincontext = this;
        setSupportActionBar(activityMainBinding.toolbar);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        checkDrawOverlyPermission();

        prefCheck();
        clicks();
        setStartStopButton();
        this.isFromService = getIntent().getBooleanExtra("isFromService", false);
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.untitled)).into(this.binding.image);

        mNativeAdNew();
        if (SharePrefUtils.getString(Constant_ad.AD_BANNER_NATIVE, "0").equals("0")) {
            Call_banner();
        } else {
            mNativeBanner();
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    private void readWritePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            startActivity(new Intent(this, GalleryActivity.class));
        } else if (Build.VERSION.SDK_INT >= 29 || hasReadWritePermissions(this, this.permsa)) {
            CY_M_Admob_Full_AD_New.getInstance().showInter(this, new CY_M_Admob_Full_AD_New.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                }
            });

        } else {
            requestPermissions(this.permsa, 1009);
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private boolean hasReadWritePermissions(Context context2, String... strArr) {
        if (!(Build.VERSION.SDK_INT < 23 || context2 == null || strArr == null)) {
            for (String checkSelfPermission : strArr) {
                if (ActivityCompat.checkSelfPermission(context2, checkSelfPermission) != 0) {
                    this.isCheckPermission = true;
                    return false;
                }
                this.isCheckPermission = false;
            }
        }
        return true;
    }

    private void prefCheck() {
        this.dialogFormat = new Dialog(this);
        this.dialogQuality = new Dialog(this);
        this.dialog = new Dialog(this);
        this.binding.txtFormat.setText(AppPref.getImageFormat(this));
        this.binding.txtQuality.setText(String.valueOf(AppPref.getImageQuality(this)));
        this.binding.txtPrefix.setText(AppPref.getPrefix(this));
        setPrefs(this.binding.imgSound, AppPref.getScreenSound(this));
        setPrefs(this.binding.imgFloatButton, AppPref.getShowButton(this));
    }

    private void clicks() {
        this.binding.startBtn.setOnClickListener(this);
        this.binding.linMyCreation.setOnClickListener(this);
        this.binding.linEditor.setOnClickListener(this);
        this.binding.linSound.setOnClickListener(this);
        this.binding.linFloatingView.setOnClickListener(this);
        this.binding.linFormat.setOnClickListener(this);
        this.binding.linQuality.setOnClickListener(this);
        this.binding.linPrefix.setOnClickListener(this);
        this.binding.menu.setOnClickListener(this);
        this.binding.card2.setOnClickListener(this);
        this.binding.linRate.setOnClickListener(this);
        this.binding.linShare.setOnClickListener(this);
        this.binding.adStting.setOnClickListener(this);
        this.binding.linPrivacy.setOnClickListener(this);
        this.binding.linTerms.setOnClickListener(this);
        this.binding.linImageSettings.setOnClickListener(this);
        this.binding.cardWebCapture.setOnClickListener(this);
        this.binding.cardMarkup.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adStting:
//                showAdsDialog();
                return;
            case R.id.card2:
                // startActivity(new Intent(this, ProVersionActivity.class).setFlags(67108864));
                return;
            case R.id.cardMarkup:
                methodRequiresTwoPermissionPhoto();
                return;
            case R.id.cardWebCapture:
                CY_M_Admob_Full_AD_New.getInstance().showInter(this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(MainActivity.this, WebviewActivity.class));
                    }
                });
                return;
            case R.id.editImage:
                methodRequiresTwoPermissionPhoto();
                return;
            case R.id.linEditor:
                CY_M_Admob_Full_AD_New.getInstance().showInter(this, new CY_M_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(MainActivity.this, PhotoEditorActivity.class));
                    }
                });
                return;
            case R.id.linFloatingView:
                if (AppPref.getShowButton(this)) {
                    AppPref.setShowButton(this, false);
                } else {
                    AppPref.setShowButton(this, true);
                }
                setPrefs(this.binding.imgFloatButton, AppPref.getShowButton(this));
                return;
            case R.id.linFormat:
                if (!this.dialogFormat.isShowing()) {
                    openFormatDialog();
                    return;
                }
                return;
            case R.id.linImageSettings:
                if (this.isImageSetting) {
                    this.isImageSetting = false;
                    this.binding.linSettings.setVisibility(View.GONE);
                    this.binding.imgUpDown.setImageResource(R.drawable.down);
                    return;
                }
                this.isImageSetting = true;
                this.binding.linSettings.setVisibility(View.VISIBLE);
                this.binding.imgUpDown.setImageResource(R.drawable.up);
                return;
            case R.id.linMyCreation:
                readWritePermission();
                return;
            case R.id.linPrefix:
                if (!this.dialog.isShowing()) {
                    openPrefixDialog();
                    return;
                }
                return;
            case R.id.linPrivacy:
                AppConstants.uriparse(this, FullShotDisclosure.strPrivacyUri);
                return;
            case R.id.linQuality:
                if (!this.dialogQuality.isShowing()) {
                    openQualityDialog();
                    return;
                }
                return;
            case R.id.linRate:
//                AppConstants.showDialog(this);
                return;
            case R.id.linShare:
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", "Screenshot - Screen Capture, Webpage Shot\n\nCapture screenshot for mobile & webpage with one-tap.\n\n- Allows to save screen capture images as PNG and JPG.\n- Allows you to capture whole webpage in high quality.\n- Save Image with your preferred quality settings.\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Share Via"));
                    return;
                } catch (Exception e) {
                    Log.d("", e.toString());
                    return;
                }
            case R.id.linSound:
                if (AppPref.getScreenSound(this)) {
                    AppPref.setScreenSound(this, false);
                } else {
                    AppPref.setScreenSound(this, true);
                }
                setPrefs(this.binding.imgSound, AppPref.getScreenSound(this));
                return;
            case R.id.linTerms:
                AppConstants.uriparse(this, FullShotDisclosure.strTermsUri);
                return;
            case R.id.menu:
                return;
            case R.id.startBtn:
                checkFloatWindowPermission();
                return;
            default:
                return;
        }
    }


    private void openPrefixDialog() {
        DialogPrefixBinding dialogPrefixBinding = (DialogPrefixBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_prefix, (ViewGroup) null, false);
        this.prefixBinding = dialogPrefixBinding;
        this.dialog.setContentView(dialogPrefixBinding.getRoot());
        this.dialog.setCancelable(true);
        this.dialog.getWindow().setBackgroundDrawableResource(17170445);
        this.dialog.getWindow().setLayout(-1, -2);
        this.dialog.show();
        this.prefixBinding.etName.setText(AppPref.getPrefix(this));
        this.prefixBinding.cardCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.dialog.dismiss();
            }
        });
        this.prefixBinding.cardSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!MainActivity.this.prefixBinding.etName.getText().toString().isEmpty()) {
                    MainActivity.this.dialog.dismiss();
                    MainActivity mainActivity = MainActivity.this;
                    AppPref.setPrefix(mainActivity, mainActivity.prefixBinding.etName.getText().toString());
                    MainActivity.this.binding.txtPrefix.setText(MainActivity.this.prefixBinding.etName.getText().toString());
                    return;
                }
                Toast.makeText(MainActivity.this, "name can't null", 0).show();
            }
        });
    }

    private void openQualityDialog() {
        DialogQualityBinding dialogQualityBinding = (DialogQualityBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_quality, (ViewGroup) null, false);
        this.qualityBinding = dialogQualityBinding;
        this.dialogQuality.setContentView(dialogQualityBinding.getRoot());
        this.dialogQuality.setCancelable(true);
        this.dialogQuality.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogQuality.getWindow().setLayout(-1, -2);
        this.dialogQuality.show();
        checkQuality();
        this.qualityBinding.rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (MainActivity.this.qualityBinding.rd50.isChecked()) {
                    MainActivity.this.quality = 50;
                } else if (MainActivity.this.qualityBinding.rd60.isChecked()) {
                    MainActivity.this.quality = 60;
                } else if (MainActivity.this.qualityBinding.rd70.isChecked()) {
                    MainActivity.this.quality = 70;
                } else if (MainActivity.this.qualityBinding.rd80.isChecked()) {
                    MainActivity.this.quality = 80;
                } else if (MainActivity.this.qualityBinding.rd90.isChecked()) {
                    MainActivity.this.quality = 90;
                } else if (MainActivity.this.qualityBinding.rd100.isChecked()) {
                    MainActivity.this.quality = 100;
                }
                MainActivity.this.dialogQuality.dismiss();
                MainActivity mainActivity = MainActivity.this;
                AppPref.setImageQuality(mainActivity, mainActivity.quality);
                MainActivity.this.binding.txtQuality.setText(String.valueOf(MainActivity.this.quality));
            }
        });
    }

    private void checkQuality() {
        if (AppPref.getImageQuality(this) == 50) {
            this.qualityBinding.rd50.setChecked(true);
        } else if (AppPref.getImageQuality(this) == 60) {
            this.qualityBinding.rd60.setChecked(true);
        } else if (AppPref.getImageQuality(this) == 70) {
            this.qualityBinding.rd70.setChecked(true);
        } else if (AppPref.getImageQuality(this) == 80) {
            this.qualityBinding.rd80.setChecked(true);
        } else if (AppPref.getImageQuality(this) == 90) {
            this.qualityBinding.rd90.setChecked(true);
        } else if (AppPref.getImageQuality(this) == 100) {
            this.qualityBinding.rd100.setChecked(true);
        }
    }

    private void openFormatDialog() {
        DialogFormatBinding dialogFormatBinding = (DialogFormatBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_format, (ViewGroup) null, false);
        this.formatBinding = dialogFormatBinding;
        this.dialogFormat.setContentView(dialogFormatBinding.getRoot());
        this.dialogFormat.setCancelable(true);
        this.dialogFormat.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogFormat.getWindow().setLayout(-1, -2);
        this.dialogFormat.show();
        checkPrefs();
        this.formatBinding.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (MainActivity.this.formatBinding.rdJpg.isChecked()) {
                    MainActivity.this.format = "JPG";
                } else {
                    MainActivity.this.format = "PNG";
                }
                MainActivity mainActivity = MainActivity.this;
                AppPref.setImageFormat(mainActivity, mainActivity.format);
                MainActivity.this.dialogFormat.dismiss();
                MainActivity.this.binding.txtFormat.setText(MainActivity.this.format);
            }
        });
    }

    private void checkPrefs() {
        if (AppPref.getImageFormat(this).equalsIgnoreCase("JPG")) {
            this.formatBinding.rdJpg.setChecked(true);
        } else {
            this.formatBinding.rdPng.setChecked(true);
        }
    }

    public void setStartStopButton() {
        if (isMyServiceRunning(FloatingControlCaptureService.class)) {
            this.binding.txtStart.setText(getString(R.string.end_capture));
            this.binding.linStart.setBackground(getDrawable(R.drawable.stop_service));
            this.binding.imgStart.setImageResource(R.drawable.ss_btn_off);

            this.binding.linSound.setEnabled(false);
            this.binding.linFloatingView.setEnabled(false);
            this.binding.frmDisable.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.txtStart.setText(getString(R.string.start_capture));
        this.binding.linStart.setBackground(getDrawable(R.drawable.start_service));
        this.binding.imgStart.setImageResource(R.drawable.ss_btn_on);
        this.binding.linSound.setEnabled(true);
        this.binding.linFloatingView.setEnabled(true);
        this.binding.frmDisable.setVisibility(View.GONE);
    }

    private void setPrefs(ImageView imageView, boolean z) {
        if (z) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
        }
    }

    private void startFloatWindow() {
        if (shownOnMain) {
            clickAction();
        } else {
            clickAction();
        }
    }

   
    public void clickAction() {
        Intent intent = new Intent(this, FloatingService.class);
//        intent.putExtra(ChatHeadService.EXTRA_CUTOUT_SAFE_AREA, FloatingViewManager.findCutoutSafeArea(activity));
        if (isMyServiceRunning(FloatingService.class)) {
            stopService(intent);
            return;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        setStartStopButton();
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        Log.i("isMyServiceRunning", "isMyServiceRunning: ");
        return false;
    }

    private boolean checkFloatWindowPermission() {

        if (Utils.isServiceRunning(HBService.class.getName(), this)) {
            LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 23) {
                intent.setAction(CY_M_Conts.ACTION_START_Audio_Service);
            }
            instance.sendBroadcast(intent);
        } else {
            startRecording();
        }
        return false;
    }


    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode != 16061 && requestCode == 105 && -1 == resultCode) {
            GetIntentForImage getIntentForImage = new GetIntentForImage();
            getIntentForImage.setIntent(intent);
            EventBus.getDefault().post(getIntentForImage);
        } else if (requestCode == PERMISSION_RECORD_DISPLAY) {
            if (resultCode != -1) {
//                Utils.showSnackBarNotification(this.iv_record, getString(R.string.recordingPermissionNotGranted), -1);
                this.mScreenCaptureIntent = null;
                return;
            }
            this.mScreenCaptureIntent = intent;
            intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, resultCode);
            doRecord();
        } else if (requestCode == 101) {
            if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
                this.isDrawOverlyAllowed = true;
                startRecording();
                return;
            }
            this.isDrawOverlyAllowed = false;
//            Utils.toast(getApplicationContext(), getString(R.string.drwaerOverlayPermission), 1);
        }
    }



    private void doRecord() {

        CY_M_Conts.ACTION_START_Service_Checker = "screenshot";

        Intent intent = new Intent(this, HBService.class);
        intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, 3006);
        intent.putExtra("android.intent.extra.INTENT", intent);
        startService(intent);


        setStartStopButton();
    }

    public void onBackPressed() {
        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }

    public void startRecording() {
            if (this.isDrawOverlyAllowed) {
                startActivityForResult(((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent(), PERMISSION_RECORD_DISPLAY);
                return;
            }
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" +getPackageName())), 101);

    }

    private void checkDrawOverlyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.isDrawOverlyAllowed = Settings.canDrawOverlays(this);
        }
    }

//    @AfterPermissionGranted(109)
//    private void methodRequiresTwoPermission() {
//        if (Build.VERSION.SDK_INT >= 29 || EasyPermissions.hasPermissions(this, this.permsa)) {
//            startFloatWindow();
//        } else {
//            EasyPermissions.requestPermissions((Activity) this, getString(R.string.camera_and_location_rationale), 109, this.permsa);
//        }
//    }

    @AfterPermissionGranted(108)
    private void methodRequiresTwoPermissionPhoto() {
        if (Build.VERSION.SDK_INT >= 29 || EasyPermissions.hasPermissions(this, this.permsa)) {
            this.activityLauncher.launch(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), new BetterActivityResult.OnActivityResult<ActivityResult>() {
                public final void onActivityResult(ActivityResult activityResult) {
                    if (activityResult.getResultCode() == -1) {
                        getData(activityResult.getData());
                    }
                }
            });
            return;
        }
        EasyPermissions.requestPermissions((Activity) this, getString(R.string.camera_and_location_rationale), 108, this.permsa);
    }


    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    private void getData(Intent intent) {
        Uri data = intent.getData();
        if (TextUtils.isEmpty(data.toString())) {
            Toast.makeText(context, R.string.no_choose, 0).show();
            return;
        }
        Intent intent2 = new Intent(context, EditImageActivity.class);
        intent2.setFlags(268435456);
        intent2.addFlags(1);
        intent2.putExtra(EditImageActivity.FROM_GALLARY, true);
        intent2.putExtra("isFromFullImageView", true);
        intent2.putExtra("FromPrivate", false);
        intent2.putExtra(EditImageActivity.FILE_PATH, data.toString());
        startActivity(intent2);
    }

    public void onPermissionsGranted(int i, List<String> list) {
        if (i == 1009) {
            startActivity(new Intent(this, GalleryActivity.class));
        }
    }

    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list) || EasyPermissions.somePermissionDenied((Activity) this, this.permsa)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }


    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showBigNativeAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(this, (ViewGroup) findViewById(R.id.Admob_Native_Frame_two));
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
}
