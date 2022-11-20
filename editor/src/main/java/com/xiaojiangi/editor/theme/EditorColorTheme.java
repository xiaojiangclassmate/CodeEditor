package com.xiaojiangi.editor.theme;

public class EditorColorTheme extends AbstractColorTheme {
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

    @Override
    protected void apply(int type) {
        getColor(type);
        int color = switch (type) {
            case LINE_NUMBER -> 0xffb2acad;
            case LINE_NUMBER_BACKGROUND -> 0xfff2f2f2;
            case CODE_BACKGROUND -> 0xffffffff;
            case CURRENT_LINE_BACKGROUND -> 0xfffcfaed;
            case LINE_COLOR -> 0xffd4d4d4;
            case CURSOR_COLOR -> 0xff000000;
            case TEXT_COLOR -> 0xff070712;
            case SELECTION_TEXT_BACKGROUND -> 0xffa6d2ff;
            default -> getColor(type);
        };
        setColor(type, color);
    }
}
