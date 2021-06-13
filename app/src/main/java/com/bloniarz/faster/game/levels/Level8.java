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
    private float timeElapsed = 0;
    private boolean lost = false;
    private final Paint badPaint;
    private final float radius = 20*unit;

    public Level8(float speed, int badColor){
        maxTime*=speed;
        progressBar = new ProgressBar(screenWidth, 5*unit, Color.GRAY, maxTime);
        badPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        badPaint.setColor(badColor);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(screenWidth/2, screenHeight/2, radius, badPaint);
        progressBar.draw(canvas);
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        progressBar.update(timeElapsed);
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
        return "Do not touch\nthe red button!";
    }
}
