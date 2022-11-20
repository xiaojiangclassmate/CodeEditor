package com.xiaojiangi.editor.text;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Text {
    private List<TextLine> mList;
    private final TextUndoManager mUndoManager;
    public Text() {
        this(null);
    }

    public Text(CharSequence text) {
        if (text == null)
            text = "";
        mList = new ArrayList<>();
        mUndoManager = new TextUndoManager();
        mList.add(new TextLine());
        insert(0, 0, text);
    }

    public Text insert(int line, int column, CharSequence text) {
        checkTextLineAndColumn(line, column);
        var current = mList.get(line);
        var linkedList = new LinkedList<TextLine>();
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                current.insert(column, text.subSequence(index, i));
                current = new TextLine();
                linkedList.add(current);
                column = 0;
                index = i;
            }
        }
        if (index != text.length()) {
            current.insert(column, text.subSequence(index, text.length()));
            current = new TextLine();
            linkedList.add(current);
        }
        if (linkedList.size() != 0) {
            mList.addAll(line, linkedList);
        }
        return this;
    }

    public Text delete(int startLine, int startColumn, int endLine, int endColumn) {
        checkTextLine(startLine);
        checkTextLine(endLine);
        if (startLine == endLine) {
            var textLine = mList.get(startLine);
            textLine.delete(startColumn, endColumn);
        }
        return this;
    }

    public TextLine get(int line) {
        checkTextLine(line);
        return mList.get(line);
    }

    private void checkTextLine(int line) {
        if (line > mList.size()) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkTextLineAndColumn(int line, int index) {
        checkTextLine(line);
        if (mList.get(line).length() < index)
            throw new ArrayIndexOutOfBoundsException();
    }

    public void undo() {

    }

    public void redo() {

    }

    @NonNull
    @Override
    public String toString() {
        if (mList.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            sb.append(mList.get(i)).append("\n");
        }
        return sb.toString();
    }
}