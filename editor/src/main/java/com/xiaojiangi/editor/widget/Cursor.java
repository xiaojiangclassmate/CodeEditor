package com.xiaojiangi.editor.widget;

public class Cursor {
    private int line;
    private int column;

    public void restart(){
        line=column=0;
    }
    public void set(int line, int column) {
        this.line = line;
        this.column = column;
    }
    public Cursor() {
        line=column=0;
    }
    public Cursor(int line, int column) {
        this.line = line;
        this.column = column;
    }
}
