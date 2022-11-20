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
    private boolean enableEdit;

    private EditorInputConnection mEditorInputConnect;
    private InputMethodManager mInputMethodManager;
    private EditorColorTheme mColorTheme;
    private Painter mPainter;
    private Text mText;

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
        enableEdit = true;
        mEditorInputConnect = new EditorInputConnection(this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mPainter = new Painter(this);
        mColorTheme = new EditorColorTheme();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

    public void setColorTheme(EditorColorTheme mColorTheme) {
        this.mColorTheme = mColorTheme;

    }
}
