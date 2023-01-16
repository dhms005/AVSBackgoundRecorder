package com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment;


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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.FloatingControlCaptureService;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.FloatingSSCapService;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.HBService;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.GetIntentForImage;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.EditImageActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.FullShotDisclosure;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.GalleryActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.PhotoEditorActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities.WebviewActivity;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.BetterActivityResult;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_MyApplication;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.databinding.ActivityMainBinding;
import com.ds.audio.video.screen.backgroundrecorder.databinding.DialogFormatBinding;
import com.ds.audio.video.screen.backgroundrecorder.databinding.DialogPrefixBinding;
import com.ds.audio.video.screen.backgroundrecorder.databinding.DialogQualityBinding;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.hbisoft.hbrecorder.Const;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */

// this is the main view which is show all  the video in list
public class Screen_Shot_Tab extends Fragment implements View.OnClickListener {

    View view;

    public static Activity context = null;
    private static Screen_Shot_Tab instance = null;
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

    private boolean hasPermissions = false;
    private boolean isDrawOverlyAllowed = false;
    private static final int PERMISSION_RECORD_DISPLAY = 3006;
    private Intent mScreenCaptureIntent = null;

    SharedPreferences prefs;


    public Screen_Shot_Tab() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.activity_main, container, false);
        view = binding.getRoot();

        shownOnMain = false;
        context = getActivity();
        instance = this;
        maincontext = getActivity();
        //here data must be an instance of the class MarsDataProvider
        this.prefs = PreferenceManager.getDefaultSharedPreferences(maincontext);
        checkDrawOverlyPermission();

        prefCheck();
        clicks();
//        setStartStopButton();
        this.isFromService = context.getIntent().getBooleanExtra("isFromService", false);
        Glide.with((FragmentActivity) context).load(Integer.valueOf(R.drawable.untitled)).into(this.binding.image);
        mNativeAdNew();
//        if (!ConsentInformation.getInstance(this).isRequestLocationInEeaOrUnknown() || AppPref.getIsAdfree()) {
//            this.binding.adStting.setVisibility(View.GONE);
//        } else {
//            this.binding.adStting.setVisibility(View.VISIBLE);
//        }
        return view;

    }

    private void readWritePermission() {
        if (Build.VERSION.SDK_INT >= 29 || hasReadWritePermissions(context, this.permsa)) {
            DevSpy_Admob_Full_AD_New.getInstance().showInter(context, new DevSpy_Admob_Full_AD_New.MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(context, GalleryActivity.class));
                }
            });

        } else {
            getActivity().requestPermissions(this.permsa, 1009);
        }
    }

    public static Screen_Shot_Tab getInstance() {
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
        this.dialogFormat = new Dialog(context);
        this.dialogQuality = new Dialog(context);
        this.dialog = new Dialog(context);
        this.binding.txtFormat.setText(AppPref.getImageFormat(context));
        this.binding.txtQuality.setText(String.valueOf(AppPref.getImageQuality(context)));
        this.binding.txtPrefix.setText(AppPref.getPrefix(context));
        setPrefs(this.binding.imgSound, AppPref.getScreenSound(context));
        setPrefs(this.binding.imgFloatButton, AppPref.getShowButton(context));
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
                DevSpy_Admob_Full_AD_New.getInstance().showInter(context, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(context, WebviewActivity.class));
                    }
                });
                return;
            case R.id.editImage:
                methodRequiresTwoPermissionPhoto();
                return;
            case R.id.linEditor:
                DevSpy_Admob_Full_AD_New.getInstance().showInter(context, new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getActivity(), PhotoEditorActivity.class));
                    }
                });
                return;
            case R.id.linFloatingView:
                if (AppPref.getShowButton(getActivity())) {
                    AppPref.setShowButton(getActivity(), false);
                } else {
                    AppPref.setShowButton(getActivity(), true);
                }
                setPrefs(this.binding.imgFloatButton, AppPref.getShowButton(getActivity()));
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
                AppConstants.uriparse(context, FullShotDisclosure.strPrivacyUri);
                return;
            case R.id.linQuality:
                if (!this.dialogQuality.isShowing()) {
                    openQualityDialog();
                    return;
                }
                return;
            case R.id.linRate:
//                AppConstants.showDialog(context);
                return;
            case R.id.linShare:
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", "Screenshot - Screen Capture, Webpage Shot\n\nCapture screenshot for mobile & webpage with one-tap.\n\n- Allows to save screen capture images as PNG and JPG.\n- Allows you to capture whole webpage in high quality.\n- Save Image with your preferred quality settings.\n\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName());
                    startActivity(Intent.createChooser(intent, "Share Via"));
                    return;
                } catch (Exception e) {
                    Log.d("", e.toString());
                    return;
                }
            case R.id.linSound:
                if (AppPref.getScreenSound(getActivity())) {
                    AppPref.setScreenSound(getActivity(), false);
                } else {
                    AppPref.setScreenSound(getActivity(), true);
                }
                setPrefs(this.binding.imgSound, AppPref.getScreenSound(getActivity()));
                return;
            case R.id.linTerms:
                AppConstants.uriparse(context, FullShotDisclosure.strTermsUri);
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
        DialogPrefixBinding dialogPrefixBinding = (DialogPrefixBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_prefix, (ViewGroup) null, false);
        this.prefixBinding = dialogPrefixBinding;
        this.dialog.setContentView(dialogPrefixBinding.getRoot());
        this.dialog.setCancelable(true);
        this.dialog.getWindow().setBackgroundDrawableResource(17170445);
        this.dialog.getWindow().setLayout(-1, -2);
        this.dialog.show();
        this.prefixBinding.etName.setText(AppPref.getPrefix(context));
        this.prefixBinding.cardCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        this.prefixBinding.cardSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!prefixBinding.etName.getText().toString().isEmpty()) {
                    dialog.dismiss();
                    AppPref.setPrefix(context, prefixBinding.etName.getText().toString());
                    binding.txtPrefix.setText(prefixBinding.etName.getText().toString());
                    return;
                }
                Toast.makeText(context, "name can't null", 0).show();
            }
        });
    }

    private void openQualityDialog() {
        DialogQualityBinding dialogQualityBinding = (DialogQualityBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_quality, (ViewGroup) null, false);
        this.qualityBinding = dialogQualityBinding;
        this.dialogQuality.setContentView(dialogQualityBinding.getRoot());
        this.dialogQuality.setCancelable(true);
        this.dialogQuality.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogQuality.getWindow().setLayout(-1, -2);
        this.dialogQuality.show();
        checkQuality();
        this.qualityBinding.rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (qualityBinding.rd50.isChecked()) {
                    quality = 50;
                } else if (qualityBinding.rd60.isChecked()) {
                    quality = 60;
                } else if (qualityBinding.rd70.isChecked()) {
                    quality = 70;
                } else if (qualityBinding.rd80.isChecked()) {
                    quality = 80;
                } else if (qualityBinding.rd90.isChecked()) {
                    quality = 90;
                } else if (qualityBinding.rd100.isChecked()) {
                    quality = 100;
                }
                dialogQuality.dismiss();

                AppPref.setImageQuality(context, quality);
                binding.txtQuality.setText(String.valueOf(quality));
            }
        });
    }

    private void checkQuality() {
        if (AppPref.getImageQuality(context) == 50) {
            this.qualityBinding.rd50.setChecked(true);
        } else if (AppPref.getImageQuality(context) == 60) {
            this.qualityBinding.rd60.setChecked(true);
        } else if (AppPref.getImageQuality(context) == 70) {
            this.qualityBinding.rd70.setChecked(true);
        } else if (AppPref.getImageQuality(context) == 80) {
            this.qualityBinding.rd80.setChecked(true);
        } else if (AppPref.getImageQuality(context) == 90) {
            this.qualityBinding.rd90.setChecked(true);
        } else if (AppPref.getImageQuality(context) == 100) {
            this.qualityBinding.rd100.setChecked(true);
        }
    }

    private void openFormatDialog() {
        DialogFormatBinding dialogFormatBinding = (DialogFormatBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_format, (ViewGroup) null, false);
        this.formatBinding = dialogFormatBinding;
        this.dialogFormat.setContentView(dialogFormatBinding.getRoot());
        this.dialogFormat.setCancelable(true);
        this.dialogFormat.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogFormat.getWindow().setLayout(-1, -2);
        this.dialogFormat.show();
        checkPrefs();
        this.formatBinding.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (formatBinding.rdJpg.isChecked()) {
                    format = "JPG";
                } else {
                    format = "PNG";
                }
                AppPref.setImageFormat(context, format);
                dialogFormat.dismiss();
                binding.txtFormat.setText(format);
            }
        });
    }

    private void checkPrefs() {
        if (AppPref.getImageFormat(context).equalsIgnoreCase("JPG")) {
            this.formatBinding.rdJpg.setChecked(true);
        } else {
            this.formatBinding.rdPng.setChecked(true);
        }
    }

    public void setStartStopButton() {
        Log.e("#DEBUG", "setStartStopButton: "+AppPref.getShowButton(getActivity()) );
        if (isMyServiceRunning(FloatingControlCaptureService.class)) {
            Log.e("#DEBUG", "setStartStopButton: inside" );
            this.binding.txtStart.setText(getString(R.string.end_capture));
            this.binding.linStart.setBackground(context.getDrawable(R.drawable.stop_service));
            this.binding.imgStart.setImageResource(R.drawable.ss_btn_off);

            this.binding.linSound.setEnabled(false);
            this.binding.linFloatingView.setEnabled(false);
            this.binding.frmDisable.setVisibility(View.VISIBLE);
            return;
        }
        Log.e("#DEBUG", "setStartStopButton: outside " );
        this.binding.txtStart.setText(getString(R.string.start_capture));
        this.binding.linStart.setBackground(context.getDrawable(R.drawable.start_service));
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

    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        Log.i("isMyServiceRunning", "isMyServiceRunning: ");
        return false;
    }

    private boolean checkFloatWindowPermission() {

        if (Utils.isServiceRunning(HBService.class.getName(), getActivity())) {
            LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getActivity());
            Intent intent = new Intent();


            if (isMyServiceRunning(FloatingControlCaptureService.class)) {
                prefs.edit().putBoolean(Const.PREFS_TOOLS_CAPTURE, false).apply();
                intent.setAction(DevSpy_Conts.ACTION_STOP_ScreenShot_Service);
            } else {
                intent.setAction(DevSpy_Conts.ACTION_START_ScreenShot_Service);
                prefs.edit().putBoolean(Const.PREFS_TOOLS_CAPTURE, true).apply();
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
                DevSpy_Conts.mScreenCaptureIntent = null;
                return;
            }
            DevSpy_Conts.hasPermissions = true;
            DevSpy_Conts.mScreenCaptureIntent = intent;
            intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, resultCode);
            doRecord();
        } else if (requestCode == 101) {
            if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(getActivity())) {
                this.isDrawOverlyAllowed = true;
                startRecording();
                return;
            }
            this.isDrawOverlyAllowed = false;
//            Utils.toast(getApplicationContext(), getString(R.string.drwaerOverlayPermission), 1);
        }
    }


    private void doRecord() {

        DevSpy_Conts.ACTION_START_Service_Checker = "screenshot";

        if (AppPref.getShowButton(DevSpy_MyApplication.getAppContext())) {
            prefs.edit().putBoolean(Const.PREFS_TOOLS_CAPTURE, true).apply();
        } else {
            prefs.edit().putBoolean(Const.PREFS_TOOLS_CAPTURE, false).apply();
        }


        Intent intent = new Intent(getActivity(), HBService.class);
        intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, 3006);
        intent.putExtra("android.intent.extra.INTENT", DevSpy_Conts.mScreenCaptureIntent);
//        getActivity().startService(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else {
            getActivity().startService(intent);
        }

        setStartStopButton();
    }

    public void startRecording() {
        if (!DevSpy_Conts.hasPermissions) {
            if (this.isDrawOverlyAllowed) {
                startActivityForResult(((MediaProjectionManager) getActivity().getSystemService("media_projection")).createScreenCaptureIntent(), PERMISSION_RECORD_DISPLAY);
                return;
            }
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getActivity().getPackageName())), 101);
        }else{
            if (checkSelfPermission("android.permission.RECORD_AUDIO", 22) && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE", 23)) {
                DevSpy_Conts.hasPermissions = true;
                doRecord();
            }
        }
    }

    private boolean checkSelfPermission(String permission, int reqCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, reqCode);
        return false;
    }

    private void checkDrawOverlyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.isDrawOverlyAllowed = Settings.canDrawOverlays(getActivity());
        }
    }

    @AfterPermissionGranted(108)
    private void methodRequiresTwoPermissionPhoto() {
        if (Build.VERSION.SDK_INT >= 29 || EasyPermissions.hasPermissions(context, permsa)) {
            this.activityLauncher.launch(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), new BetterActivityResult.OnActivityResult() {
                public final void onActivityResult(Object obj) {
                    methodRequiresTwoPermissionPhoto_MainActivity((ActivityResult) obj);
                }
            });
            return;
        }
        EasyPermissions.requestPermissions(context, getString(R.string.camera_and_location_rationale), 108, this.permsa);
    }

    public void methodRequiresTwoPermissionPhoto_MainActivity(ActivityResult activityResult) {
        if (activityResult.getResultCode() == -1) {
            getData(activityResult.getData());
        }
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
            startActivity(new Intent(context, GalleryActivity.class));
        }
    }

    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(context, list)) {
            new AppSettingsDialog.Builder(context).build().show();
        } else if (EasyPermissions.somePermissionDenied(context, this.permsa)) {
            new AppSettingsDialog.Builder(context).build().show();
        }
    }

    private void mNativeAdNew() {
        if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
            ViewGroup.LayoutParams params = view.findViewById(R.id.Admob_Native_Frame_two).getLayoutParams();
            params.height = (int) getResources().getDimension(R.dimen.native_16);
            view.findViewById(R.id.Admob_Native_Frame_two).setLayoutParams(params);
            if (SharePrefUtils.getInt(Constant_ad.NATIVEAD_TRASPARENT, 0) == 0) {
                view.findViewById(R.id.Admob_Native_Frame_two).setBackground(getResources().getDrawable(R.drawable.z_ad_border));
            }
        }

        if (SharePrefUtils.getString(Constant_ad.MEDIATION, "0").equals("0")) {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showBigNativeAds(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showBigNativeAds(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(context, (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            }
        }
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
