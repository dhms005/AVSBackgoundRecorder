package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.Keep;

@Keep
class FullscreenObserverView extends View implements ViewTreeObserver.OnGlobalLayoutListener, View.OnSystemUiVisibilityChangeListener {
    static final int NO_LAST_VISIBILITY = -1;
    private static final int OVERLAY_TYPE;
    private int mLastUiVisibility = -1;
    private final WindowManager.LayoutParams mParams;
    private final ScreenChangedListener mScreenChangedListener;
    private final Rect mWindowRect = new Rect();

    static {
        if (Build.VERSION.SDK_INT <= 25) {
            OVERLAY_TYPE = 2006;
        } else {
            OVERLAY_TYPE = 2038;
        }
    }

    FullscreenObserverView(Context context, ScreenChangedListener screenChangedListener) {
        super(context);
        this.mScreenChangedListener = screenChangedListener;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.mParams = layoutParams;
        layoutParams.width = 1;
        layoutParams.height = -1;
        layoutParams.type = OVERLAY_TYPE;
        layoutParams.flags = 56;
        layoutParams.format = -3;
    }

    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        setOnSystemUiVisibilityChangeListener(this);
    }

    
    public void onDetachedFromWindow() {
        if (Build.VERSION.SDK_INT >= 16) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        setOnSystemUiVisibilityChangeListener((OnSystemUiVisibilityChangeListener) null);
        super.onDetachedFromWindow();
    }

    public void onGlobalLayout() {
        if (this.mScreenChangedListener != null) {
            getWindowVisibleDisplayFrame(this.mWindowRect);
            this.mScreenChangedListener.onScreenChanged(this.mWindowRect, this.mLastUiVisibility);
        }
    }

    public void onSystemUiVisibilityChange(int i) {
        this.mLastUiVisibility = i;
        if (this.mScreenChangedListener != null) {
            getWindowVisibleDisplayFrame(this.mWindowRect);
            this.mScreenChangedListener.onScreenChanged(this.mWindowRect, i);
        }
    }

   
    public WindowManager.LayoutParams getWindowLayoutParams() {
        return this.mParams;
    }
}
