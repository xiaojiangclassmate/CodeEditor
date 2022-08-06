package com.xiaojiangi.editor.widget;

public class Cursor {
    private CodeEditor mEditor;
    protected int line;
    protected int column;

    public void restart(){
        line=column=0;
    }
    public void set(int line, int column) {
        this.line = line;
        this.column = column;
    }
    public Cursor(CodeEditor codeEditor) {
        line=column=0;
    }
    public Cursor(CodeEditor codeEditor,int line, int column) {
        mEditor =codeEditor;
        this.line = line;
        this.column = column;
    }
}
