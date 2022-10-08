package com.raed.drawingview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.raed.drawingview.brushes.BrushSettings;
import com.raed.drawingview.brushes.Brushes;

public class BrushView extends View {
    private Bitmap mBackground;
    private BrushPreviewPerformer mBrushPreviewPerformer;
    private Brushes mBrushes;

    public BrushView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public BrushView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrushView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0 && h != 0) {
            if (this.mBrushes != null) {
                this.mBackground = Utilities.createBlackWhiteBackground((w - getPaddingStart()) - getPaddingEnd(), (h - getPaddingTop()) - getPaddingBottom(), (int) (getResources().getDisplayMetrics().density * 10.0f));
                BrushPreviewPerformer brushPreviewPerformer = new BrushPreviewPerformer(getContext(), this.mBrushes, this.mBackground.getWidth(), this.mBackground.getHeight());
                this.mBrushPreviewPerformer = brushPreviewPerformer;
                brushPreviewPerformer.setPreviewCallbacks(new BrushPreviewPerformer.PreviewCallbacks() {
                    public void onPreviewReadyToBeDrawn() {
                        BrushView.super.invalidate();
                    }
                });
                this.mBrushPreviewPerformer.preparePreview();
                return;
            }
            throw new RuntimeException("You need to call BrushPreview.setDrawingView(drawingView)");
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((float) getPaddingStart(), (float) getPaddingTop());
        canvas.drawBitmap(this.mBackground, 0.0f, 0.0f, (Paint) null);
        this.mBrushPreviewPerformer.drawPreview(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(getPaddingStart() + ((int) (getResources().getDisplayMetrics().density * 200.0f)) + getPaddingEnd(), widthMeasureSpec), resolveSize(getPaddingTop() + ((int) (getResources().getDisplayMetrics().density * 70.0f)) + getPaddingBottom(), heightMeasureSpec));
    }

    public void invalidate() {
        BrushPreviewPerformer brushPreviewPerformer = this.mBrushPreviewPerformer;
        if (brushPreviewPerformer != null) {
            brushPreviewPerformer.preparePreview();
        }
    }

    public void setDrawingView(DrawingView drawingView) {
        Brushes brushes = drawingView.getBrushes();
        this.mBrushes = brushes;
        brushes.getBrushSettings().addBrushSettingListener(new BrushSettings.BrushSettingListener() {
            public void onSettingsChanged() {
                BrushView.this.invalidate();
            }
        });
    }
}
