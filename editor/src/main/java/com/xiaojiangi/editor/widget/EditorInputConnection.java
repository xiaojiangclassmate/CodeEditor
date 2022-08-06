package com.xiaojiangi.editor.widget;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

public class EditorInputConnection extends BaseInputConnection {
    private CodeEditor mEditor;
    public EditorInputConnection(CodeEditor targetView) {
        super(targetView, true);
        mEditor =targetView;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        mEditor.commit(text);
        return super.commitText(text, newCursorPosition);
    }
}
