package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_SharedPreHelper;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.services.Audio_Recorder_Service;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers.Utils;
import com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.services.HBService;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_TimeHelper;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class Audio_Recorder_Fragment extends Fragment {
    private static final String TAG = "CCTVRecorderFragment";
    public Handler mHandler;
    public Timer timer;
    LinearLayout ivCamera;
    TextView tvClickRecorderFrag;
    TextView tvTimer;
    ImageView img_record;
    LinearLayout llCameraRotate;
    LinearLayout llCameraPreview;
    LinearLayout llCamera;
    public static LinearLayout cPreview;
    public static SurfaceView surfaceView;
    private LocalBroadcastManager broadcastManagerStart;
    private LocalBroadcastManager broadcastManagerStop;
    private long elapsedTimer = 0;
    private Audio_SharedPreHelper preHelper;


    private boolean hasPermissions = false;
    private boolean isDrawOverlyAllowed = false;
    private static final int PERMISSION_RECORD_DISPLAY = 3006;
    private Intent mScreenCaptureIntent = null;
    private int mScreenCaptureResultCode = Utils.RESULT_CODE_FAILED;

    private BroadcastReceiver receiverStart = new BroadcastReceiver() {
        /* class com.ds.audio.video.screen.backgroundrecorder.CCTVfragment.CCTVRecorderFragment.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CY_M_Conts.ACTION_START_AUDIO_SERVICE)) {
                Log.e(Audio_Recorder_Fragment.TAG, "START");
                Audio_Recorder_Fragment.this.startTimer();
            }
        }
    };
    private BroadcastReceiver receiverStop = new BroadcastReceiver() {
        /* class com.ds.audio.video.screen.backgroundrecorder.CCTVfragment.CCTVRecorderFragment.AnonymousClass2 */

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CY_M_Conts.ACTION_STOP_AUDIO_EXTRA)) {
                Log.e(Audio_Recorder_Fragment.TAG, "STOP");
                Audio_Recorder_Fragment.this.stopTimer();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.broadcastManagerStop = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CY_M_Conts.ACTION_STOP_AUDIO_EXTRA);
        this.broadcastManagerStop.registerReceiver(this.receiverStop, intentFilter);
        this.broadcastManagerStart = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(CY_M_Conts.ACTION_START_AUDIO_SERVICE);
        this.broadcastManagerStart.registerReceiver(this.receiverStart, intentFilter2);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.audio_recorder_fragment, viewGroup, false);

        img_record = (ImageView) inflate.findViewById(R.id.img_record);
        llCameraRotate = (LinearLayout) inflate.findViewById(R.id.llCameraRotate);
        llCameraPreview = (LinearLayout) inflate.findViewById(R.id.llCameraPreview);
        llCamera = (LinearLayout) inflate.findViewById(R.id.llCamera);
        cPreview = (LinearLayout) inflate.findViewById(R.id.cPreview);
        surfaceView = inflate.findViewById(R.id.surfacetest);

        ivCamera = inflate.findViewById(R.id.iv_cam_recorder_frag);
        tvClickRecorderFrag = inflate.findViewById(R.id.tv_click_recorder_frag);
        tvTimer = inflate.findViewById(R.id.tv_timer_recorder_frag);
        checkDrawOverlyPermission();

        ButterKnife.bind(this, inflate);
        this.preHelper = new Audio_SharedPreHelper(getContext());
        if (this.preHelper.haveTimeStart()) {
            CY_M_Conts.isTimerRunning_Audio = true;
            this.img_record.setImageResource(R.drawable.ic_record_stop);
//            animationView.setVisibility(View.VISIBLE);
            this.tvClickRecorderFrag.setText(getString(R.string.click_to_stop));
            this.mHandler = new Handler() {
                /* class com.ds.audio.video.screen.backgroundrecorder.CCTVfragment.CCTVRecorderFragment.AnonymousClass3 */

                public void handleMessage(Message message) {
                    if (Audio_Recorder_Fragment.this.elapsedTimer < 0) {
                        Audio_Recorder_Fragment.this.tvTimer.setText(Video_TimeHelper.convertSecondsToHMmSs(0));
                    } else {
                        Audio_Recorder_Fragment.this.tvTimer.setText(Video_TimeHelper.convertSecondsToHMmSs(Audio_Recorder_Fragment.this.elapsedTimer));
                    }
                }
            };
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                /* class com.ds.audio.video.screen.backgroundrecorder.CCTVfragment.CCTVRecorderFragment.AnonymousClass4 */

                public void run() {
                    Audio_Recorder_Fragment cCTVRecorderFragment = Audio_Recorder_Fragment.this;
                    cCTVRecorderFragment.elapsedTimer = cCTVRecorderFragment.preHelper.getSecondsInTwoTime(Audio_Recorder_Fragment.this.preHelper.getTimeRecord());
                    Audio_Recorder_Fragment.this.mHandler.obtainMessage(1).sendToTarget();
                }
            }, 0, 1000);


        }

        llCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CY_M_Conts.isTimerRunning_Audio) {
                    if (llCamera.getVisibility() == View.GONE) {
                        llCamera.setVisibility(View.VISIBLE);
                    } else {
                        llCamera.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Please start recording...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        llCameraRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int camerasNumber = Camera.getNumberOfCameras();
//                if (camerasNumber > 1) {
//                    releaseCamera();
//                    chooseCamera();
//                } else {
//
//                }
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) TedPermission.create().setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (Video_FileHelper.getAvailableExternalMemory() < 50) {
                            Toasty.error(Audio_Recorder_Fragment.this.getContext(), Audio_Recorder_Fragment.this.getString(R.string.low_memory_cant_save), 0).show();
                        } else {

                            if (Utils.isServiceRunning(HBService.class.getName(), getActivity())) {
                                LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getActivity());
                                Intent intent = new Intent();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    intent.setAction(CY_M_Conts.ACTION_START_Audio_Service);
                                }
                                instance.sendBroadcast(intent);
                            } else {
                                startRecording();
                            }

                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> arrayList) {
                        Context context = Audio_Recorder_Fragment.this.getContext();
                        Toasty.warning(context, "Permission Denied\n" + arrayList.toString(), 0).show();
                    }
                })).setRationaleMessage(getString(R.string.need_permission))).setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")).setGotoSettingButtonText(getString(R.string.setting))).setPermissions(CY_M_Conts.permissions)).check();

            }
        });


        return inflate;
    }



    public void startRecording() {
        if (checkSelfPermission("android.permission.RECORD_AUDIO", 22) && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE", 23)) {
            this.hasPermissions = true;
        }
        if (this.hasPermissions) {
            if (this.isDrawOverlyAllowed) {
                startActivityForResult(((MediaProjectionManager) getActivity().getSystemService("media_projection")).createScreenCaptureIntent(), PERMISSION_RECORD_DISPLAY);
                return;
            }
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getActivity().getPackageName())), 101);
        }
    }

    private void checkDrawOverlyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.isDrawOverlyAllowed = Settings.canDrawOverlays(getActivity());
        }
    }

    private boolean checkSelfPermission(String permission, int reqCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, reqCode);
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PERMISSION_RECORD_DISPLAY) {
            if (resultCode != -1) {
//                Utils.showSnackBarNotification(this.iv_record, getString(R.string.recordingPermissionNotGranted), -1);
                this.mScreenCaptureIntent = null;
                return;
            }
            this.mScreenCaptureIntent = intent;
            intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, resultCode);
            this.mScreenCaptureResultCode = resultCode;
            doRecord();
        } else if (requestCode == 101) {
            if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(getActivity())) {
                this.isDrawOverlyAllowed = true;
                startRecording();
                return;
            }
            this.isDrawOverlyAllowed = false;
//            Utils.toast(getApplicationContext(), getString(R.string.drwaerOverlayPermission), 1);
        } else if (requestCode != 122) {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


    private void doRecord() {
        img_record.setImageResource(R.drawable.ic_record_stop);

        CY_M_Conts.ACTION_START_Service_Checker = "audio";

        Intent intent = new Intent(getActivity(), HBService.class);
        intent.putExtra(Utils.SCREEN_CAPTURE_INTENT_RESULT_CODE, 3006);
        intent.putExtra("android.intent.extra.INTENT", mScreenCaptureIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else {
            getActivity().startService(intent);
        }

//        getActivity().startService(intent);
    }

    public void startTimer() {
        if (img_record != null) {
            img_record.setImageResource(R.drawable.ic_record_stop);
//                animationView.setVisibility(View.VISIBLE);
        }
        TextView textView = this.tvClickRecorderFrag;
        if (textView != null) {
            textView.setText(R.string.click_to_stop);
        }

        this.mHandler = new Handler() {
            /* class com.ds.audio.video.screen.backgroundrecorder.CCTVfragment.CCTVRecorderFragment.AnonymousClass6 */
            public void handleMessage(Message message) {
                if (Audio_Recorder_Fragment.this.elapsedTimer < 0) {
                    Audio_Recorder_Fragment.this.tvTimer.setText(Video_TimeHelper.convertSecondsToHMmSs(0));
                } else {
                    Audio_Recorder_Fragment.this.tvTimer.setText(Video_TimeHelper.convertSecondsToHMmSs(Audio_Recorder_Fragment.this.elapsedTimer));
                }
            }
        };
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Audio_Recorder_Fragment cCTVRecorderFragment = Audio_Recorder_Fragment.this;
                cCTVRecorderFragment.elapsedTimer = cCTVRecorderFragment.preHelper.getSecondsInTwoTime(Audio_Recorder_Fragment.this.preHelper.getTimeRecord());
                Audio_Recorder_Fragment.this.mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        if (llCamera.getVisibility() == View.VISIBLE) {
            llCamera.setVisibility(View.GONE);
        }
        Timer timer2 = this.timer;
        if (timer2 != null) {
            timer2.cancel();
            this.timer = null;
        }

        if (img_record != null) {
            img_record.setImageResource(R.drawable.ic_record_start);
//                animationView.setVisibility(View.INVISIBLE);
        }
        TextView textView = this.tvClickRecorderFrag;
        if (textView != null) {
            textView.setText(getString(R.string.click_to_record));
        }
        TextView textView2 = this.tvTimer;
        if (textView2 != null) {
            textView2.setText("00:00");
        }


        Toasty.success(getContext(), getString(R.string.record_success_audio), 0).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.broadcastManagerStop.unregisterReceiver(this.receiverStop);
        this.broadcastManagerStart.unregisterReceiver(this.receiverStart);
    }

}
