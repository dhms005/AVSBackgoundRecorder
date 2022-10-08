package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class C0814FloatingView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {
    private static final float ANIMATION_FLING_X_FRICTION = 1.7f;
    private static final float ANIMATION_FLING_Y_FRICTION = 1.7f;
    private static final float ANIMATION_SPRING_X_DAMPING_RATIO = 0.7f;
    private static final float ANIMATION_SPRING_X_STIFFNESS = 350.0f;
    private static final int CURRENT_VELOCITY_UNITS = 1000;
    static final int DEFAULT_HEIGHT = -2;
    static final int DEFAULT_WIDTH = -2;
    public static final int DEFAULT_X = Integer.MIN_VALUE;
    public static final int DEFAULT_Y = Integer.MIN_VALUE;
    private static final int LONG_PRESS_TIMEOUT = ((int) (((float) ViewConfiguration.getLongPressTimeout()) * 1.5f));
    private static final float MAX_X_VELOCITY_SCALE_DOWN_VALUE = 9.0f;
    private static final float MAX_Y_VELOCITY_SCALE_DOWN_VALUE = 8.0f;
    private static final long MOVE_TO_EDGE_DURATION = 450;
    private static final float MOVE_TO_EDGE_OVERSHOOT_TENSION = 1.25f;
    private static final int OVERLAY_TYPE;
    private static final float SCALE_NORMAL = 1.0f;
    private static final float SCALE_PRESSED = 0.9f;
    static final int STATE_FINISHING = 2;
    static final int STATE_INTERSECTING = 1;
    static final int STATE_NORMAL = 0;
    private static final float THROW_THRESHOLD_SCALE_DOWN_VALUE = 9.0f;
    private boolean mAnimateInitialMove;
    private final FloatingAnimationHandler mAnimationHandler = new FloatingAnimationHandler(this);
    private final int mBaseNavigationBarHeight;
    private final int mBaseNavigationBarRotatedHeight;
    private final int mBaseStatusBarHeight;
    private final int mBaseStatusBarRotatedHeight;
    private int mInitX;
    private int mInitY;
    private boolean mIsDraggable;
    private boolean mIsInitialAnimationRunning;
    private boolean mIsLongPressed;
    private boolean mIsMoveAccept;
    private final boolean mIsTablet;
    private float mLocalTouchX;
    private float mLocalTouchY;
    private final LongPressHandler mLongPressHandler = new LongPressHandler(this);
    private float mMaximumFlingVelocity;
    private float mMaximumXVelocity;
    private float mMaximumYVelocity;
    private final DisplayMetrics mMetrics;
    private int mMoveDirection = 0;
    private ValueAnimator mMoveEdgeAnimator;
    private final TimeInterpolator mMoveEdgeInterpolator = new OvershootInterpolator(MOVE_TO_EDGE_OVERSHOOT_TENSION);
    
    public final Rect mMoveLimitRect;
    private float mMoveThreshold;
    private int mNavigationBarHorizontalOffset;
    private int mNavigationBarVerticalOffset;
    private OnTouchListener mOnTouchListener;
    private int mOverMargin;
    
    public final WindowManager.LayoutParams mParams;
    private final Rect mPositionLimitRect;
    private int mRotation;
    private final Rect mSafeInsetRect;
    private float mScreenTouchDownX;
    private float mScreenTouchDownY;
    private float mScreenTouchX;
    private float mScreenTouchY;
    private float mShape;
    private int mStatusBarHeight;
    private float mThrowMoveThreshold;
    private long mTouchDownTime;
    private int mTouchXOffset;
    private int mTouchYOffset;
    private boolean mUsePhysics = false;
    
    public VelocityTracker mVelocityTracker;
    private ViewConfiguration mViewConfiguration;
    private final WindowManager mWindowManager;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView.FloatingView$AnimationState */
    @interface AnimationState {
    }

    static {
        if (Build.VERSION.SDK_INT <= 25) {
            OVERLAY_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            OVERLAY_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
    }

    C0814FloatingView(Context context) {
        super(context);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        this.mWindowManager = windowManager;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.mParams = layoutParams;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mMetrics = displayMetrics;
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.type = OVERLAY_TYPE;
        layoutParams.flags = 16777768;
        layoutParams.format = -3;
        layoutParams.gravity = 83;
        Resources resources = context.getResources();
        boolean z = (resources.getConfiguration().screenLayout & 15) >= 3;
        this.mIsTablet = z;
        this.mRotation = windowManager.getDefaultDisplay().getRotation();
        this.mMoveLimitRect = new Rect();
        this.mPositionLimitRect = new Rect();
        this.mSafeInsetRect = new Rect();
        int systemUiDimensionPixelSize = getSystemUiDimensionPixelSize(resources, "status_bar_height");
        this.mBaseStatusBarHeight = systemUiDimensionPixelSize;
        if (resources.getIdentifier("status_bar_height_landscape", "dimen", "android") > 0) {
            this.mBaseStatusBarRotatedHeight = getSystemUiDimensionPixelSize(resources, "status_bar_height_landscape");
        } else {
            this.mBaseStatusBarRotatedHeight = systemUiDimensionPixelSize;
        }
        updateViewConfiguration();
        if (hasSoftNavigationBar()) {
            this.mBaseNavigationBarHeight = getSystemUiDimensionPixelSize(resources, "navigation_bar_height");
            this.mBaseNavigationBarRotatedHeight = getSystemUiDimensionPixelSize(resources, z ? "navigation_bar_height_landscape" : "navigation_bar_width");
        } else {
            this.mBaseNavigationBarHeight = 0;
            this.mBaseNavigationBarRotatedHeight = 0;
        }
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    private boolean hasSoftNavigationBar() {
        if (Build.VERSION.SDK_INT >= 17) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mWindowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            if (displayMetrics.heightPixels > this.mMetrics.heightPixels || displayMetrics.widthPixels > this.mMetrics.widthPixels) {
                return true;
            }
            return false;
        }
        Context context = getContext();
        Resources resources = context.getResources();
        boolean hasPermanentMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean deviceHasKey = KeyCharacterMap.deviceHasKey(4);
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (identifier != 0 && resources.getBoolean(identifier)) {
            return true;
        }
        if (hasPermanentMenuKey || deviceHasKey) {
            return false;
        }
        return true;
    }

    private static int getSystemUiDimensionPixelSize(Resources resources, String str) {
        int identifier = resources.getIdentifier(str, "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        refreshLimitRect();
    }

    
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateViewConfiguration();
        refreshLimitRect();
    }

    public boolean onPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        if (this.mInitX == Integer.MIN_VALUE) {
            this.mInitX = 0;
        }
        if (this.mInitY == Integer.MIN_VALUE) {
            this.mInitY = (this.mMetrics.heightPixels - this.mStatusBarHeight) - getMeasuredHeight();
        }
        this.mParams.x = this.mInitX;
        this.mParams.y = this.mInitY;
        if (this.mMoveDirection == 3) {
            int i = this.mInitX;
            int i2 = this.mInitY;
            moveTo(i, i2, i, i2, false);
        } else {
            this.mIsInitialAnimationRunning = true;
            moveToEdge(this.mInitX, this.mInitY, this.mAnimateInitialMove);
        }
        this.mIsDraggable = true;
        updateViewLayout();
        return true;
    }

   
    public void onUpdateSystemLayout(boolean z, boolean z2, boolean z3, Rect rect) {
        updateStatusBarHeight(z, z3);
        updateTouchXOffset(z2, rect.left);
        this.mTouchYOffset = z3 ? this.mSafeInsetRect.top : 0;
        updateNavigationBarOffset(z2, z3, rect);
        refreshLimitRect();
    }

    private void updateStatusBarHeight(boolean z, boolean z2) {
        if (z) {
            this.mStatusBarHeight = 0;
            return;
        }
        if (this.mSafeInsetRect.top != 0) {
            if (z2) {
                this.mStatusBarHeight = 0;
            } else {
                this.mStatusBarHeight = this.mBaseStatusBarRotatedHeight;
            }
        } else if (z2) {
            this.mStatusBarHeight = this.mBaseStatusBarHeight;
        } else {
            this.mStatusBarHeight = this.mBaseStatusBarRotatedHeight;
        }
    }

    private void updateTouchXOffset(boolean z, int i) {
        int i2 = 0;
        if (this.mSafeInsetRect.bottom != 0) {
            this.mTouchXOffset = i;
            return;
        }
        if (!z && i > 0) {
            i2 = this.mBaseNavigationBarRotatedHeight;
        }
        this.mTouchXOffset = i2;
    }

    private void updateNavigationBarOffset(boolean z, boolean z2, Rect rect) {
        int i;
        int i2;
        int i3;
        boolean hasSoftNavigationBar = hasSoftNavigationBar();
        if (Build.VERSION.SDK_INT >= 17) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mWindowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            i2 = displayMetrics.heightPixels - rect.bottom;
            i = displayMetrics.widthPixels - this.mMetrics.widthPixels;
            i3 = this.mBaseNavigationBarHeight - i2;
        } else {
            i = 0;
            i3 = 0;
            i2 = 0;
        }
        if (!z) {
            if ((i3 == 0 || this.mBaseNavigationBarHeight != 0) && (hasSoftNavigationBar || this.mBaseNavigationBarHeight == 0)) {
                this.mNavigationBarVerticalOffset = 0;
            } else if (hasSoftNavigationBar) {
                this.mNavigationBarVerticalOffset = 0;
            } else {
                this.mNavigationBarVerticalOffset = -i2;
            }
            this.mNavigationBarHorizontalOffset = 0;
        } else if (z2) {
            if (hasSoftNavigationBar || this.mBaseNavigationBarHeight == 0) {
                this.mNavigationBarVerticalOffset = this.mBaseNavigationBarHeight;
            } else {
                this.mNavigationBarVerticalOffset = 0;
            }
            this.mNavigationBarHorizontalOffset = 0;
        } else if (this.mIsTablet) {
            this.mNavigationBarVerticalOffset = this.mBaseNavigationBarRotatedHeight;
            this.mNavigationBarHorizontalOffset = 0;
        } else {
            this.mNavigationBarVerticalOffset = 0;
            if (!hasSoftNavigationBar && this.mBaseNavigationBarRotatedHeight != 0) {
                this.mNavigationBarHorizontalOffset = 0;
            } else if (!hasSoftNavigationBar || this.mBaseNavigationBarRotatedHeight != 0) {
                this.mNavigationBarHorizontalOffset = this.mBaseNavigationBarRotatedHeight;
            } else {
                this.mNavigationBarHorizontalOffset = i;
            }
        }
    }

    private void updateViewConfiguration() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mViewConfiguration = viewConfiguration;
        this.mMoveThreshold = (float) viewConfiguration.getScaledTouchSlop();
        float scaledMaximumFlingVelocity = (float) this.mViewConfiguration.getScaledMaximumFlingVelocity();
        this.mMaximumFlingVelocity = scaledMaximumFlingVelocity;
        this.mMaximumXVelocity = scaledMaximumFlingVelocity / 9.0f;
        this.mMaximumYVelocity = scaledMaximumFlingVelocity / MAX_Y_VELOCITY_SCALE_DOWN_VALUE;
        this.mThrowMoveThreshold = scaledMaximumFlingVelocity / 9.0f;
    }

    private void refreshLimitRect() {
        cancelAnimation();
        int width = this.mPositionLimitRect.width();
        int height = this.mPositionLimitRect.height();
        this.mWindowManager.getDefaultDisplay().getMetrics(this.mMetrics);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int i = this.mMetrics.widthPixels;
        int i2 = this.mMetrics.heightPixels;
        this.mMoveLimitRect.set(-measuredWidth, (-measuredHeight) * 2, i + measuredWidth + this.mNavigationBarHorizontalOffset, i2 + measuredHeight + this.mNavigationBarVerticalOffset);
        Rect rect = this.mPositionLimitRect;
        int i3 = this.mOverMargin;
        rect.set(-i3, 0, (i - measuredWidth) + i3 + this.mNavigationBarHorizontalOffset, ((i2 - this.mStatusBarHeight) - measuredHeight) + this.mNavigationBarVerticalOffset);
        int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
        if (this.mAnimateInitialMove && this.mRotation != rotation) {
            this.mIsInitialAnimationRunning = false;
        }
        if (this.mIsInitialAnimationRunning && this.mRotation == rotation) {
            moveToEdge(this.mParams.x, this.mParams.y, true);
        } else if (this.mIsMoveAccept) {
            moveToEdge(this.mParams.x, this.mParams.y, false);
        } else {
            moveTo(this.mParams.x, this.mParams.y, Math.min(Math.max(this.mPositionLimitRect.left, (int) ((((float) (this.mParams.x * this.mPositionLimitRect.width())) / ((float) width)) + 0.5f)), this.mPositionLimitRect.right), Math.min(Math.max(this.mPositionLimitRect.top, (int) ((((float) (this.mParams.y * this.mPositionLimitRect.height())) / ((float) height)) + 0.5f)), this.mPositionLimitRect.bottom), false);
        }
        this.mRotation = rotation;
    }

    
    public void onDetachedFromWindow() {
        ValueAnimator valueAnimator = this.mMoveEdgeAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
        }
        super.onDetachedFromWindow();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        if (getVisibility() != 0 || !this.mIsDraggable || this.mIsInitialAnimationRunning) {
            return true;
        }
        this.mScreenTouchX = motionEvent.getRawX();
        this.mScreenTouchY = motionEvent.getRawY();
        int action = motionEvent.getAction();
        boolean z = false;
        if (action == 0) {
            cancelAnimation();
            this.mScreenTouchDownX = this.mScreenTouchX;
            this.mScreenTouchDownY = this.mScreenTouchY;
            this.mLocalTouchX = motionEvent.getX();
            this.mLocalTouchY = motionEvent.getY();
            this.mIsMoveAccept = false;
            setScale(SCALE_PRESSED);
            VelocityTracker velocityTracker2 = this.mVelocityTracker;
            if (velocityTracker2 == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                velocityTracker2.clear();
            }
            this.mAnimationHandler.updateTouchPosition((float) getXByTouch(), (float) getYByTouch());
            this.mAnimationHandler.removeMessages(1);
            this.mAnimationHandler.sendAnimationMessage(1);
            this.mLongPressHandler.removeMessages(0);
            this.mLongPressHandler.sendEmptyMessageDelayed(0, (long) LONG_PRESS_TIMEOUT);
            this.mTouchDownTime = motionEvent.getDownTime();
            addMovement(motionEvent);
            this.mIsInitialAnimationRunning = false;
        } else if (action == 2) {
            if (this.mIsMoveAccept) {
                this.mIsLongPressed = false;
                this.mLongPressHandler.removeMessages(0);
            }
            if (this.mTouchDownTime != motionEvent.getDownTime()) {
                return true;
            }
            if (!this.mIsMoveAccept && Math.abs(this.mScreenTouchX - this.mScreenTouchDownX) < this.mMoveThreshold && Math.abs(this.mScreenTouchY - this.mScreenTouchDownY) < this.mMoveThreshold) {
                return true;
            }
            this.mIsMoveAccept = true;
            this.mAnimationHandler.updateTouchPosition((float) getXByTouch(), (float) getYByTouch());
            addMovement(motionEvent);
        } else if (action == 1 || action == 3) {
            VelocityTracker velocityTracker3 = this.mVelocityTracker;
            if (velocityTracker3 != null) {
                velocityTracker3.computeCurrentVelocity(1000);
            }
            boolean z2 = this.mIsLongPressed;
            this.mIsLongPressed = false;
            this.mLongPressHandler.removeMessages(0);
            if (this.mTouchDownTime != motionEvent.getDownTime()) {
                return true;
            }
            this.mAnimationHandler.removeMessages(1);
            setScale(1.0f);
            if (!this.mIsMoveAccept && (velocityTracker = this.mVelocityTracker) != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            if (action != 1 || z2 || this.mIsMoveAccept) {
                z = true;
            } else {
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    getChildAt(i).performClick();
                }
            }
        }
        OnTouchListener onTouchListener = this.mOnTouchListener;
        if (onTouchListener != null) {
            onTouchListener.onTouch(this, motionEvent);
        }
        if (z && this.mAnimationHandler.getState() != 2) {
            moveToEdge(true);
            VelocityTracker velocityTracker4 = this.mVelocityTracker;
            if (velocityTracker4 != null) {
                velocityTracker4.recycle();
                this.mVelocityTracker = null;
            }
        }
        return true;
    }

    private void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.mVelocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    
    public void onLongClick() {
        this.mIsLongPressed = true;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).performLongClick();
        }
    }

    public void setVisibility(int i) {
        if (i != 0) {
            cancelLongPress();
            setScale(1.0f);
            if (this.mIsMoveAccept) {
                moveToEdge(false);
            }
            this.mAnimationHandler.removeMessages(1);
            this.mLongPressHandler.removeMessages(0);
        }
        super.setVisibility(i);
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    private void moveToEdge(boolean z) {
        moveToEdge(getXByTouch(), getYByTouch(), z);
    }

    private void moveToEdge(int i, int i2, boolean z) {
        moveTo(i, i2, getGoalPositionX(i, i2), getGoalPositionY(i, i2), z);
    }

    private void moveTo(int i, int i2, int i3, int i4, boolean z) {
        int min = Math.min(Math.max(this.mPositionLimitRect.left, i3), this.mPositionLimitRect.right);
        int min2 = Math.min(Math.max(this.mPositionLimitRect.top, i4), this.mPositionLimitRect.bottom);
        if (z) {
            if ((!this.mUsePhysics || this.mVelocityTracker == null || this.mMoveDirection == 4) ? false : true) {
                startPhysicsAnimation(min, i2);
            } else {
                startObjectAnimation(i, i2, min, min2);
            }
        } else if (!(this.mParams.x == min && this.mParams.y == min2)) {
            this.mParams.x = min;
            this.mParams.y = min2;
            updateViewLayout();
        }
        this.mLocalTouchX = 0.0f;
        this.mLocalTouchY = 0.0f;
        this.mScreenTouchDownX = 0.0f;
        this.mScreenTouchDownY = 0.0f;
        this.mIsMoveAccept = false;
    }

    private void startPhysicsAnimation(int i, int i2) {
        boolean z = true;
        boolean z2 = this.mParams.x < this.mPositionLimitRect.right && this.mParams.x > this.mPositionLimitRect.left;
        if (this.mMoveDirection != 3 || !z2) {
            startSpringAnimationX(i);
        } else {
            startFlingAnimationX(Math.min(Math.max(this.mVelocityTracker.getXVelocity(), -this.mMaximumXVelocity), this.mMaximumXVelocity));
        }
        if (this.mParams.y >= this.mPositionLimitRect.bottom || this.mParams.y <= this.mPositionLimitRect.top) {
            z = false;
        }
        float f = -Math.min(Math.max(this.mVelocityTracker.getYVelocity(), -this.mMaximumYVelocity), this.mMaximumYVelocity);
        if (z) {
            startFlingAnimationY(f);
        } else {
            startSpringAnimationY(i2, f);
        }
    }

    private void startObjectAnimation(int i, int i2, int i3, int i4) {
        if (i3 == i) {
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i2, i4});
            this.mMoveEdgeAnimator = ofInt;
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    C0814FloatingView.this.mParams.y = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    C0814FloatingView.this.updateViewLayout();
                    C0814FloatingView.this.updateInitAnimation(valueAnimator);
                }
            });
        } else {
            this.mParams.y = i4;
            ValueAnimator ofInt2 = ValueAnimator.ofInt(new int[]{i, i3});
            this.mMoveEdgeAnimator = ofInt2;
            ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    C0814FloatingView.this.mParams.x = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    C0814FloatingView.this.updateViewLayout();
                    C0814FloatingView.this.updateInitAnimation(valueAnimator);
                }
            });
        }
        this.mMoveEdgeAnimator.setDuration(MOVE_TO_EDGE_DURATION);
        this.mMoveEdgeAnimator.setInterpolator(this.mMoveEdgeInterpolator);
        this.mMoveEdgeAnimator.start();
    }

    private void startSpringAnimationX(int i) {
        SpringForce springForce = new SpringForce((float) i);
        springForce.setDampingRatio(ANIMATION_SPRING_X_DAMPING_RATIO);
        springForce.setStiffness(ANIMATION_SPRING_X_STIFFNESS);
        SpringAnimation springAnimation = new SpringAnimation(new FloatValueHolder());
        springAnimation.setStartVelocity(this.mVelocityTracker.getXVelocity());
        springAnimation.setStartValue((float) this.mParams.x);
        springAnimation.setSpring(springForce);
        springAnimation.setMinimumVisibleChange(1.0f);
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                int round = Math.round(f);
                if (C0814FloatingView.this.mParams.x != round && C0814FloatingView.this.mVelocityTracker == null) {
                    C0814FloatingView.this.mParams.x = round;
                    C0814FloatingView.this.updateViewLayout();
                }
            }
        });
        springAnimation.start();
    }

    private void startSpringAnimationY(int i, float f) {
        SpringForce springForce = new SpringForce((float) (i < this.mMetrics.heightPixels / 2 ? this.mPositionLimitRect.top : this.mPositionLimitRect.bottom));
        springForce.setDampingRatio(0.75f);
        springForce.setStiffness(200.0f);
        SpringAnimation springAnimation = new SpringAnimation(new FloatValueHolder());
        springAnimation.setStartVelocity(f);
        springAnimation.setStartValue((float) this.mParams.y);
        springAnimation.setSpring(springForce);
        springAnimation.setMinimumVisibleChange(1.0f);
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                int round = Math.round(f);
                if (C0814FloatingView.this.mParams.y != round && C0814FloatingView.this.mVelocityTracker == null) {
                    C0814FloatingView.this.mParams.y = round;
                    C0814FloatingView.this.updateViewLayout();
                }
            }
        });
        springAnimation.start();
    }

    private void startFlingAnimationX(float f) {
        FlingAnimation flingAnimation = new FlingAnimation(new FloatValueHolder());
        flingAnimation.setStartVelocity(f);
        flingAnimation.setMaxValue((float) this.mPositionLimitRect.right);
        flingAnimation.setMinValue((float) this.mPositionLimitRect.left);
        flingAnimation.setStartValue((float) this.mParams.x);
        flingAnimation.setFriction(1.7f);
        flingAnimation.setMinimumVisibleChange(1.0f);
        flingAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                int round = Math.round(f);
                if (C0814FloatingView.this.mParams.x != round && C0814FloatingView.this.mVelocityTracker == null) {
                    C0814FloatingView.this.mParams.x = round;
                    C0814FloatingView.this.updateViewLayout();
                }
            }
        });
        flingAnimation.start();
    }

    private void startFlingAnimationY(float f) {
        FlingAnimation flingAnimation = new FlingAnimation(new FloatValueHolder());
        flingAnimation.setStartVelocity(f);
        flingAnimation.setMaxValue((float) this.mPositionLimitRect.bottom);
        flingAnimation.setMinValue((float) this.mPositionLimitRect.top);
        flingAnimation.setStartValue((float) this.mParams.y);
        flingAnimation.setFriction(1.7f);
        flingAnimation.setMinimumVisibleChange(1.0f);
        flingAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                int round = Math.round(f);
                if (C0814FloatingView.this.mParams.y != round && C0814FloatingView.this.mVelocityTracker == null) {
                    C0814FloatingView.this.mParams.y = round;
                    C0814FloatingView.this.updateViewLayout();
                }
            }
        });
        flingAnimation.start();
    }

    
    public void updateViewLayout() {
        if (ViewCompat.isAttachedToWindow(this)) {
            this.mWindowManager.updateViewLayout(this, this.mParams);
        }
    }

    
    public void updateInitAnimation(ValueAnimator valueAnimator) {
        if (this.mAnimateInitialMove && valueAnimator.getDuration() <= valueAnimator.getCurrentPlayTime()) {
            this.mIsInitialAnimationRunning = false;
        }
    }

    private int getGoalPositionX(int i, int i2) {
        int i3 = this.mMoveDirection;
        boolean z = false;
        if (i3 == 0) {
            if (i > (this.mMetrics.widthPixels - getWidth()) / 2) {
                z = true;
            }
            Rect rect = this.mPositionLimitRect;
            return z ? rect.right : rect.left;
        } else if (i3 == 1) {
            return this.mPositionLimitRect.left;
        } else {
            if (i3 == 2) {
                return this.mPositionLimitRect.right;
            }
            if (i3 == 4) {
                if (Math.min(i, this.mPositionLimitRect.width() - i) >= Math.min(i2, this.mPositionLimitRect.height() - i2)) {
                    return i;
                }
                if (i > (this.mMetrics.widthPixels - getWidth()) / 2) {
                    z = true;
                }
                Rect rect2 = this.mPositionLimitRect;
                return z ? rect2.right : rect2.left;
            } else if (i3 != 5) {
                return i;
            } else {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                if (velocityTracker != null && velocityTracker.getXVelocity() > this.mThrowMoveThreshold) {
                    return this.mPositionLimitRect.right;
                }
                VelocityTracker velocityTracker2 = this.mVelocityTracker;
                if (velocityTracker2 != null && velocityTracker2.getXVelocity() < (-this.mThrowMoveThreshold)) {
                    return this.mPositionLimitRect.left;
                }
                if (i > (this.mMetrics.widthPixels - getWidth()) / 2) {
                    z = true;
                }
                Rect rect3 = this.mPositionLimitRect;
                return z ? rect3.right : rect3.left;
            }
        }
    }

    private int getGoalPositionY(int i, int i2) {
        if (this.mMoveDirection != 4 || Math.min(i, this.mPositionLimitRect.width() - i) < Math.min(i2, this.mPositionLimitRect.height() - i2)) {
            return i2;
        }
        return i2 < (this.mMetrics.heightPixels - getHeight()) / 2 ? this.mPositionLimitRect.top : this.mPositionLimitRect.bottom;
    }

    private void cancelAnimation() {
        ValueAnimator valueAnimator = this.mMoveEdgeAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mMoveEdgeAnimator.cancel();
            this.mMoveEdgeAnimator = null;
        }
    }

    private void setScale(float f) {
        if (Build.VERSION.SDK_INT <= 19) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                childAt.setScaleX(f);
                childAt.setScaleY(f);
            }
            return;
        }
        setScaleX(f);
        setScaleY(f);
    }

   
    public void setDraggable(boolean z) {
        this.mIsDraggable = z;
    }

   
    public void setShape(float f) {
        this.mShape = f;
    }

   
    public float getShape() {
        return this.mShape;
    }

   
    public void setOverMargin(int i) {
        this.mOverMargin = i;
    }

   
    public void setMoveDirection(int i) {
        this.mMoveDirection = i;
    }

   
    public void usePhysics(boolean z) {
        this.mUsePhysics = z && Build.VERSION.SDK_INT >= 16;
    }

   
    public void setInitCoords(int i, int i2) {
        this.mInitX = i;
        this.mInitY = i2;
    }

   
    public void setAnimateInitialMove(boolean z) {
        this.mAnimateInitialMove = z;
    }

   
    public void getWindowDrawingRect(Rect rect) {
        int xByTouch = getXByTouch();
        int yByTouch = getYByTouch();
        rect.set(xByTouch, yByTouch, getWidth() + xByTouch, getHeight() + yByTouch);
    }

   
    public WindowManager.LayoutParams getWindowLayoutParams() {
        return this.mParams;
    }

    private int getXByTouch() {
        return (int) ((this.mScreenTouchX - this.mLocalTouchX) - ((float) this.mTouchXOffset));
    }

    private int getYByTouch() {
        return (int) (((float) (this.mMetrics.heightPixels + this.mNavigationBarVerticalOffset)) - (((this.mScreenTouchY - this.mLocalTouchY) + ((float) getHeight())) - ((float) this.mTouchYOffset)));
    }

   
    public void setNormal() {
        this.mAnimationHandler.setState(0);
        this.mAnimationHandler.updateTouchPosition((float) getXByTouch(), (float) getYByTouch());
    }

   
    public void setIntersecting(int i, int i2) {
        this.mAnimationHandler.setState(1);
        this.mAnimationHandler.updateTargetPosition((float) i, (float) i2);
    }

   
    public void setFinishing() {
        this.mAnimationHandler.setState(2);
        this.mIsMoveAccept = false;
        setVisibility(View.GONE);
    }

   
    public int getState() {
        return this.mAnimationHandler.getState();
    }

   
    public void setSafeInsetRect(Rect rect) {
        this.mSafeInsetRect.set(rect);
    }

    /* renamed from: com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView.FloatingView$FloatingAnimationHandler */
    static class FloatingAnimationHandler extends Handler {
        private static final int ANIMATION_IN_TOUCH = 1;
        private static final int ANIMATION_NONE = 0;
        private static final long ANIMATION_REFRESH_TIME_MILLIS = 10;
        private static final long CAPTURE_DURATION_MILLIS = 300;
        private static final int TYPE_FIRST = 1;
        private static final int TYPE_UPDATE = 2;
        private final WeakReference<C0814FloatingView> mFloatingView;
        private boolean mIsChangeState;
        private long mStartTime;
        private float mStartX;
        private float mStartY;
        private int mStartedCode = 0;
        private int mState = 0;
        private float mTargetPositionX;
        private float mTargetPositionY;
        private float mTouchPositionX;
        private float mTouchPositionY;

        FloatingAnimationHandler(C0814FloatingView floatingView) {
            this.mFloatingView = new WeakReference<>(floatingView);
        }

        public void handleMessage(Message message) {
            C0814FloatingView floatingView = (C0814FloatingView) this.mFloatingView.get();
            if (floatingView == null) {
                removeMessages(1);
                return;
            }
            int i = message.what;
            int i2 = message.arg1;
            WindowManager.LayoutParams access$000 = floatingView.mParams;
            boolean z = this.mIsChangeState;
            if (z || i2 == 1) {
                this.mStartTime = z ? SystemClock.uptimeMillis() : 0;
                this.mStartX = (float) access$000.x;
                this.mStartY = (float) access$000.y;
                this.mStartedCode = i;
                this.mIsChangeState = false;
            }
            float min = Math.min(((float) (SystemClock.uptimeMillis() - this.mStartTime)) / 300.0f, 1.0f);
            int i3 = this.mState;
            if (i3 == 0) {
                float calcAnimationPosition = calcAnimationPosition(min);
                Rect access$400 = floatingView.mMoveLimitRect;
                float f = this.mStartX;
                access$000.x = (int) (f + ((((float) Math.min(Math.max(access$400.left, (int) this.mTouchPositionX), access$400.right)) - f) * calcAnimationPosition));
                float f2 = this.mStartY;
                access$000.y = (int) (f2 + ((((float) Math.min(Math.max(access$400.top, (int) this.mTouchPositionY), access$400.bottom)) - f2) * calcAnimationPosition));
                floatingView.updateViewLayout();
                sendMessageAtTime(newMessage(i, 2), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
            } else if (i3 == 1) {
                float calcAnimationPosition2 = calcAnimationPosition(min);
                float width = this.mTargetPositionX - ((float) (floatingView.getWidth() / 2));
                float height = this.mTargetPositionY - ((float) (floatingView.getHeight() / 2));
                float f3 = this.mStartX;
                access$000.x = (int) (f3 + ((width - f3) * calcAnimationPosition2));
                float f4 = this.mStartY;
                access$000.y = (int) (f4 + ((height - f4) * calcAnimationPosition2));
                floatingView.updateViewLayout();
                sendMessageAtTime(newMessage(i, 2), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
            }
        }

        private static float calcAnimationPosition(float f) {
            double d;
            double d2;
            double d3 = (double) f;
            if (d3 <= 0.4d) {
                d = 0.55d;
                d2 = Math.sin((d3 * 8.0564d) - 1.5707963267948966d) * 0.55d;
            } else {
                d2 = (Math.pow((d3 * 0.417d) - 0.341d, 2.0d) * 4.0d) - (Math.pow(0.07599999999999996d, 2.0d) * 4.0d);
                d = 1.0d;
            }
            return (float) (d2 + d);
        }

       
        public void sendAnimationMessageDelayed(int i, long j) {
            sendMessageAtTime(newMessage(i, 1), SystemClock.uptimeMillis() + j);
        }

       
        public void sendAnimationMessage(int i) {
            sendMessage(newMessage(i, 1));
        }

        private static Message newMessage(int i, int i2) {
            Message obtain = Message.obtain();
            obtain.what = i;
            obtain.arg1 = i2;
            return obtain;
        }

       
        public void updateTouchPosition(float f, float f2) {
            this.mTouchPositionX = f;
            this.mTouchPositionY = f2;
        }

       
        public void updateTargetPosition(float f, float f2) {
            this.mTargetPositionX = f;
            this.mTargetPositionY = f2;
        }

       
        public void setState(int i) {
            if (this.mState != i) {
                this.mIsChangeState = true;
            }
            this.mState = i;
        }

       
        public int getState() {
            return this.mState;
        }
    }

    /* renamed from: com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView.FloatingView$LongPressHandler */
    static class LongPressHandler extends Handler {
        private static final int LONG_PRESSED = 0;
        private final WeakReference<C0814FloatingView> mFloatingView;

        LongPressHandler(C0814FloatingView floatingView) {
            this.mFloatingView = new WeakReference<>(floatingView);
        }

        public void handleMessage(Message message) {
            C0814FloatingView floatingView = (C0814FloatingView) this.mFloatingView.get();
            if (floatingView == null) {
                removeMessages(0);
            } else {
                floatingView.onLongClick();
            }
        }
    }
}
