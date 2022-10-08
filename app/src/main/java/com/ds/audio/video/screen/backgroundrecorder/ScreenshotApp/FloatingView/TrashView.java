package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.FloatingView;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Keep;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
@Keep
class TrashView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {
    static final int ANIMATION_CLOSE = 2;
    static final int ANIMATION_FORCE_CLOSE = 3;
    static final int ANIMATION_NONE = 0;
    static final int ANIMATION_OPEN = 1;
    private static final int BACKGROUND_HEIGHT = 164;
    private static final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int OVERLAY_TYPE;
    private static final float TARGET_CAPTURE_HORIZONTAL_REGION = 30.0f;
    private static final float TARGET_CAPTURE_VERTICAL_REGION = 4.0f;
    private static final long TRASH_ICON_SCALE_DURATION_MILLIS = 200;
    private int mActionTrashIconBaseHeight;
    private int mActionTrashIconBaseWidth;
    private float mActionTrashIconMaxScale;
    private final ImageView mActionTrashIconView;
    private final AnimationHandler mAnimationHandler = new AnimationHandler(this);
    
    public final FrameLayout mBackgroundView;
    private ObjectAnimator mEnterScaleAnimator;
    private ObjectAnimator mExitScaleAnimator;
    private final ImageView mFixedTrashIconView;
    private boolean mIsEnabled = true;
    
    public final DisplayMetrics mMetrics;
    
    public final WindowManager.LayoutParams mParams;
    
    public final ViewGroup mRootView;
    
    public final FrameLayout mTrashIconRootView;
    
    public TrashViewListener mTrashViewListener;
    private final WindowManager mWindowManager;

    @Retention(RetentionPolicy.SOURCE)
    @interface AnimationState {
    }

    static {
        if (Build.VERSION.SDK_INT <= 25) {
            OVERLAY_TYPE = 2007;
        } else {
            OVERLAY_TYPE = 2038;
        }
    }

    TrashView(Context context) {
        super(context);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        this.mWindowManager = windowManager;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mMetrics = displayMetrics;
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.mParams = layoutParams;
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.type = OVERLAY_TYPE;
        layoutParams.flags = 56;
        layoutParams.format = -3;
        layoutParams.gravity = 83;
        FrameLayout frameLayout = new FrameLayout(context);
        this.mRootView = frameLayout;
        frameLayout.setClipChildren(false);
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.mTrashIconRootView = frameLayout2;
        frameLayout2.setClipChildren(false);
        ImageView imageView = new ImageView(context);
        this.mFixedTrashIconView = imageView;
        ImageView imageView2 = new ImageView(context);
        this.mActionTrashIconView = imageView2;
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.mBackgroundView = frameLayout3;
        frameLayout3.setAlpha(0.0f);
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0, 1342177280});
        if (Build.VERSION.SDK_INT < 16) {
            frameLayout3.setBackgroundDrawable(gradientDrawable);
        } else {
            frameLayout3.setBackground(gradientDrawable);
        }
        LayoutParams layoutParams2 = new LayoutParams(-1, (int) (displayMetrics.density * 164.0f));
        layoutParams2.gravity = 80;
        frameLayout.addView(frameLayout3, layoutParams2);
        LayoutParams layoutParams3 = new LayoutParams(-2, -2);
        layoutParams3.gravity = 17;
        frameLayout2.addView(imageView2, layoutParams3);
        LayoutParams layoutParams4 = new LayoutParams(-2, -2);
        layoutParams4.gravity = 17;
        frameLayout2.addView(imageView, layoutParams4);
        LayoutParams layoutParams5 = new LayoutParams(-2, -2);
        layoutParams5.gravity = 81;
        frameLayout.addView(frameLayout2, layoutParams5);
        addView(frameLayout);
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        updateViewLayout();
    }

    
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateViewLayout();
    }

    public boolean onPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        FrameLayout frameLayout = this.mTrashIconRootView;
        frameLayout.setTranslationY((float) frameLayout.getMeasuredHeight());
        return true;
    }

    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTrashViewListener.onUpdateActionTrashIcon();
    }

    private void updateViewLayout() {
        this.mWindowManager.getDefaultDisplay().getMetrics(this.mMetrics);
        this.mParams.x = (this.mMetrics.widthPixels - getWidth()) / 2;
        this.mParams.y = 0;
        this.mTrashViewListener.onUpdateActionTrashIcon();
        this.mAnimationHandler.onUpdateViewLayout();
        this.mWindowManager.updateViewLayout(this, this.mParams);
    }

   
    public void dismiss() {
        this.mAnimationHandler.removeMessages(1);
        this.mAnimationHandler.removeMessages(2);
        this.mAnimationHandler.sendAnimationMessage(3);
        setScaleTrashIconImmediately(false);
    }

   
    public void getWindowDrawingRect(Rect rect) {
        ImageView imageView = hasActionTrashIcon() ? this.mActionTrashIconView : this.mFixedTrashIconView;
        float paddingLeft = (float) imageView.getPaddingLeft();
        float paddingTop = (float) imageView.getPaddingTop();
        float width = (((float) imageView.getWidth()) - paddingLeft) - ((float) imageView.getPaddingRight());
        float height = (((float) imageView.getHeight()) - paddingTop) - ((float) imageView.getPaddingBottom());
        float x = this.mTrashIconRootView.getX() + paddingLeft;
        rect.set((int) (x - (this.mMetrics.density * TARGET_CAPTURE_HORIZONTAL_REGION)), -this.mRootView.getHeight(), (int) (x + width + (this.mMetrics.density * TARGET_CAPTURE_HORIZONTAL_REGION)), (int) ((((((float) this.mRootView.getHeight()) - this.mTrashIconRootView.getY()) - paddingTop) - height) + height + (this.mMetrics.density * TARGET_CAPTURE_VERTICAL_REGION)));
    }

   
    public void updateActionTrashIcon(float f, float f2, float f3) {
        if (hasActionTrashIcon()) {
            float unused = this.mAnimationHandler.mTargetWidth = f;
            float unused2 = this.mAnimationHandler.mTargetHeight = f2;
            this.mActionTrashIconMaxScale = Math.max((f / ((float) this.mActionTrashIconBaseWidth)) * f3, (f2 / ((float) this.mActionTrashIconBaseHeight)) * f3);
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mActionTrashIconView, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(ImageView.SCALE_X, new float[]{this.mActionTrashIconMaxScale}), PropertyValuesHolder.ofFloat(ImageView.SCALE_Y, new float[]{this.mActionTrashIconMaxScale})});
            this.mEnterScaleAnimator = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setInterpolator(new OvershootInterpolator());
            this.mEnterScaleAnimator.setDuration(TRASH_ICON_SCALE_DURATION_MILLIS);
            ObjectAnimator ofPropertyValuesHolder2 = ObjectAnimator.ofPropertyValuesHolder(this.mActionTrashIconView, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(ImageView.SCALE_X, new float[]{1.0f}), PropertyValuesHolder.ofFloat(ImageView.SCALE_Y, new float[]{1.0f})});
            this.mExitScaleAnimator = ofPropertyValuesHolder2;
            ofPropertyValuesHolder2.setInterpolator(new OvershootInterpolator());
            this.mExitScaleAnimator.setDuration(TRASH_ICON_SCALE_DURATION_MILLIS);
        }
    }

   
    public float getTrashIconCenterX() {
        ImageView imageView = hasActionTrashIcon() ? this.mActionTrashIconView : this.mFixedTrashIconView;
        float paddingLeft = (float) imageView.getPaddingLeft();
        return this.mTrashIconRootView.getX() + paddingLeft + (((((float) imageView.getWidth()) - paddingLeft) - ((float) imageView.getPaddingRight())) / 2.0f);
    }

   
    public float getTrashIconCenterY() {
        ImageView imageView = hasActionTrashIcon() ? this.mActionTrashIconView : this.mFixedTrashIconView;
        float height = (float) imageView.getHeight();
        float paddingBottom = (float) imageView.getPaddingBottom();
        return ((((float) this.mRootView.getHeight()) - this.mTrashIconRootView.getY()) - height) + paddingBottom + (((height - ((float) imageView.getPaddingTop())) - paddingBottom) / 2.0f);
    }

    private boolean hasActionTrashIcon() {
        return (this.mActionTrashIconBaseWidth == 0 || this.mActionTrashIconBaseHeight == 0) ? false : true;
    }

   
    public void setFixedTrashIconImage(int i) {
        this.mFixedTrashIconView.setImageResource(i);
    }

   
    public void setActionTrashIconImage(int i) {
        this.mActionTrashIconView.setImageResource(i);
        Drawable drawable = this.mActionTrashIconView.getDrawable();
        if (drawable != null) {
            this.mActionTrashIconBaseWidth = drawable.getIntrinsicWidth();
            this.mActionTrashIconBaseHeight = drawable.getIntrinsicHeight();
        }
    }

   
    public void setFixedTrashIconImage(Drawable drawable) {
        this.mFixedTrashIconView.setImageDrawable(drawable);
    }

   
    public void setActionTrashIconImage(Drawable drawable) {
        this.mActionTrashIconView.setImageDrawable(drawable);
        if (drawable != null) {
            this.mActionTrashIconBaseWidth = drawable.getIntrinsicWidth();
            this.mActionTrashIconBaseHeight = drawable.getIntrinsicHeight();
        }
    }

    private void setScaleTrashIconImmediately(boolean z) {
        cancelScaleTrashAnimation();
        float f = 1.0f;
        this.mActionTrashIconView.setScaleX(z ? this.mActionTrashIconMaxScale : 1.0f);
        ImageView imageView = this.mActionTrashIconView;
        if (z) {
            f = this.mActionTrashIconMaxScale;
        }
        imageView.setScaleY(f);
    }

   
    public void setScaleTrashIcon(boolean z) {
        if (hasActionTrashIcon()) {
            cancelScaleTrashAnimation();
            if (z) {
                this.mEnterScaleAnimator.start();
            } else {
                this.mExitScaleAnimator.start();
            }
        }
    }

   
    public void setTrashEnabled(boolean z) {
        if (this.mIsEnabled != z) {
            this.mIsEnabled = z;
            if (!z) {
                dismiss();
            }
        }
    }

   
    public boolean isTrashEnabled() {
        return this.mIsEnabled;
    }

    private void cancelScaleTrashAnimation() {
        ObjectAnimator objectAnimator = this.mEnterScaleAnimator;
        if (objectAnimator != null && objectAnimator.isStarted()) {
            this.mEnterScaleAnimator.cancel();
        }
        ObjectAnimator objectAnimator2 = this.mExitScaleAnimator;
        if (objectAnimator2 != null && objectAnimator2.isStarted()) {
            this.mExitScaleAnimator.cancel();
        }
    }

   
    public void setTrashViewListener(TrashViewListener trashViewListener) {
        this.mTrashViewListener = trashViewListener;
    }

   
    public WindowManager.LayoutParams getWindowLayoutParams() {
        return this.mParams;
    }

   
    public void onTouchFloatingView(MotionEvent motionEvent, float f, float f2) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mAnimationHandler.updateTargetPosition(f, f2);
            this.mAnimationHandler.removeMessages(2);
            this.mAnimationHandler.sendAnimationMessageDelayed(1, (long) LONG_PRESS_TIMEOUT);
        } else if (action == 2) {
            this.mAnimationHandler.updateTargetPosition(f, f2);
            if (!this.mAnimationHandler.isAnimationStarted(1)) {
                this.mAnimationHandler.removeMessages(1);
                this.mAnimationHandler.sendAnimationMessage(1);
            }
        } else if (action == 1 || action == 3) {
            this.mAnimationHandler.removeMessages(1);
            this.mAnimationHandler.sendAnimationMessage(2);
        }
    }

    static class AnimationHandler extends Handler {
        private static final long ANIMATION_REFRESH_TIME_MILLIS = 10;
        private static final long BACKGROUND_DURATION_MILLIS = 200;
        private static final float MAX_ALPHA = 1.0f;
        private static final float MIN_ALPHA = 0.0f;
        private static final float OVERSHOOT_TENSION = 1.0f;
        private static final long TRASH_CLOSE_DURATION_MILLIS = 200;
        private static final int TRASH_MOVE_LIMIT_OFFSET_X = 22;
        private static final int TRASH_MOVE_LIMIT_TOP_OFFSET = -4;
        private static final long TRASH_OPEN_DURATION_MILLIS = 400;
        private static final long TRASH_OPEN_START_DELAY_MILLIS = 200;
        private static final int TYPE_FIRST = 1;
        private static final int TYPE_UPDATE = 2;
        private float mMoveStickyYRange;
        private final OvershootInterpolator mOvershootInterpolator = new OvershootInterpolator(1.0f);
        private float mStartAlpha;
        private long mStartTime;
        private float mStartTransitionY;
        private int mStartedCode = 0;
        
        public float mTargetHeight;
        private float mTargetPositionX;
        private float mTargetPositionY;
        
        public float mTargetWidth;
        private final Rect mTrashIconLimitPosition = new Rect();
        private final WeakReference<TrashView> mTrashView;

        AnimationHandler(TrashView trashView) {
            this.mTrashView = new WeakReference<>(trashView);
        }

        public void handleMessage(Message message) {
            TrashView trashView = (TrashView) this.mTrashView.get();
            if (trashView == null) {
                removeMessages(1);
                removeMessages(2);
                removeMessages(3);
            } else if (trashView.isTrashEnabled()) {
                int i = message.what;
                int i2 = message.arg1;
                FrameLayout access$200 = trashView.mBackgroundView;
                FrameLayout access$300 = trashView.mTrashIconRootView;
                TrashViewListener access$400 = trashView.mTrashViewListener;
                float f = (float) trashView.mMetrics.widthPixels;
                float f2 = (float) trashView.mParams.x;
                if (i2 == 1) {
                    this.mStartTime = SystemClock.uptimeMillis();
                    this.mStartAlpha = access$200.getAlpha();
                    this.mStartTransitionY = access$300.getTranslationY();
                    this.mStartedCode = i;
                    if (access$400 != null) {
                        access$400.onTrashAnimationStarted(i);
                    }
                }
                float uptimeMillis = (float) (SystemClock.uptimeMillis() - this.mStartTime);
                if (i == 1) {
                    if (access$200.getAlpha() < 1.0f) {
                        access$200.setAlpha(Math.min(this.mStartAlpha + Math.min(uptimeMillis / 200.0f, 1.0f), 1.0f));
                    }
                    if (uptimeMillis >= 200.0f) {
                        float f3 = this.mTargetPositionX;
                        float f4 = this.mTargetWidth;
                        float width = f2 + (((f3 + f4) / (f + f4)) * ((float) this.mTrashIconLimitPosition.width())) + ((float) this.mTrashIconLimitPosition.left);
                        float f5 = this.mTargetPositionY;
                        float f6 = this.mTargetHeight;
                        float min = ((this.mMoveStickyYRange * Math.min(((f5 + f6) * 2.0f) / (((float) trashView.mMetrics.heightPixels) + f6), 1.0f)) + ((float) this.mTrashIconLimitPosition.height())) - this.mMoveStickyYRange;
                        float min2 = Math.min((uptimeMillis - 200.0f) / 400.0f, 1.0f);
                        access$300.setTranslationX(width);
                        access$300.setTranslationY(((float) this.mTrashIconLimitPosition.bottom) - (min * this.mOvershootInterpolator.getInterpolation(min2)));
                        if (Build.VERSION.SDK_INT <= 17) {
                            clearClippedChildren(trashView.mRootView);
                            clearClippedChildren(trashView.mTrashIconRootView);
                        }
                    }
                    sendMessageAtTime(newMessage(i, 2), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
                } else if (i == 2) {
                    float f7 = uptimeMillis / 200.0f;
                    float min3 = Math.min(f7, 1.0f);
                    access$200.setAlpha(Math.max(this.mStartAlpha - min3, 0.0f));
                    float min4 = Math.min(f7, 1.0f);
                    if (min3 < 1.0f || min4 < 1.0f) {
                        access$300.setTranslationY(this.mStartTransitionY + (((float) this.mTrashIconLimitPosition.height()) * min4));
                        sendMessageAtTime(newMessage(i, 2), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
                        return;
                    }
                    access$300.setTranslationY((float) this.mTrashIconLimitPosition.bottom);
                    this.mStartedCode = 0;
                    if (access$400 != null) {
                        access$400.onTrashAnimationEnd(2);
                    }
                } else if (i == 3) {
                    access$200.setAlpha(0.0f);
                    access$300.setTranslationY((float) this.mTrashIconLimitPosition.bottom);
                    this.mStartedCode = 0;
                    if (access$400 != null) {
                        access$400.onTrashAnimationEnd(3);
                    }
                }
            }
        }

        private static void clearClippedChildren(ViewGroup viewGroup) {
            viewGroup.setClipChildren(true);
            viewGroup.invalidate();
            viewGroup.setClipChildren(false);
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

       
        public boolean isAnimationStarted(int i) {
            return this.mStartedCode == i;
        }

       
        public void updateTargetPosition(float f, float f2) {
            this.mTargetPositionX = f;
            this.mTargetPositionY = f2;
        }

       
        public void onUpdateViewLayout() {
            TrashView trashView = (TrashView) this.mTrashView.get();
            if (trashView != null) {
                float f = trashView.mMetrics.density;
                float measuredHeight = (float) trashView.mBackgroundView.getMeasuredHeight();
                float f2 = 22.0f * f;
                int measuredHeight2 = trashView.mTrashIconRootView.getMeasuredHeight();
                this.mTrashIconLimitPosition.set((int) (-f2), (int) (((((float) measuredHeight2) - measuredHeight) / 2.0f) - (f * -4.0f)), (int) f2, measuredHeight2);
                this.mMoveStickyYRange = measuredHeight * 0.2f;
            }
        }
    }
}
