package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.JumpingPoint;
import com.bloniarz.faster.game.objects.NoBoundariesPoint;
import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Level5 implements Level{

    private final Random random;
    private final float speed;
    private final JumpingPoint jumpingPoint;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final float width = 100, height = 100, smallWidth = 50, smallHeight = 50;
    private float timeElapsed = 0;
    private final float maxTime = 7*1000;
    private final ProgressBar progressBar;
    private final List<Point> movingPoints = new LinkedList<>();
    private float addTime = 800, addTimeElapsed = 0;
    private int badColor;

    public Level5(float speed, int badColor, int playerColor){
        random = new Random();
        this.speed = speed;
        progressBar = new ProgressBar(screenWidth, 50, Color.GRAY, maxTime);
        jumpingPoint = new JumpingPoint(50, screenHeight/2, 50+width, screenHeight/2+height, playerColor, speed);
        this.badColor = badColor;
        addPoint();
    }

    void addPoint(){
        movingPoints.add(new NoBoundariesPoint(screenWidth, screenHeight/2, screenWidth+width, screenHeight/2+height, badColor, -600*speed, 0));
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null){
            jumpingPoint.draw(canvas);
            for (Point point: movingPoints){
                point.draw(canvas);
            }
            progressBar.draw(canvas);
        }
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        if (timeElapsed >= maxTime)
            return State.PASSED;
        addTimeElapsed += time;
        if (addTimeElapsed >= addTime){
            addPoint();
            addTimeElapsed = 0;
            addTime = 800 + random.nextFloat()*800;
        }
        jumpingPoint.update(time);
        for (Point point: movingPoints){
            point.update(time);
            if (RectF.intersects(jumpingPoint, point))
                return State.LOST;
        }
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public boolean touch(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                jumpingPoint.jump();
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Jump!";
    }

    @Override
    public String getLevelDescription() {
        return "Tap the screen\nto avoid obstacles.";
    }
}
