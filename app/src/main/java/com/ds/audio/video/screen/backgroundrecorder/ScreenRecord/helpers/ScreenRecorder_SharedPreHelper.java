package com.ds.audio.video.screen.backgroundrecorder.ScreenRecord.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.ds.audio.video.screen.backgroundrecorder.CY_M_Define.CY_M_Conts;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_TimeHelper;

import java.util.Calendar;
import java.util.Random;

public class ScreenRecorder_SharedPreHelper {
    public SharedPreferences.Editor editor;
    public SharedPreferences preferences;
    private Context context;
    private Calendar timeRecord;

    public ScreenRecorder_SharedPreHelper(Context context2) {
        this.preferences = context2.getSharedPreferences(CY_M_Conts.SCREENRECORDER_NAME, 0);
        this.context = context2;
        this.editor = this.preferences.edit();
    }

    public void saveTimeToPre() {
        this.editor.putString(CY_M_Conts.TIME_RECORD, Video_TimeHelper.parseCalen2Str(Calendar.getInstance()));
        this.editor.commit();
    }

    public void remove() {
        this.editor.remove(CY_M_Conts.TIME_RECORD);
        this.editor.commit();
    }

    public boolean haveTimeStart() {
        return this.preferences.contains(CY_M_Conts.TIME_RECORD);
    }

    public Calendar getTimeRecord() {
        return Video_TimeHelper.parseStr2Calendar(this.preferences.getString(CY_M_Conts.TIME_RECORD, Video_TimeHelper.parseCalen2Str(Calendar.getInstance())));
    }

    public long getSecondsInTwoTime(Calendar calendar) {
        return (Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()) / 1000;
    }

    public void saveSendMsg() {
        if (!this.preferences.getBoolean(CY_M_Conts.FIRST_RUN, false)) {
            this.editor.putString(CY_M_Conts.SMS_START_RECORD, createRandomSendMsg());
            this.editor.putBoolean(CY_M_Conts.FIRST_RUN, true);
            this.editor.commit();
        }
    }

    public String getSendMsg() {
        return this.preferences.getString(CY_M_Conts.SMS_START_RECORD, createRandomSendMsg());
    }

    public String createRandomSendMsg() {
        String str = "0123456789";
        for (int i = 0; i < 26; i++) {
            str = str + ((char) (i + 65));
        }
        int length = str.length();
        Random random = new Random();
        String str2 = "";
        while (!hasNums(str2)) {
            String str3 = "";
            for (int i2 = 0; i2 < 6; i2++) {
                str3 = str3 + str.charAt(random.nextInt(length));
            }
            str2 = str3;
        }
        return "SECRET " + str2;
    }

    public boolean hasNums(String str) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i < str.length(); i++) {
            for (char c : cArr) {
                if (str.charAt(i) == c) {
                    return true;
                }
            }
        }
        return false;
    }
}
