package com.raed.drawingview;

class DrawingFilter {
    private float mCurrentBrushStep;
    private float[] mPoint0;
    private float[] mPoint1;

    DrawingFilter() {
    }

   
    public void filter(float x, float y, DrawingEvent points) {
        float f = x;
        float f2 = y;
        DrawingEvent drawingEvent = points;
        float[] fArr = this.mPoint0;
        char c = 0;
        if (fArr == null) {
            this.mPoint0 = new float[]{f, f2};
            this.mPoint1 = new float[]{f, f2};
            drawingEvent.add(f, f2);
            return;
        }
        float[] fArr2 = this.mPoint1;
        float f3 = 2.0f;
        float x2 = (fArr2[0] + f) / 2.0f;
        float y2 = (fArr2[1] + f2) / 2.0f;
        float distanceX = x2 - fArr[0];
        float distanceY = y2 - fArr[1];
        int pointCount = (int) Math.ceil(Math.sqrt((double) ((distanceX * distanceX) + (distanceY * distanceY))) / ((double) this.mCurrentBrushStep));
        int n = 1;
        while (n < pointCount) {
            float t = ((float) n) / ((float) pointCount);
            float tSqr = t * t;
            float tPrime = 1.0f - t;
            float tPrimeSqr = tPrime * tPrime;
            float[] fArr3 = this.mPoint1;
            float[] fArr4 = this.mPoint0;
            drawingEvent.add((tSqr * x2) + (t * f3 * tPrime * fArr3[c]) + (fArr4[c] * tPrimeSqr), (tSqr * y2) + (t * 2.0f * tPrime * fArr3[1]) + (fArr4[1] * tPrimeSqr));
            n++;
            f3 = 2.0f;
            c = 0;
        }
        drawingEvent.add(x2, y2);
        float[] fArr5 = this.mPoint0;
        fArr5[0] = x2;
        fArr5[1] = y2;
        float[] fArr6 = this.mPoint1;
        fArr6[0] = f;
        fArr6[1] = f2;
    }

   
    public void reset() {
        this.mPoint1 = null;
        this.mPoint0 = null;
    }

   
    public void setCurrentBrushStep(float currentBrushStep) {
        this.mCurrentBrushStep = currentBrushStep;
    }
}
