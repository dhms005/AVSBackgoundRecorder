package com.raed.drawingview.brushes.stampbrushes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class Calligraphy extends StampBrush {
    private float mHalfHeight;
    private float mHalfWidth;
    private RectF mTempRectF = new RectF();

    public Calligraphy(int minSizePx, int maxSizePx, int frequency) {
        super(minSizePx, maxSizePx, frequency);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setDither(true);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    public void drawFromTo(Canvas canvas, float[] lastDrawnPoint, float x1, float y1) {
        Canvas canvas2 = canvas;
        float xTerm = x1 - lastDrawnPoint[0];
        float yTerm = y1 - lastDrawnPoint[1];
        float distance = (float) Math.sqrt((double) ((xTerm * xTerm) + (yTerm * yTerm)));
        if (distance >= this.mStep) {
            float stepInPercentage = this.mStep / distance;
            float dx = x1 - lastDrawnPoint[0];
            float dy = y1 - lastDrawnPoint[1];
            float i = 0.0f;
            while (i <= 1.0f) {
                float xCenter = lastDrawnPoint[0] + (i * dx);
                float yCenter = lastDrawnPoint[1] + (i * dy);
                canvas2.rotate(-45.0f, xCenter, yCenter);
                this.mTempRectF.left = xCenter - this.mHalfWidth;
                this.mTempRectF.top = yCenter - this.mHalfHeight;
                this.mTempRectF.right = this.mHalfWidth + xCenter;
                this.mTempRectF.bottom = this.mHalfHeight + yCenter;
                canvas2.drawOval(this.mTempRectF, this.mPaint);
                canvas2.rotate(45.0f, xCenter, yCenter);
                i += stepInPercentage;
            }
            lastDrawnPoint[0] = lastDrawnPoint[0] + (i * dx);
            lastDrawnPoint[1] = lastDrawnPoint[1] + (i * dy);
        }
    }

    public void drawPoint(Canvas canvas, float left, float top) {
        canvas.rotate(-45.0f, left, top);
        this.mTempRectF.left = left - this.mHalfWidth;
        this.mTempRectF.top = top - this.mHalfHeight;
        this.mTempRectF.right = this.mHalfWidth + left;
        this.mTempRectF.bottom = this.mHalfHeight + top;
        canvas.drawOval(this.mTempRectF, this.mPaint);
        canvas.rotate(45.0f, left, top);
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    public void setSizeInPercentage(float sizePercentage) {
        super.setSizeInPercentage(sizePercentage);
        this.mHalfHeight = (float) (this.mSizeInPixel / 8);
        this.mHalfWidth = (float) (this.mSizeInPixel / 2);
    }
}
