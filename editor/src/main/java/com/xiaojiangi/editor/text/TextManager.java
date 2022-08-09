package com.xiaojiangi.editor.text;

import com.xiaojiangi.editor.widget.CodeEditor;

import java.util.ArrayList;
import java.util.List;

public class TextManager {
    private CodeEditor mEditor;
    List<Undo> undo =new ArrayList<>();
    List<Redo> redo =new ArrayList<>();
    public TextManager(CodeEditor codeEditor) {
        mEditor =codeEditor;
    }
    public void undo(){
        if (undo.size()==0)
            return;
        var current =undo.get(undo.size()-1);
        undo.remove(undo.size()-1);
        mEditor.delete(current.text,current.line,current.column);

    }
    public void redo(){
        if (redo.size()==0)
            return;
        var current =redo.get(redo.size()-1);
        redo.remove(redo.size()-1);
        mEditor.insert(current.text,current.line,current.column);
    }
    public class Stack{
        ACTION action;
        int line;
        int column;
        CharSequence text;
    }
    public enum ACTION{
        ADD,DEL
    }
    class Undo extends Stack{
        public Undo(CharSequence text,int line ,int column) {
            super.action =ACTION.DEL;
            super.line =line;
            super.column =column;
            super.text =text;
        }
    }
    class Redo extends Stack{
        public Redo(CharSequence text,int line,int column) {
            super.action =ACTION.ADD;
            super.line =line;
            super.column =column;
            super.text =text;
        }
    }
    public void tracker(ACTION action,CharSequence text,int line,int column){
        if (action == ACTION.DEL){
            redo.add(new Redo(text, line, column));
        }else if (action == ACTION.ADD){
            undo.add(new Undo(text, line, column));
        }
    }
    public void restart(){
        undo.clear();
        redo.clear();
    }
}
