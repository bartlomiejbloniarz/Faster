package com.bloniarz.faster.game.objects;

import android.graphics.Paint;

public class SensorPoint extends Point {
    public SensorPoint(float left, float top, float right, float bottom, int color, float xVelocity, float yVelocity) {
        super(left, top, right, bottom, color, xVelocity, yVelocity);
    }

    @Override
    public void update(float time) {
        this.offset(getXVelocity()*time/1000, getYVelocity()*time/1000);
        if (left < 0 || right>getScreenWidth()){
            if (left<0)
                this.offset(-left, 0);
            else
                this.offset(getScreenWidth()-right, 0);
        }
        if (top < 0 || bottom>getScreenHeight()){
            if (top<0)
                this.offset(0, -top);
            else
                this.offset(0, getScreenHeight()-bottom);
        }
    }
}
