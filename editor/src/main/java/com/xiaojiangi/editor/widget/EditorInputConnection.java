package com.xiaojiangi.editor.widget;

import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;

public class EditorInputConnection extends BaseInputConnection {
    private final CodeEditor mEditor;

    public EditorInputConnection(CodeEditor codeEditor) {
        super(codeEditor, true);
        mEditor = codeEditor;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        mEditor.insertText(text);
        return super.commitText(text, newCursorPosition);
    }

    @Nullable
    @Override
    public CharSequence getTextBeforeCursor(int length, int flags) {
        return super.getTextBeforeCursor(length, flags);
    }

}
