package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_PatterLock_Activity;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Activities.CY_M_PatterLock_FirstScreen;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.managers.SharedPreferencesManager;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_Setting_Activity;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_MyApplication;

public class ScreenRecorder_SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.screenrecorder_settings);
        findViewById(R.id.drawer_menu_button).setOnClickListener(view -> finish());   getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainPreferenceFragment()).commit();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public static class MainPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        private ListPreference key_audio_source;
        private EditTextPreference key_common_countdown;
        private ListPreference key_output_format;
        private SwitchPreference key_record_audio;
        private SwitchPreference key_reward_video;
        private ListPreference key_video_bitrate;
        private ListPreference key_video_encoder;
        private ListPreference key_video_fps;
        private ListPreference key_video_resolution;
        private String previous_countdown;
        SharedPreferences defaultSharedPreferences;

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
//            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            view.setLayoutDirection(0);
            return view;
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.screenrecorder_pref_main);
            defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            key_record_audio = (SwitchPreference) findPreference(getString(R.string.key_record_audio));
            key_reward_video = (SwitchPreference) findPreference(getString(R.string.key_reward_video));

//            if (key_reward_video != null) {
//                key_reward_video.setOnPreferenceChangeListener(this);
//            }
            key_common_countdown = (EditTextPreference) findPreference(getString(R.string.key_common_countdown));
//            this.key_common_countdown = editTextPreference;
            if (key_common_countdown != null) {
                key_common_countdown.setOnPreferenceChangeListener(this);
//                this.key_common_countdown.setOnBindEditTextListener(C0003x62ca93c5.INSTANCE);
//                this.key_common_countdown.(new EditTextPreference.OnBindEditTextListener() {
//                    @Override
//                    public void onBindEditText(@NonNull EditText editText) {
//                        editText.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
//                    }
//                });
            }
            key_audio_source = (ListPreference) findPreference(getString(R.string.key_audio_source));
            if (key_audio_source != null) {
                key_audio_source.setOnPreferenceChangeListener(this);
            }
            key_video_encoder = (ListPreference) findPreference(getString(R.string.key_video_encoder));

            if (key_video_encoder != null) {
                key_video_encoder.setOnPreferenceChangeListener(this);
            }
            key_video_resolution = (ListPreference) findPreference(getString(R.string.key_video_resolution));

            if (key_video_resolution != null) {
                key_video_resolution.setOnPreferenceChangeListener(this);
            }
            ListPreference listPreference4 = (ListPreference) findPreference(getString(R.string.key_video_fps));
            this.key_video_fps = listPreference4;
            if (listPreference4 != null) {
                listPreference4.setOnPreferenceChangeListener(this);
            }
            ListPreference listPreference5 = (ListPreference) findPreference(getString(R.string.key_video_bitrate));
            this.key_video_bitrate = listPreference5;
            if (listPreference5 != null) {
                listPreference5.setOnPreferenceChangeListener(this);
            }
            ListPreference listPreference6 = (ListPreference) findPreference(getString(R.string.key_output_format));
            this.key_output_format = listPreference6;
            if (listPreference6 != null) {
                listPreference6.setOnPreferenceChangeListener(this);
            }
            setPreviousSelectedAsSummary();

            Preference prefAppLock = findPreference("key_reward_video");
            prefAppLock.setOnPreferenceClickListener(preference -> {
                if (defaultSharedPreferences.getBoolean("key_reward_video", false)) {
                    SharedPreferencesManager.getInstance().setBoolean(Utils.IS_REWARD_VIDEO, true);
                } else {
                    SharedPreferencesManager.getInstance().setBoolean(Utils.IS_REWARD_VIDEO, false);
                }
                return false;
            });
        }

        private void setColorPreferencesTitle(EditTextPreference textPref, int color) {
            CharSequence cs = textPref.getTitle();
            Spannable coloredTitle = new SpannableString(cs.subSequence(0, cs.length()).toString());
            coloredTitle.setSpan(new ForegroundColorSpan(color), 0, coloredTitle.length(), 0);
            textPref.setTitle((CharSequence) coloredTitle);
        }


        public boolean onPreferenceChange(Preference var1_1, Object var2_2) {

            String var3_3 = var1_1.getKey();

            if (getString(R.string.key_output_format).equals(var3_3)) {
                key_output_format = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_output_format));
                if (key_output_format == null) return true;
                key_output_format.setSummary(key_output_format.getEntries()[key_output_format.findIndexOfValue(var2_2.toString())]);
                key_output_format.setValue(var2_2.toString());
                return true;
            } else if (getString(R.string.key_audio_source).equals(var3_3)) {
                key_audio_source = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_audio_source));
                if (key_audio_source == null) return true;
                key_audio_source.setSummary(key_audio_source.getEntries()[key_audio_source.findIndexOfValue(var2_2.toString())]);
                return true;
            } else if (getString(R.string.key_video_encoder).equals(var3_3)) {
                key_video_encoder = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_video_encoder));
                if (key_video_encoder == null) return true;
                key_video_encoder.setSummary(key_video_encoder.getEntries()[key_video_encoder.findIndexOfValue(var2_2.toString())]);
                key_video_encoder.setValue(var2_2.toString());
                return true;
            } else if (getString(R.string.key_video_resolution).equals(var3_3)) {
                key_video_resolution = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_video_resolution));
                if (key_video_resolution == null) return true;
                key_video_resolution.setSummary(key_video_resolution.getEntries()[key_video_resolution.findIndexOfValue(var2_2.toString())]);
                key_video_resolution.setValue(var2_2.toString());
                return true;

            } else if (getString(R.string.key_video_fps).equals(var3_3)) {
                key_video_fps = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_video_fps));
                if (key_video_fps == null) return true;
                key_video_fps.setSummary(key_video_fps.getEntries()[key_video_fps.findIndexOfValue(var2_2.toString())]);
                key_video_fps.setValue(var2_2.toString());
                return true;

            } else if (getString(R.string.key_video_bitrate).equals(var3_3)) {
                key_video_bitrate = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_video_bitrate));
                if (key_video_bitrate == null) return true;
                key_video_bitrate.setSummary(key_video_bitrate.getEntries()[key_video_bitrate.findIndexOfValue(var2_2.toString())]);
                key_video_bitrate.setValue(var2_2.toString());
                return true;

            } else if (getString(R.string.key_video_bitrate).equals(var3_3)) {
                key_video_bitrate = (ListPreference) this.findPreference((CharSequence) this.getString(R.string.key_video_bitrate));
                if (key_video_bitrate == null) return true;
                key_video_bitrate.setSummary(key_video_bitrate.getEntries()[key_video_bitrate.findIndexOfValue(var2_2.toString())]);
                key_video_bitrate.setValue(var2_2.toString());
                return true;

            }
           /* else if (getString(R.string.key_reward_video).equals(var3_3)) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                if (prefs.getBoolean("key_reward_video", false)) {
                    Log.e("#DEBUG1", "true");
                    return true;
                } else {
                    Log.e("#DEBUG1", "false");
                    return false;
                }


            } */
            else if (getString(R.string.key_common_countdown).equals(var3_3)) {
                try {
                    if (Integer.parseInt(var2_2.toString()) <= 15) {
                        this.key_common_countdown = (EditTextPreference) this.findPreference((CharSequence) this.getString(R.string.key_common_countdown));
                        StringBuilder var6_12 = new StringBuilder();
                        var6_12.append("");
                        var6_12.append(var2_2);
                        var6_12.append("s");
                        key_common_countdown.setSummary((CharSequence) var6_12.toString());
                        this.previous_countdown = var2_2.toString();
                        return true;
                    } else {
                        Toast.makeText((Context) CY_M_MyApplication.getAppContext(), (CharSequence) "Maximum value for countdown is 15 seconds", (int) 0).show();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText((Context) CY_M_MyApplication.getAppContext(), (CharSequence) "Please enter valid number", (int) 0).show();
                    return false;
                }
            } else {
//                Toast.makeText((Context) BaseApplication.getContext(), (CharSequence) "Maximum value for countdown is 15 seconds", (int) 0).show();
                return false;
            }
        }


        private void setPreviousSelectedAsSummary() {
            if (getActivity() != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String video_resolution = prefs.getString(getResources().getString(R.string.key_video_resolution), (String) null);
                boolean audio_enabled = prefs.getBoolean(getResources().getString(R.string.key_record_audio), true);
//                boolean reward_video = SharedPreferencesManager.getInstance().getBoolean(Utils.IS_REWARD_VIDEO, false);
                boolean reward_video = prefs.getBoolean(getResources().getString(R.string.key_reward_video), false);
                String audio_source = prefs.getString(getResources().getString(R.string.key_audio_source), (String) null);
                String video_encoder = prefs.getString(getResources().getString(R.string.key_video_encoder), (String) null);
                String video_fps = prefs.getString(getResources().getString(R.string.key_video_fps), (String) null);
                String video_bitrate = prefs.getString(getResources().getString(R.string.key_video_bitrate), (String) null);
                String output_format = prefs.getString(getResources().getString(R.string.key_output_format), (String) null);
                this.previous_countdown = prefs.getString(getResources().getString(R.string.key_common_countdown), (String) null);
//                Log.d("Summary", "common_countdown: " + this.previous_countdown);
//                Log.d("Summary", "reward_video_enabled: " + reward_video);
//                Log.d("Summary", "audio_enabled: " + audio_enabled);
//                Log.d("Summary", "audio_source: " + audio_source);
//                Log.d("Summary", "video_resolution: " + video_resolution);
//                Log.d("Summary", "video_encoder: " + video_encoder);
//                Log.d("Summary", "video_frame_rate: " + video_fps);
//                Log.d("Summary", "video_bit_rate: " + video_bitrate);
//                Log.d("Summary", "output_format: " + output_format);
                this.key_record_audio.setChecked(audio_enabled);
                this.key_reward_video.setChecked(reward_video);
                SharedPreferencesManager.getInstance().setBoolean(Utils.IS_REWARD_VIDEO, reward_video);
                if (this.previous_countdown != null) {
                    EditTextPreference editTextPreference = this.key_common_countdown;
                    editTextPreference.setSummary((CharSequence) "" + this.previous_countdown + "s");
                }
                if (audio_source != null) {
                    int index = this.key_audio_source.findIndexOfValue(audio_source);
                    ListPreference listPreference = this.key_audio_source;
                    listPreference.setSummary(listPreference.getEntries()[index]);
                } else {
                    this.key_audio_source.setSummary(PreferenceManager.getDefaultSharedPreferences(this.key_audio_source.getContext()).getString(this.key_audio_source.getKey(), ""));
                }
                if (video_encoder != null) {
                    int findIndexOfValue2 = this.key_video_encoder.findIndexOfValue(video_encoder);
                    ListPreference listPreference2 = this.key_video_encoder;
                    listPreference2.setSummary(listPreference2.getEntries()[findIndexOfValue2]);
                } else {
                    ListPreference listPreference3 = this.key_video_encoder;
                    listPreference3.setSummary(PreferenceManager.getDefaultSharedPreferences(listPreference3.getContext()).getString(this.key_video_encoder.getKey(), ""));
                }
                if (video_resolution != null) {
                    int findIndexOfValue3 = this.key_video_resolution.findIndexOfValue(video_resolution);
                    ListPreference listPreference4 = this.key_video_resolution;
                    listPreference4.setSummary(listPreference4.getEntries()[findIndexOfValue3]);
                } else {
                    ListPreference listPreference5 = this.key_video_resolution;
                    listPreference5.setSummary(PreferenceManager.getDefaultSharedPreferences(listPreference5.getContext()).getString(this.key_video_resolution.getKey(), ""));
                }
                if (video_fps != null) {
                    int findIndexOfValue4 = this.key_video_fps.findIndexOfValue(video_fps);
                    ListPreference listPreference6 = this.key_video_fps;
                    listPreference6.setSummary(listPreference6.getEntries()[findIndexOfValue4]);
                } else {
                    ListPreference listPreference7 = this.key_video_fps;
                    listPreference7.setSummary(PreferenceManager.getDefaultSharedPreferences(listPreference7.getContext()).getString(this.key_video_fps.getKey(), ""));
                }
                if (video_bitrate != null) {
                    int findIndexOfValue5 = this.key_video_bitrate.findIndexOfValue(video_bitrate);
                    ListPreference listPreference8 = this.key_video_bitrate;
                    listPreference8.setSummary(listPreference8.getEntries()[findIndexOfValue5]);
                } else {
                    ListPreference listPreference9 = this.key_video_bitrate;
                    listPreference9.setSummary(PreferenceManager.getDefaultSharedPreferences(listPreference9.getContext()).getString(this.key_video_bitrate.getKey(), ""));
                }
                if (output_format != null) {
                    int findIndexOfValue6 = this.key_output_format.findIndexOfValue(output_format);
                    ListPreference listPreference10 = this.key_output_format;
                    listPreference10.setSummary(listPreference10.getEntries()[findIndexOfValue6]);
                    return;
                }
                ListPreference listPreference11 = this.key_output_format;
                listPreference11.setSummary(PreferenceManager.getDefaultSharedPreferences(listPreference11.getContext()).getString(this.key_output_format.getKey(), ""));
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
