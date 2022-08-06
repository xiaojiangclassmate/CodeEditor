package com.xiaojiangi.editor.text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Content {
    private final List<ContentLine> mList;
    private ContentLine maxLength;
    public Content() {
        this(null);
    }

    public Content(@Nullable CharSequence text) {
        mList =new ArrayList<>();
        maxLength =new ContentLine();
        mList.add(maxLength);
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
                    if (maxLength.length < column)
                        maxLength = currentLine;
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
        if (maxLength.length < currentLine.length())
            maxLength = currentLine;
    }
    public Content append(int line,char c){
        var contentLine =get(line);
        contentLine.append(c);
        if (contentLine!=maxLength)
            if (contentLine.length> maxLength.length())
                maxLength =contentLine;
        return this;
    }
    public int size(){
        return mList.size();
    }
    public ContentLine get(int pos){
        return mList.get(pos);
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
