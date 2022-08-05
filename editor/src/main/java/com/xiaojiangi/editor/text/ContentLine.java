package com.xiaojiangi.editor.text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentLine implements CharSequence {
    private char[] value;
    private int length;
    public ContentLine() {
        value =new char[16];
        length=0;
    }
    public ContentLine(@Nullable CharSequence text){
        if (text==null)
            text="";
        if (text instanceof String)
            ((String) text).toCharArray();
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return value[index];
    }

    @NonNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }
}
