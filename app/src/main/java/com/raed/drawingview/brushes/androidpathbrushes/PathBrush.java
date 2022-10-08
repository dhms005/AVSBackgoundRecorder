package com.raed.drawingview.brushes.androidpathbrushes;

import android.graphics.Paint;

import com.raed.drawingview.brushes.Brush;

public abstract class PathBrush extends Brush {
    PathBrush(int minSizePx, int maxSizePx) {
        super(minSizePx, maxSizePx);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setSizeInPercentage(float sizePercentage) {
        super.setSizeInPercentage(sizePercentage);
        this.mPaint.setStrokeWidth((float) getSizeInPixel());
    }

    public int getSizeForSafeCrop() {
        return super.getSizeForSafeCrop() * 2;
    }
}
