package com.xiaojiangi.editor.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.xiaojiangi.editor.theme.BaseCodeTheme;

public class Painter {
    private CodeEditor mEditor;
    private BaseCodeTheme mTheme;
    private Cursor mCursor;
    private final Paint mPaint;
    private final Paint mPaintOther;
    private float spaceWidth;
    private float tabWidth;
    private int tabCount;
    private final float numberOffset;
    private final float textOffset;
    private float offset;
    protected void onDraw(Canvas canvas){
        var mText=mEditor.getContent();
        int lineStart =Math.max((int)(mEditor.getOverScroller().getCurrY() /getLineHeight()),0);
        int lineEnd =Math.min(mText.size(),(int) ((mEditor.getHeight()+mEditor.getOverScroller().getCurrY()) /getLineHeight() +1));
        float lineNumberOffset =mPaint.measureText(String.valueOf(mText.size()));
        float lineNumberBackgroundOffset =lineNumberOffset+numberOffset;
        offset =lineNumberBackgroundOffset+textOffset;
        float start =lineStart*getLineHeight();
        float end =lineEnd*getLineHeight();

        if (lineEnd==mText.size())
            end+=canvas.getHeight();

        if (mEditor.isCursor()){
            if (lineStart<=mCursor.line && mCursor.line <=lineEnd){
                var contentLine =mText.get(mCursor.line);
                int tab=0;
                for (int i = 0; i < mCursor.column; i++) {
                    if (contentLine.charAt(i)=='\t')
                        tab+=1;
                }
                float cursorOffset= mPaint.measureText(contentLine,0,mCursor.column)+offset;
                if (tab != 0) {
                    cursorOffset-=tab*mPaint.measureText("\t");
                }
                mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.CURSOR_COLOR));
                mPaintOther.setStrokeWidth(4);
                mPaintOther.setTextAlign(Paint.Align.CENTER);
                canvas.drawLine(cursorOffset,mCursor.line*getLineHeight(),cursorOffset,(mCursor.line+1)*getLineHeight(),mPaintOther);

            }
        }

        //全局背景
        mEditor.setBackgroundColor(mTheme.getColor(BaseCodeTheme.CODE_BACKGROUND));
        //绘制行号背景
        mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.LINE_NUMBER_BACKGROUND));
        mPaintOther.setTextAlign(Paint.Align.LEFT);
        canvas.drawRect(0f,start,lineNumberBackgroundOffset,end,mPaintOther);

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
            int offset=0;
            for (int j = 0; j <contentLine.length() ; j++) {
                if (contentLine.charAt(j)=='\t') {
                    canvas.drawText(contentLine,0,j+1,currentOffset,textBaseLine,mPaint);
                    currentOffset +=tabWidth+mPaint.measureText(contentLine,0,j+1);
                    offset=j;
                }
            }
            canvas.drawText(contentLine,offset,contentLine.length(),currentOffset,textBaseLine,mPaint);
        }
    }
    public Painter(CodeEditor codeEditor) {
        mEditor =codeEditor;
        mCursor = codeEditor.getCursor();
        mTheme =codeEditor.getTheme();
        mPaint =new Paint();
        mPaint.setAntiAlias(true);
        mPaintOther =new Paint();
        mPaintOther.setAntiAlias(true);
        tabCount =4;
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        numberOffset =8*codeEditor.mDpUnit;
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
    public void setPaintTypeface(Typeface typeface){
        mPaint.setTypeface(typeface);
        mPaintOther.setTypeface(typeface);
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        mEditor.invalidate();
    }
    public float measureText(String text){
        return mPaint.measureText(text);
    }
    public float getOffset(){
        return offset;
    }
}
