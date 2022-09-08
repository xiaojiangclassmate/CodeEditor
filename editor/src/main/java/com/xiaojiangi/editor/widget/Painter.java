package com.xiaojiangi.editor.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

import com.xiaojiangi.editor.R;
import com.xiaojiangi.editor.theme.BaseCodeTheme;

public class Painter {
    private CodeEditor mEditor;
    private BaseCodeTheme mTheme;
    private Cursor mCursor;
    private Selection mSelection;
    private HandShankStyle handShankStyle;
    private final Paint mPaint;
    private final Paint mPaintOther;
    private float spaceWidth;
    private float tabWidth;
    private int tabCount;
    private final float numberBackOffset;
    private final float textOffset;
    private float offset;
    protected void onDraw(Canvas canvas){
        var mText=mEditor.getContent();
        mCursor = mText.getCursor();
        int lineStart =Math.max((int)(mEditor.getOverScroller().getCurrY() /getLineHeight()),0);
        int lineEnd =Math.min(mText.size(),(int) ((mEditor.getHeight()+mEditor.getOverScroller().getCurrY()) /getLineHeight() +1));
        float lineNumberOffset =mPaint.measureText(String.valueOf(mText.size()))+2*mEditor.mDpUnit;
        float lineNumberBackgroundOffset =lineNumberOffset+numberBackOffset;
        offset =lineNumberBackgroundOffset+textOffset;
        float start = lineStart * getLineHeight();
        float end = lineEnd * getLineHeight();
        if (lineEnd == mText.size())
            end += canvas.getHeight();
        //全局背景
        mEditor.setBackgroundColor(mTheme.getColor(BaseCodeTheme.CODE_BACKGROUND));
        //绘制行号背景
        mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.LINE_NUMBER_BACKGROUND));
        mPaintOther.setTextAlign(Paint.Align.LEFT);
        canvas.drawRect(0f, start, lineNumberBackgroundOffset, end, mPaintOther);
        //选中时,left right 为选中文字的X轴
        //非选中时, left为光标x轴 ,right为y轴
        float left, right;
        left = right = 0f;
        //绘制选中
        if (mSelection.isSelection()) {
            if (lineStart <= mSelection.getLineStart() && mSelection.getLineEnd() <= lineEnd) {
                var contentLine = mText.get(mSelection.getLineStart());
                if (contentLine.length() == 0) {
                    left = offset;
                    right = spaceWidth + offset;
                } else {
                    left = measureText(contentLine, 0, mSelection.getColumnStart()) + offset;
                    right = measureText(contentLine, 0, mSelection.getColumnEnd()) + offset;
                }
                mPaint.setColor(mTheme.getColor(BaseCodeTheme.SELECTION_TEXT_BACKGROUND));
                canvas.drawRect(left, getLineHeight() * mSelection.getLineStart(), right, getLineHeight() * (mSelection.getLineStart() + 1), mPaint);

            }
        }
        //如果光标所在行在可视行中 则绘制光标和当前行背景
        if (lineStart <= mCursor.line && mCursor.line <= lineEnd && !mSelection.isSelection()) {
            //绘制当前行背景
            mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.CURRENT_LINE_BACKGROUND));
            float currX = mEditor.getOverScroller().getCurrX();
            canvas.drawRect(currX, getLineHeight() * mCursor.line, currX + canvas.getWidth(), getLineHeight() * (mCursor.line + 1), mPaintOther);

            //绘制光标
            if (mEditor.isCursor()) {
                var contentLine = mText.get(mCursor.line);
                int tab = 0;
                for (int i = 0; i < mCursor.column; i++) {
                    if (contentLine.charAt(i)=='\t')
                        tab += 1;
                }
                int column = mCursor.column;
                float cursorOffset = mPaint.measureText(contentLine, 0, column) + offset;
                if (tab != 0) {
                    cursorOffset -= tab * mPaint.measureText("\t");
                }
                mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.CURSOR_COLOR));
                mPaintOther.setStrokeWidth(4f);
                mPaintOther.setTextAlign(Paint.Align.CENTER);
                left = cursorOffset;
                right = (mCursor.line + 1) * getLineHeight();
                canvas.drawLine(cursorOffset, mCursor.line * getLineHeight(), cursorOffset, right, mPaintOther);

            }

        }
        //绘制分割线
        mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.LINE_COLOR));
        mPaintOther.setStrokeWidth(2);
        canvas.drawLine(lineNumberBackgroundOffset,start, lineNumberBackgroundOffset,end,mPaintOther);

        for (int i = lineStart; i < lineEnd; i++) {
            var textBaseLine =(getLineHeight()*i)-mPaint.ascent();
            mPaint.setColor(mTheme.getColor(BaseCodeTheme.LINE_NUMBER));
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(i+1),lineNumberOffset,textBaseLine,mPaint);
            mPaint.setColor(mTheme.getColor(BaseCodeTheme.TEXT_COLOR));
            mPaint.setTextAlign(Paint.Align.LEFT);
            float currentOffset=offset;
            var contentLine =mText.get(i);
            int offset = 0;
            for (int j = 0; j < contentLine.length(); j++) {
                if (contentLine.charAt(j) == '\t') {
                    canvas.drawText(contentLine, 0, j + 1, currentOffset, textBaseLine, mPaint);
                    currentOffset += tabWidth + mPaint.measureText(contentLine, 0, j + 1);
                    offset = j;
                }
            }
            canvas.drawText(contentLine, offset, contentLine.length(), currentOffset, textBaseLine, mPaint);
        }
        if (mSelection.isSelection()) {
            //绘制选中线
            mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.CURSOR_COLOR));
            mPaintOther.setStrokeWidth(4f);
            float startLineHeight = getLineHeight() * mSelection.getLineStart();
            float endLineHeight = getLineHeight() * mSelection.getLineEnd();
            canvas.drawLine(left, startLineHeight, left, startLineHeight + getLineHeight(), mPaintOther);
            canvas.drawLine(right, endLineHeight, right, endLineHeight + getLineHeight(), mPaintOther);
            handShankStyle.draw(canvas, mTheme.getColor(BaseCodeTheme.CURSOR_STYLE_COLOR), left, startLineHeight + getLineHeight(), right, endLineHeight + getLineHeight());
        } else {
            if (lineStart <= mCursor.line && mCursor.line <= lineEnd) {
                handShankStyle.draw(canvas, mTheme.getColor(BaseCodeTheme.CURSOR_STYLE_COLOR), left, right);
            }

        }
    }
    public Painter(CodeEditor codeEditor) {
        mEditor =codeEditor;
        mTheme = codeEditor.getTheme();
        mSelection = codeEditor.getSelection();
        handShankStyle = codeEditor.getHandShankStyle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaintOther =new Paint();
        mPaintOther.setAntiAlias(true);
        tabCount =4;
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        numberBackOffset =8*codeEditor.mDpUnit;
        textOffset =5*codeEditor.mDpUnit;
    }

    public float getLineHeight(){
        return mPaint.descent() -mPaint.ascent();
    }

    public void setTabCount(int count){
        tabCount =count;
        tabWidth =count*spaceWidth;
    }
    public int getTabCount(){
        return tabCount;
    }
    public float getTabWidth(){
        return tabWidth;
    }
    public void setPaintSize(float size){
        size =mEditor.mDpUnit*size;
        mPaint.setTextSize(size);
        mPaintOther.setTextSize(size);
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        mEditor.invalidate();
    }
    public float getPaintSize(){
        return mPaint.getTextSize();
    }

    public void setPaintTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
        mPaintOther.setTypeface(typeface);
        spaceWidth = mPaint.measureText(" ");
        setTabCount(tabCount);
        mEditor.invalidate();
    }

    public float measureText(String text) {
        return mPaint.measureText(text);
    }

    public float measureText(CharSequence text, int start, int end) {
        return mPaint.measureText(text, start, end);
    }

    public float getOffset() {
        return offset;
    }
}
