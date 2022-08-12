package com.xiaojiangi.editor.widget;


import com.xiaojiangi.editor.text.Content;

public final class Cursor {
    private Content mText;
    public int line;
    public int column;

    public void restart(){
        line=column=0;
    }
    public void set(int line, int column) {
        this.line = line;
        this.column = column;
    }
    public void lineFeed(){
        line++;
        column=0;
    }
    public void dpadLeft(){
        if (column==0 && line!=0){
            line--;
            column =mText.get(line).length();
            return;
        }
        if (column!=0)
            column--;
    }
    public void dpadRight(){
        if (column ==mText.get(line).length() &&line != mText.size()-1){
            column=0;
            line++;
            return;
        }
        if (column!=mText.get(line).length())
            column++;
    }
    public void dpadUp(){
        if (line!=0)
            line--;
            if (column>mText.get(line).length())
                column =mText.get(line).length();
    }
    public void dpadDown(){
        if (line!=mText.size()-1)
            line++;
            if (column>mText.get(line).length())
                column =mText.get(line).length();
    }
    public Cursor(Content content) {
        mText =content;
        line=column=0;
    }
    public Cursor(Content content,int line, int column) {
        mText =content;
        this.line = line;
        this.column = column;
    }
}
