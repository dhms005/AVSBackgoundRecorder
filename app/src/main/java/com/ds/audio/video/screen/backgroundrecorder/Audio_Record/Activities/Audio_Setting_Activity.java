package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_SharedPreHelper;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_FaqActivity;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_PatterLock_Activity;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_PatterLock_FirstScreen;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_PrivacyPolicyActivity;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.gun0912.tedpermission.PermissionListener;

;

public class Audio_Setting_Activity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Audio_SharedPreHelper sharedPreHelper;
    private PermissionListener smsPermissionListener;
    SharedPreferences defaultSharedPreferences;
    SharedPreferences.Editor editor;

    public void onPostCreate(Bundle bundle) {
        CardView appBarLayout;
        int i;
        super.onPostCreate(bundle);
        this.sharedPreHelper = new Audio_SharedPreHelper(this);


        @SuppressLint("ResourceType") LinearLayout linearLayout = (LinearLayout) findViewById(16908298).getParent().getParent().getParent();
        appBarLayout = (CardView) LayoutInflater.from(this).inflate(R.layout.audio_toolbar_settings, (ViewGroup) linearLayout, false);
        linearLayout.addView(appBarLayout, 0);
        // linearLayout.setBackground(getResources().getDrawable(R.drawable.splash_bg));

        ((Toolbar) appBarLayout.getChildAt(0)).setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Audio_Setting_Activity.this);
        editor = defaultSharedPreferences.edit();

        setupSimplePreferencesScreen();
        preferenceClickEvent();
    }

    private void preferenceClickEvent() {
        Preference prefShare = findPreference("prefShare");
        prefShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " \n https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

                startActivity(Intent.createChooser(share, "Share link!"));
                return false;
            }
        });
        Preference prefPrivacyPolicy = findPreference("prefPrivacyPolicy");
        prefPrivacyPolicy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Audio_Setting_Activity.this, CY_M_PrivacyPolicyActivity.class);
                startActivity(intent);
                return false;
            }
        });
        Preference prefRate = findPreference("prefRate");
        prefRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));

                return true;
            }
        });


        Preference prefFAQ = findPreference("prefFAQ");
        prefFAQ.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Audio_Setting_Activity.this, CY_M_FaqActivity.class);
                startActivity(intent);
                return false;
            }
        });

        Preference prefChangePattern = findPreference("prefChangePattern");
        if (defaultSharedPreferences.getBoolean("prefAppLock", false)) {
            prefChangePattern.setEnabled(true);
        } else {


            prefChangePattern.setEnabled(false);
        }
        prefChangePattern.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Audio_Setting_Activity.this, CY_M_PatterLock_FirstScreen.class);
                intent.putExtra("from", "change_pattern");
                startActivity(intent);
                return false;
            }
        });

        Preference prefAppLock = findPreference("prefAppLock");
        prefAppLock.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (defaultSharedPreferences.getBoolean("prefAppLock", false)) {
                    Intent intent = new Intent(Audio_Setting_Activity.this, CY_M_PatterLock_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Audio_Setting_Activity.this, CY_M_PatterLock_FirstScreen.class);
                    intent.putExtra("from", "setting");
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.audio_pref_setting);
        PreferenceManager.setDefaultValues(this, R.xml.audio_pref_setting, false);
        initSummary(getPreferenceScreen());
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        View onCreateView = super.onCreateView(str, context, attributeSet);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        if (onCreateView != null) {
            return onCreateView;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return null;
        }
        if (str.equals("EditText")) {
            return new AppCompatEditText(this, attributeSet);
        }
        if (str.equals("Spinner")) {
            return new AppCompatSpinner(this, attributeSet);
        }
        if (str.equals("CheckBox")) {
            return new AppCompatCheckBox(this, attributeSet);
        }
        if (str.equals("RadioButton")) {
            return new AppCompatRadioButton(this, attributeSet);
        }
        if (str.equals("CheckedTextView")) {
            return new AppCompatCheckedTextView(this, attributeSet);
        }
        return null;
    }

    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onBackPressed() {

        CY_M_Admob_Full_AD_New.getInstance().showInterBack(this, new CY_M_Admob_Full_AD_New.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        updatePrefSummary(findPreference(str));
    }

    private void initSummary(Preference preference) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                initSummary(preferenceGroup.getPreference(i));
            }
            return;
        }
        updatePrefSummary(preference);
    }

    private void updatePrefSummary(Preference preference) {
        if (preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getEntry());
        }
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            if (preference.getTitle().toString().toLowerCase().contains("password")) {
                preference.setSummary("******");
            } else {
                preference.setSummary(editTextPreference.getText());
            }
        }
        if (preference instanceof MultiSelectListPreference) {
            preference.setSummary(((EditTextPreference) preference).getText());
        }
    }
}
