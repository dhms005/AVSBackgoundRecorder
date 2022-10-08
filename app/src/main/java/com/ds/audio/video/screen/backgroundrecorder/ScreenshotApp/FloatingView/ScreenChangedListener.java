package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView;

import android.graphics.Rect;

import androidx.annotation.Keep;

@Keep
interface ScreenChangedListener {
    void onScreenChanged(Rect rect, int i);
}
