package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Level {
    float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    float unit = screenWidth/100;
    enum State{
        PASSED, LOST, RUNNING;
    }
    void draw(Canvas canvas);
    State update(float time);
    boolean touch(MotionEvent event);
    String getLevelName();
    String getLevelDescription();
}
