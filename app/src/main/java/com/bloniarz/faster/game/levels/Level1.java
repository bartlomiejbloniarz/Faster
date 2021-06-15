package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.NoBoundariesPoint;
import com.bloniarz.faster.game.objects.Point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Level1 implements Level {
    private final float width = 10*unit, height = width;
    private float xVelocity = 0, yVelocity = 35*unit, dXVelocity = 10*unit, movingPointXVelocity=30*unit;
    private final float smallSize = 5*unit;
    private final Paint squareColor = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final HashSet<Integer> leftSet = new HashSet<>(), rightSet = new HashSet<>();
    private final List<Point> stillPoints = new LinkedList<>(), movingPoints = new LinkedList<>();
    private final Point mainRect;

    public Level1(float speed, int goodColor, int badColor, int playerColor){
        float x = (screenWidth - width)/2;
        float y = unit;
        xVelocity *= speed;
        yVelocity *= speed;
        dXVelocity *= speed;
        movingPointXVelocity *= speed;
        mainRect = new NoBoundariesPoint(x, y, x+width, y+height, playerColor, xVelocity, yVelocity); //new RectF(x, y, x+width, y+height);
        squareColor.setColor(Color.BLACK);
        for (float part = 0.25f; part<= 0.75f; part+=0.25f)
            stillPoints.add(new Point(screenWidth*part, screenHeight*part, screenWidth*part+smallSize, screenHeight*part+smallSize, goodColor, 0, 0));
        for (float part = 0.375f; part<= 0.625f; part+=0.25f)
            movingPoints.add(new Point(screenWidth*part, screenHeight*part, screenWidth*part+smallSize, screenHeight*part+smallSize, badColor, part<0.5f ? movingPointXVelocity : -movingPointXVelocity, 0));

    }
    @Override
    public synchronized void draw(Canvas canvas) {
        if (canvas != null){
            mainRect.draw(canvas);
            for (Point point: stillPoints)
                point.draw(canvas);
            for (Point point: movingPoints)
                point.draw(canvas);
        }
    }

    @Override
    public synchronized boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (event.getX(actionIndex) > screenWidth / 2) {
                    mainRect.setXVelocity(mainRect.getXVelocity() + dXVelocity);
                    rightSet.add(pointerId);
                }
                else {
                    mainRect.setXVelocity(mainRect.getXVelocity() - dXVelocity);
                    leftSet.add(pointerId);
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                if (event.getX(actionIndex) > screenWidth / 2 && rightSet.contains(pointerId)) {
                    mainRect.setXVelocity(mainRect.getXVelocity() - dXVelocity);
                    rightSet.remove(pointerId);
                }
                else if (event.getX(actionIndex) <= screenWidth / 2 && leftSet.contains(pointerId)){
                    mainRect.setXVelocity(mainRect.getXVelocity() + dXVelocity);
                    leftSet.remove(pointerId);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                for (Iterator<Integer> i = leftSet.iterator(); i.hasNext();){
                    Integer element = i.next();
                    int pointerIndex = event.findPointerIndex(element);
                    if (event.getX(pointerIndex) > screenWidth / 2) {
                        mainRect.setXVelocity(mainRect.getXVelocity() + 2*dXVelocity);
                        i.remove();
                        rightSet.add(element);
                    }
                }
                for (Iterator<Integer> i = rightSet.iterator(); i.hasNext();){
                    Integer element = i.next();
                    int pointerIndex = event.findPointerIndex(element);
                    if (event.getX(pointerIndex) <= screenWidth / 2) {
                        mainRect.setXVelocity(mainRect.getXVelocity() - 2*dXVelocity);
                        i.remove();
                        leftSet.add(element);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public synchronized State update(float time) {
        mainRect.update(time);
        if (mainRect.bottom >= screenHeight - unit) {
            if (stillPoints.size() == 0)
                return State.PASSED;
            else
                return State.LOST;
        }
        if (mainRect.left<0)
            mainRect.offset(-mainRect.left, 0);
        if (mainRect.right>screenWidth)
            mainRect.offset(screenWidth - mainRect.right, 0);
        for (Point point: movingPoints) {
            point.update(time);
            if (RectF.intersects(mainRect, point))
                return State.LOST;
        }
        for (Iterator<Point> i = stillPoints.iterator(); i.hasNext();){
            Point element = i.next();
            element.update(time);
            if (RectF.intersects(mainRect, element)) {
                i.remove();
            }
        }
        return State.RUNNING;
    }

    @Override
    public String getLevelName() {
        return "Falling square";
    }

    @Override
    public String getLevelDescription() {
        return "Put your fingers\non the sides\nto avoid red squares\nand\ncollect the green ones!";
    }
}
