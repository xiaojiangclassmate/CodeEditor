package com.xiaojiangi.editor.widget;

import android.view.inputmethod.BaseInputConnection;

public class EditorInputConnection extends BaseInputConnection {
    public EditorInputConnection(CodeEditor codeEditor) {
        super(codeEditor, true);
    }
}
