package com.xiaojiangi.editor.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiaojiangi.editor.text.Text;
import com.xiaojiangi.editor.theme.AbstractColorTheme;
import com.xiaojiangi.editor.theme.EditorColorTheme;

public class CodeEditor extends View {

    /**
     * 默认文本字体大小
     */
    public static final float DEFAULT_TEXT_SIZE = 18f;
    /**
     * 默认制表符空格的数量
     */
    public static final int DEFAULT_TAB_SPACE_COUNT = 4;

    private boolean enableEdit;
    private int mTabSpaceCount;
    private EditorInputConnection mEditorInputConnect;
    private InputMethodManager mInputMethodManager;
    private AbstractColorTheme mColorTheme;
    private EditorTouchEventHandler mEditorTouchEventHandler;
    private EditorPainter mEditorPainter;
    private OverScroller mOverScroller;
    private Text mText;
    private float mTextSize;
    private float mDpUnit;
    private GestureDetector mGestureDetector;

    public CodeEditor(Context context) {
        this(context, null);
    }

    public CodeEditor(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CodeEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEnableEdit(true);
        mDpUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics()) / 10F;

        mEditorInputConnect = new EditorInputConnection(this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mOverScroller = new OverScroller(getContext());
        mEditorTouchEventHandler =new EditorTouchEventHandler(this);
        mGestureDetector = new GestureDetector(getContext(), mEditorTouchEventHandler);
        mGestureDetector.setOnDoubleTapListener(mEditorTouchEventHandler);
        mEditorPainter = new EditorPainter(this);
        setTabWidth(DEFAULT_TAB_SPACE_COUNT);
        setTextSize(DEFAULT_TEXT_SIZE);
        setTextTypeface(Typeface.MONOSPACE);
        setColorTheme(new EditorColorTheme());

        setText(null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEditorPainter.onDraw(canvas);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());
            postInvalidate();
        }
    }


    @Override
    public EditorInputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
        return mEditorInputConnect;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean r1 =mGestureDetector.onTouchEvent(event);
        boolean r2 =mEditorTouchEventHandler.onTouchEvent(event);
        return (r1||r2 );
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return isEnableEdit();
    }


    public void showSoftInput() {
        if (!isEnableEdit())
            return;
        if (isInTouchMode()) {
            requestFocusFromTouch();
        }
        if (!hasFocus()) {
            requestFocus();
        }
        mInputMethodManager.showSoftInput(this, 0);
        invalidate();
    }

    public void hideSoftInput() {
        if (mInputMethodManager.isActive())
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @NonNull
    public CharSequence getText() {
        return mText.toString();
    }

    public void setText(@Nullable CharSequence text) {
        mText = new Text(text);
        mOverScroller.startScroll(0, 0, 0, 0, 0);
        invalidate();
    }

    public void undo() {
        if (isEnableEdit()) {
            mText.undo();
        }
    }

    public void redo() {
        if (isEnableEdit()) {
            mText.redo();
        }
    }

    public boolean isEnableEdit() {
        return enableEdit;
    }

    public void setEnableEdit(boolean enableEdit) {
        this.enableEdit = enableEdit;
        invalidate();
    }

    public AbstractColorTheme getColorTheme() {
        return mColorTheme;
    }

    public void setColorTheme(@NonNull AbstractColorTheme colorTheme) {
        this.mColorTheme = colorTheme;
        mEditorPainter.setColorTheme(colorTheme);
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mEditorPainter.setTextSize(textSize);
        invalidate();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTabWidth(int spaceCount) {
        if (spaceCount < 0)
            return;
        this.mTabSpaceCount = spaceCount;
        mEditorPainter.setTabWidth(spaceCount);
        invalidate();
    }

    public int getTabWidth() {
        return mTabSpaceCount;
    }

    public void setTextTypeface(Typeface typeface) {
        mEditorPainter.setTextTypeface(typeface);
        invalidate();
    }

    public OverScroller getOverScroller() {
        return mOverScroller;
    }

    public int getViewMaxX() {
        return (int) Math.max(0, mEditorPainter.getMaxTextLineLength(mText.max().toCharArray(), mText.maxLength()));
    }

    public int getViewMaxY() {
        return (int) Math.max(0, (mEditorPainter.getLineHeight() * mText.size() - (getHeight() / 2f)));
    }

    protected Text getContent() {
        return mText;
    }

    protected float getDpUnit() {
        return mDpUnit;
    }
}