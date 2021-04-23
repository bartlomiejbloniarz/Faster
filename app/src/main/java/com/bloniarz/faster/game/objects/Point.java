package com.bloniarz.faster.game.objects;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Objects;

public class Point extends RectF {
    Paint paint;

    public Point(float left, float top, float right, float bottom, int color) {
        super(left, top, right, bottom);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public Paint getPaint() {
        return paint;
    }
}
