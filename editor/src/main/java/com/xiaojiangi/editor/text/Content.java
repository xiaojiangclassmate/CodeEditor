package com.xiaojiangi.editor.text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiaojiangi.editor.widget.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Content {
    private List<ContentLine> mList;
    private ContentLine maxContentLine;
    private StringBuilder a;
    public Content() {
        this(null);
    }

    public Content(@Nullable CharSequence text) {
        mList =new ArrayList<>();
        maxContentLine =new ContentLine();
        mList.add(maxContentLine);
        if (text==null)
            text="";
        insert(0,0,text);
    }
    public void insert(int line, int column, @NonNull CharSequence text) {
        var list = new LinkedList<ContentLine>();
        var currentLine = mList.get(line);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\n':
                    if (maxContentLine.length < column)
                        maxContentLine = currentLine;
                    line++;
                    column = 0;
                    var contentLine = new ContentLine();
                    currentLine = contentLine;
                    list.add(contentLine);
                    break;
                default:
                    currentLine.insert(column,c);
                    column++;
            }
        }
        mList.addAll(list);
        if (maxContentLine.length < currentLine.length())
            maxContentLine = currentLine;
    }
    public Content insert(Cursor cursor,@NonNull CharSequence text){
        if (text.length()==1) {
            if (text.charAt(0)=='\n'){
                var contentLine =mList.get(cursor.line);
                if (contentLine.length== cursor.column){
                    mList.add(cursor.line+1,new ContentLine());
                    cursor.line++;
                    cursor.column=0;
                    return this;
                }
                mList.add(cursor.line+1,new ContentLine(contentLine.subSequence(cursor.column,contentLine.length)));
                contentLine.delete(cursor.column, contentLine.length);
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
    public Content delete(Cursor cursor){
        var contentLine =get(cursor.line);
        if (contentLine.length==0){
            mList.remove(cursor.line);
            cursor.line--;
            cursor.column =get(cursor.line).length;
            return this;
        }
        if (cursor.column == 0 && cursor.line != 0){
            get(cursor.line-1).append(contentLine);
            remove(cursor.line);
            cursor.line--;
            cursor.column =get(cursor.line).length;
            return this;
        }
        contentLine.delete(cursor.column-1,cursor.column);
        cursor.column--;
        return this;
    }
    public Content append(int line,char c){
        var contentLine =get(line);
        contentLine.append(c);
        if (contentLine!=maxContentLine)
            if (contentLine.length> maxContentLine.length())
                maxContentLine =contentLine;
        return this;
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
