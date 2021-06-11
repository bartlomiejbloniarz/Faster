package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;

public class Level8 implements Level {

    private final ProgressBar progressBar;
    private float maxTime = 2*1000;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private float timeElapsed = 0;
    private final Paint textPaint;
    private final Level fakeLevel;
    private boolean lost = false;

    public Level8(Level fakeLevel){
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        progressBar = new ProgressBar(screenWidth, 50, Color.GRAY, maxTime);
        this.fakeLevel = fakeLevel;
    }
    @Override
    public void draw(Canvas canvas) {
        fakeLevel.draw(canvas);
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        progressBar.update(timeElapsed);
        fakeLevel.update(time);
        if (lost)
            return State.LOST;
        if(timeElapsed > maxTime)
            return State.PASSED;
        return State.RUNNING;
    }

    @Override
    public boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                lost = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Be careful!";
    }

    @Override
    public String getLevelDescription() {
        return "Do not touch the screen!";
    }
}
