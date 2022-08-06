package com.xiaojiangi.editor.widget;

import android.view.View;
import android.view.inputmethod.BaseInputConnection;

public class EditorInputConnection extends BaseInputConnection {
    public EditorInputConnection(View targetView) {
        super(targetView, true);
    }
}
