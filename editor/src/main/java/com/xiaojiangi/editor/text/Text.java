package com.xiaojiangi.editor.text;




import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Text {
    public  static final char LINE_BREAK ='\n';
    private List<TextLine> mList;
    private final TextUndoManager mUndoManager;
    private final Cursor mCursor;
    private TextLine maxTextLine;
    public Text() {
        this(null);
    }

    public Text(CharSequence text) {
        if (text == null)
            text = "";
        mList = new ArrayList<>();
        mUndoManager = new TextUndoManager();
        maxTextLine = new TextLine();
        mCursor = new Cursor(this);
        mList.add(maxTextLine);
        insert(text);
    }

    public Text insertText(int line, int column,@NonNull CharSequence text) {
        checkTextLineAndColumn(line, column);
        var current = mList.get(line);
        var linkedList = new LinkedList<TextLine>();
        int workLine = line;
        int workColumn =column;
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == LINE_BREAK) {
                current.insert(workColumn, text.subSequence(index, i));
                current = new TextLine();
                linkedList.add(current);
                workColumn = 0;
                index = i+1;
                workLine++;
            }
        }
        if (index != text.length()) {
            var sub = text.subSequence(index, text.length());
            current.insert(workColumn, sub);
            workColumn += sub.length();
        }
        if (workLine!=line){
            var lastLine = get(line);
            current.append(lastLine.delete(column,lastLine.length()));
        }
        if (linkedList.size() != 0) {
            mList.addAll(line + 1, linkedList);
        }
        setMaxTextLine();
        mCursor.set(workLine, workColumn);
        mUndoManager.insertText(line,column,workLine,workColumn,text);
        return this;
    }
    public Text deleteText(int startLine, int startColumn, int endLine, int endColumn,CharSequence text) {
        checkTextLine(startLine);
        checkTextLine(endLine);
        if (startLine == endLine) {
            var textLine = mList.get(startLine);
            mUndoManager.deleteText(startLine,startColumn,endLine,endColumn,textLine.delete(startColumn, endColumn));
        }else {
            if (endColumn==0){
                StringBuilder sb =new StringBuilder();
                for (int i = startLine; i <=endLine ; i++) {
                    var textLine =get(i);
                    if (i ==startLine){
                        sb.append(textLine.delete(startColumn,textLine.length()));
                    }else if (i ==endLine) {
                        sb.append(remove(textLine));
                    }else {
                        sb.append(remove(i));
                    }

                }
                get(startLine).append(sb);
                mUndoManager.deleteText(startLine,startColumn,endLine,endColumn,sb);
            }
        }
        mCursor.set(startLine,startColumn);
        return this;
    }

    /**
     * 删除列3前一个字符
     * @param deleteLine 光标行
     * @param deleteColumn 光标列
     */
    public Text deleteText(int deleteLine, int deleteColumn){
        int endLine=deleteLine;
        int endColumn =deleteColumn;
        if (deleteColumn ==0){
            if (deleteLine !=0){
                if (deleteColumn ==get(deleteLine).length()){
                    TextLine textLine =remove(deleteLine);
                    TextLine last =get(--deleteLine);
                    deleteColumn = last.length();
                    last.append(textLine);
                    mCursor.set(deleteLine,deleteColumn);
                    mUndoManager.deleteText(deleteLine,deleteColumn,endLine,endColumn,"\n");
                }else {
                    deleteColumn =get(deleteLine-1).length();
                    get(deleteLine-1).append(get(deleteLine));
                    remove(deleteLine);
                    mCursor.set(--deleteLine,deleteColumn);
                }

            }
            return this;
        }
        var text=get(deleteLine).delete(--deleteColumn,endColumn);
        mCursor.set(deleteLine,deleteColumn);
        mUndoManager.deleteText(deleteLine,endColumn-text.length(),endLine,endColumn,text);
        return this;
    }
    public TextLine get(int line) {
        checkTextLine(line);
        return mList.get(line);
    }

    public TextLine max() {
        return maxTextLine;
    }

    public int maxLength() {
        return maxTextLine.length();
    }

    public void undo() {
        mUndoManager.undo(this);
    }

    public void redo() {
        mUndoManager.redo(this);
    }

    public int getTextLineCount() {
        return mList.size();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public int getCursorLine() {
        return mCursor.line;
    }

    public int getCursorColumn() {
        return mCursor.column;
    }

    public void setCursorPos(int line, int column) {
        mCursor.set(line, column);
    }

    public void cursorMoveToLeft() {
        mCursor.moveToLeft();
    }

    public void cursorMoveToRight() {
        mCursor.moveToRight();
    }

    public void cursorMoveToTop() {
        mCursor.moveToTop();
    }

    public void cursorMoveToBottom() {
        mCursor.moveToBottom();
    }

    private void insert(@NonNull CharSequence text) {
        checkTextLineAndColumn(0, 0);
        var current = mList.get(0);
        var linkedList = new LinkedList<TextLine>();
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                current.insert(0, text.subSequence(index, i-1));
                current = new TextLine();
                linkedList.add(current);
                index = i+1;
            }
        }
        if (index != text.length()) {
            current.insert(0, text.subSequence(index, text.length()));
        }
        if (linkedList.size() != 0) {
            mList.addAll(1, linkedList);
        }
        setMaxTextLine();
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

    private void setMaxTextLine() {
        TextLine textLine = get(0);
        for (int i = 0; i < mList.size(); i++) {
            if (textLine.length() < mList.get(i).length())
                textLine = mList.get(i);
        }
        maxTextLine =textLine;
    }
    private TextLine remove(int index){
        checkTextLine(index);
        var current =mList.get(index);
        mList.remove(current);
        return current;
    }
    private TextLine remove(TextLine textLine){
        mList.remove(textLine);
        return textLine;
    }


    @NonNull
    @Override
    public String toString() {
        if (mList.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            sb.append(mList.get(i)).append(LINE_BREAK);
        }
        return sb.toString();
    }
}
