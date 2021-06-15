package com.bloniarz.faster.game.levels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Ball;
import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level9 implements Level {

    private final float targetZoneHeight = 35*unit, targetZoneWidth = targetZoneHeight;
    private final Point targetZone;
    private final List<Ball> balls;
    private final ProgressBar progressBar;
    private final float maxTime;
    private float timeElapsed = 0;
    private final int ballsCount = 5;
    private final Random random;
    private final float ballRadius = 8*unit;
    private boolean pointerDown = false;
    private Ball currentBall;
    private int currentPointerId;
    private final float targetZoneLeft = screenWidth/2 - targetZoneWidth/2, targetZoneTop = screenHeight/20;

    public Level9(float speed, int goodColor, int neutralColor){
        maxTime = 15*1000/speed;
        random = new Random();
        targetZone = new Point(targetZoneLeft, targetZoneTop, targetZoneLeft + targetZoneWidth, targetZoneTop + targetZoneHeight, goodColor, 0, 0);
        progressBar = new ProgressBar(screenWidth, 5*unit, Color.GRAY, maxTime);
        balls = new ArrayList<>();
        for (int i=0; i<ballsCount; i++)
            addBall(neutralColor);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null){
            targetZone.draw(canvas);
            progressBar.draw(canvas);
            for (Ball p: balls){
                p.draw(canvas);
            }
        }
    }

    @Override
    public State update(float time) {
        timeElapsed += time;
        if (balls.size() == 0)
            return State.PASSED;
        if (timeElapsed >= maxTime)
            return State.LOST;
        progressBar.update(timeElapsed);
        return State.RUNNING;
    }

    @Override
    public boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN: {
                currentPointerId = pointerId;
                int pointerIndex = event.findPointerIndex(currentPointerId);
                for (Ball p: balls){
                    if (p.contains(event.getX(pointerIndex), event.getY(pointerIndex))){
                        currentBall = p;
                        pointerDown = true;
                        break;
                    }
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                if (pointerId == currentPointerId){
                    currentPointerId = -1;
                    if (pointerDown && targetZone.contains(currentBall))
                        balls.remove(currentBall);
                    pointerDown = false;
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                if (currentPointerId != -1) {
                    int pointerIndex = event.findPointerIndex(currentPointerId);
                    if (pointerIndex != -1 && pointerDown) {
                        currentBall.setCenter(event.getX(pointerIndex), event.getY(pointerIndex));
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Catch them all";
    }

    @Override
    public String getLevelDescription() {
        return "Put all the balls\nin the target zone!";
    }

    private void addBall(int neutralColor){
        float x = random.nextFloat()*(screenWidth - ballRadius - 2*ballRadius)+ballRadius;
        float y = random.nextFloat()*(screenHeight - targetZoneTop - targetZoneHeight - 2*ballRadius) + targetZoneTop + targetZoneHeight + ballRadius;
        Ball temp = new Ball(x, y, ballRadius, neutralColor, 0, 0);
        for (Ball p: balls){
            if (RectF.intersects(p, temp)) {
                addBall(neutralColor);
                return;
            }
        }
        if (RectF.intersects(temp, targetZone)){
            addBall(neutralColor);
            return;
        }
        balls.add(temp);
    }
}
