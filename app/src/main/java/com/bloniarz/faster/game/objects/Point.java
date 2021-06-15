package com.bloniarz.faster.game.objects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Objects;

public class Point extends RectF {
    Paint paint;
    private float xVelocity, yVelocity;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels,
            screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Point(float left, float top, float right, float bottom, int color, float xVelocity, float yVelocity) {
        super(left, top, right, bottom);
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public void update(float time){
        time /= 1000;
        if (left + xVelocity * time < 0 || right + xVelocity * time >screenWidth)
            xVelocity *= -1;
        if (top + yVelocity * time < 0 || bottom + yVelocity * time >screenHeight){
            yVelocity *= -1;
        }
        if (left<0)
            this.offset(-left, 0);
        else if (right > screenWidth)
            this.offset(screenWidth-right, 0);
        if (top<0)
            this.offset(0, -top);
        else if (bottom >screenHeight)
            this.offset(0, screenHeight-bottom);
        this.offset(xVelocity*time, yVelocity*time);
    }

    public Paint getPaint() {
        return paint;
    }

    public float getXVelocity() {
        return xVelocity;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void setCenter(float x, float y){
        offsetTo(x - (right-left)/2, y - (bottom-top)/2);
    }

    private float getRoundRadius(){
        return Float.min((bottom - top)/5, (right - left)/5);
    }

    public void draw(Canvas canvas){
        canvas.drawRoundRect(this, getRoundRadius(), getRoundRadius(), paint);
    }
}
