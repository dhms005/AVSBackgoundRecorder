package com.raed.drawingview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

class Utilities {
    Utilities() {
    }

    static Bitmap createBlackWhiteBackground(int w, int h, int squareSize) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(4);
        canvas.drawColor(Color.argb(255, 248, 248, 248));
        paint.setColor(Color.argb(255, 216, 216, 216));
        int i = 0;
        while (i < bitmap.getWidth()) {
            int j = 0;
            while (j < bitmap.getHeight()) {
                Paint paint2 = paint;
                canvas.drawRect((float) i, (float) j, (float) (i + squareSize), (float) (j + squareSize), paint2);
                float l = (float) (i + squareSize);
                float t = (float) (j + squareSize);
                canvas.drawRect(l, t, l + ((float) squareSize), t + ((float) squareSize), paint2);
                j += squareSize * 2;
            }
            i += squareSize * 2;
        }
        return bitmap;
    }

    static void cubicBezier(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float t, float[] point) {
        float f = t;
        point[0] = (float) ((Math.pow((double) (1.0f - f), 3.0d) * ((double) x0)) + (Math.pow((double) (1.0f - f), 2.0d) * 3.0d * ((double) f) * ((double) x1)) + ((double) ((1.0f - f) * 3.0f * f * f * x2)) + ((double) (f * f * f * x3)));
        point[1] = (float) ((Math.pow((double) (1.0f - f), 3.0d) * ((double) y0)) + (Math.pow((double) (1.0f - f), 2.0d) * 3.0d * ((double) f) * ((double) y1)) + ((double) ((1.0f - f) * 3.0f * f * f * y2)) + ((double) (f * f * f * y3)));
    }

    static float dist(float x1, float y1, float x2, float y2) {
        float x22 = x2 - x1;
        float y22 = y2 - y1;
        return (float) Math.sqrt((double) ((x22 * x22) + (y22 * y22)));
    }

    static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / ((float) width), ((float) newHeight) / ((float) height));
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}
