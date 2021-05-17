package com.bloniarz.faster.game.levels;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.bloniarz.faster.game.objects.Point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Level2 implements Level {

    private float x, y, xVelocity = 0, yVelocity = 350, dXVelocity = 100, movingPointXVelocity=300;
    private final float width = 200, height = 200, speed;
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final List<Point> stillPoints = new LinkedList<>(), movingPoints = new LinkedList<>();
    private final int maxCount;
    private final Random random;
    private float timeElapsed = 0;
    private final int tileColor;
    private final Paint textPaint;


    public Level2(float speed, int tileColor){
        maxCount = 10;
        random = new Random();
        this.speed = speed;
        this.tileColor = tileColor;
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(200);
        textPaint.setTextAlign(Paint.Align.CENTER);
        for(int i=0; i<8; i++)
            addRandomPoint();
    }

    void addRandomPoint(){
        float x1 = random.nextFloat()*screenWidth, y1 = random.nextFloat()*screenHeight+100;
        if (x1+width>=screenWidth)
            x1-=width;
        if (y1+height>=screenHeight)
            y1-=height;
        if (random.nextBoolean()){
            stillPoints.add(new Point(x1, y1, x1+width, y1+height, tileColor, 0, 0));
        }
        else{
            movingPoints.add(new Point(x1, y1, x1+width, y1+height, tileColor, (random.nextInt(500)-250)*speed, (random.nextInt(500)-250)*speed));
        }
    }


    @Override
    public synchronized void draw(Canvas canvas) {
        if (canvas != null){
            for (Point point: stillPoints)
                canvas.drawRect(point, point.getPaint());
            for (Point point: movingPoints)
                canvas.drawRect(point, point.getPaint());
            canvas.drawText(String.format(Locale.US, "%d", stillPoints.size()+movingPoints.size()), screenWidth/2, screenHeight/5, textPaint);
        }
    }

    @Override
    public synchronized State update(float time) {
        for (Point point: movingPoints) {
            point.update(time);
        }
        timeElapsed += time;
        if (timeElapsed>500){
            addRandomPoint();
            timeElapsed = 0;
        }
        if (stillPoints.size() + movingPoints.size() >= maxCount)
            return State.LOST;
        if (stillPoints.size() + movingPoints.size() == 0)
            return State.PASSED;
        return State.RUNNING;

    }

    @Override
    public synchronized boolean touch(MotionEvent event) {
        int actionIndex = event.getActionIndex(), pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                for (Iterator<Point> i = stillPoints.iterator(); i.hasNext();){
                    Point element = i.next();
                    if ((element.left <= event.getX(actionIndex)) && (event.getX(actionIndex) <= element.right)
                    && (element.top <= event.getY(actionIndex)) && (event.getY(actionIndex) <= element.bottom)) {
                        i.remove();
                    }
                }
                for (Iterator<Point> i = movingPoints.iterator(); i.hasNext();){
                    Point element = i.next();
                    if ((element.left <= event.getX(actionIndex)) && (event.getX(actionIndex) <= element.right)
                            && (element.top <= event.getY(actionIndex)) && (event.getY(actionIndex) <= element.bottom)) {
                        i.remove();
                    }
                }
                return true;
            }
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//            case MotionEvent.ACTION_MOVE:
//                return true;
        }
        return false;
    }

    @Override
    public String getLevelName() {
        return "Tap tap tap!";
    }

    @Override
    public String getLevelDescription() {
        return String.format(Locale.US, "Tap all the squares.\nDon't let the counter\nget to %d!", maxCount);
    }
}
