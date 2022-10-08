package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView;

import androidx.annotation.Keep;

@Keep
interface TrashViewListener {
    void onTrashAnimationEnd(int i);

    void onTrashAnimationStarted(int i);

    void onUpdateActionTrashIcon();
}
