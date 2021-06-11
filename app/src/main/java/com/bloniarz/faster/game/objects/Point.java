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
        if (left < 0 || right>screenWidth){
            xVelocity *= -1;
            if (left<0)
                this.offset(-left, 0);
            else
                this.offset(screenWidth-right, 0);
        }
        if (top < 0 || bottom>screenHeight){
            yVelocity *= -1;
            if (top<0)
                this.offset(-top, 0);
            else
                this.offset(screenHeight-bottom, 0);
        }
        this.offset(xVelocity*time/1000, yVelocity*time/1000);
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
