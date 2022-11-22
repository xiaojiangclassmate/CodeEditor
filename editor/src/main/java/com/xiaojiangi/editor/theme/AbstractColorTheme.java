package com.xiaojiangi.editor.theme;

import android.util.SparseIntArray;

public abstract class AbstractColorTheme {
    protected SparseIntArray colors;
    /**
     * 行号
     */
    public static final int LINE_NUMBER = 1;
    /**
     * 行号背景
     */
    public static final int LINE_NUMBER_BACKGROUND = 2;
    /**
     * 分割线
     */
    public static final int LINE_COLOR = 3;
    /**
     * 文本背景
     */
    public static final int CODE_BACKGROUND = 4;
    /**
     * 当前行背景
     */
    public static final int CURRENT_LINE_BACKGROUND = 5;
    /**
     * 光标颜色
     */
    public static final int CURSOR_COLOR = 6;
    /**
     * 文本颜色
     */
    public static final int TEXT_COLOR = 7;
    /**
     * 选择背景颜色
     */
    public static final int SELECTION_TEXT_BACKGROUND = 8;
    protected int START = 1;
    protected int END = 8;

    protected abstract void apply(int type);

    public AbstractColorTheme() {
        colors = new SparseIntArray();
        apply();
    }

    private void apply() {
        for (int i = START; i <= END; i++) {
            apply(i);
        }
    }

    public int getColor(int type) {
        return colors.get(type);
    }

    public void setColor(int type, int color) {
        int old = getColor(type);
        if (old != color) {
            colors.put(type, color);
        }
    }
}
