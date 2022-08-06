package com.xiaojiangi.editor.widget;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.OverScroller;

public class EditorTouchEventHandler implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    private CodeEditor mEditor;
    private OverScroller mOverScroller;
    public EditorTouchEventHandler(CodeEditor codeEditor) {
        mEditor =codeEditor;
        mOverScroller =new OverScroller(mEditor.getContext());
    }

    public boolean onTouchEvent(MotionEvent event){
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
            if (e.getX()+mOverScroller.getCurrX()<columnOffset){
                column=i;
                break;
            }

        }
        if (e.getX()+mOverScroller.getCurrX()>columnOffset){
            column =contentLine.length();
        }
        mEditor.getCursor().set(line,column);
        mEditor.invalidate();
        return true;
    }

    //拖动
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

    //长按
    @Override
    public void onLongPress(MotionEvent e) {

    }

    //用户按下触摸屏、快速移动后松开
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mOverScroller.forceFinished(true);
        mOverScroller.fling(mEditor.getScrollX(),mEditor.getScrollY(),
                -(int) velocityX,-(int) velocityY,
                0, mEditor.getMaxX(), 0,mEditor.getMaxY());
        mEditor.invalidate();
        return true;
    }


    //单击之后短时间内没有再次单击
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    //在按下并抬起时发生
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }


    public OverScroller getOverScroller(){
        return mOverScroller;
    }
}
