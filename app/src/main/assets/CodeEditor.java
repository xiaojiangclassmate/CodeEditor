package com.xiaojiangi.editor.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.OverScroller;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xiaojiangi.editor.text.Content;
import com.xiaojiangi.editor.text.TextManager;
import com.xiaojiangi.editor.theme.BaseCodeTheme;

public class CodeEditor extends View {

    protected float mDpUnit;
    private Content mText;
    private BaseCodeTheme mTheme;
    private Painter mPainter;
    private Cursor mCursor;
    private boolean isCursor=true;
    private EditorInputConnection mInputConnection;
    private InputMethodManager mInputMethodManager;
    private GestureDetector mGestureDetector;
    private EditorTouchEventHandler mEventHandler;
    private OverScroller mOverScroller;
    private TextManager mTextManager;
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
        setCursor(true);
        setTextSize(18);
        setText("");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPainter.onDraw(canvas);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean r1 =mGestureDetector.onTouchEvent(event);
        boolean r2 =mEventHandler.onTouchEvent(event);
        return (r1||r2 );
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
        return mInputConnection;
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()){
            scrollTo(mOverScroller.getCurrX(),mOverScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mCursor.dpadLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mCursor.dpadRight();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                mCursor.dpadUp();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mCursor.dpadDown();
                break;
        }
        switch (keyCode){
            case KeyEvent.KEYCODE_DEL:
                deleteText(" ",mCursor.line,mCursor.column);
                break;
            case KeyEvent.KEYCODE_ENTER:
                commitText("\n");
                break;

        }
        return super.onKeyDown(keyCode, event);
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
    public void hideSoftInput(){
        if(mInputMethodManager.isActive())
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void setText(@Nullable CharSequence text){
        if (text==null)
            text="";

        mOverScroller.startScroll(0,0,0,0);
        mText =new Content(text);
        mCursor =mText.getCursor();
        invalidate();
    }
    public void commitText(CharSequence text){
        insertText(text, mCursor.line, mCursor.column);
    }

    public void insertText(CharSequence text,int line,int column){
        mCursor.set(line,column);
        mText.insert(mCursor,text);
        invalidate();
    }

    public void deleteText(CharSequence text,int line,int column){
        if (mCursor.line==0 &&mCursor.column==0)
            return;
        mCursor.set(line,column);
        mText.delete(mCursor,text);

        invalidate();
    }
    public void undo(){

    }
    public void redo(){
    }
    public void setTextSize(float size){
        mPainter.setPaintSize(size);
    }
    public float getTextSize(){
        return mPainter.getPaintSize();
    }
    public void setTextStyle(Typeface typeface){
        mPainter.setPaintTypeface(typeface);
    }
    public void setTabCount(int count){
        mPainter.setTabCount(count);
    }
    public int getTabCount(){
        return mPainter.getTabCount();
    }
    public float getLineHeight(){
        return mPainter.getLineHeight();
    }
    public Painter getPainter(){
        return mPainter;
    }
    public Cursor getCursor(){
        return mCursor;
    }

    public boolean isCursor() {
        return isCursor;
    }
    public void setCursor(boolean b){
        isCursor =b;
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
    public Content getContent(){
        return mText;
    }
    protected OverScroller getOverScroller(){
        return mOverScroller;
    }
    public int getMaxX(){
        return (int)Math.max(0, mPainter.getOffset()+mPainter.measureText(mText.getMaxContentLine().toString()) - getWidth()/2f );
    }
    public int getMaxY(){
        return (int)Math.max(0, (mPainter.getLineHeight()*mText.size()- (getHeight()/2f)));
    }
}
