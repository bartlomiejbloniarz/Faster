package com.bloniarz.faster.game.objects;

import android.graphics.Canvas;

public class Ball extends Point{
    public Ball(float x, float y, float r, int color, float xVelocity, float yVelocity) {
        super(x-r, y-r, x+r, y+r, color, xVelocity, yVelocity);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(getCenterX(), getCenterY(), getRadius(), paint);
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
}
