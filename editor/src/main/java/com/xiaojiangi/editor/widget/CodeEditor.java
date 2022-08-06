package com.xiaojiangi.editor.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.xiaojiangi.editor.text.Content;
import com.xiaojiangi.editor.theme.BaseCodeTheme;

public class CodeEditor extends View {

    protected float mDpUnit;
    private Content mText;
    private BaseCodeTheme mTheme;
    private Painter mPainter;
    private EditorInputConnection mInputConnection;
    private InputMethodManager mInputMethodManager;
    private GestureDetector mGestureDetector;
    private EditorTouchEventHandler mEventHandler;
    private OverScroller mOverScroller;
    public CodeEditor(Context context) {this(context,null);}
    public CodeEditor(Context context, @Nullable AttributeSet attrs) {this(context, attrs,0);}
    public CodeEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {this(context, attrs, defStyleAttr,0);}
    public CodeEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        mDpUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics()) / 10F;
        mInputConnection =new EditorInputConnection(this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mEventHandler =new EditorTouchEventHandler(this);
        mOverScroller =mEventHandler.getOverScroller();
        mGestureDetector = new GestureDetector(getContext(), mEventHandler);
        mGestureDetector.setOnDoubleTapListener(mEventHandler);
        mText =new Content();
        mTheme =new BaseCodeTheme();
        mPainter =new Painter(this);
    }

    public void showSoftInput(){
        if (isInTouchMode()) {
            requestFocusFromTouch();
        }
        if (!hasFocus()) {
            requestFocus();
        }
        mInputMethodManager.showSoftInput(this, 0);
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPainter.onDraw(canvas);
    }
    public void setText(@Nullable CharSequence text){
        if (text==null)
            text="";
        mText =new Content(text);
        invalidate();
    }
    public CharSequence getText(){
        return mText.toString();
    }
    public BaseCodeTheme getTheme(){
        return mTheme;
    }
    public void setTheme(BaseCodeTheme theme){
        mTheme =theme;
    }
    protected Content getContent(){
        return mText;
    }
    protected OverScroller getOverScroller(){
        return mOverScroller;
    }
}
