package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.ds.audio.video.screen.backgroundrecorder.Utils.CY_M_Constant;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppConstants;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.utills.AppPref;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class FullShotDisclosure extends AppCompatActivity implements View.OnClickListener {
    public static String strPrivacyUri = CY_M_Constant.PRIVACY_POLICY;
    public static String strTermsUri = "";

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView((int) R.layout.activity_fullshot_disclosure);
    }

    public void agreeAndContinueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We would like to inform you regarding the 'Consent to Collection and Use Of Data'\n\nGive permission to access your storage to save captured screenshot into your phone.\n\nWe store your data on your device only, we don’t store them on our server.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agreeAndContinue:
                AppPref.setIsTermsAccept(this, true);
                GoToMainScreen();
                return;
            case R.id.privacyPolicy:
                AppConstants.uriparse(this, strPrivacyUri);
                return;
            case R.id.termsOfService:
                AppConstants.uriparse(this, strTermsUri);
                return;
            case R.id.userAgreement:
                agreeAndContinueDialog();
                return;
            default:
                return;
        }
    }

    private void GoToMainScreen() {
        try {
            startActivity(new Intent(this, MainActivity.class).setFlags(67108864));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
