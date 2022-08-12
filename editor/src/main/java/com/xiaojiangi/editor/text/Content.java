package com.xiaojiangi.editor.text;

import android.util.Log;

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
    public void insert(int line, int column, @NonNull CharSequence text) {
        int endLine =line;
        int endColumn =column;
        var list = new LinkedList<ContentLine>();
        var currentLine = mList.get(endLine);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\n':
                    if (maxContentLine.length < endColumn)
                        maxContentLine = currentLine;
                    endLine++;
                    endColumn = 0;
                    var contentLine = new ContentLine();
                    currentLine = contentLine;
                    list.add(contentLine);
                    break;
                default:
                    currentLine.insert(endColumn,c);
                    endColumn++;
            }
        }
        mList.addAll(list);
        if (maxContentLine.length < currentLine.length())
            maxContentLine = currentLine;
        mTextManager.insertText(line,column,endLine,endColumn,text);
        Log.d("Editor insert","startLine: "+line+" startColumn: "+column +" endLine: "+endLine +" endColumn: "+endColumn+" text: "+text);
        mCursor.set(endLine,endColumn);
    }
    public Content insert(@NonNull CharSequence text){

        return this;
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
    public Content delete(int startLine,int startColumn,int endLine,int endColumn){
        if (startLine==endLine){

            var text =get(startLine).subSequence(startColumn,endColumn);
            get(startLine).delete(startColumn,endColumn);
            int length=endColumn-startColumn;
            mTextManager.deleteStack(startLine,startColumn-length,endLine-length,length,"");
            Log.d("Editor delete","startLine: "+startLine+" startColumn: "+(startColumn) +" endLine: "+endLine +" endColumn: "+(endColumn)+" text: "+text);
            mCursor.set(startLine,startColumn);
        }
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
    public Content delete(Cursor cursor,CharSequence text){
        if (text.length()==1){
            delete(cursor);
            return this;
        }
        var currentContentLine =get(cursor.line);
//        //删除该行全部内容时
//        if (text.length()==currentContentLine.length){
//            if (cursor.line!=0){
//                mList.remove(cursor.line);
//                cursor.line--;
//                cursor.column =get(cursor.line).length;
//            }else {
//                currentContentLine.clear();
//                cursor.restart();
//            }
//        }
        int line = cursor.line;
        int column = cursor.column;
        Log.d("Editor","column: "+column);
//        Log.d("Editor","text: "+get(line).delete(column-1,column));
        column--;
        cursor.set(line,column);
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
