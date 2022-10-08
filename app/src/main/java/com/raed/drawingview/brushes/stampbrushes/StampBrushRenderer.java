package com.raed.drawingview.brushes.stampbrushes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import com.raed.drawingview.DrawingEvent;
import com.raed.drawingview.brushes.Brush;
import com.raed.drawingview.brushes.BrushRenderer;

public class StampBrushRenderer implements BrushRenderer {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private float[] mLastPoint;
    private StampBrush mStampBrush;

    public void onTouch(DrawingEvent drawingEvent) {
        if (drawingEvent.getAction() == 0) {
            this.mLastPoint = null;
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        int size = drawingEvent.size();
        for (int i = 0; i + 1 < size - 2; i += 2) {
            drawTo(drawingEvent.mPoints[i], drawingEvent.mPoints[i + 1]);
        }
        if (size != 0) {
            float lastX = drawingEvent.mPoints[size - 2];
            float lastY = drawingEvent.mPoints[size - 1];
            if (drawingEvent.getAction() == 1) {
                drawAndStop(lastX, lastY);
            } else {
                drawTo(lastX, lastY);
            }
        }
    }

    public void setBrush(Brush brush) {
        this.mStampBrush = (StampBrush) brush;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mCanvas = new Canvas(this.mBitmap);
    }

    private void drawTo(float x, float y) {
        float[] fArr = this.mLastPoint;
        if (fArr == null) {
            this.mLastPoint = new float[]{x, y};
            return;
        }
        this.mStampBrush.drawFromTo(this.mCanvas, fArr, x, y);
    }

    private void drawAndStop(float x, float y) {
        float[] fArr = this.mLastPoint;
        float tempX = fArr[0];
        float tempY = fArr[1];
        this.mStampBrush.drawFromTo(this.mCanvas, fArr, x, y);
        float[] fArr2 = this.mLastPoint;
        if (tempX == fArr2[0] && tempY == fArr2[1]) {
            this.mStampBrush.drawPoint(this.mCanvas, x, y);
        }
    }
}
