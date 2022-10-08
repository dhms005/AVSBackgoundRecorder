package com.raed.drawingview.brushes;

import android.graphics.Paint;

public abstract class Brush {
    private int mMaxSizeInPixel;
    private int mMinSizeInPixel;
    protected Paint mPaint = new Paint(5);
    private float mSizeInPercentage;
    protected int mSizeInPixel;

    public abstract void setColor(int i);

    protected Brush(int minSizeInPixel, int maxSizeInPixel) {
        this.mMinSizeInPixel = minSizeInPixel;
        if (minSizeInPixel < 1) {
            this.mMaxSizeInPixel = 1;
        }
        this.mMaxSizeInPixel = maxSizeInPixel;
        if (maxSizeInPixel < 1) {
            this.mMaxSizeInPixel = 1;
        }
    }

    public void setSizeInPercentage(float sizeInPercentage) {
        this.mSizeInPercentage = sizeInPercentage;
        int i = this.mMinSizeInPixel;
        this.mSizeInPixel = (int) (((float) i) + (((float) (this.mMaxSizeInPixel - i)) * sizeInPercentage));
    }

    /* access modifiers changed from: protected */
    public int getSizeInPixel() {
        return this.mSizeInPixel;
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public float getSizeInPercentage() {
        return this.mSizeInPercentage;
    }

    public int getSizeForSafeCrop() {
        return getSizeInPixel();
    }

    public void setMinAndMaxSizeInPixel(int minSizeInPixel, int maxSizeInPixel) {
        if (maxSizeInPixel < minSizeInPixel) {
            throw new IllegalArgumentException("maxSizeInPixel must be >= minSizeInPixel");
        } else if (this.mMinSizeInPixel < 1 || this.mMaxSizeInPixel < 1) {
            throw new IllegalArgumentException("maxSizeInPixel and minSizeInPixel must be >= 1");
        } else {
            this.mMinSizeInPixel = minSizeInPixel;
            this.mMaxSizeInPixel = maxSizeInPixel;
        }
    }

    public int getMinSizeInPixel() {
        return this.mMinSizeInPixel;
    }

    public int getMaxSizeInPixel() {
        return this.mMaxSizeInPixel;
    }

    public float getStep() {
        float step = ((float) this.mSizeInPixel) / 5.0f;
        if (step > 1.0f) {
            return step;
        }
        return 1.0f;
    }
}
