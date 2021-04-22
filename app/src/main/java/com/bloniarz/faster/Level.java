package com.bloniarz.faster;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Level {
    enum State{
        PASSED, LOST, RUNNING;
    }
    void draw(Canvas canvas);
    State update(float time);
    boolean touch(MotionEvent event);
}
