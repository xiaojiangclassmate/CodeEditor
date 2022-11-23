package com.xiaojiangi.editor.widget;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.OverScroller;

import androidx.annotation.NonNull;

public class EditorTouchEventHandler implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private final CodeEditor mEditor;
    private final OverScroller mOverScroller;

    public EditorTouchEventHandler(CodeEditor codeEditor) {
        mEditor = codeEditor;
        mOverScroller = mEditor.getOverScroller();
    }

    protected boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        mEditor.showSoftInput();
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        int dx = (int) distanceX;
        int dy = (int) distanceY;
        if (mOverScroller.getCurrX() + distanceX > mEditor.getViewMaxX()) {
            dx = mEditor.getViewMaxX() - mOverScroller.getCurrX();
        } else if (mOverScroller.getCurrX() + distanceX < 0) {
            dx = 0;
        }
        if (mOverScroller.getCurrY() + distanceY > mEditor.getViewMaxY()) {
            dy = mEditor.getViewMaxY() - mOverScroller.getCurrY();
        } else if (mOverScroller.getCurrY() + distanceY < 0) {
            dy = 0;
        }
        mOverScroller.forceFinished(true);
        mOverScroller.startScroll(mOverScroller.getCurrX(), mOverScroller.getCurrY(), dx, dy, 0);
        mEditor.invalidate();
        return true;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }
    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        mOverScroller.forceFinished(true);
        mOverScroller.fling(mEditor.getScrollX(), mEditor.getScrollY(),
                -(int) velocityX, -(int) velocityY,
                0, mEditor.getViewMaxX(), 0, mEditor.getViewMaxY());

        mEditor.invalidate();
        return true;
    }
}
