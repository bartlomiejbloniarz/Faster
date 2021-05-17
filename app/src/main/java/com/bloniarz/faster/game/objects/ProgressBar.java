package com.bloniarz.faster.game.objects;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.RectF;

public class ProgressBar extends RectF {
    private final Paint paint;
    private final float width;
    private float time;

    public ProgressBar(float width, float height, int color, float time) {
        super(0, 0, width, height);
        this.width = width;
        this.time = time;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAlpha(128);
    }

    public void update(float timeElapsed){
        this.right = width*(time - timeElapsed)/time;
    }

    public Paint getPaint() {
        return paint;
    }

}
