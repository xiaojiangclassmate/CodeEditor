package com.xiaojiangi.editor.widget;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.xiaojiangi.editor.theme.EditorColorTheme;

public class Painter {
    private final CodeEditor mEditor;
    private EditorColorTheme mEditorColorTheme;
    private Paint mPaint;
    private Paint mNumberPaint;
    private Paint mOtherPaint;

    void onDraw(Canvas canvas) {

    }

    public void drawLineNumber() {

    }

    public Painter(CodeEditor mEditor) {
        this.mEditor = mEditor;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mOtherPaint = mNumberPaint = mPaint;
    }

    public void setColorTheme(EditorColorTheme colorTheme) {
        mEditorColorTheme = colorTheme;
        mEditor.invalidate();
    }
}
