package com.raed.drawingview;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ActionStack {
    private static final String TAG = "ActionStack";
    private static final long mMaxSize = (Runtime.getRuntime().maxMemory() / 4);
    private long mCurrentSize;
    private List<DrawingAction> mRedoStack = new ArrayList();
    private List<DrawingAction> mUndoStack = new ArrayList();

   
    public void addAction(DrawingAction action) {
        Log.d(TAG, "Add getAction: " + action);
        if (this.mRedoStack.size() > 0) {
            for (DrawingAction s : this.mRedoStack) {
                this.mCurrentSize -= (long) s.getSize();
            }
            this.mRedoStack.clear();
        }
        addActionToStack(this.mUndoStack, action);
    }

   
    public void addActionToRedoStack(DrawingAction action) {
        Log.d(TAG, "Add getAction to redo stack: " + action);
        addActionToStack(this.mRedoStack, action);
    }

   
    public void addActionToUndoStack(DrawingAction action) {
        Log.d(TAG, "Add getAction to undo stack: " + action);
        addActionToStack(this.mUndoStack, action);
    }

   
    public DrawingAction previous() {
        return freeLastItem(this.mUndoStack);
    }

   
    public DrawingAction next() {
        return freeLastItem(this.mRedoStack);
    }

   
    public boolean isRedoStackEmpty() {
        return this.mRedoStack.size() == 0;
    }

   
    public boolean isUndoStackEmpty() {
        return this.mUndoStack.size() == 0;
    }

    private void freeItem() {
        if (this.mUndoStack.size() >= this.mRedoStack.size()) {
            this.mCurrentSize -= (long) this.mUndoStack.remove(0).getSize();
        } else {
            this.mCurrentSize -= (long) this.mRedoStack.remove(0).getSize();
        }
    }

    private void addActionToStack(List<DrawingAction> stack, DrawingAction action) {
        StringBuilder sb = new StringBuilder();
        sb.append("MaxSize = ");
        long j = mMaxSize;
        sb.append(j);
        Log.d(TAG, sb.toString());
        Log.d(TAG, "Before:CurSize = " + this.mCurrentSize);
        Log.d(TAG, "Dr+mCSi = " + (this.mCurrentSize + ((long) action.getSize())));
        if (((long) action.getSize()) > j) {
            this.mUndoStack.clear();
            this.mRedoStack.clear();
            this.mCurrentSize = 0;
            return;
        }
        while (this.mCurrentSize + ((long) action.getSize()) > mMaxSize) {
            freeItem();
        }
        stack.add(action);
        this.mCurrentSize += (long) action.getSize();
        Log.d(TAG, "After:CurSize = " + this.mCurrentSize);
    }

    private DrawingAction freeLastItem(List<DrawingAction> list) {
        this.mCurrentSize -= (long) list.get(list.size() - 1).getSize();
        return list.remove(list.size() - 1);
    }
}
