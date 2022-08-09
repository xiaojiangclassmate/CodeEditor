package com.xiaojiangi.editor.widget;


public class Cursor {
    private CodeEditor mEditor;
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
        if (column!=0)
            column--;
            mEditor.invalidate();
    }
    public void dpadRight(){
        if (column!=mEditor.getContent().get(line).length())
            column++;
            mEditor.invalidate();
    }
    public void dpadUp(){
        if (line!=0)
            line--;
            if (column>mEditor.getContent().get(line).length())
                column =mEditor.getContent().get(line).length();
            mEditor.invalidate();
    }
    public void dpadDown(){
        if (line!=mEditor.getContent().size()-1)
            line++;
            if (column>mEditor.getContent().get(line).length())
                column =mEditor.getContent().get(line).length();
                mEditor.invalidate();
    }
    public Cursor(CodeEditor codeEditor) {
        mEditor =codeEditor;
        line=column=0;
    }
    public Cursor(CodeEditor codeEditor,int line, int column) {
        mEditor =codeEditor;
        this.line = line;
        this.column = column;
    }
}
