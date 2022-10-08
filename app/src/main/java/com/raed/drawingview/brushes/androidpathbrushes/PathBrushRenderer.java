package com.raed.drawingview.brushes.androidpathbrushes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.raed.drawingview.DrawingEvent;
import com.raed.drawingview.brushes.Brush;
import com.raed.drawingview.brushes.BrushRenderer;

public class PathBrushRenderer implements BrushRenderer {
    public Paint mCurrentPathToolPaint;
    public Path mPath = new Path();

    public void onTouch(DrawingEvent drawingEvent) {
        int pointsLength = drawingEvent.size();
        switch (drawingEvent.getAction()) {
            case 0:
                this.mPath.reset();
                this.mPath.moveTo(drawingEvent.mPoints[0], drawingEvent.mPoints[1]);
                for (int i = 2; i + 1 < pointsLength; i += 2) {
                    this.mPath.lineTo(drawingEvent.mPoints[i], drawingEvent.mPoints[i + 1]);
                }
                return;
            case 1:
            case 2:
                for (int i2 = 0; i2 + 1 < pointsLength; i2 += 2) {
                    this.mPath.lineTo(drawingEvent.mPoints[i2], drawingEvent.mPoints[i2 + 1]);
                }
                return;
            default:
                return;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(this.mPath, this.mCurrentPathToolPaint);
    }

    public void setBrush(Brush brush) {
        this.mCurrentPathToolPaint = brush.getPaint();
    }
}
