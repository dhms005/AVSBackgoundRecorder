package com.raed.drawingview.brushes.androidpathbrushes;

public class Pen extends PathBrush {
    public Pen(int minSizePx, int maxSizePx) {
        super(minSizePx, maxSizePx);
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }
}
