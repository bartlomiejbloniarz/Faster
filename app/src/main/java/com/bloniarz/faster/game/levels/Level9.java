package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.R;
import com.bloniarz.faster.game.objects.Point;
import com.bloniarz.faster.game.objects.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level9 implements Level {

    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final float targetZoneHeight = screenWidth/3, targetZoneWidth = targetZoneHeight;
    private final Point targetZone;
    private final List<Point> balls;
    private final ProgressBar progressBar;
    private final float maxTime;
    private float timeElapsed = 0;
    private final int ballsCount = 5;
    private final Random random;
    private final float ballSize = 150;
    private boolean pointerDown = false;
    private Point currentPoint;
    private int currentPointerId;
    private final float targetZoneLeft = screenWidth/2 - targetZoneWidth/2, targetZoneTop = screenHeight/20;

    public Level9(float speed, int goodColor, int neutralColor){
        maxTime = 15*1000/speed;
        random = new Random();
        targetZone = new Point(targetZoneLeft, targetZoneTop, targetZoneLeft + targetZoneWidth, targetZoneTop + targetZoneHeight, goodColor, 0, 0);
        progressBar = new ProgressBar(screenWidth, 50, Color.GRAY, maxTime);
        balls = new ArrayList<>();
        for (int i=0; i<ballsCount; i++)
            addBall(neutralColor);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null){
            targetZone.draw(canvas);
            progressBar.draw(canvas);
            for (Point p: balls){
                canvas.drawCircle((p.left+p.right)/2, (p.bottom+p.top)/2, (p.right-p.left)/2, p.getPaint());
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
                for (Point p: balls){
                    if (p.contains(event.getX(pointerIndex), event.getY(pointerIndex))){
                        currentPoint = p;
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
                    if (pointerDown && targetZone.contains(currentPoint))
                        balls.remove(currentPoint);
                    pointerDown = false;
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                if (currentPointerId != -1) {
                    int pointerIndex = event.findPointerIndex(currentPointerId);
                    if (pointerIndex != -1 && pointerDown) {
                        currentPoint.setCenter(event.getX(pointerIndex), event.getY(pointerIndex));
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
        float x = random.nextFloat()*(screenWidth - ballSize - 2*ballSize)+ballSize;
        float y = random.nextFloat()*(screenHeight - targetZoneTop - targetZoneHeight - 2*ballSize) + targetZoneTop + targetZoneHeight + ballSize;
        Point temp = new Point(x, y, x+ballSize, y+ballSize, neutralColor, 0, 0);
        for (Point p: balls){
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
