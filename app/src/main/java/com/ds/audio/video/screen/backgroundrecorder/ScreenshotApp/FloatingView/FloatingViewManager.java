package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Keep;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
@Keep
public class FloatingViewManager implements ScreenChangedListener, View.OnTouchListener, TrashViewListener {
    public static final int DISPLAY_MODE_HIDE_ALWAYS = 2;
    public static final int DISPLAY_MODE_HIDE_FULLSCREEN = 3;
    public static final int DISPLAY_MODE_SHOW_ALWAYS = 1;
    public static final int MOVE_DIRECTION_DEFAULT = 0;
    public static final int MOVE_DIRECTION_LEFT = 1;
    public static final int MOVE_DIRECTION_NEAREST = 4;
    public static final int MOVE_DIRECTION_NONE = 3;
    public static final int MOVE_DIRECTION_RIGHT = 2;
    public static final int MOVE_DIRECTION_THROWN = 5;
    public static final float SHAPE_CIRCLE = 1.0f;
    public static final float SHAPE_RECTANGLE = 1.4142f;
    private Context mContext;
    private final DisplayMetrics mDisplayMetrics;
    private int mDisplayMode;
    private final FloatingListener mFloatingListener;
    private final ArrayList<C0814FloatingView> mFloatingViewList;
    private final Rect mFloatingViewRect;
    private final FullscreenObserverView mFullscreenObserverView;
    private boolean mIsMoveAccept;
    private final Resources mResources;
    private final Rect mSafeInsetRect;
    private C0814FloatingView mTargetFloatingView;
    private final TrashView mTrashView;
    private final Rect mTrashViewRect;
    private final WindowManager mWindowManager;
    WeakReference<Context> weakReference = new WeakReference<>(this.mContext);

    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MoveDirection {
    }

    public static class Options {
        public boolean animateInitialMove = true;
        public int floatingViewHeight = -2;
        public int floatingViewWidth = -2;
        public int floatingViewX = Integer.MIN_VALUE;
        public int floatingViewY = Integer.MIN_VALUE;
        public int moveDirection = 0;
        public int overMargin = 0;
        public float shape = 1.0f;
        public boolean usePhysics = true;
    }

    public FloatingViewManager(Context context, FloatingListener floatingListener) {
        this.mContext = context;
        this.mResources = context.getResources();
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mDisplayMetrics = new DisplayMetrics();
        this.mFloatingListener = floatingListener;
        this.mFloatingViewRect = new Rect();
        this.mTrashViewRect = new Rect();
        this.mIsMoveAccept = false;
        this.mDisplayMode = 3;
        this.mSafeInsetRect = new Rect();
        this.mFloatingViewList = new ArrayList<>();
        this.mFullscreenObserverView = new FullscreenObserverView(context, this);
        this.mTrashView = new TrashView(context);
    }

    private boolean isIntersectWithTrash() {
        if (!this.mTrashView.isTrashEnabled()) {
            return false;
        }
        this.mTrashView.getWindowDrawingRect(this.mTrashViewRect);
        this.mTargetFloatingView.getWindowDrawingRect(this.mFloatingViewRect);
        return Rect.intersects(this.mTrashViewRect, this.mFloatingViewRect);
    }


    @Override
    public void onScreenChanged(Rect windowRect, int visibility) {
        // detect status bar
        final boolean isFitSystemWindowTop = windowRect.top == 0;
        boolean isHideStatusBar;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && visibility != FullscreenObserverView.NO_LAST_VISIBILITY) {
            // Support for screen rotation when setSystemUiVisibility is used
            isHideStatusBar = isFitSystemWindowTop || (visibility & View.SYSTEM_UI_FLAG_LOW_PROFILE) == View.SYSTEM_UI_FLAG_LOW_PROFILE;
        } else {
            isHideStatusBar = isFitSystemWindowTop;
        }

        // detect navigation bar
        final boolean isHideNavigationBar;
        if (visibility == FullscreenObserverView.NO_LAST_VISIBILITY) {
            // At the first it can not get the correct value, so do special processing
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mWindowManager.getDefaultDisplay().getRealMetrics(mDisplayMetrics);
                isHideNavigationBar = windowRect.width() - mDisplayMetrics.widthPixels == 0 && windowRect.bottom - mDisplayMetrics.heightPixels == 0;
            } else {
                mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
                isHideNavigationBar = windowRect.width() - mDisplayMetrics.widthPixels > 0 || windowRect.height() - mDisplayMetrics.heightPixels > 0;
            }
        } else {
            isHideNavigationBar = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        final boolean isPortrait = mResources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        // update FloatingView layout
        mTargetFloatingView.onUpdateSystemLayout(isHideStatusBar, isHideNavigationBar, isPortrait, windowRect);

        // フルスクリーンでの非表示モードでない場合は何もしない
        if (mDisplayMode != DISPLAY_MODE_HIDE_FULLSCREEN) {
            return;
        }

        mIsMoveAccept = false;
        final int state = mTargetFloatingView.getState();
        // 重なっていない場合は全て非表示処理
        if (state == C0814FloatingView.STATE_NORMAL) {
            final int size = mFloatingViewList.size();
            for (int i = 0; i < size; i++) {
                final C0814FloatingView floatingView = mFloatingViewList.get(i);
                floatingView.setVisibility(isFitSystemWindowTop ? View.GONE : View.VISIBLE);
            }
            mTrashView.dismiss();
        }
        // 重なっている場合は削除
        else if (state == C0814FloatingView.STATE_INTERSECTING) {
            mTargetFloatingView.setFinishing();
            mTrashView.dismiss();
        }
    }


    public void onUpdateActionTrashIcon() {
        this.mTrashView.updateActionTrashIcon((float) this.mTargetFloatingView.getMeasuredWidth(), (float) this.mTargetFloatingView.getMeasuredHeight(), this.mTargetFloatingView.getShape());
    }

    public void onTrashAnimationStarted(int i) {
        if (i == 2 || i == 3) {
            int size = this.mFloatingViewList.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mFloatingViewList.get(i2).setDraggable(false);
            }
        }
    }

    public void onTrashAnimationEnd(int i) {
        if (this.mTargetFloatingView.getState() == 2) {
            removeViewToWindow(this.mTargetFloatingView);
        }
        int size = this.mFloatingViewList.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mFloatingViewList.get(i2).setDraggable(true);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0 && !this.mIsMoveAccept) {
            return false;
        }
        int state = this.mTargetFloatingView.getState();
        C0814FloatingView floatingView = (C0814FloatingView) view;
        this.mTargetFloatingView = floatingView;
        if (action == 0) {
            this.mIsMoveAccept = true;
        } else if (action == 2) {
            boolean isIntersectWithTrash = isIntersectWithTrash();
            boolean z = state == 1;
            if (isIntersectWithTrash) {
                this.mTargetFloatingView.setIntersecting((int) this.mTrashView.getTrashIconCenterX(), (int) this.mTrashView.getTrashIconCenterY());
            }
            if (isIntersectWithTrash && !z) {
                this.mTargetFloatingView.performHapticFeedback(0);
                this.mTrashView.setScaleTrashIcon(true);
            } else if (!isIntersectWithTrash && z) {
                this.mTargetFloatingView.setNormal();
                this.mTrashView.setScaleTrashIcon(false);
            }
        } else if (action == 1 || action == 3) {
            if (state == 1) {
                floatingView.setFinishing();
                this.mTrashView.setScaleTrashIcon(false);
            }
            this.mIsMoveAccept = false;
            if (this.mFloatingListener != null) {
                boolean z2 = this.mTargetFloatingView.getState() == 2;
                WindowManager.LayoutParams windowLayoutParams = this.mTargetFloatingView.getWindowLayoutParams();
                this.mFloatingListener.onTouchFinished(z2, windowLayoutParams.x, windowLayoutParams.y);
            }
        }
        if (state == 1) {
            this.mTrashView.onTouchFloatingView(motionEvent, (float) this.mFloatingViewRect.left, (float) this.mFloatingViewRect.top);
        } else {
            WindowManager.LayoutParams windowLayoutParams2 = this.mTargetFloatingView.getWindowLayoutParams();
            this.mTrashView.onTouchFloatingView(motionEvent, (float) windowLayoutParams2.x, (float) windowLayoutParams2.y);
        }
        return false;
    }

    public void setFixedTrashIconImage(int i) {
        this.mTrashView.setFixedTrashIconImage(i);
    }

    public void setActionTrashIconImage(int i) {
        this.mTrashView.setActionTrashIconImage(i);
    }

    public void setFixedTrashIconImage(Drawable drawable) {
        this.mTrashView.setFixedTrashIconImage(drawable);
    }

    public void setActionTrashIconImage(Drawable drawable) {
        this.mTrashView.setActionTrashIconImage(drawable);
    }

    public void setDisplayMode(int i) {
        this.mDisplayMode = i;
        if (i == 1 || i == 3) {
            Iterator<C0814FloatingView> it = this.mFloatingViewList.iterator();
            while (it.hasNext()) {
                it.next().setVisibility(View.VISIBLE);
            }
        } else if (i == 2) {
            Iterator<C0814FloatingView> it2 = this.mFloatingViewList.iterator();
            while (it2.hasNext()) {
                it2.next().setVisibility(View.GONE);
            }
            this.mTrashView.dismiss();
        }
    }

    public void setTrashViewEnabled(boolean z) {
        this.mTrashView.setTrashEnabled(z);
    }

    public boolean isTrashViewEnabled() {
        return this.mTrashView.isTrashEnabled();
    }

    public void setSafeInsetRect(Rect rect) {
        if (rect == null) {
            this.mSafeInsetRect.setEmpty();
        } else {
            this.mSafeInsetRect.set(rect);
        }
        int size = this.mFloatingViewList.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                this.mFloatingViewList.get(i).setSafeInsetRect(this.mSafeInsetRect);
            }
            this.mFullscreenObserverView.onGlobalLayout();
        }
    }

    public void addViewToWindow(View view, Options options) {
        if (this.mContext == null && this.weakReference.get() != null) {
            this.mContext = (Context) this.weakReference.get();
        }
        if (this.mWindowManager != null && this.mContext != null) {
            boolean isEmpty = this.mFloatingViewList.isEmpty();
            C0814FloatingView floatingView = new C0814FloatingView(this.mContext);
            floatingView.setInitCoords(options.floatingViewX, options.floatingViewY);
            floatingView.setOnTouchListener(this);
            floatingView.setShape(options.shape);
            floatingView.setOverMargin(options.overMargin);
            floatingView.setMoveDirection(options.moveDirection);
            floatingView.usePhysics(options.usePhysics);
            floatingView.setAnimateInitialMove(options.animateInitialMove);
            floatingView.setSafeInsetRect(this.mSafeInsetRect);
            view.setLayoutParams(new FrameLayout.LayoutParams(options.floatingViewWidth, options.floatingViewHeight));
            floatingView.addView(view);
            if (this.mDisplayMode == 2) {
                floatingView.setVisibility(View.GONE);
            }
            this.mFloatingViewList.add(floatingView);
            this.mTrashView.setTrashViewListener(this);
            this.mWindowManager.addView(floatingView, floatingView.getWindowLayoutParams());
            if (isEmpty) {
                WindowManager windowManager = this.mWindowManager;
                FullscreenObserverView fullscreenObserverView = this.mFullscreenObserverView;
                windowManager.addView(fullscreenObserverView, fullscreenObserverView.getWindowLayoutParams());
                this.mTargetFloatingView = floatingView;
            } else {
                removeViewImmediate(this.mTrashView);
            }
            WindowManager windowManager2 = this.mWindowManager;
            TrashView trashView = this.mTrashView;
            windowManager2.addView(trashView, trashView.getWindowLayoutParams());
        }
    }

    private void removeViewToWindow(C0814FloatingView floatingView) {
        FloatingListener floatingListener;
        int indexOf = this.mFloatingViewList.indexOf(floatingView);
        if (indexOf != -1) {
            removeViewImmediate(floatingView);
            this.mFloatingViewList.remove(indexOf);
        }
        if (this.mFloatingViewList.isEmpty() && (floatingListener = this.mFloatingListener) != null) {
            floatingListener.onFinishFloatingView();
        }
    }

    public void removeAllViewToWindow() {
        removeViewImmediate(this.mFullscreenObserverView);
        removeViewImmediate(this.mTrashView);
        int size = this.mFloatingViewList.size();
        for (int i = 0; i < size; i++) {
            removeViewImmediate(this.mFloatingViewList.get(i));
        }
        this.mFloatingViewList.clear();
    }

    private void removeViewImmediate(View view) {
        try {
            this.mWindowManager.removeViewImmediate(view);
        } catch (IllegalArgumentException unused) {
        }
    }

    public static Rect findCutoutSafeArea(Activity activity) {
        WindowInsets rootWindowInsets;
        DisplayCutout displayCutout;
        Rect rect = new Rect();
        if (!(Build.VERSION.SDK_INT < 28 || (rootWindowInsets = activity.getWindow().getDecorView().getRootWindowInsets()) == null || (displayCutout = rootWindowInsets.getDisplayCutout()) == null)) {
            rect.set(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
        }
        return rect;
    }
}
