package com.xiaojiangi.editor.text;

import android.text.GetChars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class TextLine implements CharSequence {
    private char[] value;
    private int length;

    public TextLine() {
        this(null);
    }

    public TextLine(@Nullable CharSequence text) {
        if (text == null)
            text = "";
        length = text.length();
        if (text instanceof String str) {
            value = str.toCharArray();
            return;
        }
        value = new char[length + 16];
        for (int i = 0; i < text.length(); i++) {
            value[i] = text.charAt(i);
        }
    }

    public TextLine(int length) {
        this.length = length;
        value = new char[length + 16];
    }

    public TextLine insert(int offset, String str) {
        if (offset < 0)
            throw new ArrayIndexOutOfBoundsException();
        if (offset > length)
            throw new ArrayIndexOutOfBoundsException();
        ensureCapacity(length + str.length());
        System.arraycopy(value, offset, value, str.length() + offset, length - offset);
        System.arraycopy(str.toCharArray(), 0, value, offset, str.length());
        length += str.length();
        return this;

    }

    public TextLine insert(int offset, CharSequence csq) {
        if (offset < 0)
            throw new ArrayIndexOutOfBoundsException();
        if (offset > length)
            throw new ArrayIndexOutOfBoundsException();
        ensureCapacity(length + csq.length());
        System.arraycopy(value, offset, value, csq.length() + offset, length - offset);
        for (int i = 0; i < csq.length(); i++) {
            value[offset + i] = csq.charAt(i);
        }
        length += csq.length();
        return this;
    }

    public TextLine insert(int offset, char c) {
        if (offset < 0)
            throw new ArrayIndexOutOfBoundsException();
        if (offset > length)
            throw new ArrayIndexOutOfBoundsException();
        ensureCapacity(length + 1);
        System.arraycopy(value, offset, value, 1 + offset, length - offset);
        value[offset] = c;
        length++;
        return this;
    }

    public TextLine append(CharSequence csq) {
        ensureCapacity(length + csq.length());
        for (int i = 0; i < csq.length(); i++) {
            value[length + i] = csq.charAt(i);
        }
        length += csq.length();
        return this;
    }

    public TextLine append(String str) {
        ensureCapacity(length + str.length());
        System.arraycopy(str.toCharArray(), 0, value, length, str.length());
        length += str.length();
        return this;
    }

    public TextLine append(char c) {
        ensureCapacity(length + 1);
        value[length] = c;
        length++;
        return this;
    }

    public TextLine append(TextLine textLine) {
        ensureCapacity(length + textLine.length);
        System.arraycopy(textLine.value, 0, value, length, textLine.length);
        length += textLine.length;
        return this;
    }

    public TextLine delete(int start, int end) {
        if (start > end || start < 0 || end > length)
            throw new ArrayIndexOutOfBoundsException();
        if (start == end)
            return this;
        System.arraycopy(value, end, value, start, value.length - end);
        length = value.length - (end - start);
        ensureCapacity(length);
        return this;
    }

    public int indexOf(char c) {
        for (int i = 0; i < length; i++) {
            if (c == value[i])
                return i;
        }
        return -1;
    }

    public int indexOf(CharSequence csq) {

        return -1;
    }

    public TextLine setEmpty() {
        length = 0;
        value = new char[16];
        return this;
    }

    public char[] toCharArray() {
        char[] v = new char[length];
        System.arraycopy(value, 0, v, 0, length);
        return v;
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
        if (index < 0 || index > length)
            throw new ArrayIndexOutOfBoundsException();
        return value[index];
    }

    @NonNull
    @Override
    public String subSequence(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end > length)
            throw new StringIndexOutOfBoundsException(end);
        if (start > end)
            throw new StringIndexOutOfBoundsException(end - start);
        return new String(value, start, end - start);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (char v : value) {
            h = 31 * h + (v & 0xff);
        }
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof TextLine textLine) {
            if (textLine.length == length) {
                for (int i = 0; i < length; i++) {
                    if (value[i] != textLine.value[i])
                        return false;
                }
                return true;
            } else {
                return false;
            }
        }
        if (obj instanceof String str) {
            if (length == str.length()) {
                for (int i = 0; i < length; i++) {
                    if (value[i] != str.charAt(i))
                        return false;
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return new String(value, 0, length);
    }


}