package com.ds.audio.video.screen.backgroundrecorder.DevSpy_Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;


import java.util.List;

public class DevSpy_PermissionScreen extends AppCompatActivity {

    TextView tvAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.activity_devspy_mpermission_screen);

        tvAccept = findViewById(R.id.tvAccept);

        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvAccept.getText().toString().equalsIgnoreCase("ACCEPT")) {
                    if (Permission_check(DevSpy_PermissionScreen.this)) {
                        Intent i = new Intent(DevSpy_PermissionScreen.this, DevSpy_FirstActivity.class);
                        startActivity(i);
                    }
                } else if (tvAccept.getText().toString().equalsIgnoreCase("SCREEN OVERLAY\n PERMISSION")) {
                    onViewClicked();
                }

            }
        });
    }

    public boolean Permission_check(Activity context) {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(PERMISSIONS, 2);
            }
        } else {
            return true;
        }

        return false;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.e("code123", "onActivityResult: app download failed"+requestCode);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                tvAccept.setText("SCREEN OVERLAY\n PERMISSION");
            } else {
                int permissionCount = SharePrefUtils.getInt(Constant_ad.PERMISSION_COUNT, 0) + 1;
                SharePrefUtils.putInt(Constant_ad.PERMISSION_COUNT, permissionCount);
                if (permissionCount >= 2) {
                    SharePrefUtils.putInt(Constant_ad.PERMISSION_COUNT, 0);
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onViewClicked() {
        ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) TedPermission.create().setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                SharePrefUtils.putBoolean(Constant_ad.PERMISSION, true);
                Intent i = new Intent(DevSpy_PermissionScreen.this, DevSpy_FirstActivity.class);
                startActivity(i);
            }

            @Override
            public void onPermissionDenied(List<String> arrayList) {
                SharePrefUtils.putBoolean(Constant_ad.PERMISSION, true);
                Intent i = new Intent(DevSpy_PermissionScreen.this, DevSpy_FirstActivity.class);
                startActivity(i);
            }
        })).setRationaleMessage(getString(R.string.need_permission))).setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")).setGotoSettingButtonText(getString(R.string.setting))).setPermissions(DevSpy_Conts.permissions)).check();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (SharePrefUtils.getBoolean(Constant_ad.PERMISSION, false)) {
            Intent i = new Intent(DevSpy_PermissionScreen.this, DevSpy_FirstActivity.class);
            startActivity(i);
        }
    }
}