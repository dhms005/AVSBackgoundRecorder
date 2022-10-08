package com.raed.drawingview.brushes.stampbrushes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public class BitmapBrush extends StampBrush {
    float mHalfResizedBrushHeight;
    float mHalfResizedBrushWidth;
    private Bitmap mOriginalBrush;
    Bitmap mResizedBrush;
    private Canvas mResizedBrushCanvas = new Canvas();

    public BitmapBrush(Bitmap bitmap, int minSizePx, int maxSizePx, int frequency) {
        super(minSizePx, maxSizePx, frequency);
        this.mOriginalBrush = bitmap;
    }

    public void drawFromTo(Canvas canvas, float[] lastDrawnPoint, float x1, float y1) {
        float xTerm = x1 - lastDrawnPoint[0];
        char c = 1;
        float yTerm = y1 - lastDrawnPoint[1];
        float dist = (float) Math.sqrt((double) ((xTerm * xTerm) + (yTerm * yTerm)));
        if (dist >= this.mStep) {
            float stepInPercentage = this.mStep / dist;
            float i = 0.0f;
            float dx = x1 - lastDrawnPoint[0];
            float dy = y1 - lastDrawnPoint[1];
            while (i <= 1.0f) {
                float xCenter = lastDrawnPoint[0] + (i * dx);
                float yCenter = lastDrawnPoint[c] + (i * dy);
                canvas.drawBitmap(this.mResizedBrush, xCenter - this.mHalfResizedBrushWidth, yCenter - this.mHalfResizedBrushHeight, (Paint) null);
                i += stepInPercentage;
                c = 1;
            }
            Canvas canvas2 = canvas;
            lastDrawnPoint[0] = lastDrawnPoint[0] + (i * dx);
            lastDrawnPoint[1] = lastDrawnPoint[1] + (i * dy);
        }
    }

    public void drawPoint(Canvas canvas, float left, float top) {
        canvas.drawBitmap(this.mResizedBrush, left - this.mHalfResizedBrushWidth, top - this.mHalfResizedBrushHeight, (Paint) null);
    }

    public void setSizeInPercentage(float sizePercentage) {
        super.setSizeInPercentage(sizePercentage);
        Bitmap createBitmap = Bitmap.createBitmap(this.mSizeInPixel, (int) ((((float) this.mSizeInPixel) / ((float) this.mOriginalBrush.getWidth())) * ((float) this.mOriginalBrush.getHeight())), Bitmap.Config.ARGB_8888);
        this.mResizedBrush = createBitmap;
        this.mResizedBrushCanvas.setBitmap(createBitmap);
        updateResizeBrush();
        this.mHalfResizedBrushWidth = (float) (this.mResizedBrush.getWidth() / 2);
        this.mHalfResizedBrushHeight = (float) (this.mResizedBrush.getHeight() / 2);
    }

    public void setColor(int color) {
        this.mPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        this.mPaint.setAlpha(Color.alpha(color));
        updateResizeBrush();
    }

    private void updateResizeBrush() {
        this.mResizedBrushCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        float xScale = ((float) this.mResizedBrush.getWidth()) / ((float) this.mOriginalBrush.getWidth());
        float yScale = ((float) this.mResizedBrush.getHeight()) / ((float) this.mOriginalBrush.getHeight());
        this.mResizedBrushCanvas.scale(xScale, yScale);
        this.mResizedBrushCanvas.drawBitmap(this.mOriginalBrush, 0.0f, 0.0f, this.mPaint);
        this.mResizedBrushCanvas.scale(1.0f / xScale, 1.0f / yScale);
    }
}
