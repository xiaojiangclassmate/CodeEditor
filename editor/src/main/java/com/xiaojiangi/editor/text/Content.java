package com.xiaojiangi.editor.text;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiaojiangi.editor.widget.Cursor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Content {
    private List<ContentLine> mList;
    private ContentLine maxContentLine;
    private TextManager mTextManager;
    private Cursor mCursor;
    public Content() {
        this(null);
    }

    public Content(@Nullable CharSequence text) {
        mList =new ArrayList<>();
        mCursor = new Cursor(this);
        mTextManager =new TextManager();
        maxContentLine =new ContentLine();
        mList.add(maxContentLine);
        if (text==null)
            text="";
        
        int line=0;
        int column=0;
        var list = new LinkedList<ContentLine>();
        var currentLine = mList.get(line);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                maxContentLine(currentLine);
                line++;
                column = 0;
                var contentLine = new ContentLine();
                currentLine = contentLine;
                list.add(contentLine);
            } else {
                currentLine.insert(column, c);
                column++;
            }
        }
        if (mList.size() != 0){
            mList.addAll(list);
        }
        maxContentLine(currentLine);

    }
    public void insert(int line, int column, @NonNull CharSequence text) {
        int endLine =line;
        int endColumn =column;
        var currentLine = mList.get(endLine);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {//在行尾换行
                if (currentLine.length == endColumn) {
                    var contentLine = new ContentLine();
                    mList.add(endLine + 1, contentLine);
                    maxContentLine(currentLine);
                    currentLine = contentLine;
                } else {
                    mList.add(endLine + 1, new ContentLine(currentLine.subSequence(endColumn, currentLine.length)));
                    currentLine.delete(endColumn, currentLine.length);
                    maxContentLine(currentLine);
                }
                endLine++;
                endColumn = 0;
            } else {
                currentLine.insert(endColumn, c);
                endColumn++;
            }
        }
        maxContentLine(currentLine);
        mTextManager.insertText(line,column,endLine,endColumn,text);
        mCursor.set(endLine,endColumn);
    }

    public Content insert(Cursor cursor,@NonNull CharSequence text){
        if (text.length()==1) {
            if (text.charAt(0)=='\n'){
                var contentLine =mList.get(cursor.line);
                if (contentLine.length== cursor.column){
                    //行尾换行
                    mList.add(cursor.line+1,new ContentLine());
                    cursor.line++;
                    cursor.column=0;
                    return this;
                }else {
                    mList.add(cursor.line+1,new ContentLine(contentLine.subSequence(cursor.column,contentLine.length)));
                    contentLine.delete(cursor.column, contentLine.length);
                }

            }else {
                var contentLine =mList.get(cursor.line);
                contentLine.insert(cursor.column, text.charAt(0));
                cursor.column++;
                return this;
            }
        }
        var list = new LinkedList<ContentLine>();
        var currentLine = mList.get(cursor.line);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                if (maxContentLine.length < cursor.column)
                    maxContentLine = currentLine;
                cursor.line++;
                cursor.column = 0;
                var contentLine = new ContentLine();
                currentLine = contentLine;
                list.add(contentLine);
            } else {
                currentLine.insert(cursor.column, c);
                cursor.column++;
            }
        }
        mList.addAll(list);
        return this;
    }
    public Content delete(int startLine,int startColumn,int endLine,int endColumn) {

        //同行操作
        if (startLine == endLine) {
            var contentLine = get(startLine);
            int length = endColumn - startColumn;
            //删除行全部
            if (length == contentLine.length) {
                mTextManager.deleteStack(startLine, startColumn - length, endLine - length, length, new StringBuilder(contentLine).append('\n'));
                remove(startLine);
            } else {
                StringBuilder sb;
                if (startColumn == 0) {
                    sb = new StringBuilder('\n').append(get(startLine).subSequence(startColumn, endColumn));
                    get(startLine).delete(startColumn, endColumn);
                } else {
                    sb = new StringBuilder(get(startLine).subSequence(startColumn, endColumn));
                    get(startLine).delete(startColumn, endColumn);
                }

                mTextManager.deleteStack(startLine, startColumn - length, endLine, length, sb);
            }


        } else {
            var contentLine = get(startLine);
            //全部删除
            if (startLine == 0 && startColumn == 0 && endLine == (size() - 1) && endColumn == (get(endLine).length)) {
                mTextManager.deleteStack(startLine, startColumn, endLine, endColumn, toString());
                mList.clear();
                maxContentLine = new ContentLine();
                mList.add(maxContentLine);
                setCursor(startLine, startColumn);
                return this;
            }
            StringBuilder sb;
            if (contentLine.length == startColumn) {
                sb = new StringBuilder('\n').append(get(endLine).subSequence(0, endColumn));
                get(endLine).delete(0, endColumn);
                contentLine.append(get(endLine));
                remove(endLine);
                mTextManager.deleteStack(startLine, startColumn, endLine, endColumn, sb);
            }


        }
        setCursor(startLine, startColumn);
        return this;
    }
    public Content delete(Cursor cursor){
        var contentLine =get(cursor.line);
        if (contentLine.length==0){
            mList.remove(cursor.line);
            cursor.line--;
            cursor.column =get(cursor.line).length;
            return this;
        }
        if (cursor.column == 0 && cursor.line != 0){
            int len =contentLine.length;
            get(cursor.line-1).append(contentLine);
            remove(cursor.line);
            cursor.line--;
            cursor.column =get(cursor.line).length-len;
            return this;
        }
        contentLine.delete(cursor.column-1,cursor.column);
        cursor.column--;
        return this;
    }

    /**
     * 删除光标前的一个字符
     */
    public Content delete(int line,int column){
        int startLine =line;
        int startColumn =column;
        StringBuilder sb;
        if (column==0){
            var contentLine =get(startLine);
            if (contentLine.length==0){
                sb=new StringBuilder("\n");
                mList.remove(startLine);
                startLine--;
                startColumn =get(startLine).length;
            }else {
                var lastContentLine =get(line-1);
                startColumn=lastContentLine.length;
                startLine--;
                lastContentLine.append(contentLine);
                maxContentLine(lastContentLine);
                remove(line);
                sb =new StringBuilder("\n");
            }
        }else {
            sb =new StringBuilder(get(line).subSequence(startColumn-1,startColumn));
            get(line).delete(startColumn-1,startColumn);
            startColumn--;
        }
        mTextManager.deleteStack(startLine,startColumn,line,column,sb);
        setCursor(startLine,startColumn);
        return this;
    }
    public Content append(int line,char c){
        var contentLine =get(line);
        contentLine.append(c);
        maxContentLine(contentLine);
        return this;
    }
    public void undo(){
        mTextManager.undo(this);
    }
    public void redo(){
        mTextManager.redo(this);
    }
    public Cursor getCursor(){
        return mCursor;
    }
    public int size(){
        return mList.size();
    }
    public ContentLine get(int pos){
        return mList.get(pos);
    }
    public void remove(int pos){
        mList.remove(pos);
    }
    public ContentLine getMaxContentLine(){
        return maxContentLine;
    }

    private void setCursor(int line,int column){
        if (line >= mList.size()) {
            throw new StringIndexOutOfBoundsException("cursor line greater than Content size.");
        }
        if (column > get(line).length) {
            throw new StringIndexOutOfBoundsException("cursor column greater than ContentLine length.");
        }
        mCursor.set(line,column);
    }
    private void maxContentLine(ContentLine contentLine){
        if (maxContentLine!=contentLine){
            if (contentLine.length> maxContentLine.length())
                maxContentLine =contentLine;
        }
    }
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            sb.append(mList.get(i).toString()).append('\n');
        }
        return sb.toString();
    }
}
