package com.raed.drawingview.brushes.stampbrushes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class RandRotBitmapBrush extends BitmapBrush {
    private static final int RANDOM_ANGLES_NUMBER = 100;
    private static float[] sRandomAnglesArray = new float[100];
    private static int sRandomAnglesIndex = 0;

    static {
        for (int i = 0; i < 100; i++) {
            sRandomAnglesArray[i] = (float) (Math.random() * 360.0d);
        }
    }

    public RandRotBitmapBrush(Bitmap bitmap, int minSizePx, int maxSizePx, int frequency) {
        super(bitmap, minSizePx, maxSizePx, frequency);
    }

    public void drawFromTo(Canvas canvas, float[] lastDrawnPoint, float x1, float y1) {
        Canvas canvas2 = canvas;
        char c = 0;
        float xTerm = x1 - lastDrawnPoint[0];
        float yTerm = y1 - lastDrawnPoint[1];
        float dist = (float) Math.sqrt((double) ((xTerm * xTerm) + (yTerm * yTerm)));
        if (dist >= this.mStep) {
            float stepInPercentage = this.mStep / dist;
            float i = 0.0f;
            float dx = x1 - lastDrawnPoint[0];
            float dy = y1 - lastDrawnPoint[1];
            while (i <= 1.0f) {
                float xCenter = lastDrawnPoint[c] + (i * dx);
                float yCenter = lastDrawnPoint[1] + (i * dy);
                canvas2.rotate(sRandomAnglesArray[sRandomAnglesIndex], xCenter, yCenter);
                canvas2.drawBitmap(this.mResizedBrush, xCenter - this.mHalfResizedBrushWidth, yCenter - this.mHalfResizedBrushHeight, (Paint) null);
                canvas2.rotate(-sRandomAnglesArray[sRandomAnglesIndex], xCenter, yCenter);
                int i2 = sRandomAnglesIndex + 1;
                sRandomAnglesIndex = i2;
                sRandomAnglesIndex = i2 % 100;
                i += stepInPercentage;
                c = 0;
            }
            lastDrawnPoint[0] = lastDrawnPoint[0] + (i * dx);
            lastDrawnPoint[1] = lastDrawnPoint[1] + (i * dy);
        }
    }

    public void drawPoint(Canvas canvas, float left, float top) {
        float rotation = (float) (Math.random() * 360.0d);
        canvas.rotate(rotation, left, top);
        canvas.drawBitmap(this.mResizedBrush, left - this.mHalfResizedBrushWidth, top - this.mHalfResizedBrushHeight, (Paint) null);
        canvas.rotate(-rotation, left, top);
    }

    public int getSizeForSafeCrop() {
        return getSizeInPixel() * 2;
    }
}
