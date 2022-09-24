package com.xiaojiangi.editor.widget;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Region;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.OverScroller;

public class EditorTouchEventHandler implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private final CodeEditor mEditor;
    private final OverScroller mOverScroller;
    private final Selection mSelection;
    private final HandShankStyle mHandShankStyle;

    public EditorTouchEventHandler(CodeEditor codeEditor) {
        mEditor = codeEditor;
        mOverScroller = new OverScroller(mEditor.getContext());
        mSelection = mEditor.getSelection();
        mHandShankStyle = mEditor.getHandShankStyle();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    //触摸
    @Override
    public boolean onDown(MotionEvent e) {

        return true;
    }

    //用户轻触触摸屏，还没有松开或拖动
    @Override
    public void onShowPress(MotionEvent e) {
        if (!mSelection.isSelection()) {
            select(e);
        } else {
            int x = (int) e.getX() + mOverScroller.getCurrX();
            int y = (int) e.getY() + mOverScroller.getCurrY();
            if (mEditor.getHandShankStyle().isContain(x, y)) {
                if (mEditor.getHandShankStyle().isLeftHandShank(x, y)) {
                    Log.d("Editor HandShank", "LEFT");
                }
                if (mEditor.getHandShankStyle().isRightHandShank(x, y)) {
                    Log.d("Editor HandShank", "RIGHT");
                }
            }
        }
    }

    //轻触松开
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mEditor.showSoftInput();
        //计算行
        int line =(int)Math.min(mEditor.getContent().size()-1,(e.getY()+mOverScroller.getCurrY())/mEditor.getLineHeight());
        int column =0;
        float columnOffset =mEditor.getPainter().getOffset();
        var contentLine =mEditor.getContent().get(line);
        //计算列
        for (int i = 0; i < contentLine.length(); i++) {
            char c=contentLine.charAt(i);
            if (c=='\t'){
                columnOffset+=mEditor.getPainter().getTabWidth();
            }else {
                columnOffset+=mEditor.getPainter().measureText(String.valueOf(c));
            }
            if (e.getX() + mOverScroller.getCurrX() < columnOffset) {
                column = i;
                break;
            }

        }
        if (e.getX() + mOverScroller.getCurrX() > columnOffset) {
            column = contentLine.length();
        }
        mEditor.getCursor().set(line, column);
        if (mSelection.isSelection()) {
            mSelection.restart();
            mHandShankStyle.restart();
        }
        mEditor.invalidate();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int dx=(int)distanceX;
        int dy=(int)distanceY;
        if (mOverScroller.getCurrX() + distanceX > mEditor.getMaxX()) {
            dx = mEditor.getMaxX() - mOverScroller.getCurrX();
        } else if (mOverScroller.getCurrX() + distanceX < 0) {
            dx = 0;
        }
        if (mOverScroller.getCurrY() + distanceY > mEditor.getMaxY()) {
            dy = mEditor.getMaxY() - mOverScroller.getCurrY();
        } else if (mOverScroller.getCurrY() + distanceY < 0) {
            dy = 0;
        }
        mOverScroller.forceFinished(true);
        mOverScroller.startScroll(mOverScroller.getCurrX(), mOverScroller.getCurrY(), dx, dy, 0);
        mEditor.invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mOverScroller.forceFinished(true);
        mOverScroller.fling(mEditor.getScrollX(),mEditor.getScrollY(),
                -(int) velocityX,-(int) velocityY,
                0, mEditor.getMaxX(), 0,mEditor.getMaxY());

        mEditor.invalidate();
        return true;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (!mSelection.isSelection()) {
            select(e);
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return true;
    }

    private void select(MotionEvent e) {
        int line = (int) Math.min(mEditor.getContent().size() - 1, (e.getY() + mOverScroller.getCurrY()) / mEditor.getLineHeight());
        var contentLine = mEditor.getContent().get(line);
        //该行为空
        if (contentLine.length() == 0)
            return;
        int column = 0;
        float columnOffset = mEditor.getPainter().getOffset();

        //计算列
        for (int i = 0; i < contentLine.length(); i++) {
            char c = contentLine.charAt(i);
            if (c == '\t') {
                columnOffset += mEditor.getPainter().getTabWidth();
            } else {
                columnOffset += mEditor.getPainter().measureText(String.valueOf(c));
            }
            if (e.getX() + mOverScroller.getCurrX() < columnOffset) {
                column = i;
                break;
            }
        }
        if (e.getX() + mOverScroller.getCurrX() > columnOffset) {
            if (contentLine.length() != 0) {
                column = contentLine.length() - 1;
            } else {
                column = 0;
            }
        }
        int leftPos = 0;
        int rightPos = contentLine.length();
        if (isSymbol(contentLine.charAt(column))) {
            rightPos = column + 1;
            leftPos = column;
            mSelection.set(line, leftPos, line, rightPos);
            mEditor.invalidate();
            return;
        }
        for (int i = column; i > 0; i--) {
            char c = contentLine.charAt(i);
            if (isSymbol(c)) {
                leftPos = i + 1;
                break;
            }
        }
        //right
        for (int i = column; i < contentLine.length(); i++) {
            char c = contentLine.charAt(i);
            if (isSymbol(c)) {
                rightPos = i;
                break;
            }
        }
        if (leftPos != rightPos) {
            mSelection.set(line, leftPos, line, rightPos);
            mEditor.invalidate();
        }
    }

    private boolean isSymbol(char c) {
        // 数字 小数点 大写字母 小写字母
        return !((57 >= (int) c && (int) c >= 48) || (int) c == 46 || (int) c == 95 || (90 >= (int) c && (int) c >= 65) || (122 >= (int) c && (int) c >= 97));

    }


    public OverScroller getOverScroller() {
        return mOverScroller;
    }
}
