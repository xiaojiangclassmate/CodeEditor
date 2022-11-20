package com.xiaojiangi.editor.theme;

import android.util.SparseIntArray;

public abstract class AbstractColorTheme {
    protected SparseIntArray colors;
    protected int START = 0;
    protected int END = 0;

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
