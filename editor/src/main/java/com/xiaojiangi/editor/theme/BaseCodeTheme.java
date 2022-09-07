package com.xiaojiangi.editor.theme;

import android.util.SparseIntArray;

public class BaseCodeTheme {
    private SparseIntArray colors;
    public static final int LINE_NUMBER = 1;    //行号
    public static final int LINE_NUMBER_BACKGROUND = 2;    //行号背景
    public static final int LINE_COLOR = 3;    //分割线
    public static final int CODE_BACKGROUND = 4;    //文本背景
    public static final int CURRENT_LINE_BACKGROUND = 5;    //当前行背景
    public static final int CURSOR_COLOR = 6;    //光标颜色
    public static final int TEXT_COLOR = 7; //文本颜色
    public static final int SELECTION_TEXT_BACKGROUND = 8; //选择背景颜色
    public static final int CURSOR_STYLE_COLOR = 9;

    private static final int MIN = 1;
    private static final int MAX = 9;

    protected void apply(int type){
        int color=getColor(type);
        switch (type){
            case LINE_NUMBER:
                color =0xffb2acad;
                break;
            case LINE_NUMBER_BACKGROUND:
                color =0xfff2f2f2;
                break;
            case CODE_BACKGROUND:
                color =0xffffffff;
                break;
            case CURRENT_LINE_BACKGROUND:
                color =0xfffcfaed;
                break;
            case LINE_COLOR:
                color = 0xffd4d4d4;
                break;
            case CURSOR_COLOR:
                color = 0xff000000;
                break;
            case TEXT_COLOR:
                color = 0xff070712;
                break;
            case SELECTION_TEXT_BACKGROUND:
                color = 0xffa6d2ff;
                break;
            case CURSOR_STYLE_COLOR:
                color = 0xff2962ff;
                break;
        }
        setColor(type,color);
    }
    private void apply(){
        for (int i = MIN; i <= MAX; i++) {
            apply(i);
        }
    }

    public BaseCodeTheme(){
        colors =new SparseIntArray();
        apply();
    }
    public int getColor(int type){
        return colors.get(type);
    }
    public void setColor(int type,int color){
        int old =getColor(type);
        if (old !=color){
            colors.put(type,color);
        }
    }
}
