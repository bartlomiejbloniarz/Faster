package com.bloniarz.faster.game.objects;

import android.content.res.Resources;

public class NoBoundariesPoint extends Point{

    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels,
            screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public NoBoundariesPoint(float left, float top, float right, float bottom, int color, float xVelocity, float yVelocity) {
        super(left, top, right, bottom, color, xVelocity, yVelocity);
    }

    public void update(float time){
        this.offset(getXVelocity()*time/1000, getYVelocity()*time/1000);
    }
}
