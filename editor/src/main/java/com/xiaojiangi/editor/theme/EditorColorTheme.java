package com.xiaojiangi.editor.theme;

public class EditorColorTheme extends AbstractColorTheme {
    public EditorColorTheme() {
    }

    @Override
    protected void apply(int type) {
        getColor(type);
        int color = switch (type) {
            case LINE_NUMBER -> 0xffb2acad;
            case LINE_NUMBER_BACKGROUND -> 0xfff2f2f2;
            case CODE_BACKGROUND -> 0xffffffff;
            case CURRENT_LINE_BACKGROUND -> 0xfffcfae3;
            case LINE_COLOR -> 0xffd4d4d4;
            case CURSOR_COLOR -> 0xff000000;
            case TEXT_COLOR -> 0xff070712;
            case SELECTION_TEXT_BACKGROUND -> 0xffa6d2ff;
            default -> getColor(type);
        };
        setColor(type, color);
    }
}
