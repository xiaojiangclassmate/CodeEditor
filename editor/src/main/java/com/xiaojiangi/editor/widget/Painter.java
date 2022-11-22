package com.xiaojiangi.editor.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.xiaojiangi.editor.theme.EditorColorTheme;

public class Painter {
    private final CodeEditor mEditor;
    private EditorColorTheme mEditorColorTheme;
    private Paint mPaint;
    private Paint mNumberPaint;
    private Paint mOtherPaint;
    private float spaceLength;
    private float tabLength;
    private int tabCount;

    protected void onDraw(Canvas canvas) {

    }

    void drawLineNumber() {

    }

    public Painter(CodeEditor codeEditor) {
        this.mEditor = codeEditor;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mOtherPaint = mNumberPaint = mPaint;
        setSpaceWidth();
    }

    public void setColorTheme(EditorColorTheme colorTheme) {
        mEditorColorTheme = colorTheme;
        mEditor.invalidate();
    }

    public void setTextTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
        mNumberPaint.setTypeface(typeface);
    }

    public void setTextSize(float size) {

    }

    void setTabWidth(int tabSpaceCount) {
        tabCount = tabSpaceCount;
        tabLength = tabSpaceCount * spaceLength;
    }

    float getSpaceWidth() {
        return spaceLength;
    }

    void setSpaceWidth() {
        spaceLength = mPaint.measureText(" ");
    }

    float getLineHeight() {
        return mPaint.descent() - mPaint.ascent();
    }
}
