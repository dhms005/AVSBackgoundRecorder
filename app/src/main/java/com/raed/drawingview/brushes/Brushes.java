package com.raed.drawingview.brushes;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import androidx.core.view.ViewCompat;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.raed.drawingview.brushes.androidpathbrushes.Eraser;
import com.raed.drawingview.brushes.androidpathbrushes.Pen;
import com.raed.drawingview.brushes.stampbrushes.BitmapBrush;
import com.raed.drawingview.brushes.stampbrushes.Calligraphy;
import com.raed.drawingview.brushes.stampbrushes.RandRotBitmapBrush;

public class Brushes {
    public static final int AIR_BRUSH = 3;
    public static final int CALLIGRAPHY = 2;
    public static final int ERASER = 4;
    public static final int PEN = 0;
    public static final int PENCIL = 1;
    private BrushSettings mBrushSettings;
    private Brush[] mBrushes;

    public Brushes(Resources resources) {
        Brush[] brushArr = new Brush[5];
        this.mBrushes = brushArr;
        brushArr[0] = new Pen(resources.getDimensionPixelSize(R.dimen.pen_min_stroke_size), resources.getDimensionPixelSize(R.dimen.pen_max_stroke_size));
        RandRotBitmapBrush pencil = new RandRotBitmapBrush(BitmapFactory.decodeResource(resources, R.drawable.brush_pencil), resources.getDimensionPixelSize(R.dimen.pencil_min_stroke_size), resources.getDimensionPixelSize(R.dimen.pencil_max_stroke_size), 6);
        Brush[] brushArr2 = this.mBrushes;
        brushArr2[1] = pencil;
        brushArr2[4] = new Eraser(resources.getDimensionPixelSize(R.dimen.eraser_min_stroke_size), resources.getDimensionPixelSize(R.dimen.eraser_max_stroke_size));
        this.mBrushes[3] = new BitmapBrush(BitmapFactory.decodeResource(resources, R.drawable.brush_0), resources.getDimensionPixelSize(R.dimen.brush0_min_stroke_size), resources.getDimensionPixelSize(R.dimen.brush0_max_stroke_size), 6);
        Calligraphy calligraphy = new Calligraphy(resources.getDimensionPixelSize(R.dimen.calligraphy_min_stroke_size), resources.getDimensionPixelSize(R.dimen.calligraphy_max_stroke_size), 20);
        Brush[] brushArr3 = this.mBrushes;
        brushArr3[2] = calligraphy;
        for (Brush brush : brushArr3) {
            brush.setSizeInPercentage(0.5f);
            brush.setColor(ViewCompat.MEASURED_STATE_MASK);
        }
        this.mBrushSettings = new BrushSettings(this);
    }

    public Brush getBrush(int brushID) {
        Brush[] brushArr = this.mBrushes;
        if (brushID < brushArr.length && brushID >= 0) {
            return brushArr[brushID];
        }
        throw new IllegalArgumentException("There is no brush with id = " + brushID + " in " + getClass());
    }

   
    public Brush[] getBrushes() {
        return this.mBrushes;
    }

    public BrushSettings getBrushSettings() {
        return this.mBrushSettings;
    }
}
