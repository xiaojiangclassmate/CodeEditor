package com.xiaojiangi.editor.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private int size;
    private final Paint paint;

    @SuppressLint("UseCompatLoadingForDrawables")
    public HandShankStyle(CodeEditor codeEditor) {
        drawable = codeEditor.getContext().getDrawable(R.drawable.ic_handle).mutate();
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, codeEditor.getContext().getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, codeEditor.getContext().getResources().getDisplayMetrics());
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22f, codeEditor.getContext().getResources().getDisplayMetrics());
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void draw(Canvas canvas, float x, float y) {
        var left = (int) (x - (width * scale) / 2);
        var top = (int) y;
        var right = (int) (x + (width * scale) / 2);
        var bottom = (int) (y + height * scale);
        drawable.setBounds(left, top, right, bottom);
        drawable.setAlpha(alpha);
        drawable.draw(canvas);
    }

    public void draw(Canvas canvas, int color, float x, float y) {
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        draw(canvas, x, y);
    }

    public void draw(Canvas canvas, int color, float startX, float startY, float endX, float endY) {
        paint.setColor(color);
        float radius = size / 2f;
        float leftX = startX - radius;
        canvas.drawCircle(leftX, startY + radius, radius, paint);
        canvas.drawRect(leftX, startY, leftX + radius, startY + radius, paint);

        float rightX = endX + radius;
        canvas.drawCircle(rightX, endY + radius, radius, paint);
        canvas.drawRect(rightX - radius, endY, rightX, endY + radius, paint);

    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
