package com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_ActivityPager;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_SavedList_Activity;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_ScheduleListActivity;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities.Audio_Setting_Activity;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

/**
 * A simple {@link Fragment} subclass.
 */

// this is the main view which is show all  the video in list
public class Audio_Record_Tab extends Fragment {

    View view;
    Context context;
    ImageView ll_start;
    ImageView ll_creation;
    ImageView img_setting;
    ImageView ll_start_schedule;


    SharedPreferences defaultSharedPreferences;
    SharedPreferences.Editor editor;

    public Audio_Record_Tab() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.audio_record_tab, container, false);
        context = getContext();

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = defaultSharedPreferences.edit();

        mNativeAdNew();

        this.ll_start = (ImageView) view.findViewById(R.id.ll_start);
        this.ll_creation = (ImageView) view.findViewById(R.id.ll_creation);
        this.img_setting = (ImageView) view.findViewById(R.id.img_setting);
        this.ll_start_schedule = (ImageView) view.findViewById(R.id.ll_start_schedule);


        this.ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), Audio_ActivityPager.class);
                        startActivity(intent);
                    }
                });
            }
        });

        this.ll_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), Audio_SavedList_Activity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        this.img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (SharePrefUtils.getString(Constant_ad.PATTERN_NUMBER, "").equals("")) {
                            editor.putBoolean("prefAppLock", false);
                            editor.commit();
                        }
                        if (SharePrefUtils.getBoolean(Constant_ad.PATTERN_CONFIRM, false)) {
                            editor.putBoolean("prefChangePattern", true);
                            editor.commit();
                        } else {
                            editor.putBoolean("prefChangePattern", false);
                            editor.commit();
                        }
                        Intent intent = new Intent(getActivity(), Audio_Setting_Activity.class);
                        startActivity(intent);
                    }
                });
            }
        });


        ll_start_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), Audio_ScheduleListActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        return view;
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
                new Custom_NativeAd_Admob().showNativeSmallAds(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showBigNativeAds(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showBigNativeAds(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showNativeSmallAds(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            }
        } else {
            if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("1")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("2")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("3")) {
                new Custom_NativeAd_Admob().showAppLovinNativeAd(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            } else if (SharePrefUtils.getString(Constant_ad.NATIVE_SIZE, "2").equals("4")) {
                new Custom_NativeAd_Admob().showAppLovinSmallNativeAd(getActivity(), (ViewGroup) view.findViewById(R.id.Admob_Native_Frame_two));
            }
        }
    }

}
