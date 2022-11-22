package com.xiaojiangi.editor.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.xiaojiangi.editor.theme.AbstractColorTheme;
import com.xiaojiangi.editor.theme.EditorColorTheme;

public class Painter {
    private final CodeEditor mEditor;
    private AbstractColorTheme mEditorColorTheme;
    private Paint mPaint;
    private Paint mNumberPaint;
    private Paint mOtherPaint;
    private float spaceLength;
    private float tabLength;
    private int tabCount;
    private float dpUnit;

    public Painter(CodeEditor codeEditor) {
        this.mEditor = codeEditor;
        mPaint = new Paint();
        mOtherPaint = new Paint();
        mNumberPaint = new Paint();
        mPaint.setAntiAlias(true);
        mOtherPaint.setAntiAlias(true);
        mNumberPaint.setAntiAlias(true);
        dpUnit = mEditor.getDpUnit();
        setSpaceWidth();
    }

    protected void onDraw(Canvas canvas) {

        var mText = mEditor.getContent();
        int visibleLineStart = Math.max((int) (mEditor.getOverScroller().getCurrY() / getLineHeight()), 0);
        int visibleLineEnd = Math.min(mText.size(), (int) ((mEditor.getHeight() + mEditor.getOverScroller().getCurrY()) / getLineHeight() + 1));
        /*
          行号偏移量
         */
        float lineNumberOffset = mPaint.measureText(String.valueOf(mText.size())) + (2 * mEditor.getDpUnit());

        /*
         *  行号背景偏移量
         */
        float lineNumberBackgroundOffset = lineNumberOffset + (8 * dpUnit);
        /*
         * 行文本偏移量
         */
        float lineTextOffset = lineNumberBackgroundOffset + (4 * dpUnit);

        /*
         * 通过 (可视行*行高) 来计算行号背景和行号线的x轴
         */
        float start = visibleLineStart * getLineHeight();
        float end = visibleLineEnd * getLineHeight();

        if (visibleLineEnd == mText.size())
            end += canvas.getHeight();

        drawLineNumberBackground(start, end, lineNumberBackgroundOffset, canvas);
        drawLineNumberLine(start, end, lineNumberBackgroundOffset, canvas);
        drawLineNumberAndText(visibleLineStart, visibleLineEnd, lineNumberOffset, lineTextOffset, canvas);

    }

    /**
     * 绘制行号和文本内容
     *
     * @param visibleLineStart 绘制的第一行
     * @param visibleLineEnd   绘制的最后一行
     * @param numberOffset     行号偏移量
     * @param textOffset       文本偏移量
     * @param canvas           画布
     */
    protected void drawLineNumberAndText(int visibleLineStart, int visibleLineEnd, float numberOffset, float textOffset, Canvas canvas) {
        mNumberPaint.setColor(mEditorColorTheme.getColor(EditorColorTheme.LINE_NUMBER));
        mNumberPaint.setTextAlign(Paint.Align.RIGHT);

        for (int i = visibleLineStart; i < visibleLineEnd; i++) {
            /* 当前行文本基线 */
            var textBaseLine = (getLineHeight() * i) - mPaint.ascent();

            canvas.drawText(String.valueOf(i + 1), numberOffset, textBaseLine, mNumberPaint);
        }
    }

    /**
     * 绘制行号背景
     *
     * @param lineTopX    开始位置的X轴
     * @param lineBottomX 结束位置的X轴
     * @param offset      行号背景偏移量
     */
    protected void drawLineNumberBackground(float lineTopX, float lineBottomX, float offset, Canvas canvas) {
        mOtherPaint.setColor(mEditorColorTheme.getColor(EditorColorTheme.LINE_NUMBER_BACKGROUND));
        mOtherPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawRect(0f, lineTopX, offset, lineBottomX, mOtherPaint);
    }

    /**
     * 绘制行号线
     *
     * @param lineTopX    开始位置的X轴
     * @param lineBottomX 结束位置的X轴
     * @param offset      行号背景偏移量
     */
    protected void drawLineNumberLine(float lineTopX, float lineBottomX, float offset, Canvas canvas) {
        mOtherPaint.setColor(mEditorColorTheme.getColor(EditorColorTheme.LINE_COLOR));
        mOtherPaint.setStrokeWidth(2);
        canvas.drawLine(offset, lineTopX, offset, lineBottomX, mOtherPaint);
    }

    /**
     * 绘制视图背景颜色
     */
    protected void drawViewBackground() {
        mEditor.setBackgroundColor(mEditorColorTheme.getColor(EditorColorTheme.CODE_BACKGROUND));
    }

    protected void setColorTheme(AbstractColorTheme colorTheme) {
        mEditorColorTheme = colorTheme;
        drawViewBackground();
    }

    protected void setTextTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
        mNumberPaint.setTypeface(typeface);
    }

    public void setTextSize(float size) {
        mNumberPaint.setTextSize(size * dpUnit);
        mPaint.setTextSize(size * dpUnit);
    }

    protected void setTabWidth(int tabSpaceCount) {
        tabCount = tabSpaceCount;
        tabLength = tabSpaceCount * spaceLength;
    }

    protected float getSpaceWidth() {
        return spaceLength;
    }

    protected void setSpaceWidth() {
        spaceLength = mPaint.measureText(" ");
    }

    protected float getLineHeight() {
        return mPaint.descent() - mPaint.ascent();
    }

    protected float getMaxTextLineLength(char[] chars, int count) {
        return mPaint.measureText(chars, 0, count);
    }

    protected float getMaxTextLineLength(char[] chars) {
        return mPaint.measureText(chars, 0, chars.length);
    }
}
