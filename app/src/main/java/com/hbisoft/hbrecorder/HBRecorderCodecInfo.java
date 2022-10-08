package com.hbisoft.hbrecorder;

import android.content.Context;
import android.media.CamcorderProfile;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;

public class HBRecorderCodecInfo {
    private Context context;
    HashMap<String, String> mAudioMap = new HashMap<>();
    HashMap<String, String> mVideoMap = new HashMap<>();
    ArrayList<String> supportedAudioFormats = new ArrayList<>();
    ArrayList<String> supportedVideoFormats = new ArrayList<>();

    public int getMaxSupportedWidth() {
        return getRecordingInfo().width;
    }

    public int getMaxSupportedHeight() {
        return getRecordingInfo().height;
    }

    private RecordingInfo getRecordingInfo() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int displayDensity = displayMetrics.densityDpi;
        boolean isLandscape = this.context.getResources().getConfiguration().orientation == 2;
        CamcorderProfile camcorderProfile = CamcorderProfile.get(1);
        int cameraHeight = -1;
        int cameraWidth = camcorderProfile != null ? camcorderProfile.videoFrameWidth : -1;
        if (camcorderProfile != null) {
            cameraHeight = camcorderProfile.videoFrameHeight;
        }
        return calculateRecordingInfo(displayWidth, displayHeight, displayDensity, isLandscape, cameraWidth, cameraHeight, camcorderProfile != null ? camcorderProfile.videoFrameRate : 30, 100);
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    static final class RecordingInfo {
        final int density;
        final int frameRate;
        final int height;
        final int width;

        RecordingInfo(int width2, int height2, int frameRate2, int density2) {
            this.width = width2;
            this.height = height2;
            this.frameRate = frameRate2;
            this.density = density2;
        }
    }

    static RecordingInfo calculateRecordingInfo(int displayWidth, int displayHeight, int displayDensity, boolean isLandscapeDevice, int cameraWidth, int cameraHeight, int cameraFrameRate, int sizePercentage) {
        int displayWidth2 = (displayWidth * sizePercentage) / 100;
        int displayHeight2 = (displayHeight * sizePercentage) / 100;
        if (cameraWidth == -1 && cameraHeight == -1) {
            return new RecordingInfo(displayWidth2, displayHeight2, cameraFrameRate, displayDensity);
        }
        int frameWidth = isLandscapeDevice ? cameraWidth : cameraHeight;
        int frameHeight = isLandscapeDevice ? cameraHeight : cameraWidth;
        if (frameWidth >= displayWidth2 && frameHeight >= displayHeight2) {
            return new RecordingInfo(displayWidth2, displayHeight2, cameraFrameRate, displayDensity);
        }
        if (isLandscapeDevice) {
            frameWidth = (displayWidth2 * frameHeight) / displayHeight2;
        } else {
            frameHeight = (displayHeight2 * frameWidth) / displayWidth2;
        }
        return new RecordingInfo(frameWidth, frameHeight, cameraFrameRate, displayDensity);
    }

}
