package com.xiaojiangi.editor.text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Content {
    private List<ContentLine> mList;
    private ContentLine maxContentLine;
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
