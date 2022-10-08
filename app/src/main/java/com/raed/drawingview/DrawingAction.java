package com.raed.drawingview;

import android.graphics.Bitmap;
import android.graphics.Rect;

class DrawingAction {
    Bitmap mBitmap;
    Rect mRect;

    DrawingAction(Bitmap bitmap, Rect rect) {
        this.mBitmap = bitmap;
        this.mRect = new Rect(rect);
    }

   
    public int getSize() {
        return this.mBitmap.getAllocationByteCount();
    }
}
