package com.raed.drawingview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;

import com.raed.drawingview.brushes.Brush;
import com.raed.drawingview.brushes.Brushes;
import com.raed.drawingview.brushes.androidpathbrushes.Eraser;
import com.raed.drawingview.brushes.androidpathbrushes.PathBrush;
import com.raed.drawingview.brushes.stampbrushes.StampBrush;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BrushPreviewPerformer {
    private Brush mBrush;
    private Brushes mBrushes;
    private Path mCurvePath;
    private Paint mEraserPaint;
    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message message) {
            float[] points;
            if (BrushPreviewPerformer.this.mStampBrush == null) {
                return true;
            }
            if (message.what == 0) {
                float[] unused = BrushPreviewPerformer.this.mLastPoint = null;
                BrushPreviewPerformer.this.mPreviewCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            }
            if (message.what == 0) {
                points = BrushPreviewPerformer.this.mPoints0;
            } else if (message.what == 1) {
                points = BrushPreviewPerformer.this.mPoints1;
            } else if (message.what == 2) {
                points = BrushPreviewPerformer.this.mPoints2;
            } else if (message.what == 3) {
                points = BrushPreviewPerformer.this.mPoints3;
            } else {
                throw new IllegalArgumentException("Undefiled message");
            }
            for (int i = 0; i + 1 < points.length - 2; i += 2) {
                BrushPreviewPerformer.this.drawTo(points[i], points[i + 1]);
            }
            int pointsLength = points.length;
            if (message.what == 3) {
                BrushPreviewPerformer.this.drawAndStop(points[pointsLength - 2], points[pointsLength - 1]);
                BrushPreviewPerformer.this.mPreviewCallbacks.onPreviewReadyToBeDrawn();
            } else {
                BrushPreviewPerformer.this.drawTo(points[pointsLength - 2], points[pointsLength - 1]);
            }
            return true;
        }
    });
    
    public float[] mLastPoint;
    
    public float[] mPoints0;
    
    public float[] mPoints1;
    
    public float[] mPoints2;
    
    public float[] mPoints3;
    private final Bitmap mPreviewBitmap;
    
    public PreviewCallbacks mPreviewCallbacks;
    
    public final Canvas mPreviewCanvas;
    
    public StampBrush mStampBrush;

    public interface PreviewCallbacks {
        void onPreviewReadyToBeDrawn();
    }

    BrushPreviewPerformer(Context context, Brushes brushes, int w, int h) {
        this.mBrushes = brushes;
        List<float[]> curvePoints = initializeCurve(context, w, h);
        initializeCurvePoints(curvePoints);
        Path path = new Path();
        this.mCurvePath = path;
        path.moveTo(curvePoints.get(0)[0], curvePoints.get(0)[1]);
        for (int i = 1; i < curvePoints.size(); i++) {
            float[] point = curvePoints.get(i);
            this.mCurvePath.lineTo(point[0], point[1]);
        }
        Bitmap createBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.mPreviewBitmap = createBitmap;
        this.mPreviewCanvas = new Canvas(createBitmap);
        Paint paint = new Paint(5);
        this.mEraserPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mEraserPaint.setColor(-1);
    }

   
    public void drawPreview(Canvas canvas) {
        Brush brush = this.mBrush;
        if (brush != null) {
            if (brush instanceof StampBrush) {
                canvas.drawBitmap(this.mPreviewBitmap, 0.0f, 0.0f, (Paint) null);
                return;
            }
            PathBrush pathBrush = (PathBrush) brush;
            if (pathBrush instanceof Eraser) {
                this.mEraserPaint.setStrokeWidth(pathBrush.getPaint().getStrokeWidth());
                canvas.drawPath(this.mCurvePath, this.mEraserPaint);
                return;
            }
            canvas.drawPath(this.mCurvePath, pathBrush.getPaint());
        }
    }

   
    public void preparePreview() {
        this.mStampBrush = null;
        Brush brush = this.mBrushes.getBrush(this.mBrushes.getBrushSettings().getSelectedBrush());
        this.mBrush = brush;
        if (brush instanceof StampBrush) {
            this.mStampBrush = (StampBrush) brush;
            this.mHandler.removeCallbacksAndMessages((Object) null);
            Message.obtain(this.mHandler, 0).sendToTarget();
            Message.obtain(this.mHandler, 1).sendToTarget();
            Message.obtain(this.mHandler, 2).sendToTarget();
            Message.obtain(this.mHandler, 3).sendToTarget();
            return;
        }
        this.mPreviewCallbacks.onPreviewReadyToBeDrawn();
    }

   
    public void setPreviewCallbacks(PreviewCallbacks previewCallbacks) {
        this.mPreviewCallbacks = previewCallbacks;
    }

    
    public void drawTo(float x, float y) {
        float[] fArr = this.mLastPoint;
        if (fArr == null) {
            this.mLastPoint = new float[]{x, y};
            return;
        }
        this.mStampBrush.drawFromTo(this.mPreviewCanvas, fArr, x, y);
    }

    
    public void drawAndStop(float x, float y) {
        float[] fArr = this.mLastPoint;
        float tempX = fArr[0];
        float tempY = fArr[1];
        this.mStampBrush.drawFromTo(this.mPreviewCanvas, fArr, x, y);
        float[] fArr2 = this.mLastPoint;
        if (tempX == fArr2[0] && tempY == fArr2[1]) {
            this.mStampBrush.drawPoint(this.mPreviewCanvas, x, y);
        }
    }

    private void initializeCurvePoints(List<float[]> curvePoints) {
        float[] fArr;
        float[] fArr2;
        float[] fArr3;
        int pointListSize = curvePoints.size();
        this.mPoints0 = new float[((pointListSize / 4) * 2)];
        this.mPoints1 = new float[((pointListSize / 4) * 2)];
        this.mPoints2 = new float[((pointListSize / 4) * 2)];
        this.mPoints3 = new float[((pointListSize - ((pointListSize / 4) * 3)) * 2)];
        int i = 0;
        while (true) {
            fArr = this.mPoints0;
            if (i >= fArr.length) {
                break;
            }
            fArr[i] = curvePoints.get(i / 2)[i % 2];
            i++;
        }
        int startFrom = fArr.length;
        int i2 = 0;
        while (true) {
            fArr2 = this.mPoints1;
            if (i2 >= fArr2.length) {
                break;
            }
            fArr2[i2] = curvePoints.get((startFrom + i2) / 2)[i2 % 2];
            i2++;
        }
        int startFrom2 = this.mPoints0.length + fArr2.length;
        int i3 = 0;
        while (true) {
            fArr3 = this.mPoints2;
            if (i3 >= fArr3.length) {
                break;
            }
            fArr3[i3] = curvePoints.get((startFrom2 + i3) / 2)[i3 % 2];
            i3++;
        }
        int startFrom3 = this.mPoints0.length + this.mPoints1.length + fArr3.length;
        int i4 = 0;
        while (true) {
            float[] fArr4 = this.mPoints3;
            if (i4 < fArr4.length) {
                fArr4[i4] = curvePoints.get((startFrom3 + i4) / 2)[i4 % 2];
                i4++;
            } else {
                return;
            }
        }
    }

    private List<float[]> initializeCurve(Context context, int w, int h) {
        int i = w;
        int i2 = h;
        if (i == 0 || i2 == 0) {
            throw new IllegalArgumentException("width & height must be > 0");
        }
        int i3 = 2;
        float[][] curvePoints = (float[][]) Array.newInstance(float.class, new int[]{4, 2});
        curvePoints[0][0] = ((float) i) / 10.0f;
        curvePoints[1][0] = ((float) i) * 0.35f;
        curvePoints[2][0] = ((float) i) * 0.65f;
        curvePoints[3][0] = ((float) i) * 0.9f;
        curvePoints[0][1] = ((float) i2) / 2.0f;
        curvePoints[1][1] = 0.0f;
        curvePoints[2][1] = (float) i2;
        curvePoints[3][1] = ((float) i2) / 2.0f;
        List<float[]> pointList = new ArrayList<>();
        float approximatedArcLength = Utilities.dist(curvePoints[0][0], curvePoints[0][1], curvePoints[1][0], curvePoints[1][1]) + Utilities.dist(curvePoints[1][0], curvePoints[1][1], curvePoints[2][0], curvePoints[2][1]) + Utilities.dist(curvePoints[2][0], curvePoints[2][1], curvePoints[3][0], curvePoints[3][1]);
        float step = context.getResources().getDisplayMetrics().density;
        int size = (int) (approximatedArcLength / step);
        int i4 = 0;
        while (i4 < size) {
            float[] point = new float[i3];
            pointList.add(point);
            Utilities.cubicBezier(curvePoints[0][0], curvePoints[0][1], curvePoints[1][0], curvePoints[1][1], curvePoints[i3][0], curvePoints[i3][1], curvePoints[3][0], curvePoints[3][1], (((float) i4) * step) / approximatedArcLength, point);
            i4++;
            i3 = 2;
        }
        return pointList;
    }
}
