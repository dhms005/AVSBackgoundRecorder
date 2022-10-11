package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities;

import static com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts.FROM_AUDIO;
import static com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts.FROM_VIDEO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_FileHelper;
import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_TimeHelper;
import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities.Video_Save_Schedule_Activity;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_TimeHelper;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Receiver.Audio_AlarmReceiver;
import com.ds.audio.video.screen.backgroundrecorder.ads.CY_M_Admob_Full_AD_New;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.ScheduleVideo;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordViewModel;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Audio_Save_Schedule_Activity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_REPLY = "com.ds.audio.video.screen.backgroundrecorder.REPLY";

    TextView btnSave;
    TextView tvDate;
    TextView tvDuration;
    RelativeLayout tvEditDate;
    RelativeLayout tvEditDuration;
    RelativeLayout tvEditTime;
    RelativeLayout tvEditUseCam;
    TextView tvTime;
    TextView tvUseCam;
    private DatePickerDialog datePickerDialog;
    private int duration = 5;
    private Activity mContext;
    private Calendar now;
    private TimePickerDialog tpd;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private WordViewModel mWordViewModel;
    public static ArrayList<ScheduleVideo> schedeluLisst = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.audio_scheduletime_activity);
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mWordViewModel.getScheduleAudio().observe(this, scheduleVideos -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                schedeluLisst.clear();
                schedeluLisst.addAll(scheduleVideos);
            }
        });

        btnSave = findViewById(R.id.btn_save);
        tvDate = findViewById(R.id.tv_show_date_frag);
        tvDuration = findViewById(R.id.tv_show_duration_frag);
        tvEditDate = findViewById(R.id.tv_edit_date_frag);
        tvEditDuration = findViewById(R.id.tv_edit_duration_frag);
        tvEditTime = findViewById(R.id.tv_edit_time_frag);
        tvEditUseCam = findViewById(R.id.tv_edit_use_cam_frag);
        tvTime = findViewById(R.id.tv_show_time_frag);
        tvUseCam = findViewById(R.id.tv_show_use_cam_frag);

        ButterKnife.bind(this);

        this.now = Calendar.getInstance();
        setupDisplay();
        this.tvEditDate.setOnClickListener(this);
        this.tvEditTime.setOnClickListener(this);
        this.tvEditDuration.setOnClickListener(this);
        this.tvEditUseCam.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
        TextView textView = this.tvDuration;
        textView.setText(this.duration + " " + getString(R.string.min));

        findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
//        View inflate = layoutInflater.inflate(R.layout.cy_m_fragment_setting_time, viewGroup, false);
//
//        return inflate;
//    }

    private void setupDisplay() {

        Log.e("Date", "" + Calendar.getInstance().getTime());

        this.tvDate.setText("" + Audio_TimeHelper.parseDate2Str(Calendar.getInstance().getTime()));
        this.tvTime.setText("" + Audio_TimeHelper.parseTime2Str(Calendar.getInstance()));
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_save) {
            switch (id) {
                case R.id.tv_edit_date_frag:
                    this.datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {
                            Audio_Save_Schedule_Activity.this.now.set(i, i2, i3);
                            Audio_Save_Schedule_Activity.this.tvDate.setText(Audio_TimeHelper.parseDate2Str(Audio_Save_Schedule_Activity.this.now.getTime()));
                        }
                    }, this.now.get(1), this.now.get(2), this.now.get(5));
                    this.datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
                    this.datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
                    return;
                case R.id.tv_edit_duration_frag:
                    showAlertDialog();
                    return;
                case R.id.tv_edit_time_frag:
                    this.tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePickerDialog timePickerDialog, int i, int i2, int i3) {
                            Audio_Save_Schedule_Activity.this.now.set(11, i);
                            Audio_Save_Schedule_Activity.this.now.set(12, i2);
                            Audio_Save_Schedule_Activity.this.tvTime.setText(Audio_TimeHelper.parseTime2Str(Audio_Save_Schedule_Activity.this.now));
                        }
                    }, this.now.get(11), this.now.get(12), false);
                    this.tpd.setVersion(TimePickerDialog.Version.VERSION_1);
                    this.tpd.show(getSupportFragmentManager(), "Timepickerdialog");
                    return;
                case R.id.tv_edit_use_cam_frag:
                    if (this.tvUseCam.getText().equals(getString(R.string.back))) {
                        this.tvUseCam.setText(getString(R.string.front));
                        return;
                    } else {
                        this.tvUseCam.setText(getString(R.string.back));
                        return;
                    }
                default:
                    return;
            }
        } else {
            CY_M_Admob_Full_AD_New.getInstance().showInter(mContext, new CY_M_Admob_Full_AD_New.MyCallback() {
                @Override
                public void callbackCall() {
                    ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) ((TedPermission.Builder) TedPermission.create().setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Log.e("#DEUBG", "onPermissionGranted");
                            callMethod();
                        }

                        @Override
                        public void onPermissionDenied(List<String> arrayList) {
                            Log.e("#DEUBG", "onPermissionDenied");
                            Toasty.warning(Audio_Save_Schedule_Activity.this, "Permission Denied\n" + arrayList.toString(), 0).show();
                        }
                    })).setRationaleMessage(getString(R.string.need_permission))).setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")).setGotoSettingButtonText(getString(R.string.setting))).setPermissions(CY_M_Conts.permissions)).check();
                }
            });
        }
    }

    @SuppressLint("WrongConstant")
    private void callMethod() {
        if (Audio_FileHelper.getAvailableExternalMemory() < 50) {
            Toasty.error(Audio_Save_Schedule_Activity.this, Audio_Save_Schedule_Activity.this.getString(R.string.low_memory_cant_save), 0).show();
        } else {
            if (schedeluLisst.size() == 0) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, now.getTimeInMillis());
                setResult(RESULT_OK, replyIntent);
                Intent intent = new Intent(mContext, Audio_AlarmReceiver.class);
                intent.putExtra(CY_M_Conts.CAMERA_USE, String.valueOf(tvUseCam.equals(getString(R.string.front))));
                intent.putExtra(CY_M_Conts.CAMERA_DURATION, String.valueOf(duration * 60));
                SharePrefUtils.putString(CY_M_Conts.AUDIO_CURRENT_TIME, String.valueOf(now.getTimeInMillis()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), PendingIntent.getBroadcast(Audio_Save_Schedule_Activity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE));
                } else {
                    ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), PendingIntent.getBroadcast(Audio_Save_Schedule_Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
                Context context = mContext;
                Toast.makeText(context, "Start recorder at : " + Audio_TimeHelper.parseCalen2Str(now), Toast.LENGTH_SHORT).show();

                ScheduleVideo word = new ScheduleVideo(now.getTimeInMillis(),FROM_AUDIO,duration,String.valueOf(tvUseCam.equals(getString(R.string.front))));
                mWordViewModel.insert(word);
            } else {
                 Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, now.getTimeInMillis());
                setResult(RESULT_OK, replyIntent);
                Toast.makeText(mContext, "Start recorder at : " + Audio_TimeHelper.parseCalen2Str(now), Toast.LENGTH_SHORT).show();

                ScheduleVideo word = new ScheduleVideo(now.getTimeInMillis(),FROM_AUDIO,duration,String.valueOf(tvUseCam.equals(getString(R.string.front))));
                mWordViewModel.insert(word);
            }
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showAlertDialog() {
        View inflate = ((LayoutInflater) this.mContext.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cy_m_dialog_seekbar, (ViewGroup) null);
        AlertDialog.Builder cancelable = new AlertDialog.Builder(this.mContext).setView(inflate).setCancelable(true);
        final SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.seekBar1);
        final TextView textView = (TextView) inflate.findViewById(R.id.tv_duration_dialog);
        textView.setText((seekBar.getProgress() + 2) + " " + getString(R.string.min));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                this.progress = i;
                textView.setText((this.progress + 2) + " " + Audio_Save_Schedule_Activity.this.getString(R.string.min));
            }
        });
        cancelable.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialogInterface, int i) {
                Audio_Save_Schedule_Activity.this.duration = seekBar.getProgress() + 2;
                TextView textView = Audio_Save_Schedule_Activity.this.tvDuration;
                textView.setText(Audio_Save_Schedule_Activity.this.duration + " " + Audio_Save_Schedule_Activity.this.getString(R.string.min));
            }
        });
        cancelable.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        cancelable.create().show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            ScheduleVideo word = new ScheduleVideo(now.getTimeInMillis(),FROM_AUDIO,duration,String.valueOf(tvUseCam.equals(getString(R.string.front))));
            mWordViewModel.insert(word);
//            Log.e("DBINSERT", "" + mWordViewModel.getAllWords().toString());
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Not Saved",
                    Toast.LENGTH_LONG).show();
        }
    }
}
