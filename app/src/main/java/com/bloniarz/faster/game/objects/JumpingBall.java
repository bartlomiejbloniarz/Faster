package com.bloniarz.faster.game.objects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class JumpingBall extends RectF {
    Paint paint;
    private float xVelocity, yVelocity;
    private float xAcceleration = 0, yAcceleration = 0;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels,
            screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean override = false;

    public JumpingBall(float left, float top, float right, float bottom, int color, float xVelocity, float yVelocity) {
        super(left, top, right, bottom);
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public void update(float time) {
        if (!override) {
            time /= 1000;
            xVelocity += xAcceleration * time;
            yVelocity += yAcceleration * time;
            if (left + xVelocity * time < 0 || right + xVelocity * time > screenWidth) {
                xVelocity *= -0.5;
            }
            if (top + yVelocity * time < 0 || bottom + yVelocity * time > screenHeight) {
                yVelocity *= -0.5;
            }
            if (left<0)
                offset(-left, 0);
            if (right > screenWidth)
                offset(screenWidth - right, 0);
            if (top < 0)
                offset(0, -top);
            if (bottom > screenHeight)
                offset(0, screenHeight -bottom);
            this.offset(xVelocity * time, yVelocity * time);
        }
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
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

    public void setCenter(float x, float y){
        offsetTo(x - (right-left)/2, y - (bottom-top)/2);
    }

    public float getRadius(){
        return (right-left)/2;
    }

    public float getCenterX(){
        return (right+left)/2;
    }

    public float getCenterY(){
        return (bottom+top)/2;
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(getCenterX(), getCenterY(), getRadius(), paint);
    }

    public Paint getPaint() {
        return paint;
    }
}
