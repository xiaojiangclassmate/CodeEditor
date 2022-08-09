package com.xiaojiangi.editor.text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

public class ContentLine implements CharSequence {
    char[] value;
    int length;
    public ContentLine() {
        this(null);
    }
    public ContentLine(@Nullable CharSequence text) {
        if (text==null)
            text="";
        length= text.length();
        value =new char[length+16];
        for (int i = 0; i < text.length(); i++) {
            value[i] = text.charAt(i);
        }
    }

    public ContentLine insert(int offset, char c) {
        ensureCapacity(length + 1);
        if (offset < length) {
            System.arraycopy(value, offset, value, offset + 1, length - offset);
        }
        value[offset] = c;
        length += 1;
        return this;
    }

    public ContentLine insert(int offset, char[] str) {
        if ((offset < 0) || (offset > length()))
            throw new StringIndexOutOfBoundsException(offset);
        int len = str.length;
        ensureCapacity(length + len);
        System.arraycopy(value, offset, value, offset + len, length - offset);
        System.arraycopy(str, 0, value, offset, len);
        length += len;
        return this;
    }
    public ContentLine insert(int index, char[] str, int offset,
                              int len) {
        if ((index < 0) || (index > length))
            throw new StringIndexOutOfBoundsException(index);
        if ((offset < 0) || (len < 0) || (offset > str.length - len))
            throw new StringIndexOutOfBoundsException(
                    "offset " + offset + ", len " + len + ", str.length "
                            + str.length);
        ensureCapacity(length + len);
        System.arraycopy(value, index, value, index + len, length - index);
        System.arraycopy(str, offset, value, index, len);
        length += len;
        return this;
    }
    public ContentLine append(char c){
        ensureCapacity(length+1);
        value[length] =c;
        length+=1;
        return this;
    }
    public ContentLine append(CharSequence text){
        ensureCapacity(length+text.length());
        for (int i = 0; i < text.length(); i++) {
            value[length+i] = text.charAt(i);
        }
        length+= text.length();
        return this;
    }
    public ContentLine append(ContentLine contentLine){
        ensureCapacity(length+contentLine.length);
        System.arraycopy(contentLine.value,0,value,length,contentLine.length);
        length+=contentLine.length;
        return this;
    }
    public ContentLine delete(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end > length)
            end = length;
        if (start > end)
            throw new StringIndexOutOfBoundsException();
        int len = end - start;
        if (len > 0) {
            System.arraycopy(value, start+len, value, start, length-end);
            length -= len;
        }
        return this;
    }
    private void ensureCapacity(int minimumCapacity){
        if (minimumCapacity- value.length>0){
            int newLength = value.length * 2 < minimumCapacity ? minimumCapacity + 2 : value.length * 2;
            char[] copy = new char[newLength];
            System.arraycopy(value, 0, copy, 0, Math.min(value.length, newLength));
            value = copy;
        }
    }

    public ContentLine clear(){
        value =new char[16];
        length =0;
        return this;
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
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end > length)
            throw new StringIndexOutOfBoundsException(end);
        if (start > end)
            throw new StringIndexOutOfBoundsException(end - start);
        return new String(value, start, end - start);
    }

    @NonNull
    @Override
    public String toString() {
        return new String(value,0,length);
    }
}
