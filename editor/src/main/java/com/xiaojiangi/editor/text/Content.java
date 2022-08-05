package com.xiaojiangi.editor.text;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private List<ContentLine> mList;
    public Content() {
        this(null);
    }

    public Content(@Nullable CharSequence text) {
        mList =new ArrayList<>();
        mList.add(new ContentLine());
        if (text==null)
            text="";

    }
}
