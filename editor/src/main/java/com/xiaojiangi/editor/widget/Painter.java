package com.xiaojiangi.editor.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.xiaojiangi.editor.theme.BaseCodeTheme;

public class Painter {
    private final CodeEditor mEditor;
    private BaseCodeTheme mTheme;
    private final Paint mPaint;
    private final Paint mPaintOther;
    private float spaceWidth;
    private float tabWidth;
    private int tabCount;
    private float numberOffset;
    protected void onDraw(Canvas canvas){
        var mText=mEditor.getContent();
        int lineStart =Math.max((int)(mEditor.getOverScroller().getCurrY() /getLineHeight()),0);
        int lineEnd =Math.min(mText.size(),(int) ((mEditor.getHeight()+mEditor.getOverScroller().getCurrY()) /getLineHeight() +1));
        float lineNumberOffset =mPaint.measureText(String.valueOf(mText.size()));
        float lineNumberBackgroundOffset =lineNumberOffset+numberOffset;
        float start =lineStart*getLineHeight();
        float end =lineEnd*getLineHeight();

        if (lineEnd==mText.size())
            end+=canvas.getHeight();

        mPaintOther.setColor(mTheme.getColor(BaseCodeTheme.LINE_NUMBER_BACKGROUND));
        canvas.drawRect(0f,start,lineNumberBackgroundOffset,end,mPaint);
        Log.d("Editor",String.valueOf(mText.size()));
    }
    public Painter(CodeEditor codeEditor) {
        mEditor =codeEditor;
        mTheme =codeEditor.getTheme();
        mPaint =new Paint();
        mPaint.setAntiAlias(true);
        mPaintOther =new Paint();
        mPaintOther.setAntiAlias(true);
        tabCount =4;
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        numberOffset =8*codeEditor.mDpUnit;
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
    public void setPaintSize(float size){
        size =mEditor.mDpUnit*size;
        mPaint.setTextSize(size);
        mPaintOther.setTextSize(size);
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        mEditor.invalidate();
    }
    public void setPaintTypeface(Typeface typeface){
        mPaint.setTypeface(typeface);
        mPaintOther.setTypeface(typeface);
        spaceWidth =mPaint.measureText(" ");
        setTabCount(tabCount);
        mEditor.invalidate();
    }
    public void setNumberOffset(float offset){
        numberOffset=offset*mEditor.mDpUnit;
        mEditor.invalidate();
    }


}
