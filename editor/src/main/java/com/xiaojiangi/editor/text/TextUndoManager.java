package com.xiaojiangi.editor.text;

import java.util.LinkedList;

public final class TextUndoManager {
    private static final int MAX_STACK = 256;
    public static long MERGE_TIME = 5000L;
    private LinkedList<Stack> stacks;
    private int stackPos;
    private boolean isExecutive;

    public TextUndoManager() {
        stacks = new LinkedList<Stack>();
    }

    protected abstract static class Stack {
        public int lineStart, LineEnd, columnStart, columnEnd;
        public long time = System.currentTimeMillis();
        public CharSequence text;

        public abstract void undo(Text text);

        public abstract void redo(Text text);

        public abstract boolean canMerge(Stack stack);

        public abstract void merge(Stack stack);

    }

    protected class InsertStack extends Stack {

        @Override
        public void undo(Text text) {

        }

        @Override
        public void redo(Text text) {

        }

        @Override
        public boolean canMerge(Stack stack) {
            return false;
        }

        @Override
        public void merge(Stack stack) {

        }
    }
}
