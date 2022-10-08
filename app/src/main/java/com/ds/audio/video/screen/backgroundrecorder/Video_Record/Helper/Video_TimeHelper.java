package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Video_TimeHelper {
    public static String parseDate2Str(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date);
    }

    public static String parseTime2Str(Calendar calendar) {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.getTime());
    }

    public static String parseCalen2Str(Calendar calendar) {
        return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault()).format(calendar.getTime());
    }

    public static String parseCalen2StrNoSS(Calendar calendar) {
        return new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime());
    }

    public static Calendar parseStr2Calendar(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
        Calendar instance = Calendar.getInstance();
        try {
            instance.setTime(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return instance;
    }
    public static String GetTimeandDatefromMiliSeconds(long milliSeconds ) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String convertSecondsToHMmSs(long j) {
        return String.format("%02d:%02d", Long.valueOf((j / 60) % 60), Long.valueOf(j % 60));
    }

    public static String convertMilliSecondsToHMmSs(long j) {
        return String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j))));
    }
}
