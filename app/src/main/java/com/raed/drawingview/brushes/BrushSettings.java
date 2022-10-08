package com.raed.drawingview.brushes;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

public class BrushSettings {
    private Brushes mBrushes;
    private int mColor = ViewCompat.MEASURED_STATE_MASK;
    private List<BrushSettingListener> mListeners = new ArrayList();
    private int mSelectedBrush = 1;

    public interface BrushSettingListener {
        void onSettingsChanged();
    }

    BrushSettings(Brushes brushes) {
        this.mBrushes = brushes;
    }

    public int getSelectedBrush() {
        return this.mSelectedBrush;
    }

    public void setSelectedBrush(int selectedBrush) {
        this.mSelectedBrush = selectedBrush;
        this.mBrushes.getBrush(selectedBrush).setColor(this.mColor);
        notifyListeners();
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        this.mBrushes.getBrush(this.mSelectedBrush).setColor(this.mColor);
        notifyListeners();
    }

    public void setSelectedBrushSize(float size) {
        setBrushSize(this.mSelectedBrush, size);
    }

    public float getSelectedBrushSize() {
        return getBrushSize(this.mSelectedBrush);
    }

    public void setBrushSize(int brushID, float size) {
        if (size > 1.0f || size < 0.0f) {
            throw new IllegalArgumentException("Size must be between 0 and 1");
        }
        this.mBrushes.getBrush(brushID).setSizeInPercentage(size);
        notifyListeners();
    }

    public float getBrushSize(int brushID) {
        return this.mBrushes.getBrush(brushID).getSizeInPercentage();
    }

   
    public Brushes getBrushes() {
        return this.mBrushes;
    }

    public void addBrushSettingListener(BrushSettingListener listener) {
        this.mListeners.add(listener);
    }

    private void notifyListeners() {
        for (BrushSettingListener listener : this.mListeners) {
            listener.onSettingsChanged();
        }
    }
}
