package com.raed.drawingview.brushes.stampbrushes;

import android.graphics.Canvas;

import com.raed.drawingview.brushes.Brush;

public abstract class StampBrush extends Brush {
    private final int mFrequency;
    float mStep;

    public abstract void drawFromTo(Canvas canvas, float[] fArr, float f, float f2);

    public abstract void drawPoint(Canvas canvas, float f, float f2);

    StampBrush(int minSizePx, int maxSizePx, int frequency) {
        super(minSizePx, maxSizePx);
        this.mFrequency = frequency;
        updateStep();
    }

    public void setSizeInPercentage(float sizePercentage) {
        super.setSizeInPercentage(sizePercentage);
        updateStep();
    }

    public float getStep() {
        return this.mStep;
    }

    private void updateStep() {
        float f = (float) (this.mSizeInPixel / this.mFrequency);
        this.mStep = f;
        if (f < 1.0f) {
            this.mStep = 1.0f;
        }
    }
}
