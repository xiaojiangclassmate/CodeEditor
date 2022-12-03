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

    private int mTabSpaceCount;
    private float mTextSize;
    private float mDpUnit;
    private boolean enableEdit;
    private boolean fixedLineNumber;
    private Text mText;
    private EditorInputConnection mEditorInputConnect;
    private InputMethodManager mInputMethodManager;
    private AbstractColorTheme mColorTheme;
    private EditorTouchEventHandler mEditorTouchEventHandler;
    private EditorPainter mEditorPainter;
    private OverScroller mOverScroller;
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
        setFixedLineNumber(false);
        mDpUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics()) / 10F;
        mEditorInputConnect = new EditorInputConnection(this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mOverScroller = new OverScroller(getContext());
        mEditorTouchEventHandler = new EditorTouchEventHandler(this);
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
        boolean r1 = mGestureDetector.onTouchEvent(event);
        boolean r2 = mEditorTouchEventHandler.onTouchEvent(event);
        return (r1 || r2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mText.cursorMoveToLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mText.cursorMoveToRight();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                mText.cursorMoveToTop();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mText.cursorMoveToBottom();
                break;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DEL -> deleteText();
            case KeyEvent.KEYCODE_ENTER -> insertText("\n");
        }
        invalidate();
        return true;

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
        restart();
        invalidate();
    }

    public void undo() {
        if (isEnableEdit()) {
            mText.undo();
        }
        invalidate();
    }

    public void redo() {
        if (isEnableEdit()) {
            mText.redo();
        }
        invalidate();
    }

    protected void insertText(CharSequence text) {
        mText.insert(mText.getCursorLine(), mText.getCursorColumn(), text);
        invalidate();
    }

    protected void deleteText() {

    }

    /**
     * @see #setEnableEdit(boolean)
     */
    public boolean isEnableEdit() {
        return enableEdit;
    }

    /**
     * 设置 CodeEditor是否可以编辑
     */
    public void setEnableEdit(boolean enableEdit) {
        this.enableEdit = enableEdit;
        invalidate();
    }

    /**
     * @see #setColorTheme(AbstractColorTheme)
     */
    public AbstractColorTheme getColorTheme() {
        return mColorTheme;
    }

    /**
     * 设置CodeEditor的颜色样式
     *
     */
    public <T extends AbstractColorTheme> void setColorTheme(@NonNull T theme) {
        this.mColorTheme = theme;
        mEditorPainter.setColorTheme(theme);
        invalidate();
    }

    /**
     * 得到字体的大小
     *
     * @see #setTextSize(float)
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * 设置CodeEditor的字体大小
     *
     * @param textSize 字体大小
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mEditorPainter.setTextSize(textSize);
        invalidate();
    }

    /**
     * @see #setFixedLineNumber(boolean is)
     */
    public boolean isFixedLineNumber() {
        return fixedLineNumber;
    }

    /**
     * 设置CodeEditor是否固定行号
     *
     * @param is
     */
    public void setFixedLineNumber(boolean is) {
        fixedLineNumber = is;
        invalidate();
    }

    /**
     * @return 制表符的空格数量
     * @see #setTabWidth(int)
     */
    public int getTabWidth() {
        return mTabSpaceCount;
    }

    /**
     * 设置制表符的宽度
     *
     * @param spaceCount 空格数量
     */
    public void setTabWidth(int spaceCount) {
        if (spaceCount < 0)
            return;
        this.mTabSpaceCount = spaceCount;
        mEditorPainter.setTabWidth(spaceCount);
        invalidate();
    }

    public void setTextTypeface(Typeface typeface) {
        mEditorPainter.setTextTypeface(typeface);
        invalidate();
    }

    /**
     * 跳转行
     *
     * @param lineNumber 行数
     */
    public void jumpToLine(int lineNumber) {
        if (mText.size() < lineNumber)
            return;
        lineNumber--;
        int endY;
        if (mEditorPainter.getVisibleLineStart() < lineNumber) {
            endY = -(int) ((mOverScroller.getCurrX() - lineNumber) * mEditorPainter.getLineHeight());
            mText.setCursorPos(lineNumber, 0);
        } else {
            endY = -(int) ((mEditorPainter.getVisibleLineStart() + lineNumber) * mEditorPainter.getLineHeight());
        }
        mOverScroller.startScroll(mOverScroller.getCurrX(), mOverScroller.getCurrY(), mOverScroller.getCurrX(), endY);
        invalidate();
    }

    protected OverScroller getOverScroller() {
        return mOverScroller;
    }

    protected int getScrollMaxX() {
        return (int) Math.max(0, mEditorPainter.getOffset() + mEditorPainter.measureTextWidth(mText.max().toCharArray()) - (getWidth() / 2f));
    }

    protected int getScrollMaxY() {
        return (int) Math.max(0, (mEditorPainter.getLineHeight() * mText.size() - (getHeight() / 2f)));
    }

    protected Text getContent() {
        return mText;
    }

    protected float getDpUnit() {
        return mDpUnit;
    }

    protected EditorPainter getEditorPainter() {
        return mEditorPainter;
    }
    private void restart(){
        mOverScroller.startScroll(0, 0, 0, 0, 0);
    }

}
