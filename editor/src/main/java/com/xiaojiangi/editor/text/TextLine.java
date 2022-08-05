package com.xiaojiangi.editor.text;

import android.text.GetChars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class TextLine implements CharSequence, GetChars {
    private int length;
    private char[] value;

    public TextLine() {
        this(null);
    }

    public TextLine(@Nullable CharSequence csq) {
        if (csq == null)
            csq = "";
        length = csq.length();
        value = new char[length + 16];
        if (csq instanceof String str) {
            value = str.toCharArray();
            return;
        }
        for (int i = 0; i < csq.length(); i++) {
            value[i] = csq.charAt(i);
        }
    }

    public TextLine(int length) {
        this.length = length;
        value = new char[length + 16];
    }

    public TextLine insert(CharSequence csq, int offset) {
        if (offset < 0 || offset > length)
            throw new ArrayIndexOutOfBoundsException();

        return this;
    }

    public TextLine append(CharSequence csq) {
        if (csq instanceof String str) {

            return this;
        }
        return this;
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity - value.length > 0) {
            int newLength = value.length * 2 < minimumCapacity ? minimumCapacity + 2 : value.length * 2;
            char[] copy = new char[newLength];
            System.arraycopy(value, 0, copy, 0, Math.min(value.length, newLength));
            value = copy;
        }
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        if (index > length)
            throw new ArrayIndexOutOfBoundsException("index > array length");
        return value[index];
    }

    @NonNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return new String(value, start, end);
    }

    @NonNull
    @Override
    public String toString() {
        return new String(value, 0, length);
    }

    @Override
    public void getChars(int start, int end, char[] dest, int destOff) {

    }
}
