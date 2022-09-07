package com.xiaojiangi.editor.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.xiaojiangi.editor.R;

public class HandShankStyle {
    private final Drawable drawable;
    private final int height;
    private final int width;
    private float scale = 1.0f;
    private int alpha = 255;

    @SuppressLint("UseCompatLoadingForDrawables")
    public HandShankStyle(Context context) {
        drawable = context.getDrawable(R.drawable.ic_handle).mutate();
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, context.getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, context.getResources().getDisplayMetrics());
    }

    public void draw(Canvas canvas, float x, float y) {
        var left = (int) (x - (width * scale) / 2);
        var top = (int) y;
        var right = (int) (x + (width * scale) / 2);
        var bottom = (int) (y + height * scale);
        drawable.setAlpha(alpha);
        drawable.setBounds(left, top, right, bottom);

        drawable.draw(canvas);
    }

    public void draw(Canvas canvas, int color, float x, float y) {
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        draw(canvas, x, y);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
