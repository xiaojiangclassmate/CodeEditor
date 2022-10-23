package com.xiaojiangi.editor.lang;

import com.xiaojiangi.editor.common.Lexer;

import java.util.HashMap;

public abstract class Language {
    protected HashMap<String, Integer> keywords;

    private final static char[] OPERATORS = {
            '(', ')', '{', '}', '.', ',', ';', '=', '+', '-',
            '/', '*', '&', '!', '|', ':', '[', ']', '<', '>',
            '?', '~', '%', '^'
    };

    public abstract void reset();

    protected void setKeywords(String[] kd) {
        keywords = new HashMap<>(kd.length);
        for (int i = 0; i < kd.length; i++) {
            keywords.put(kd[i], Lexer.KEYWORD);
        }
    }

    //是否是空格
    public boolean isSpace(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f';
    }

    /**
     * 是否是行注释
     */
    public boolean isAnnotationLine(char c1, char c2) {
        return c1 == c2 && c1 == '/';
    }

    /**
     * 多行注释开始
     */
    public boolean isAnnotationLineStart(char c1, char c2) {
        return c1 == '/' && c2 == '*';
    }

    /**
     * 多行注释结束
     */
    public boolean isAnnotationLineEnd(char c1, char c2) {
        return c1 == '*' && c2 == '/';
    }
}
