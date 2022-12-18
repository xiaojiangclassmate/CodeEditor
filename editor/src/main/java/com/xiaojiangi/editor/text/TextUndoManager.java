package com.xiaojiangi.editor.text;

import java.util.LinkedList;


public final class TextUndoManager {
    private LinkedList<Stack> stacks;
    private static final int MAX_STACK =256;
    public static long MERGE_TIME =5000L;
    private int stackPos;
    private boolean isExecutive;
    public TextUndoManager() {
        stacks =new LinkedList<>();
        stackPos =0;
        isExecutive =false;
    }

    public boolean canUndo(){
        return stackPos > 0;
    }
    public boolean canRedo(){
        return stackPos < stacks.size();
    }

    public void undo(Text t){
        if (canUndo()){
            isExecutive =true;
            stacks.get(stackPos-1).undo(t);
            stackPos--;
            isExecutive =false;
        }

    }
    public void redo(Text text){
        if (canRedo()){
            isExecutive =true;
            stacks.get(stackPos).redo(text);
            stackPos++;
            isExecutive =false;
        }

    }
    private void cleanBeforePush(){
        while (stackPos<stacks.size()){
            stacks.removeLast();
        }
    }
    private void clearPush(){
        while (stackPos>1 && stacks.size() > MAX_STACK){
            stacks.remove(0);
            stackPos--;
        }
    }

    private void push(Stack stack){
        cleanBeforePush();
        if (stacks.isEmpty()){
            stacks.add(stack);
            stackPos++;
        }else {
            var s =stacks.get(stackPos-1);
            if(s.canMerge(stack)){
                s.merge(stack);
            }else {
                stacks.add(stack);
                stackPos++;
            }
        }
        clearPush();
    }

    public void insertText(int startLine, int startColumn, int endLine, int endColumn,CharSequence text){
        if (isExecutive)
            return;
        var InsertStack =new InsertStack();
        InsertStack.startLine =startLine;
        InsertStack.startColumn =startColumn;
        InsertStack.endColumn =endColumn;
        InsertStack.endLine =endLine;
        InsertStack.mText = text;
        push(InsertStack);
    }

    public void deleteText(int startLine, int startColumn, int endLine, int endColumn,CharSequence text){
        if (isExecutive)
            return;
        var deleteStack =new DeleteStack();
        deleteStack.startLine =startLine;
        deleteStack.startColumn =startColumn;
        deleteStack.endColumn =endColumn;
        deleteStack.endLine =endLine;
        deleteStack.mText =text;
        push(deleteStack);
    }


    private class InsertStack extends Stack{

        @Override
        public void undo(Text text) {
            text.deleteText(startLine,startColumn,endLine,endColumn,mText);
        }

        @Override
        public void redo(Text text) {
            text.insertText(startLine,startColumn,mText);
        }

        @Override
        public boolean canMerge(Stack stack) {
            if (stack instanceof InsertStack InsertStack){
                return InsertStack.startColumn==endColumn && InsertStack.startLine==endLine
                        && InsertStack.mText.length() +mText.length() <10000 &&
                        Math.abs(InsertStack.time - time) < MERGE_TIME;
            }
            return false;
        }

        @Override
        public void merge(Stack stack) {
            var InsertStack=(InsertStack)stack;
            this.endLine =InsertStack.endLine;
            this.endColumn =InsertStack.endColumn;
            StringBuilder sb;
            if (mText instanceof StringBuilder) {
                sb = (StringBuilder) mText;
            } else {
                sb = new StringBuilder(mText);
                mText = sb;
            }
            sb.append(InsertStack.mText);
        }

    }

    private class DeleteStack extends Stack{

        @Override
        public void undo(Text text) {
            text.insertText(startLine,startColumn,mText);
        }

        @Override
        public void redo(Text text) {
            text.deleteText(startLine,startColumn,endLine,endColumn,mText);
        }

        @Override
        public boolean canMerge(Stack stack) {
            if (stack instanceof DeleteStack DeleteStack){
                return DeleteStack.startColumn==endColumn && DeleteStack.startLine==endLine
                        && DeleteStack.mText.length() +mText.length() <10000 &&
                        Math.abs(DeleteStack.time - time) < MERGE_TIME;
            }
            return false;
        }

        @Override
        public void merge(Stack stack) {
            var DeleteStack=(DeleteStack)stack;
            this.endLine =DeleteStack.endLine;
            this.endColumn =DeleteStack.endColumn;
            StringBuilder sb;
            if (mText instanceof StringBuilder) {
                sb = (StringBuilder) mText;
            } else {
                sb = new StringBuilder(mText);
                mText = sb;
            }
            sb.insert(0,DeleteStack.mText);
        }


    }
    private abstract class Stack{
        public int startLine, endLine, startColumn, endColumn;
        public long time = System.currentTimeMillis();
        public CharSequence mText;
        public abstract void undo(Text text);
        public abstract void redo(Text text);
        public abstract boolean canMerge(Stack stack);
        public abstract void merge(Stack stack);

    }
}
