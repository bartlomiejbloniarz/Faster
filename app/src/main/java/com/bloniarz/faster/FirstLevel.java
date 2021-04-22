package com.bloniarz.faster;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FirstLevel implements Level {
    private float x, y, xVelocity = 0, yVelocity = 350, dXVelocity = 100;
    private final float width = 100, height = 100;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final Paint squareColor = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final HashSet<Integer> leftSet = new HashSet<>(), rightSet = new HashSet<>();
    private final List<Point> points = new LinkedList<>();
    private final RectF mainRect;

    public FirstLevel(float speed){
        x = (screenWidth - width)/2;
        y = 10;
        xVelocity *= speed;
        yVelocity *= speed;
        dXVelocity *= speed;
        mainRect = new RectF(x, y, x+width, y+height);
        squareColor.setColor(Color.BLACK);
        float part = 0.25f;
        points.add(new Point(screenWidth*part, screenHeight*part, screenWidth*part+50, screenHeight*part+50, Color.GREEN));
        part = 0.5f;
        points.add(new Point(screenWidth*part, screenHeight*part, screenWidth*part+50, screenHeight*part+50, Color.GREEN));
        part = 0.75f;
        points.add(new Point(screenWidth*part, screenHeight*part, screenWidth*part+50, screenHeight*part+50, Color.GREEN));
    }
    @Override
    public synchronized void draw(Canvas canvas) {
        if (canvas != null){
            canvas.drawColor(Color.BLUE);
            canvas.drawRect(mainRect, squareColor);
            for (Point point: points)
                canvas.drawRect(point, point.paint);
        }
    }

    @Override
    public synchronized boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (event.getX(actionIndex) > screenWidth / 2) {
                    xVelocity += dXVelocity;
                    rightSet.add(pointerId);
                }
                else {
                    xVelocity -= dXVelocity;
                    leftSet.add(pointerId);
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                if (event.getX(actionIndex) > screenWidth / 2 && rightSet.contains(pointerId)) {
                    xVelocity -= dXVelocity;
                    rightSet.remove(pointerId);
                }
                else if (event.getX(actionIndex) <= screenWidth / 2 && leftSet.contains(pointerId)){
                    xVelocity += dXVelocity;
                    leftSet.remove(pointerId);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                for (Iterator<Integer> i = leftSet.iterator(); i.hasNext();){
                    Integer element = i.next();
                    int pointerIndex = event.findPointerIndex(element);
                    if (event.getX(pointerIndex) > screenWidth / 2) {
                        xVelocity += 2*dXVelocity;
                        i.remove();
                        rightSet.add(element);
                    }
                }
                for (Iterator<Integer> i = rightSet.iterator(); i.hasNext();){
                    Integer element = i.next();
                    int pointerIndex = event.findPointerIndex(element);
                    if (event.getX(pointerIndex) <= screenWidth / 2) {
                        xVelocity -= 2*dXVelocity;
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
        x += xVelocity*time/1000;
        y += yVelocity*time/1000;
        x = Math.min(x, screenWidth - width);
        x = Math.max(x, 0);
        mainRect.offsetTo(x, y);
        if (y > screenHeight - height) {
            if (points.size() == 0)
                return State.PASSED;
            else
                return State.LOST;
        }
        for (Iterator<Point> i = points.iterator(); i.hasNext();){
            Point element = i.next();
            if (RectF.intersects(mainRect, element)) {
                i.remove();
            }
        }
        return State.RUNNING;
    }
}
