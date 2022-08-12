package com.xiaojiangi.editor.text;





import java.util.LinkedList;

public final class TextManager {
    private LinkedList<Stack> stacks;
    private static final int MAX_STACK =256;
    public static long MERGE_TIME =5000L;
    private int stackPos;
    private boolean isExecutive;
    public TextManager() {
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

    public void undo(Content content){
        if (canUndo()){
            isExecutive =true;
            stacks.get(stackPos-1).undo(content);
            stackPos--;
            isExecutive =false;
        }

    }
    public void redo(Content content){
        if (canRedo()){
            isExecutive =true;
            stacks.get(stackPos).redo(content);
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
        InsertStack.text = text;
        push(InsertStack);
    }

    public void deleteStack(int startLine, int startColumn, int endLine, int endColumn,CharSequence text){
        if (isExecutive)
            return;
        var deleteStack =new DeleteStack();
        deleteStack.startLine =startLine;
        deleteStack.startColumn =startColumn;
        deleteStack.endColumn =endColumn;
        deleteStack.endLine =endLine;
        deleteStack.text =text;
        push(deleteStack);
    }


    private class InsertStack extends Stack{

        @Override
        public void undo(Content content) {
            content.delete(startLine,startColumn,endLine,endColumn);
        }

        @Override
        public void redo(Content content) {
            content.insert(startLine,startColumn,text);
        }

        @Override
        public boolean canMerge(Stack stack) {
            if (stack instanceof InsertStack){
                var InsertStack =(InsertStack)stack;
                return InsertStack.startColumn==endColumn && InsertStack.startLine==endLine
                        && InsertStack.text.length() +text.length() <10000 &&
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
            if (text instanceof StringBuilder) {
                sb = (StringBuilder) text;
            } else {
                sb = new StringBuilder(text);
                text = sb;
            }
            sb.append(InsertStack.text);
        }

    }

    private class DeleteStack extends Stack{

        @Override
        public void undo(Content content) {
            content.insert(startLine,startColumn,text);
        }

        @Override
        public void redo(Content content) {
            content.delete(startLine,startColumn,endLine,endColumn);
        }

        @Override
        public boolean canMerge(Stack stack) {
            if (stack instanceof DeleteStack){
                var DeleteStack =(DeleteStack)stack;
                return DeleteStack.startColumn==endColumn && DeleteStack.startLine==endLine
                        && DeleteStack.text.length() +text.length() <10000 &&
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
            if (text instanceof StringBuilder) {
                sb = (StringBuilder) text;
            } else {
                sb = new StringBuilder(text);
                text = sb;
            }
            sb.insert(0,DeleteStack.text);
        }


    }
    private abstract class Stack{
        public int startLine, endLine, startColumn, endColumn;
        public long time = System.currentTimeMillis();
        public CharSequence text;
        public abstract void undo(Content content);
        public abstract void redo(Content content);
        public abstract boolean canMerge(Stack stack);
        public abstract void merge(Stack stack);

    }
}
