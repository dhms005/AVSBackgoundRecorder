package com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities.DevSpy_BatterySaverActivity;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities.DevSpy_CPUCoolerActivity;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities.DevSpy_DeviceInfoActivity;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities.DevSpy_LanguageActivity;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities.DevSpy_SpeedBoosterActivity;
import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Activities.DevSpy_SystemUsageActivity;
import com.ds.audio.video.screen.backgroundrecorder.ads.DevSpy_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.Extra_camaradetctor.DevSpy_camera_activity.DevSpy_Camere_Home_Activity;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ads.Custom_NativeAd_Admob;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

/**
 * A simple {@link Fragment} subclass.
 */

// this is the main view which is show all  the video in list
public class Tools_Tab extends Fragment {

    View view;
    Context context;
    private LinearLayout hide_camera_ll;
    private LinearLayout btn_device_info;
    private LinearLayout img_system_usage;
    private LinearLayout img_battery_saver;
    private LinearLayout img_speed_booster;
    private LinearLayout img_cpu_cooler;
    private LinearLayout img_language;

    public Tools_Tab() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.devspy_fragment_tools, container, false);
        context = getContext();

        mNativeAdNew();

        hide_camera_ll = view.findViewById(R.id.hide_camera_ll);
        btn_device_info = view.findViewById(R.id.btn_device_info);
        img_system_usage = view.findViewById(R.id.img_system_usage);
        img_battery_saver = view.findViewById(R.id.img_battery_saver);
        img_speed_booster = view.findViewById(R.id.img_speed_booster);
        img_cpu_cooler = view.findViewById(R.id.img_cpu_cooler);
        img_language = view.findViewById(R.id.img_language);

        hide_camera_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_Camere_Home_Activity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        btn_device_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_DeviceInfoActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        img_system_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_SystemUsageActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        img_battery_saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_BatterySaverActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        img_speed_booster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_SpeedBoosterActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        img_cpu_cooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_CPUCoolerActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        img_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevSpy_Admob_Full_AD_New.getInstance().showInter(getActivity(), new DevSpy_Admob_Full_AD_New.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent = new Intent(getActivity(), DevSpy_LanguageActivity.class);
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

