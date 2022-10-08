package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.BetterActivityResult;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.FileUtills;
import com.ds.audio.video.screen.backgroundrecorder.databinding.ActivityPhotoEditorBinding;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PhotoEditorActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    ActivityPhotoEditorBinding binding;
    String[] permsa = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityPhotoEditorBinding activityPhotoEditorBinding = (ActivityPhotoEditorBinding) DataBindingUtil.setContentView(this, R.layout.activity_photo_editor);
        this.binding = activityPhotoEditorBinding;
        setSupportActionBar(activityPhotoEditorBinding.toolbar);
        this.binding.ivBack.setOnClickListener(this);
        this.binding.cardWebCapture.setOnClickListener(this);
        this.binding.cardMarkup.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cardMarkup) {
            methodRequiresTwoPermissionPhoto();
        } else if (id == R.id.cardWebCapture) {
            startActivity(new Intent(this, WebviewActivity.class));
            finish();
        } else if (id == R.id.ivBack) {
            onBackPressed();
        }
    }

    @AfterPermissionGranted(108)
    private void methodRequiresTwoPermissionPhoto() {
        if (Build.VERSION.SDK_INT >= 29 || EasyPermissions.hasPermissions(this, this.permsa)) {
            this.activityLauncher.launch(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), new BetterActivityResult.OnActivityResult<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
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

    public void onPermissionsGranted(int i, List<String> list) {
        this.activityLauncher.launch(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), new BetterActivityResult.OnActivityResult<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    getData(activityResult.getData());
                }
            }

        });
    }


    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list) || EasyPermissions.somePermissionDenied((Activity) this, this.permsa)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }

    private void getData(Intent intent) {
        Cursor query;
        Uri data = intent.getData();
        if (Build.VERSION.SDK_INT < 29) {
            String[] strArr = {"_data"};
            if (data != null && (query = getContentResolver().query(data, strArr, (String) null, (String[]) null, (String) null)) != null) {
                query.moveToFirst();
                FileUtills.start(this, query.getString(query.getColumnIndex(strArr[0])));
            }
        } else if (TextUtils.isEmpty(data.toString())) {
            Toast.makeText(this, R.string.no_choose, 0).show();
        } else {
            Intent intent2 = new Intent(this, EditImageActivity.class);
            intent2.setFlags(268435456);
            intent2.putExtra(EditImageActivity.FROM_GALLARY, true);
            intent2.putExtra(EditImageActivity.FILE_PATH, data.toString());
            startActivity(intent2);
            finish();
        }
    }
}
