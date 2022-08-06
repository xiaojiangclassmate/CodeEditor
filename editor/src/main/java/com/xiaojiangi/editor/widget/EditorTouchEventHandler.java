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
        return true;
    }

    //拖动
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return true;
    }

    //长按
    @Override
    public void onLongPress(MotionEvent e) {

    }

    //用户按下触摸屏、快速移动后松开
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return true;
    }


    //单击之后短时间内没有再次单击，才会触发该函数
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
