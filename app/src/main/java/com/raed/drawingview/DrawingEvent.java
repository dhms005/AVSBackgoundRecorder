package com.raed.drawingview;

public class DrawingEvent {
    private int mAction;
    public float[] mPoints = new float[20000];
    private int mSize;

   
    public void add(float x, float y) {
        float[] fArr = this.mPoints;
        int i = this.mSize;
        fArr[i] = x;
        fArr[i + 1] = y;
        this.mSize = i + 2;
    }

   
    public void clear() {
        this.mSize = 0;
    }

   
    public void setAction(int action) {
        this.mAction = action;
    }

    public int size() {
        return this.mSize;
    }

    public int getAction() {
        return this.mAction;
    }
}
