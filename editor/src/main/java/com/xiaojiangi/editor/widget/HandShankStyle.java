package com.xiaojiangi.editor.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
    private final RectF leftRectF;
    private final RectF rightRectF;
    private Canvas canvas;
    private RectF r = new RectF();
    private CodeEditor codeEditor;

    @SuppressLint("UseCompatLoadingForDrawables")
    public HandShankStyle(CodeEditor codeEditor) {
        this.codeEditor = codeEditor;
        drawable = codeEditor.getContext().getDrawable(R.drawable.ic_handle).mutate();
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, codeEditor.getContext().getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, codeEditor.getContext().getResources().getDisplayMetrics());
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22f, codeEditor.getContext().getResources().getDisplayMetrics());
        paint = new Paint();
        paint.setAntiAlias(true);
        leftRectF = new RectF();
        rightRectF = new RectF();
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
//        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
//        draw(canvas, x, y);
    }

    public void draw(Canvas canvas, int color, float startX, float startY, float endX, float endY) {
        this.canvas = canvas;
        paint.setColor(color);
        float radius = size / 2f;
        float leftX = startX - radius;
        canvas.drawCircle(leftX, startY + radius, radius, paint);
        canvas.drawRect(leftX, startY, leftX + radius, startY + radius, paint);
        leftRectF.set(leftX - radius, startY, startX, startY + radius + radius);

        float rightX = endX + radius;
        canvas.drawCircle(rightX, endY + radius, radius, paint);
        canvas.drawRect(rightX - radius, endY, rightX, endY + radius, paint);
        rightRectF.set(rightX + radius, endY, endX, endY + radius + radius);
        canvas.drawRect(leftRectF, paint);
        canvas.drawRect(rightRectF, paint);

    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public boolean isContain(int x, int y) {
        if (isRightHandShank(x, y))
            return true;
        return isLeftHandShank(x, y);
    }

    public boolean isLeftHandShank(int x, int y) {
        return leftRectF.contains(x, y);
    }

    public boolean isRightHandShank(int x, int y) {
        return rightRectF.contains(x, y);
    }

    public void restart() {
        leftRectF.setEmpty();
        rightRectF.setEmpty();

    }
}
