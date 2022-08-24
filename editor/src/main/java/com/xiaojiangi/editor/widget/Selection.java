package com.xiaojiangi.editor.widget;

public class Selection {
    private int lineStart, columnStart, lineEnd, columnEnd;

    public Selection() {
        lineStart = columnStart = lineEnd = columnEnd = 0;
    }

    public Selection(int lineStart, int columnStart, int lineEnd, int columnEnd) {
        this.lineStart = lineStart;
        this.columnStart = columnStart;
        this.lineEnd = lineEnd;
        this.columnEnd = columnEnd;
    }

    public void set(int lineStart, int columnStart, int lineEnd, int columnEnd) {
        this.lineStart = lineStart;
        this.columnStart = columnStart;
        this.lineEnd = lineEnd;
        this.columnEnd = columnEnd;
    }

    public void restart() {
        lineStart = columnStart = lineEnd = columnEnd = 0;
    }

    public boolean isSelection() {
        return !(lineStart == lineEnd && columnStart == columnEnd);
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getColumnStart() {
        return columnStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public int getColumnEnd() {
        return columnEnd;
    }
}
