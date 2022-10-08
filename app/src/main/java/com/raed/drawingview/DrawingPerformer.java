package com.raed.drawingview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.raed.drawingview.brushes.Brush;
import com.raed.drawingview.brushes.BrushRenderer;
import com.raed.drawingview.brushes.Brushes;
import com.raed.drawingview.brushes.androidpathbrushes.Eraser;
import com.raed.drawingview.brushes.androidpathbrushes.PathBrushRenderer;
import com.raed.drawingview.brushes.stampbrushes.StampBrush;
import com.raed.drawingview.brushes.stampbrushes.StampBrushRenderer;

public class DrawingPerformer {
    private static final String TAG = "DrawingPerformer";
    private Bitmap mBitmap;
    private Brushes mBrushes;
    private Canvas mCanvas;
    private BrushRenderer mCurrentBrushRenderer;
    private boolean mDrawing = false;
    private DrawingBoundsRect mDrawingBoundsRect;
    private final DrawingFilter mDrawingFilter = new DrawingFilter();
    private DrawingPerformerListener mDrawingPerformerListener;
    private PathBrushRenderer mPathBrushRenderer = new PathBrushRenderer();
    private Brush mSelectedBrush;
    private StampBrushRenderer mStampBrushRenderer = new StampBrushRenderer();
    private DrawingEvent mTemDrawingEvent = new DrawingEvent();

    interface DrawingPerformerListener {
        void onDrawingPerformed(Bitmap bitmap, Rect rect);

        void onDrawingPerformed(Path path, Paint paint, Rect rect);
    }

    public DrawingPerformer(Brushes brushes) {
        this.mBrushes = brushes;
        this.mDrawingBoundsRect = new DrawingBoundsRect();
    }

   
    public void draw(Canvas canvas, Bitmap bitmap) {
        if (this.mSelectedBrush.getClass().equals(Eraser.class)) {
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            this.mCanvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            this.mCurrentBrushRenderer.draw(this.mCanvas);
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
            return;
        }
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        this.mCurrentBrushRenderer.draw(canvas);
    }

   
    public void onTouch(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 3) {
            action = 1;
        }
        float x = event.getX();
        float y = event.getY();
        if (action == 0) {
            updateSelectedBrush();
            this.mDrawing = true;
            this.mDrawingFilter.reset();
        }
        if (this.mDrawing) {
            this.mTemDrawingEvent.clear();
            this.mDrawingFilter.filter(x, y, this.mTemDrawingEvent);
            this.mTemDrawingEvent.setAction(action);
            if (action == 0) {
                this.mDrawingBoundsRect.reset(x, y);
            } else {
                this.mDrawingBoundsRect.update(this.mTemDrawingEvent);
            }
            this.mCurrentBrushRenderer.onTouch(this.mTemDrawingEvent);
            if (action == 1) {
                this.mDrawing = false;
                notifyListener();
            }
        }
    }

   
    public void setPaintPerformListener(DrawingPerformerListener listener) {
        this.mDrawingPerformerListener = listener;
    }

   
    public boolean isDrawing() {
        return this.mDrawing;
    }

   
    public void setWidthAndHeight(int width, int height) {
        this.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mStampBrushRenderer.setBitmap(this.mBitmap);
    }

    private Rect getDrawingBoundsRect() {
        int size = this.mSelectedBrush.getSizeForSafeCrop();
        Log.d(TAG, "getDrawingBoundsRect: " + size);
        int left = (int) (this.mDrawingBoundsRect.mMinX - ((float) (size / 2)));
        int top = 0;
        int left2 = left > 0 ? left : 0;
        int top2 = (int) (this.mDrawingBoundsRect.mMinY - ((float) (size / 2)));
        if (top2 > 0) {
            top = top2;
        }
        int width = (int) ((this.mDrawingBoundsRect.mMaxX - this.mDrawingBoundsRect.mMinX) + ((float) size));
        int width2 = width > this.mBitmap.getWidth() - left2 ? this.mBitmap.getWidth() - left2 : width;
        int height = (int) ((this.mDrawingBoundsRect.mMaxY - this.mDrawingBoundsRect.mMinY) + ((float) size));
        return new Rect(left2, top, left2 + width2, top + (height > this.mBitmap.getHeight() - top ? this.mBitmap.getHeight() - top : height));
    }

    private void updateSelectedBrush() {
        Brush brush = this.mBrushes.getBrush(this.mBrushes.getBrushSettings().getSelectedBrush());
        this.mSelectedBrush = brush;
        if (brush instanceof StampBrush) {
            this.mCurrentBrushRenderer = this.mStampBrushRenderer;
        } else {
            this.mCurrentBrushRenderer = this.mPathBrushRenderer;
        }
        this.mCurrentBrushRenderer.setBrush(brush);
        this.mDrawingFilter.setCurrentBrushStep(this.mSelectedBrush.getStep());
    }

    private void notifyListener() {
        Rect rect = getDrawingBoundsRect();
        if (rect.right - rect.left > 0 && rect.bottom - rect.top > 0) {
            if (this.mSelectedBrush instanceof StampBrush) {
                this.mDrawingPerformerListener.onDrawingPerformed(Bitmap.createBitmap(this.mBitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top), rect);
                return;
            }
            this.mDrawingPerformerListener.onDrawingPerformed(this.mPathBrushRenderer.mPath, this.mPathBrushRenderer.mCurrentPathToolPaint, rect);
        }
    }

    private static class DrawingBoundsRect {
        
        public float mMaxX;
        
        public float mMaxY;
        
        public float mMinX;
        
        public float mMinY;

        private DrawingBoundsRect() {
        }

       
        public void update(DrawingEvent drawingEvent) {
            int size = drawingEvent.size();
            for (int i = 0; i + 1 < size; i += 2) {
                if (drawingEvent.mPoints[i] < this.mMinX) {
                    this.mMinX = drawingEvent.mPoints[i];
                } else if (drawingEvent.mPoints[i] > this.mMaxX) {
                    this.mMaxX = drawingEvent.mPoints[i];
                }
                if (drawingEvent.mPoints[i + 1] < this.mMinY) {
                    this.mMinY = drawingEvent.mPoints[i + 1];
                } else if (drawingEvent.mPoints[i + 1] > this.mMaxY) {
                    this.mMaxY = drawingEvent.mPoints[i + 1];
                }
            }
        }

       
        public void reset(float x, float y) {
            this.mMaxX = x;
            this.mMinX = x;
            this.mMaxY = y;
            this.mMinY = y;
        }
    }
}
