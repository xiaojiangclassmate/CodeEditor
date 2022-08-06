package com.xiaojiangi.editor.widget;

import android.text.Editable;
import android.text.method.MetaKeyKeyListener;

public class MetaKeyKeyStatus extends MetaKeyKeyListener {
    private final CodeEditor mEditor;
    private final Editable editable =Editable.Factory.getInstance().newEditable("");
    public MetaKeyKeyStatus(CodeEditor codeEditor) {
        mEditor =codeEditor;
    }


}
