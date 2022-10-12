package com.ds.audio.video.screen.backgroundrecorder.CY_M_Define;

public class CY_M_Conts {
    public static final String ACTION_START_SERVICE = "START_EXTRA";
    public static final String ACTION_STOP_EXTRA = "STOP_EXTRA";

    public static final String ACTION_START_VIDEO_SERVICE = "ACTION_START_VIDEO_SERVICE";
    public static final String ACTION_STOP_VIDEO_EXTRA = "ACTION_STOP_VIDEO_EXTRA";

    public static final String ACTION_START_AUDIO_SERVICE = "ACTION_START_AUDIO_SERVICE";
    public static final String ACTION_STOP_AUDIO_EXTRA = "ACTION_STOP_AUDIO_EXTRA";

    public static final String ACTION_START_SCREEN_RECORDER_SERVICE = "START_EXTRA_RECORDER_SERVICE";
    public static final String ACTION_STOP_SCREEN_RECORDER_EXTRA = "STOP_EXTRA_RECORDER_SERVICE";

    public static final String CAMERA_DURATION = "camera_duration";
    public static final String CURRENT_TIME = "current_time";
    public static final String AUDIO_CURRENT_TIME = "audio_current_time";
    public static final String AUDIO_FROM_SCHEDULE = "AUDIO_FROM_SCHEDULE";
    public static final String VIDEO_FROM_SCHEDULE = "VIDEO_FROM_SCHEDULE";
    public static final String CAMERA_USE = "camera_use";
    public static final String FIRST_RUN = "first_run";
    public static final String INTENT_KEY_FILE_PATH = "filepath";
    public static final String VIDEO_NAME = "video_data";
    public static final String SCREENRECORDER_NAME = "screenRecordred_data";
    public static final String AUDIO_NAME = "audio-data";
    public static final String SMS_START_RECORD = "sms_start_record";
    public static final String TIME_RECORD = "time_record";
    public static final String[] permissions = {"android.permission.INTERNET",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.RECEIVE_BOOT_COMPLETED",
            "android.permission.ACCESS_NOTIFICATION_POLICY",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static final String FROM_VIDEO = "video";
    public static final String FROM_AUDIO = "audio";
    public static final String FROM_SCREEN_RECORD = "screen_record";

    public static boolean  isTimerRunning_Video = false;
    public static final String ACTION_START_Video_Service = "ACTION_START_Video_Service";
    public static String ACTION_START_Service_Checker = "cctv";

    public static boolean  isTimerRunning_Audio = false;
    public static final String ACTION_START_Audio_Service = "ACTION_START_Audio_Service";

    public static boolean  isTimerRunning_ScreenRecorder = false;
    public static final String ACTION_START_ScreenRecorder_Service = "ACTION_START_ScreenRecorder_Service";

    public static final String ACTION_START_ScreenShot_Service = "ACTION_START_ScreenShot_Service";
    public static final String ACTION_STOP_ScreenShot_Service = "ACTION_STOP_ScreenShot_Service";

    public static Boolean mRecordingStarted_Other = false;
    public static Boolean mOpenAppChecker = true;

    public static boolean  Video_Backgorund_Forgroundchecker = false;
}
