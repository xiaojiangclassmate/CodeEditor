package com.xiaojiangi.editor.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

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
    private EditorColorTheme mColorTheme;
    private Painter mPainter;
    private Text mText;
    private float mTextSize;

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
        setColorTheme(new EditorColorTheme());

        mEditorInputConnect = new EditorInputConnection(this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mPainter = new Painter(this);
        setEnableEdit(true);
        setTabWidth(DEFAULT_TAB_SPACE_COUNT);
        setTextSize(DEFAULT_TEXT_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPainter.onDraw(canvas);
    }

    @Override
    public EditorInputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
        return mEditorInputConnect;
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
        invalidate();
    }

    public void undo() {
        if (enableEdit) {
            mText.undo();
        }
    }

    public void redo() {
        if (enableEdit) {
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

    public EditorColorTheme getColorTheme() {
        return mColorTheme;
    }

    public void setColorTheme(@NonNull EditorColorTheme colorTheme) {
        this.mColorTheme = colorTheme;
        mPainter.setColorTheme(colorTheme);
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mPainter.setTextSize(textSize);
        invalidate();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTabWidth(int spaceCount) {
        if (spaceCount < 0)
            return;
        this.mTabSpaceCount = spaceCount;
        mPainter.setTabWidth(spaceCount);
        invalidate();
    }

    public int getTabWidth() {
        return mTabSpaceCount;
    }

}
