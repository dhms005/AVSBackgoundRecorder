package com.raed.drawingview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.raed.drawingview.brushes.BrushSettings;
import com.raed.drawingview.brushes.Brushes;


public class DrawingView extends View {
    private static final float MAX_SCALE = 5.0f;
    private static final float MIN_SCALE = 0.1f;
    
    public ActionStack mActionStack;
    private Bitmap mBGBitmap;
    private int mBGColor;
    private Brushes mBrushes;
    
    public Canvas mCanvas;
    
    public boolean mCleared;
    
    public Bitmap mDrawingBitmap;
    private DrawingPerformer mDrawingPerformer;
    
    public float mDrawingTranslationX;
    
    public float mDrawingTranslationY;
    
    public float[] mLastX;
    
    public float[] mLastY;
    
    public OnDrawListener mOnDrawListener;
    private int mPointerId;
    
    public float mScaleFactor;
    private ScaleGestureDetector mScaleGestureDetector;
    private Paint mSrcPaint;
    private boolean mZoomMode;
    private boolean translateAction;

    public interface OnDrawListener {
        void onDraw();
    }

    public DrawingView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mZoomMode = false;
        this.mDrawingTranslationX = 0.0f;
        this.mDrawingTranslationY = 0.0f;
        this.mScaleFactor = 1.0f;
        this.mLastX = new float[2];
        this.mLastY = new float[2];
        this.mCleared = true;
        this.mSrcPaint = new Paint() {
            {
                setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            }
        };
        this.mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                float xCenter = (DrawingView.this.mLastX[0] + DrawingView.this.mLastX[1]) / 2.0f;
                float yCenter = (DrawingView.this.mLastY[0] + DrawingView.this.mLastY[1]) / 2.0f;
                float xd = xCenter - DrawingView.this.mDrawingTranslationX;
                float yd = yCenter - DrawingView.this.mDrawingTranslationY;
                DrawingView drawingView = DrawingView.this;
                float unused = drawingView.mScaleFactor = drawingView.mScaleFactor * detector.getScaleFactor();
                if (DrawingView.this.mScaleFactor == DrawingView.MAX_SCALE || DrawingView.this.mScaleFactor == 0.1f) {
                    return true;
                }
                float unused2 = DrawingView.this.mDrawingTranslationX = xCenter - (detector.getScaleFactor() * xd);
                float unused3 = DrawingView.this.mDrawingTranslationY = yCenter - (detector.getScaleFactor() * yd);
                DrawingView.this.checkBounds();
                DrawingView.this.invalidate();
                return true;
            }
        });
        this.translateAction = true;
        this.mBrushes = new Brushes(context.getResources());
        if (attrs != null) {
            initializeAttributes(attrs);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mDrawingBitmap == null && w != 0 && h != 0) {
            initializeDrawingBitmap((w - getPaddingStart()) - getPaddingEnd(), (h - getPaddingTop()) - getPaddingBottom());
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipRect(getPaddingStart(), getPaddingTop(), canvas.getWidth() - getPaddingRight(), canvas.getHeight() - getPaddingBottom());
        canvas.translate(((float) getPaddingStart()) + this.mDrawingTranslationX, ((float) getPaddingTop()) + this.mDrawingTranslationY);
        float f = this.mScaleFactor;
        canvas.scale(f, f);
        canvas.clipRect(0, 0, this.mDrawingBitmap.getWidth(), this.mDrawingBitmap.getHeight());
        canvas.drawColor(this.mBGColor);
        Bitmap bitmap = this.mBGBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        }
        if (this.mDrawingPerformer.isDrawing()) {
            this.mDrawingPerformer.draw(canvas, this.mDrawingBitmap);
        } else {
            canvas.drawBitmap(this.mDrawingBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minDimension = (int) (getResources().getDisplayMetrics().density * 250.0f);
        setMeasuredDimension(resolveSize(getPaddingStart() + minDimension + getPaddingEnd(), widthMeasureSpec), resolveSize(getPaddingTop() + minDimension + getPaddingBottom(), heightMeasureSpec));
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.mZoomMode) {
            return handleZoomAndTransEvent(event);
        }
        if (event.getPointerCount() > 1) {
            return false;
        }
        event.setLocation((event.getX() - this.mDrawingTranslationX) / this.mScaleFactor, (event.getY() - this.mDrawingTranslationY) / this.mScaleFactor);
        this.mDrawingPerformer.onTouch(event);
        invalidate();
        return true;
    }

    public boolean handleZoomAndTransEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getActionMasked() == 6 && event.getPointerCount() == 1) {
            return false;
        }
        if (event.getPointerCount() <= 1) {
            if (this.translateAction) {
                switch (event.getActionMasked()) {
                    case 0:
                        this.mPointerId = event.getPointerId(0);
                        break;
                    case 2:
                    case 3:
                        int pointerIndex = event.findPointerIndex(this.mPointerId);
                        if (pointerIndex != -1) {
                            this.mDrawingTranslationX += event.getX(pointerIndex) - this.mLastX[0];
                            this.mDrawingTranslationY += event.getY(pointerIndex) - this.mLastY[0];
                            break;
                        }
                        break;
                }
            }
        } else {
            this.translateAction = false;
            this.mScaleGestureDetector.onTouchEvent(event);
        }
        if (event.getActionMasked() == 1) {
            this.translateAction = true;
        }
        this.mLastX[0] = event.getX(0);
        this.mLastY[0] = event.getY(0);
        if (event.getPointerCount() > 1) {
            this.mLastX[1] = event.getX(1);
            this.mLastY[1] = event.getY(1);
        }
        checkBounds();
        invalidate();
        return true;
    }

    public Bitmap exportDrawing() {
        Bitmap bitmap = Bitmap.createBitmap(this.mDrawingBitmap.getWidth(), this.mDrawingBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(this.mBGColor);
        Bitmap bitmap2 = this.mBGBitmap;
        if (bitmap2 != null) {
            canvas.drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
        }
        canvas.drawBitmap(this.mDrawingBitmap, 0.0f, 0.0f, (Paint) null);
        return bitmap;
    }

    public Bitmap exportDrawingWithoutBackground() {
        return this.mDrawingBitmap;
    }

    public void setDrawingBackground(int color) {
        this.mBGColor = color;
        invalidate();
    }

    public void setUndoAndRedoEnable(boolean enabled) {
        if (enabled) {
            this.mActionStack = new ActionStack();
        } else {
            this.mActionStack = null;
        }
    }

    public void setBackgroundImage(Bitmap bitmap) {
        privateSetBGBitmap(bitmap);
        initializeDrawingBitmap(this.mBGBitmap.getWidth(), this.mBGBitmap.getHeight());
        if (this.mActionStack != null) {
            this.mActionStack = new ActionStack();
        }
        invalidate();
    }

    public int getDrawingBackground() {
        return this.mBGColor;
    }

    public void resetZoom() {
        float targetSF = calcAppropriateScaleFactor(this.mDrawingBitmap.getWidth(), this.mDrawingBitmap.getHeight());
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(this, "scaleFactor", new float[]{this.mScaleFactor, targetSF});
        ObjectAnimator xTranslationAnimator = ObjectAnimator.ofFloat(this, "drawingTranslationX", new float[]{this.mDrawingTranslationX, (((float) getWidth()) - (((float) this.mDrawingBitmap.getWidth()) * targetSF)) / 2.0f});
        ObjectAnimator yTranslationAnimator = ObjectAnimator.ofFloat(this, "drawingTranslationY", new float[]{this.mDrawingTranslationY, (((float) getHeight()) - (((float) this.mDrawingBitmap.getHeight()) * targetSF)) / 2.0f});
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{scaleAnimator, xTranslationAnimator, yTranslationAnimator});
        animatorSet.start();
    }

    public boolean clear() {
        if (this.mCleared) {
            return false;
        }
        Rect rect = new Rect(0, 0, this.mDrawingBitmap.getWidth(), this.mDrawingBitmap.getHeight());
        if (this.mActionStack != null) {
            this.mActionStack.addAction(new DrawingAction(Bitmap.createBitmap(this.mDrawingBitmap), rect));
        }
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
        this.mCleared = true;
        return true;
    }

    public boolean isCleared() {
        return this.mCleared;
    }

   
    public Brushes getBrushes() {
        return this.mBrushes;
    }

    public boolean undo() {
        ActionStack actionStack = this.mActionStack;
        if (actionStack == null) {
            throw new IllegalStateException("Undo functionality is disable you can enable it by calling setUndoAndRedoEnable(true)");
        } else if (actionStack.isUndoStackEmpty() || this.mDrawingPerformer.isDrawing()) {
            return false;
        } else {
            DrawingAction previousAction = this.mActionStack.previous();
            this.mActionStack.addActionToRedoStack(getOppositeAction(previousAction));
            performAction(previousAction);
            return true;
        }
    }

    public boolean redo() {
        ActionStack actionStack = this.mActionStack;
        if (actionStack == null) {
            throw new IllegalStateException("Redo functionality is disable you can enable it by calling setUndoAndRedoEnable(true)");
        } else if (actionStack.isRedoStackEmpty() || this.mDrawingPerformer.isDrawing()) {
            return false;
        } else {
            DrawingAction nextAction = this.mActionStack.next();
            this.mActionStack.addActionToUndoStack(getOppositeAction(nextAction));
            performAction(nextAction);
            return true;
        }
    }

    public boolean isUndoStackEmpty() {
        ActionStack actionStack = this.mActionStack;
        if (actionStack != null) {
            return actionStack.isUndoStackEmpty();
        }
        throw new IllegalStateException("Undo functionality is disable you can enable it by calling setUndoAndRedoEnable(true)");
    }

    public boolean isRedoStackEmpty() {
        ActionStack actionStack = this.mActionStack;
        if (actionStack != null) {
            return actionStack.isRedoStackEmpty();
        }
        throw new IllegalStateException("Undo functionality is disable you can enable it by calling setUndoAndRedoEnable(true)");
    }

    public BrushSettings getBrushSettings() {
        return this.mBrushes.getBrushSettings();
    }

    public boolean enterZoomMode() {
        if (this.mDrawingPerformer.isDrawing()) {
            return false;
        }
        this.mZoomMode = true;
        return true;
    }

    public void exitZoomMode() {
        this.mZoomMode = false;
    }

    public boolean isInZoomMode() {
        return this.mZoomMode;
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        this.mOnDrawListener = onDrawListener;
    }

    public float getDrawingTranslationX() {
        return this.mDrawingTranslationX;
    }

    public void setDrawingTranslationX(float drawingTranslationX) {
        this.mDrawingTranslationX = drawingTranslationX;
        invalidate();
    }

    public float getDrawingTranslationY() {
        return this.mDrawingTranslationY;
    }

    public void setDrawingTranslationY(float drawingTranslationY) {
        this.mDrawingTranslationY = drawingTranslationY;
        invalidate();
    }

    public float getScaleFactor() {
        return this.mScaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.mScaleFactor = scaleFactor;
        invalidate();
    }

    private void performAction(DrawingAction action) {
        this.mCleared = false;
        this.mCanvas.drawBitmap(action.mBitmap, (float) action.mRect.left, (float) action.mRect.top, this.mSrcPaint);
        invalidate();
    }

    private DrawingAction getOppositeAction(DrawingAction action) {
        Rect rect = action.mRect;
        return new DrawingAction(Bitmap.createBitmap(this.mDrawingBitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top), rect);
    }

    /* access modifiers changed from: protected */
    public void checkBounds() {
        int width = this.mDrawingBitmap.getWidth();
        int height = this.mDrawingBitmap.getHeight();
        float f = this.mScaleFactor;
        int contentWidth = (int) (((float) width) * f);
        int contentHeight = (int) (((float) height) * f);
        float widthBound = (float) (getWidth() / 6);
        float heightBound = (float) (getHeight() / 6);
        if (((float) contentWidth) < widthBound) {
            float f2 = this.mDrawingTranslationX;
            if (f2 < ((float) ((-contentWidth) / 2))) {
                this.mDrawingTranslationX = ((float) (-contentWidth)) / 2.0f;
            } else if (f2 > ((float) (getWidth() - (contentWidth / 2)))) {
                this.mDrawingTranslationX = ((float) getWidth()) - (((float) contentWidth) / 2.0f);
            }
        } else if (this.mDrawingTranslationX > ((float) getWidth()) - widthBound) {
            this.mDrawingTranslationX = ((float) getWidth()) - widthBound;
        } else if (this.mDrawingTranslationX + ((float) contentWidth) < widthBound) {
            this.mDrawingTranslationX = widthBound - ((float) contentWidth);
        }
        if (((float) contentHeight) < heightBound) {
            float f3 = this.mDrawingTranslationY;
            if (f3 < ((float) ((-contentHeight) / 2))) {
                this.mDrawingTranslationY = ((float) (-contentHeight)) / 2.0f;
            } else if (f3 > ((float) (getHeight() - (contentHeight / 2)))) {
                this.mDrawingTranslationY = ((float) getHeight()) - (((float) contentHeight) / 2.0f);
            }
        } else if (this.mDrawingTranslationY > ((float) getHeight()) - heightBound) {
            this.mDrawingTranslationY = ((float) getHeight()) - heightBound;
        } else if (this.mDrawingTranslationY + ((float) contentHeight) < heightBound) {
            this.mDrawingTranslationY = heightBound - ((float) contentHeight);
        }
    }

    private void initializeDrawingBitmap(int w, int h) {
        this.mDrawingBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mDrawingBitmap);
        if (this.mDrawingPerformer == null) {
            DrawingPerformer drawingPerformer = new DrawingPerformer(this.mBrushes);
            this.mDrawingPerformer = drawingPerformer;
            drawingPerformer.setPaintPerformListener(new MyDrawingPerformerListener());
        }
        this.mDrawingPerformer.setWidthAndHeight(w, h);
    }

    private void initializeAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DrawingView, 0, 0);
        try {
            BrushSettings settings = this.mBrushes.getBrushSettings();
            settings.setColor(typedArray.getColor(R.styleable.DrawingView_brush_color, ViewCompat.MEASURED_STATE_MASK));
            settings.setSelectedBrush(typedArray.getInteger(R.styleable.DrawingView_brush, 1));
            float size = typedArray.getFloat(R.styleable.DrawingView_brush_size, 0.5f);
            if (size < 0.0f || size > 1.0f) {
                throw new IllegalArgumentException("DrawingView brush_size attribute should have a value between 0 and 1 in your xml file");
            }
            settings.setSelectedBrushSize(size);
            this.mBGColor = typedArray.getColor(R.styleable.DrawingView_drawing_background_color, -1);
        } finally {
            typedArray.recycle();
        }
    }

    private void privateSetBGBitmap(Bitmap bitmap) {
        float canvasWidth = (float) ((getWidth() - getPaddingStart()) - getPaddingEnd());
        float canvasHeight = (float) ((getHeight() - getPaddingTop()) - getPaddingBottom());
        float bitmapWidth = (float) bitmap.getWidth();
        float bitmapHeight = (float) bitmap.getHeight();
        float scaleFactor = 1.0f;
        if (bitmapWidth > canvasWidth && bitmapHeight > canvasHeight) {
            scaleFactor = canvasHeight / bitmapHeight;
            if (((float) ((int) (((float) bitmap.getWidth()) * scaleFactor))) > canvasWidth) {
                scaleFactor = canvasWidth / bitmapWidth;
            }
        } else if (bitmapWidth > canvasWidth && bitmapHeight < canvasHeight) {
            scaleFactor = canvasWidth / bitmapWidth;
        } else if (bitmapWidth < canvasWidth && bitmapHeight > canvasHeight) {
            scaleFactor = canvasHeight / bitmapHeight;
        }
        if (scaleFactor != 1.0f) {
            bitmap = Utilities.resizeBitmap(bitmap, (int) (((float) bitmap.getWidth()) * scaleFactor), (int) (((float) bitmap.getHeight()) * scaleFactor));
        }
        this.mBGBitmap = bitmap;
        this.mScaleFactor = calcAppropriateScaleFactor(bitmap.getWidth(), this.mBGBitmap.getHeight());
        this.mDrawingTranslationX = (canvasWidth - (((float) this.mBGBitmap.getWidth()) * this.mScaleFactor)) / 2.0f;
        this.mDrawingTranslationY = (canvasHeight - (((float) this.mBGBitmap.getHeight()) * this.mScaleFactor)) / 2.0f;
    }

    private float calcAppropriateScaleFactor(int bitmapWidth, int bitmapHeight) {
        float canvasWidth = (float) ((getWidth() - getPaddingStart()) - getPaddingEnd());
        float canvasHeight = (float) ((getHeight() - getPaddingTop()) - getPaddingBottom());
        if (((float) bitmapWidth) >= canvasWidth || ((float) bitmapHeight) >= canvasHeight) {
            return 1.0f;
        }
        float scaleFactor = canvasHeight / ((float) bitmapHeight);
        if (((float) bitmapWidth) * scaleFactor > canvasWidth) {
            return canvasWidth / ((float) bitmapWidth);
        }
        return scaleFactor;
    }

    private class MyDrawingPerformerListener implements DrawingPerformer.DrawingPerformerListener {
        private MyDrawingPerformerListener() {
        }

        public void onDrawingPerformed(Bitmap bitmap, Rect rect) {
            boolean unused = DrawingView.this.mCleared = false;
            if (DrawingView.this.mActionStack != null) {
                storeAction(rect);
            }
            DrawingView.this.mCanvas.drawBitmap(bitmap, (float) rect.left, (float) rect.top, (Paint) null);
            DrawingView.this.invalidate();
            if (DrawingView.this.mOnDrawListener != null) {
                DrawingView.this.mOnDrawListener.onDraw();
            }
        }

        public void onDrawingPerformed(Path path, Paint paint, Rect rect) {
            boolean unused = DrawingView.this.mCleared = false;
            if (DrawingView.this.mActionStack != null) {
                storeAction(rect);
            }
            DrawingView.this.mCanvas.drawPath(path, paint);
            DrawingView.this.invalidate();
            if (DrawingView.this.mOnDrawListener != null) {
                DrawingView.this.mOnDrawListener.onDraw();
            }
        }

        private void storeAction(Rect rect) {
            DrawingView.this.mActionStack.addAction(new DrawingAction(Bitmap.createBitmap(DrawingView.this.mDrawingBitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top), rect));
        }
    }
}
