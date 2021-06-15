package com.bloniarz.faster.game.objects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class JumpingBall extends Ball{
    private float xAcceleration = 0, yAcceleration = 0;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels,
            screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean override = false;

    public JumpingBall(float x, float y, float r, int color, float xVelocity, float yVelocity) {
        super(x, y, r, color, xVelocity, yVelocity);
    }

    public void update(float time) {
        if (!override) {
            time /= 1000;
            setXVelocity(getXVelocity() + xAcceleration * time);
            setYVelocity(getYVelocity() + yAcceleration * time);
            float xVelocity = getXVelocity(), yVelocity = getYVelocity();
            if (left + xVelocity * time < 0 || right + xVelocity * time > screenWidth) {
                setXVelocity(xVelocity * -0.5f);
            }
            if (top + yVelocity * time < 0 || bottom + yVelocity * time > screenHeight) {
                setYVelocity(yVelocity * -0.5f);
            }
            if (left<0)
                offset(-left, 0);
            if (right > screenWidth)
                offset(screenWidth - right, 0);
            if (top < 0)
                offset(0, -top);
            if (bottom > screenHeight)
                offset(0, screenHeight -bottom);
            this.offset(getXVelocity() * time, getYVelocity() * time);
        }
    }


    public void setXAcceleration(float xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public void setYAcceleration(float yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
